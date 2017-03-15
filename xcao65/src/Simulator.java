//YOUR NAME HERE

import java.util.TreeSet;

//singleton
public class Simulator {

    //singleton
    private static SimulatorEngine instance = null;
    //Class variable is built by default

    public static SimulatorEngine getSim() {
        if(instance == null) {
            instance = new SimulatorEngine();
        }
        return instance;
    }

    public static void stopAt(double time) {
        Event stopEvent = new SimulatorEvent(time, getSim(), SimulatorEvent.STOP_EVENT);
        schedule(stopEvent);
    }

    public static void run() {
        getSim().run();
    }

    public static double getCurrentTime() {
        return getSim().getCurrentTime();
    }

    //Set event happening time;
    //Event happening time = event delay time + current time
    public static void schedule(Event event) {
        event.setTime(event.getTime() + getSim().getCurrentTime());
        getSim().schedule(event);
    }
}

//Simulator invokes simulator enginee, 
//Input is AirpotEvent with Airport(EventHandler)
//
