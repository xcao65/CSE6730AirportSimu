//YOUR NAME HERE

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;


public class AirportSim {
    
    public static void main(String[] args) {
        try{
            System.out.println("how many airplanes do you want, please input a number");
            Scanner in = new Scanner(System.in);
            int n = in.nextInt();
            in.close();
            List<Airplane> airplanes = new ArrayList<>();
            
            //instantiate five airports, the last two parameters are X, Y coordinates
            Airport LAX = new Airport("LAX", 10, 10, 10, 34.05, -118.24, 8);
            Airport ATL  = new Airport("ATL", 8, 8, 8, 33.75, -84.39, 6);
            Airport NY = new Airport("NY", 12, 12, 12, 40.73, -73.94, 2);
            Airport SFO = new Airport("SFO", 6, 8, 8, 37.73, -112.44, 4);
            Airport dallas = new Airport("Dallas", 15, 12, 12, 32.73, -96.86, 3);
            Airport Shanghai = new Airport("Shanghai", 11, 11, 18, 31.27, 121.52, 6);
            Airport HK = new Airport("HK", 9, 10, 9, 22.37, 114.12, 7);
            
            
            // initialize airplanes here
            for(int i = 0; i < n; i++) {
                airplanes.add(get_a_plane());
            }
            
            for(Airplane airplane: airplanes) {
                arrangeEvent(airplane, 0, 0);
            }
            
            Simulator.stopAt(2000);
            Simulator.run();
            
            for(Airplane airplane: airplanes) {
                airplane.outputTraceFile();
                airplane.printTrace();
            }
            
            for(Airport a: Airport.get_global_airports()) {
                a.printPassengerFlow();
                a.printTotalCyclingTime();
                System.out.println();
            }
            
        }catch(Exception e) {
            System.err.println("please input a integer");
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
        Random rand = new Random();
        List<Airport> airportList = Airport.get_global_airports();
        int idx = rand.nextInt(airportList.size());
    	AirportEvent event = new AirportEvent(startTime, airportList.get(idx), eventType, airplane);
        Simulator.schedule(event);
    }
}
