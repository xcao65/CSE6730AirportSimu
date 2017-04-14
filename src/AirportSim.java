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
  private static int m_numberofAirports;     //number of airports

  private static Random seed = new Random(10); // use as random seed;

  //getter method of number of airports
  public static int getNumberofAirports() {
    return m_numberofAirports;
  }

  public static void setNumberofAirports(int n){
     m_numberofAirports = n;
  }


  public static void main(String[] args) {
    try{

/*
      System.out.println("how many airplanes do you want, please input a number");
      Scanner in = new Scanner(System.in);
      int n = in.nextInt();
      //in.close();
/*
      System.out.println("Do you want to schedule emergency events? Yes: 1; No: 0");
      int emergency_flag = in.nextInt();

      if (emergency_flag){
        System.out.println("Which airport do you want to schedule emergency events?");
        int airport_flag = in.nextInt();
        System.out.println("Which airport do you want to schedule emergency events?");
        int
      }

      in.close();
*/

/**
     * args:
     *    args[0] - number of airport, default is 10;
     *    args[1] - number of runways for each airport, default is 2;
     *    args[2] - number of planes, default is 50;
     *    args[3] - total of minutes for simulation run, default is 2000;
     *    args[4] - number of airwayCapacity between two airports, default is 0;
     *    args[5] - random seed, default is 0.
     *    args[6] - emergency event schedule flag 1 for yes, default is 0;
     *    args[7] - emergency event scheduled airport index, default is 0;
     *    args[8] - emergency event schedule start_time, default is 500;
     *    args[9] - emergency event scheduled duration, default is 600;
     **/

      Random rdg = new Random(args.length < 6 ? 0 : Integer.parseInt(args[5]));

      List<Airplane> airplanes = new ArrayList<>();
      //initiate several airports(airport_name, runwayTimeToLand, requiredTimeOnGround,
      //takeOffTime, X, Y coordinates, number_of_runways)
      String locationName;
      Airport airport;

      setNumberofAirports(args.length < 1 ? 10: Integer.parseInt(args[0]));

      int runway_flag = args.length < 2 ? 2: Integer.parseInt(args[1]);
      for (int i=0; i < getNumberofAirports(); ++i) {
        locationName = "Location-"+ Integer.toString(i);
        int x = rdg.nextInt(50);
        int y = rdg.nextInt(30);
        airport = new Airport(locationName, 10, 50, 10, 34.05+x*i, -118.24+y*i, runway_flag);
        airport.setAirwayCap(args.length < 5 ? 8: Integer.parseInt(args[4]));
      }

      Airport.get_nearest_neighbor();

      // initialize airplanes list here
      for(int i = 0; i < (args.length < 3 ? 50: Integer.parseInt(args[2])) ; i++) {
        airplanes.add(get_a_plane(rdg));
      }

      //schedule arrival event for all airplanes, for details see method arrangeEvent()
      for(Airplane airplane: airplanes) {
        arrangeEvent(airplane, 0, 0, rdg);
      }

      // schedule emergency events
      int emergency_flag = args.length < 7 ? 0: Integer.parseInt(args[6]);
      if (emergency_flag == 1){
          int emergency_airport = args.length < 8 ? 0: Integer.parseInt(args[7]);
          double emergency_start = args.length < 9 ? 500: Double.parseDouble(args[8]);
          double emergency_duration = args.length < 10 ? 600: Double.parseDouble(args[9]);

          AirportEvent EME = new AirportEvent(emergency_start, Airport.get_global_airports().get(emergency_airport), 4, null);
          EME.setDelay(emergency_duration);
          Simulator.schedule(EME);
      }

      Simulator.stopAt(args.length < 4 ? 2000: Integer.parseInt(args[3]));
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

  public static Airplane get_a_plane(Random rand){
    Airplane plane;
    //int idx = new Random().nextInt(6);
    int idx = rand.nextInt(6);
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

  public static void arrangeEvent(Airplane airplane, double startTime, int eventType, Random rand) {

    //get random start and destination airport
    //Random rand = new Random();
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
