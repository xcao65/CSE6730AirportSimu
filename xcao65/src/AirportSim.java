//YOUR NAME HERE

import java.util.TreeSet;
import java.io.*;


public class AirportSim {
    public static void main(String[] args)throws IOException {

        //Initialize AirportList
        Airport[] destination = new Airport[5];
        String[] location = {"DEN", "ATL", "SEA", "LAX", "BOS"};
        
        //initialize Distance Matrix
        int[][] distance = {{0,1220,1030,812,1783}, 
                            {1220,0,2200,1943,959},
                            {1030,2200,0,923,2504},
                            {812,1943,923,0,2594},
                            {1783,959,2504,2594,0}};

        for (int i=0; i<destination.length; i++) {
            destination[i] = new Airport(location[i], 2, 10, 2, distance);
        }

        String planeName = null;
        int m_planes = Integer.parseInt(args[0]);
        for(int j=0; j<5; j++) {
            for (int i=0; i<m_planes; i++) {
                planeName = "Boeing 787-"+ Integer.toString(j) + Integer.toString(i);
                //Initialize Airplane
                Airplane delta = new Airplane( planeName, 8, 220, destination);
                //Initialize Event
                AirportEvent landingEvent = new AirportEvent( 2*i, destination[j], AirportEvent.PLANE_ARRIVES, delta);
                Simulator.schedule(landingEvent);
            }
        }
             
        Simulator.stopAt(10000);	//CurrentTime is 0 at this point!! it works!
        Simulator.run();

        //output results
        PrintWriter pw = new PrintWriter(new FileOutputStream("output.txt"));
        for (int i=0; i<5; i++) {
            destination[i].outFile(pw);
            //destination[i].outFile();
            //destination[i].closeFile();
        }
        pw.close();
    }
}

/*
* Add an airplane class.  
It should have at minimum properties of speed and maximum passinger capacity.  
Feel free to add other properties you feel are important.

* You need to add an Airplane parameter for the Airport events 
so you know which plane the event is related to.

* Pick 5 airports in the world and add them to the simulator.  
You'll need the distance between them to calculate flight time for each flight.

* For each plane departure, select a remote airport
Distance matrix
planeSpeed
flightTime(schedule each arriving event)

* and use some sort of random distribution to calculate 
	the number of passengers on the flight.

* For each airport, keep stats on the number of passengers arriving and departing.  
also sum the total amount circling time for each airport.  
This is time where an airplane is ready to land but is waiting for an opening.

* Adjust the simulator to run for an appropriate period 
and adjust timings (timeToLand, timeOnGround etc) accordingly

-----------------------------------------------------------------------
-----------------------------------------------------------------------

* Develop a queuing system to ensure planes are not arriving 
and taking off at the same time on a single runway.
When an airplane is in the process of landing, 
no other plane should be taking off or landing on that runway.

need a event takeoff set boolean to false!!!
*/
