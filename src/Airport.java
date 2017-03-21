import java.nio.DoubleBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.omg.CORBA.IRObject;

//YOUR NAME HERE

public class Airport implements EventHandler {

    //TODO add landing and takeoff queues, random variables

    private int m_inTheAir;
    private int m_onTheGround;

    private boolean[] runwayFree;

    //initiate index to store start and destination airport
    private int startAirport;
    private int destination;

    //initialize airway capacity same for all airways
    private int airwayCapacity = 10;

    private double m_runwayTimeToLand;
    private double m_requiredTimeOnGround;
    private double m_takeOffTime;
    private int passengersIn;
    private int passengersOut;
    private String m_airportName;
    private double total_cycling_time;
    private Random rand;
    
    private double coordinate_x;
    private double coordinate_y;
    
    private Deque<AirportEvent> eventQueue;
    
    //list of airports(static type) shared by every airport
    private static List<Airport> global_airports = new ArrayList<Airport>();
    
    //initiate matrix to store number of planes on the airway between each pair of airport, by default 0
    private static int number_of_airports = AirportSim.getNumberofAirports();
    private static int[][] m_airwayNumber = new int[number_of_airports][number_of_airports];

    public static void setAirwayNumber(int start, int destination) {
        m_airwayNumber[start][destination]++;
    }
    
    public Airport(String name, double runwayTimeToLand, double requiredTimeOnGround, 
        double takeOffTime, double coordinate_X, double coordinate_Y, int number_of_runway) {
        m_airportName = name;
        m_inTheAir =  0;
        m_onTheGround = 0;
        m_runwayTimeToLand = runwayTimeToLand;
        m_requiredTimeOnGround = requiredTimeOnGround;
        m_takeOffTime = takeOffTime;
        coordinate_x = coordinate_X;
        coordinate_y = coordinate_Y;
        passengersIn = 0;
        passengersOut = 0;
        total_cycling_time = 0;
        rand = new Random();
        eventQueue = new ArrayDeque<>();

        //add airport object to the static airport list
        global_airports.add(this);
        
        //initiate and initailize a boolean array to store runway status
        runwayFree = new boolean[number_of_runway];
        Arrays.fill(runwayFree, true);
    }

    public String getName() {
        return m_airportName;
    }
    
    public void printPassengerFlow() {
    	System.out.println(String.format("%s: %d passengers landed, %d passengers departed", this.getName(), passengersIn, passengersOut));
    }
    
    public void printTotalCyclingTime() {
    	System.out.println(String.format("total cycling time: %.2f", total_cycling_time));
    }
    
    public double getTotalCirclingTime() {
    	return total_cycling_time;
    }
    
    public double calculate_distance(Airport airport) {
    	return Math.sqrt(Math.pow(coordinate_x - airport.coordinate_x, 2) + Math.pow(coordinate_y - airport.coordinate_y, 2));
    }

    //get destination airport index
    public int getDestinationIndex() {
        while(true) {
            int idx = rand.nextInt(global_airports.size());
            if(global_airports.get(idx) != this) {
                return idx;
            }
        }
    }
        
    //get method for airport list
    public static List<Airport> get_global_airports() {
        return global_airports;
    }

    public void handle(Event event) {

        AirportEvent airEvent = (AirportEvent)event;
        Airplane airplane = airEvent.checkFlight();

        switch(airEvent.getType()) {

            case AirportEvent.PLANE_ARRIVES:
                m_inTheAir++;

                //decrease airway by 1
                startAirport = airplane.getStartAirport();
                destination = airplane.getDestination();
                m_airwayNumber[startAirport][destination]--;

                //set new start airport and destination
                startAirport = (global_airports.indexOf(this));
                destination = getDestinationIndex();
                airplane.setStartAirport(startAirport);
                airplane.setDestination(destination);

                //add a record in the airplane
                String trace1 = String.format("%.2f: arrived at %s", Simulator.getCurrentTime(), this.getName());
                airplane.addTrace(trace1);
                int i = 0;
                for(i = 0; i < runwayFree.length; i++) {
                    if(runwayFree[i]) {
                        runwayFree[i] = false;
                        AirportEvent landedEvent = new AirportEvent(m_runwayTimeToLand, this, AirportEvent.PLANE_LANDED, airplane);
                        landedEvent.setRunWay(i);
                        Simulator.schedule(landedEvent);
                        break;
                    }
                }

                if(i == runwayFree.length) eventQueue.add(airEvent);
                
                break;


            case AirportEvent.PLANE_LANDED:
                m_inTheAir--;
            	passengersIn += airplane.getPassengerNo();
                int local_runway = airEvent.getRunWay();
                //add a record in the airplane
            	String trace2 = String.format("%.2f: landed at %s", Simulator.getCurrentTime(), this.getName());
            	airplane.addTrace(trace2);
                
                AirportEvent takeOffEvent = new AirportEvent(m_requiredTimeOnGround, this, AirportEvent.PLANE_TAKEOFF, airplane);
                Simulator.schedule(takeOffEvent);
                
                if(!eventQueue.isEmpty())
                {
                	AirportEvent ae = eventQueue.poll();
                	total_cycling_time += Simulator.getCurrentTime() - ae.getTime();
                    
                	if(ae.getType() == AirportEvent.PLANE_ARRIVES) {
                		AirportEvent landingEvent = new AirportEvent(m_runwayTimeToLand, this, AirportEvent.PLANE_LANDED, ae.checkFlight());
                        landingEvent.setRunWay(local_runway);
                        Simulator.schedule(landingEvent);
                	}
                    
                	else {
            			AirportEvent departEvent = new AirportEvent(m_takeOffTime, this, AirportEvent.PLANE_DEPARTS, ae.checkFlight());
                        departEvent.setRunWay(local_runway);
                        Simulator.schedule(departEvent);
            		}	                   
                }
                else
                {
                    runwayFree[local_runway] = true;
                }
                break;
                
            case AirportEvent.PLANE_TAKEOFF:
            	m_onTheGround++;

                //set airplane passenger number
            	airplane.setPassenger(rand.nextInt(airplane.getCapacity()));
                
                //add a record in airplane
            	String trace3 = String.format("%.2f: is ready to go with %d passengers", Simulator.getCurrentTime(), airplane.getPassengerNo());
            	airplane.addTrace(trace3);

                //test if the airway is full
                startAirport = airplane.getStartAirport();
                destination = airplane.getDestination();
                if(m_airwayNumber[startAirport][destination] < airwayCapacity) {

                    for(i = 0; i < runwayFree.length; i++) {
                        if(runwayFree[i]) {
                            runwayFree[i] = false;
                            AirportEvent departureEvent = new AirportEvent(m_takeOffTime, this, AirportEvent.PLANE_DEPARTS, airplane);
                            
                            //mark the runway index to set free later
                            departureEvent.setRunWay(i);
                            Simulator.schedule(departureEvent);
                            break;
                        }
                    }
                    //if runway is false, delay the plane takeoff event
                    if(i == runwayFree.length) eventQueue.add(airEvent);

                } else {
                    //if airway is full, delay the plane takeoff event
                    eventQueue.add(airEvent);
                }

            	break;
            	
            	
            case AirportEvent.PLANE_DEPARTS:
            	m_onTheGround--;
            	passengersOut += airplane.getPassengerNo();
                local_runway = airEvent.getRunWay();

                //get the destination which is stored in airplane object, reassigned during arrival event
                destination = airplane.getDestination();
            	Airport destinationAirport = global_airports.get(destination);

                //increase corresponding airway matrix number
                startAirport = airplane.getStartAirport();
                m_airwayNumber[startAirport][destination]++;
                
                //calculate flight time
            	double speed = airplane.getSpeed();
            	double distance = calculate_distance(destinationAirport);
            	double flightTime = distance / speed;
                
                //add a record in airplane
            	String trace4 = String.format("%.2f: departs from %s to %s", Simulator.getCurrentTime(), this.getName(), destinationAirport.getName());
            	airplane.addTrace(trace4);
                
            	AirportEvent arrivalEvent = new AirportEvent(flightTime, destinationAirport, AirportEvent.PLANE_ARRIVES, airplane);
            	Simulator.schedule(arrivalEvent);
                
            	if(!eventQueue.isEmpty())
                {    
            		AirportEvent ae = eventQueue.poll();
            		if(ae.getType() == AirportEvent.PLANE_ARRIVES) {
                		AirportEvent landingEvent = new AirportEvent(m_runwayTimeToLand, this, AirportEvent.PLANE_LANDED, ae.checkFlight());
                        landingEvent.setRunWay(local_runway);
                        Simulator.schedule(landingEvent);
                	}
            		else {
            			AirportEvent departEvent = new AirportEvent(m_takeOffTime, this, AirportEvent.PLANE_DEPARTS, ae.checkFlight());
                        departEvent.setRunWay(local_runway);
                        Simulator.schedule(departEvent);
            		}	
                }
                else
                {
                    runwayFree[local_runway] = true;
                }
                break;
        }
    }
}
