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
  private int airwayCapacity;

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

  // add emergency status of airport
  private boolean emergency;
  // add nearest airport for each airport
  private Airport neighbor;
  //
  private static double R =  6371;

  private double emergency_duration;

  //private List<Airport> neighbors = new ArrayList<Airport>();

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
    // add emergency status
    emergency = false;
    neighbor = null;
    emergency_duration = 0;

    //add airport object to the static airport list
    global_airports.add(this);

    //if the number of runways is negative or zero, it will be set to default value 1
    if (number_of_runway<=0)  number_of_runway = 1;
    //initiate and initailize a boolean array to store runway status
    runwayFree = new boolean[number_of_runway];
    Arrays.fill(runwayFree, true);
  }

  public String getName() {
    return m_airportName;
  }

  public void setAirwayCap(int n){
     this.airwayCapacity = n;
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

  public double getX(){
    return coordinate_x;
  }

  public double getY(){
    return coordinate_y;
  }

  public double getED(){
    return emergency_duration;
  }

  public void setED(double d){
    emergency_duration = d;
  }

  public double calculate_distance(Airport airport) {
  return Math.sqrt(Math.pow(this.getX() - airport.getX(), 2) + Math.pow(this.getY() - airport.getY(), 2));
}

/*
// TODO CHANGE this function!!!
public double calculate_distance(Airport b) {
  double d1 = b.getX()- this.getX();
  double d2 = b.getY() - this.getY();
  double a0 = Math.sin(d1 / 2) * Math.sin(d1 / 2)
  + Math.cos(this.getX()) * Math.cos(this.getY())
  * Math.sin(d2 / 2) * Math.sin(d2 / 2);
  double c = 2 * Math.atan2(Math.sqrt(a0), Math.sqrt(1 - a0));
  return R * c;
}
*/

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

// get method for neighbor
public Airport getNeighbor(){
  return neighbor;
}

public void setNeighbor(Airport near){
  neighbor = near;
}

// add function to calculate the nearest neighbor
public static void get_nearest_neighbor(){
  for (int i=0; i<global_airports.size(); i++){
    Airport curr = global_airports.get(i);

    if (curr.getNeighbor() == null){
      double min = Double.MAX_VALUE;
      int min_idx = -1;
      for (int j = 0; j<global_airports.size(); j++){
        Airport next = global_airports.get(j);
        if (curr!=next){
          double currDist = curr.calculate_distance(next);
          if (currDist < min) {
            min = currDist;
            min_idx = j;
          }
        }
      }
      curr.setNeighbor(global_airports.get(min_idx));
      //global_airports.get(min_idx).setNeighbor(curr);
    } // end of if statement
  }
  // end of for loop
}

// get method for emergency
public boolean getEmergency(){
  return emergency;
}

public boolean isEmergency(Airport a){
  return a.getEmergency();
}


public void handle(Event event) {

  AirportEvent airEvent = (AirportEvent)event;
  Airplane airplane = airEvent.checkFlight();

  switch(airEvent.getType()) {

    case AirportEvent.PLANE_ARRIVES:
    //decrease airway by 1
    startAirport = airplane.getStartAirport();
    destination = airplane.getDestination();
    m_airwayNumber[startAirport][destination]--;

    if (this.getEmergency() == false){
      m_inTheAir++;
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

    } else {
      //? schedule arrival events.....
      startAirport = (global_airports.indexOf(this));
      destination = (global_airports.indexOf(this.getNeighbor()));
      airplane.setStartAirport(startAirport);
      airplane.setDestination(destination);

      Airport destinationAirport = global_airports.get(destination);

      m_airwayNumber[startAirport][destination]++;

      //calculate flight time
      double speed = airplane.getSpeed();
      double distance = calculate_distance(destinationAirport);
      double flightTime = distance / speed;

      //add a record in airplane
      String trace1 = String.format("%.2f: re-depatured at %s", Simulator.getCurrentTime(), this.getName());
      airplane.addTrace(trace1);
      String trace4 = String.format("%.2f: departs from %s to %s", Simulator.getCurrentTime(), this.getName(), destinationAirport.getName());
      airplane.addTrace(trace4);

      AirportEvent arrivalEvent = new AirportEvent(flightTime, destinationAirport, AirportEvent.PLANE_ARRIVES, airplane);
      Simulator.schedule(arrivalEvent);
    }

    break;


    case AirportEvent.PLANE_LANDED:
    m_inTheAir--;
    passengersIn += airplane.getPassengerNo();
    int local_runway = airEvent.getRunWay(); // get the runway number to use
    //add a record in the airplane
    String trace2 = String.format("%.2f: landed at %s", Simulator.getCurrentTime(), this.getName());
    airplane.addTrace(trace2);

    double extra_delay = 0;
    // check if emergency occurs
    if (this.getEmergency()) extra_delay = this.getED();

    AirportEvent takeOffEvent = new AirportEvent(m_requiredTimeOnGround + extra_delay, this, AirportEvent.PLANE_TAKEOFF, airplane);
    Simulator.schedule(takeOffEvent);

    if (this.getEmergency() == false){
      if(!eventQueue.isEmpty())
      {
        AirportEvent ae = eventQueue.poll(); // what is this?
        total_cycling_time += Simulator.getCurrentTime() - ae.getTime(); //?

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
    }
    // end of if emergency

    break;


    case AirportEvent.PLANE_TAKEOFF:

    if (this.getEmergency()){
      // extra_delay = this.getED();
      AirportEvent newtakeOffEvent = new AirportEvent(this.getED(), this, AirportEvent.PLANE_TAKEOFF, airplane);
      Simulator.schedule(newtakeOffEvent);
      String trace3 = String.format("%.2f: is reschedule to take off", Simulator.getCurrentTime());
      airplane.addTrace(trace3);

    } else {
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
        int i;
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
    }
    break;


    case AirportEvent.PLANE_DEPARTS:
    m_onTheGround--;
    passengersOut += airplane.getPassengerNo();
    local_runway = airEvent.getRunWay();

    //get the destination which is stored in airplane object, reassigned during arrival event
    destination = airplane.getDestination();

    Airport destinationAirport = global_airports.get(destination);

    // if destination is in emergency
    if (destinationAirport.getEmergency()){
      String trace5 = String.format("%.2f: get emergency at %s", Simulator.getCurrentTime(), destinationAirport.getName());
      airplane.addTrace(trace5);
      destinationAirport = destinationAirport.getNeighbor();
      trace5 = String.format("%.2f: re-route to %s", Simulator.getCurrentTime(), destinationAirport.getName());
      airplane.addTrace(trace5);
      destination = global_airports.indexOf(destinationAirport);
    }
    //increase corresponding airway matrix number
    startAirport = airplane.getStartAirport();
    m_airwayNumber[startAirport][destination]++;

    //calculate flight time
    double speed = airplane.getSpeed();
    double distance = calculate_distance(destinationAirport);
    double flightTime = distance / speed;

    //add a record in airplane
    if (destinationAirport.getEmergency()){

    }
    String trace4 = String.format("%.2f: departs from %s to %s", Simulator.getCurrentTime(), this.getName(), destinationAirport.getName());
    airplane.addTrace(trace4);

    airplane.setDestination(destination);
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


    case AirportEvent.AIRPORT_EMER_START:
    this.emergency = true;
    this.setED(airEvent.getDelay());
    for(int i = 0; i < runwayFree.length; i++) {
      runwayFree[i] = false;
    }
    //System.out.println("")
    AirportEvent emergency_End = new AirportEvent(500, this, AirportEvent.AIRPORT_EMER_END, null);
    Simulator.schedule(emergency_End);

    // check if there is waiting planes to departure or landing
    // if departure: reschedule a departure event + emergency_duration
    // if arrival: reschedule an arrival events for it to the nearest airports
    Deque<AirportEvent> currQueue = new ArrayDeque<>();
    while (!eventQueue.isEmpty()){
      AirportEvent ae = eventQueue.poll();
      if (ae.getType() == AirportEvent.PLANE_TAKEOFF) {
        ae.setDelay(this.getED() + ae.getDelay());
        currQueue.add(ae);
      } else {
        destinationAirport = this.getNeighbor();
        destination = global_airports.indexOf(destinationAirport);
        startAirport = global_airports.indexOf(this);
        airplane.setStartAirport(startAirport);
        airplane.setDestination(destination);
        m_airwayNumber[startAirport][destination]++;
        speed = airplane.getSpeed();
        distance = calculate_distance(destinationAirport);
        flightTime = distance / speed;
        arrivalEvent = new AirportEvent(flightTime, destinationAirport, AirportEvent.PLANE_ARRIVES, airplane);
        Simulator.schedule(arrivalEvent);
      }
    }
    while (!currQueue.isEmpty()){
      eventQueue.add(currQueue.poll());
    }

    break;


    case AirportEvent.AIRPORT_EMER_END:
    this.emergency = false;
    this.setED(airEvent.getDelay());
    for(int i = 0; i < runwayFree.length; i++) {
      runwayFree[i] = true;
    }
    // check if eventQueue is empty or not, to schedule further events.
    int j = 0;
    while (j < runwayFree.length && !eventQueue.isEmpty()){
      AirportEvent ae = eventQueue.poll();
      AirportEvent departEvent = new AirportEvent(m_takeOffTime, this, AirportEvent.PLANE_DEPARTS, ae.checkFlight());
      departEvent.setRunWay(j);
      Simulator.schedule(departEvent);
      j++;
    }

/*
    if (!eventQueue.isEmpty()){
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
*/
    break;

  }
}
}
