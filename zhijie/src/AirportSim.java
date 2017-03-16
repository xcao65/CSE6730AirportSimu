//YOUR NAME HERE

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.Random;


public class AirportSim {
    public static void main(String[] args) {
        /**
         * Random object
         */
        Random random = new Random();

        /**
         * distances between each airports
         */
        int[][] distance = new int[5][5];  //--------------km/h--------------------
        for(int i=0; i<5; i++) {
            distance[i][i] = 0;
            for(int j=0; j<i; j++) {
                distance[i][j] = distance[j][i] = random.nextInt(5000)+5000;
            }
        }

        /**
         *  Five airports
         */
        Airport[] airports = new Airport[5];
        airports[0] = new Airport("LAX", 0, 0.3, 2, distance, airports);   //---index, delay, time on the ground------
        airports[1] = new Airport("JFK", 1, 0.3, 3, distance, airports);
        airports[2] = new Airport("SFO", 2, 0.3, 2, distance, airports);
        airports[3] = new Airport("ATL", 3, 0.3, 3, distance, airports);
        airports[4] = new Airport("SEA", 4, 0.5, 2, distance, airports);


        /**
         * Every airport have a queue also a boolean to know how many airplanes in the air
         * When one airplane left one airport, his arrival event will be scheduled in the TreeSet
         *
         *
         */

        /**
         * Events initialization: 10 planes.
         */
        Airplane A0 = new Airplane("A0", 200, 150, 700);
        AirportEvent landingEvent1 = new AirportEvent(A0, 0, airports[0], AirportEvent.PLANE_ARRIVES);
        Airplane A1 = new Airplane("A1", 300, 222, 800);
        AirportEvent landingEvent2 = new AirportEvent(A1, 0, airports[1], AirportEvent.PLANE_ARRIVES);
        Airplane A2 = new Airplane("A2", 400, 345, 900);
        AirportEvent landingEvent3 = new AirportEvent(A2, 0, airports[2], AirportEvent.PLANE_ARRIVES);
        Airplane A3 = new Airplane("A3", 250, 210, 750);
        AirportEvent landingEvent4 = new AirportEvent(A3, 0, airports[3], AirportEvent.PLANE_ARRIVES);
        Airplane A4 = new Airplane("A4", 300, 300, 900);
        AirportEvent landingEvent5 = new AirportEvent(A4, 0, airports[4], AirportEvent.PLANE_ARRIVES);
//        Airplane A5 = new Airplane("A5", 200, 0, 925);
//        AirportEvent landedEvent1 = new AirportEvent(A5, 0, airports[0], AirportEvent.PLANE_LANDED);
//        Airplane A6 = new Airplane("A6", 400, 0, 800);
//        AirportEvent landedEvent2 = new AirportEvent(A6, 0, airports[1], AirportEvent.PLANE_LANDED);
//        Airplane A7 = new Airplane("A7", 350, 0, 850);
//        AirportEvent landedEvent3 = new AirportEvent(A7, 0, airports[2], AirportEvent.PLANE_LANDED);
//        Airplane A8 = new Airplane("A8", 250, 0, 700);
//        AirportEvent landedEvent4 = new AirportEvent(A8, 0, airports[3], AirportEvent.PLANE_LANDED);
//        Airplane A9 = new Airplane("A9", 300, 0, 800);
//        AirportEvent landedEvent5 = new AirportEvent(A9, 0, airports[4], AirportEvent.PLANE_LANDED);

        Simulator.schedule(landingEvent1);
        Simulator.schedule(landingEvent2);
        Simulator.schedule(landingEvent3);
        Simulator.schedule(landingEvent4);
        Simulator.schedule(landingEvent5);
//        Simulator.schedule(landedEvent1);
//        Simulator.schedule(landedEvent2);
//        Simulator.schedule(landedEvent3);
//        Simulator.schedule(landedEvent4);
//        Simulator.schedule(landedEvent5);

//        AirportEvent landingEvent = new AirportEvent(5, lax, AirportEvent.PLANE_ARRIVES);
//        Simulator.schedule(landingEvent);

        Simulator.stopAt(500);
        Simulator.run();
        for(int i=0; i<5; i++) {
            System.out.println(airports[i].getName()+" total circling Time: " + airports[i].getM_sumCirclingTime());
            System.out.println(airports[i].getName()+" total departing passengers: "+airports[i].getM_outcoming());
            System.out.println(airports[i].getName()+" total arriving passengers: "+airports[i].getM_incoming());
            System.out.println();
        }


    }
}
