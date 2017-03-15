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
    	if(args.length < 2) {
    		System.out.println("Please input simulation running time in days (an integer)");
    		System.out.println("How many airplane squads you want?(6 airplanes per squad) (an integer)");
    		System.exit(0);
    	}
    	int runningDays = Integer.parseInt(args[0]);
    	int n = Integer.parseInt(args[1]);
    	//instantiate five airports, the last two parameters are X, Y coordinates
    	List<Airport> airports = new ArrayList<>();
    	List<Airplane> airplanes = new ArrayList<>();
    	
    	// initialize airports
        Airport LAX = new Airport("LAX", 10, 10, 10, 34.05, -118.24);
        Airport ATL  = new Airport("ATL", 8, 8, 8, 33.75, -84.39);
        Airport NY = new Airport("NY", 12, 12, 12, 40.73, -73.94);
        Airport SFO = new Airport("SFO", 6, 8, 8, 37.73, -112.44);
        Airport dallas = new Airport("Dallas", 15, 12, 12, 32.73, -96.86);
        Airport Shanghai = new Airport("Shanghai", 11, 11, 18, 31.27, 121.52);
        Airport HK = new Airport("HK", 9, 10, 9, 22.37, 114.12);
        // add airports to a list
        airports.add(LAX);
        airports.add(ATL);
        airports.add(NY);
        airports.add(SFO);
        airports.add(dallas);  
        airports.add(Shanghai);
        airports.add(HK);
        
	    for(int i = 0; i < n; i++) {
	        // initialize airplanes here
	    	Airplane A320 = new Airplane("A320_" + i, 0.28, 250);
	        Airplane A380 = new Airplane("A380_" + i, 0.4, 400);
	        Airplane Boeing747 = new Airplane("Boeing747_" + i, 0.45, 320);
	        Airplane Boeing737 = new Airplane("Boeing737_" + i, 0.35, 200);
	        Airplane F22 = new Airplane("F22_" + i, 0.5, 20);
	        Airplane CRJ = new Airplane("CRJ-900_" + i, 0.3, 285);
	        airplanes.add(A320);
	        airplanes.add(A380);
	        airplanes.add(Boeing747);
	        airplanes.add(Boeing737);
	        airplanes.add(F22);
	        airplanes.add(CRJ);     	
	    }
	            
	    //schedule each plane's timetable and arrange event for each plane
	    int startTime = 0;
	    for(Airplane airplane: airplanes) {
	    	airplaneScheduler(airplane, airports);
	        arrangeEvent(airplane, startTime, AirportEvent.PLANE_ARRIVES);
	        startTime += 5;
	    }
	            
	    Simulator.stopAt(runningDays*1440);
	    Simulator.run();
	            
	    for(Airplane airplane: airplanes) {
	          airplane.outputTraceFile();
	          airplane.printTrace();
	    }
	    for(Airport a: airports) {
	          a.printPassengerFlow();
	          a.printTotalCyclingTime();
	          System.out.println();
	    	}
    }
    
    public static void airplaneScheduler(Airplane airplane, List<Airport> airports) {
    	List<Integer> list = new ArrayList<>();
    	Random rand = new Random();
    	for(int i = 0; i < 20; i++) {
    		if(i == 0) list.add(rand.nextInt(7));
    		else {
    			int k = rand.nextInt(7);
    			if(k != list.get(list.size() - 1)) {
    				list.add(k);
    			}
    		}
    	}
    	for(int e: list) {
    		airplane.addDestination(airports.get(e));
    	}
    }
    
    public static void arrangeEvent(Airplane airplane, double startTime, int eventType) {
    	if(airplane.hasNextDest()) {
        	AirportEvent event = new AirportEvent(startTime, airplane.nextDestination(), eventType, airplane);
        	Simulator.schedule(event);
        }
    	else {
    		System.out.println("Please schedule destinations for this airplane");
    	}
    }
}
