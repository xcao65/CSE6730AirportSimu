//YOUR NAME HERE

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;



public class AirportSim {


  /*
  **@input number of airports
  */
  private static int m_numberofAirports = 8;     //number of airports

  private static Random seed = new Random(10); // use as random seed;

  //getter method of number of airports
  public static int getNumberofAirports() {
    return m_numberofAirports;
  }

  public static void main(String[] args) {
    try{
      System.out.println("how many airplanes do you want, please input a number");
      Scanner in = new Scanner(System.in);
      int n = in.nextInt();
      in.close();

      List<Airplane> airplanes = new ArrayList<>();
      //initiate several airports(airport_name, runwayTimeToLand, requiredTimeOnGround,
      //takeOffTime, X, Y coordinates, number_of_runways)
      String locationName;
      Airport airport;
      for (int i=0; i<m_numberofAirports; ++i) {
        locationName = "Location-"+ Integer.toString(i);
        int x = seed.nextInt(50);
        int y = seed.nextInt(30);
        airport = new Airport(locationName, 10, 10, 10, 34.05+x*i, -118.24+y*i, 2);
      }

      Airport.get_nearest_neighbor();

      // initialize airplanes list here
      for(int i = 0; i < n; i++) {
        airplanes.add(get_a_plane());
      }

      //schedule arrival event for all airplanes, for details see method arrangeEvent()
      for(Airplane airplane: airplanes) {
        arrangeEvent(airplane, 0, 0);
      }

      AirportEvent EME = new AirportEvent(500, Airport.get_global_airports().get(0), 4, null);
      Simulator.schedule(EME);

      Simulator.stopAt(2000);
      Simulator.run();

      for(Airplane airplane: airplanes) {
        airplane.outputTraceFile();
        airplane.printTrace();
      }

      for(Airport a: Airport.get_global_airports()) {
        a.printPassengerFlow();
        a.printTotalCyclingTime();
        //System.out.println("" + " "+ a.getName() + " " + a.getNeighbor().getName());
        //System.out.println("" + Double.toString(a.calculate_distance(a.getNeighbor())));
        System.out.println();
      }

    }catch(Exception e) {
      e.printStackTrace();
      System.err.println("please input an integer");
    }
  }

  public static Airplane get_a_plane(){
    Airplane plane;
    int idx = new Random().nextInt(6);
    switch(idx) {
      case 0: plane = new Airplane("A320", 0.28, 250);
              break;
      case 1: plane = new Airplane("A380", 0.4, 400);
              break;
      case 2: plane = new Airplane("Boeing747", 0.45, 320);
              break;
      case 3: plane = new Airplane("Boeing737", 0.35, 200);
              break;
      case 4: plane = new Airplane("F22", 0.5, 20);
              break;
      default: plane =new Airplane("CRJ-900", 0.3, 285);
              break;
    }
    return plane;
  }

  public static void arrangeEvent(Airplane airplane, double startTime, int eventType) {

    //get random start and destination airport
    Random rand = new Random();
    List<Airport> airportList = Airport.get_global_airports();
    int destinationIdx = rand.nextInt(airportList.size());
    int startIdx = rand.nextInt(airportList.size());

    //increase airway number for each airpline, assuming all planes starting from airport 0.
    airplane.setStartAirport(startIdx);
    airplane.setDestination(destinationIdx);

    //increase airway number
    Airport.setAirwayNumber(startIdx, destinationIdx);

    //schedule arrivial airportevent
    AirportEvent event = new AirportEvent(startTime, airportList.get(destinationIdx), eventType, airplane);
    Simulator.schedule(event);
  }
}
