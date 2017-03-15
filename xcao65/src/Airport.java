//Xun Cao
import java.io.*;
import java.util.*;

public class Airport implements EventHandler {

    //TODO add landing and takeoff queues, random variables

    private int m_inTheAir;     //# of flights in air
    private int m_onTheGround;  //# of flights on ground

    private boolean m_freeToLand;   //if runway free

    private LinkedList<Airplane> m_planeList;
    private LinkedList<Airplane> m_hoverList;

    private double m_flightTime;
    private double m_runwayTimeToLand;
    private double m_requiredTimeOnGround;
    private double m_runwayTimeTakeoff;
    private int [][] m_distance;

    private String m_airportName;
    private int m_passengerArrive;
    private int m_passengerLeave;
    private double m_circleTime;
    Random rand = new Random();

    PrintWriter pw;

    public Airport(String name, double runwayTimeToLand, double requiredTimeOnGround, double runwayTimeTakeoff, int[][] distance) throws IOException {
        m_airportName = name;
        m_inTheAir =  0;
        m_onTheGround = 0;
        m_freeToLand = true;
        m_runwayTimeToLand = runwayTimeToLand;
        m_requiredTimeOnGround = requiredTimeOnGround;
        m_runwayTimeTakeoff = runwayTimeTakeoff;
        m_distance = distance;
        m_planeList = new LinkedList<Airplane>();
        m_hoverList = new LinkedList<Airplane>();
        m_passengerArrive = 0;
        m_passengerLeave = 0;
        m_circleTime = 0;
        
        //pw = new PrintWriter(new FileOutputStream("output/" + m_airportName + "output.txt"));
    }

    public void outFile(PrintWriter pw) {
        pw.print(m_passengerArrive+","+m_passengerLeave+","+m_circleTime);
        pw.println();
    }

    public void outScreen() {
        System.out.print(m_passengerArrive+","+m_passengerLeave+","+m_circleTime);
        System.out.println();
    }

    public void closeFile() {
        pw.close();
    }

    public String getName() {
        return m_airportName;
    }

    //Calculate Passenger for each flight (landed or departed)
    public int calPassengerNum(Airplane flight){
        int maxNum = flight.getNumPassengers();
        int randNum = rand.nextInt( (int)(maxNum*0.2) ) + 1;
        return (int)(maxNum*0.8) + randNum;
    }

    //Calculate the flight time = distance / speed
    //Distance = distance[i][j]
    //i,j is got from destinationlist
    public int calFlightTime(Airport destination, Airplane flight) {
        int m=0;
        int n=0;
        int speed = flight.getSpeed();
        Airport[] destinationlist = flight.getDestinationList();

        for (int i=0; i<destinationlist.length; i++) {
            if(this.equals(destinationlist[i])){
                m = i;
            }
            if(destination.equals(destinationlist[i])) {
                n = i;
            }
        }
        return m_distance[m][n] / speed;
    }

    public void handle(Event event) {
        AirportEvent airEvent = (AirportEvent)event;

        switch(airEvent.getType()) {
            case AirportEvent.PLANE_ARRIVES:
                //Set plane's arrive time
                airEvent.getFlight().setArriveTime(Simulator.getCurrentTime());
                m_inTheAir++;
                System.out.println(Simulator.getCurrentTime() + ": Plane " + airEvent.getFlight().getName() +" arrived at airport " + this.getName());
                if(m_freeToLand) {
                    AirportEvent landedEvent = new AirportEvent(m_runwayTimeToLand, this, AirportEvent.PLANE_LANDED, airEvent.getFlight());
                    Simulator.schedule(landedEvent);
                    m_freeToLand = false;
                } else {
                    m_hoverList.add(airEvent.getFlight());
                }
                break;

            case AirportEvent.PLANE_START:

                Airport destination; 
                //get a random destination airport which is different from current one
                do{
                    destination = airEvent.getFlight().getRandomAirport();
                } while(this.equals(destination));
                
                System.out.println(Simulator.getCurrentTime() + ": Plane " + airEvent.getFlight().getName() +" now heads to airport " + destination.getName());    
                m_flightTime = calFlightTime(destination, airEvent.getFlight());
                AirportEvent takeoffEvent = new AirportEvent(m_flightTime, destination, AirportEvent.PLANE_ARRIVES, airEvent.getFlight());
                //Choose another airport helper function
                Simulator.schedule(takeoffEvent);

                if(!m_hoverList.isEmpty()) {
                    AirportEvent landingEventa = new AirportEvent(m_runwayTimeToLand, this, AirportEvent.PLANE_LANDED, m_hoverList.pop());
                    Simulator.schedule(landingEventa);
                }
                else if(!m_planeList.isEmpty()) {
                    AirportEvent redepartureEventa = new AirportEvent(0, this, AirportEvent.PLANE_DEPARTS, m_planeList.pop());
                    Simulator.schedule(redepartureEventa);
                    m_freeToLand = true;
                } else {
                    m_freeToLand = true;
                }
            
                break;

            case AirportEvent.PLANE_LANDED:
                m_inTheAir--;
                m_onTheGround++;
                //Calculate Circle time
                m_circleTime = m_circleTime + airEvent.getFlight().getCircleTime(Simulator.getCurrentTime() - m_runwayTimeToLand);
                System.out.println(Simulator.getCurrentTime() + ": Plane " + airEvent.getFlight().getName() + " lands at airport " + this.getName());
                m_passengerArrive = m_passengerArrive + calPassengerNum(airEvent.getFlight());
                System.out.println(this.getName() + " now has " + m_passengerArrive + " passengers arrived");

                AirportEvent departureEvent = new AirportEvent(m_requiredTimeOnGround, this, AirportEvent.PLANE_DEPARTS, airEvent.getFlight());
                Simulator.schedule(departureEvent);        
                //If hoverlist is not empty, schedule land first 
                if(!m_hoverList.isEmpty()) {
                    AirportEvent landingEvent = new AirportEvent(m_runwayTimeToLand, this, AirportEvent.PLANE_LANDED, m_hoverList.pop());
                    Simulator.schedule(landingEvent);
                
                } else if (!m_planeList.isEmpty()){
                    //If planelist is not empty, schedule takeoff first
                    //m_onTheGround--;
                    //m_planeList.add(airEvent.getFlight());
                    //System.out.println(Simulator.getCurrentTime() + ": Plane " + airEvent.getFlight().getName() +" starts to depart from airport");
                    AirportEvent redepartureEvent = new AirportEvent(0, this, AirportEvent.PLANE_DEPARTS, m_planeList.pop());
                    Simulator.schedule(redepartureEvent);
                    m_freeToLand = true;
                } else {
                    //runway free to land or depart
                    m_freeToLand = true;
                }
                break;

            case AirportEvent.PLANE_DEPARTS:
                if(m_freeToLand) {
                    m_onTheGround--;
                    System.out.println(Simulator.getCurrentTime() + ": Plane " + airEvent.getFlight().getName() +" starts to depart from airport " + this.getName());
                    m_passengerLeave += calPassengerNum(airEvent.getFlight());
                    
                    //System.out.println(this.getName() + " now has " + m_passengerLeave + " passengers left");
                    //System.out.println(this.getName() + " total circle time is " + m_circleTime);

                    AirportEvent setoffEvent = new AirportEvent(m_runwayTimeTakeoff, this, AirportEvent.PLANE_START, airEvent.getFlight());
                    Simulator.schedule(setoffEvent);
                    m_freeToLand = false;

                } else {

                    m_planeList.add(airEvent.getFlight());
                }
                break;

        }
    }
}