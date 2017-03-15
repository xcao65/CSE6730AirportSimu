//YOUR NAME HERE
/**
 * Author: Jing Gu (jgu47)
 * Date: 02/13/2017
*/
import java.util.TreeSet;
import java.util.Random;

public class AirportSim {
    /**
     * args:
     *    args[0] - number of planes, default is 3;
     *    args[1] - total of minutes for simulation run, default is 500;
     *    args[2] - random seed, default is 0.
     *    args[3] - queuing strategy, default is 0:
     *              0 - Landing first
     *              1 - Departing first
     *              2 - Sort only by timestamp for queuing
     **/
    public static void main(String[] args) {
        Random rdg = new Random(args.length < 3? 0 : Integer.parseInt(args[2]));
        Airport.initRandomGenerator(rdg);

        int stg = args.length < 4? 0 :Integer.parseInt(args[3]); // que strategy

        Airport atl = new Airport("ATL", 20, 35, 33.6366997, -84.4281006, stg);
        Airport jfk = new Airport("JFK", 25, 45, 40.639801, -73.7789002, stg);
        Airport lax = new Airport("LAX", 20, 40, 33.9425011, -118.4079971, stg);
        Airport sfo = new Airport("SFO", 15, 30, 37.6189995, -122.375, stg);
        Airport sea = new Airport("SEA", 15, 25, 47.4490013, -122.3089981, stg);

/*
        Airport lax = new Airport("LAX", 10, 10, 33.9425011, -118.4079971, stg);
        Airport pvg = new Airport("PVG", 20, 20, 31.1434002, 121.8050003, stg);
        Airport ltr = new Airport("LTR", 25, 25, 54.9513016, -7.6728301, stg);
        new Airport("SYD", 15, 60, -33.9460983, 151.177002, stg);
        new Airport("CAI", 90, 75, 30.1219006, 31.4055996, stg);
*/
        // Testing picking remote airport
        // System.out.println(Airport.pickOtherThan(ltr));
        // System.out.println(Airport.pickOtherThan(pvg));
        // System.out.println(Airport.pickOtherThan(lax));

        // Testing against https://www.world-airport-codes.com/distance/?a1=PVG&a2=LTR
        // System.out.println("Dist between LAX PVG is " + Airport.dist(lax, pvg));
        // System.out.println("Dist between LAX LTR is " + Airport.dist(lax, ltr));
        // System.out.println("Dist between LTR PVG is " + Airport.dist(ltr, pvg));
        /*
        LTR 54.9513016, -7.6728301
        PVG 31.1434002, 121.8050003
        SYD -33.9460983, 151.177002
        CAI 30.1219006, 31.4055996
        */

        Airplane[] planes = {};
        //System.out.println("  ------The initial scheduled aircrafts are------  ");
        for(int i = 0; i < (args.length < 1? 3 : Integer.parseInt(args[0])); i++) {
          Airplane p = new Airplane("p" + i, 400 + rdg.nextInt(200), 100 + rdg.nextInt(260), rdg);
          Airport dest = Airport.pickOtherThan(null);
          Simulator.schedule(new AirportEvent(rdg.nextInt(100), dest, AirportEvent.PLANE_ARRIVES, p));
        }

        Simulator.stopAt(args.length < 2? 500 : Integer.parseInt(args[1]));
        //System.out.println("  ------End of initial scheduled aircrafts------  ");
        //System.out.println("  --------------------------------------  ");
        //System.out.println(" ");
        System.out.println("  -------------------------------------------------  ");
        System.out.println("  ------Start to schedule events by timestamp------  ");

        Simulator.run();
        Airport.print_circling_time();
    }
}
