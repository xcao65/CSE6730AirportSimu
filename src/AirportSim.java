//YOUR NAME HERE

import java.util.TreeSet;


public class AirportSim {
    public static void main(String[] args) {
        Airport lax = new Airport("LAX", 10, 10, 20);
        Airport ord = new Airport("ORD", 12, 8, 18);
        Airport sfo = new Airport("SFO", 9, 7, 19);
        Airport sea = new Airport("SEA", 11, 12, 22);
        Airport las = new Airport("LAS", 8, 11, 21);
        AirportEvent landingEvent1 = new AirportEvent(5, lax, AirportEvent.PLANE_DEPARTS);
        landingEvent1.setM_airplane(lax.p747);
        AirportEvent landingEvent2 = new AirportEvent(43, ord, AirportEvent.PLANE_ARRIVES);
        landingEvent2.setM_airplane(lax.p747);
        AirportEvent landingEvent3 = new AirportEvent(6, las, AirportEvent.PLANE_DEPARTS);
        landingEvent3.setM_airplane(las.p748);
        AirportEvent landingEvent4 = new AirportEvent(41, sfo, AirportEvent.PLANE_ARRIVES);
        landingEvent4.setM_airplane(las.p748);
        AirportEvent landingEvent5 = new AirportEvent(8, sea, AirportEvent.PLANE_DEPARTS);
        landingEvent5.setM_airplane(sea.p749);
        AirportEvent landingEvent6 = new AirportEvent(46, ord, AirportEvent.PLANE_ARRIVES);
        landingEvent6.setM_airplane(sea.p749);

        Simulator.schedule(landingEvent1);
        Simulator.schedule(landingEvent2);
        Simulator.schedule(landingEvent3);
        Simulator.schedule(landingEvent4);
        Simulator.schedule(landingEvent5);
        Simulator.schedule(landingEvent6);
        Simulator.stopAt(50);
        Simulator.run();
    }
}
