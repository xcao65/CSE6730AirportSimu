//YOUR NAME HERE

import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.logging.Handler;

//singleton
public class Simulator {

    //singleton
    private static SimulatorEngine instance = null;

    public static SimulatorEngine getSim() {
        if(instance == null) {
            instance = new SimulatorEngine();
        }
        return instance;
    }

    public static void stopAt(double time) {
        SimulatorEvent stopEvent = new SimulatorEvent(time, getSim(), SimulatorEvent.STOP_EVENT);
        schedule(stopEvent);
    }

    public static void run() {
        getSim().run();
    }

    public static double getCurrentTime() {
        return getSim().getCurrentTime();
    }

    /**
     *  Decision Tree:
     *      1. Is it Airport Event or Stopping Event? If Airport Go to 2, else go to 8.
     *      2. Is it LANDING/DEPARTING event?  If Yes go to 3, else go to 8.
     *      3. Is this Airport Queuing System Empty? If No go to 4, else go to 5 and 8.
     *      4. Event End time = Max(Queue.peek, currentTime)+ lastingTime. Go to 5.
     *      5. Insert the event into the Airport Queuing System. LANDING or DEPARTING? if Landing go 6, else go 7.
     *      6. Queuing time is the same as the end time.
     *      7. Queuing time is the same as the start time.
     *      8. Set the end time = start time + lasting time.
     */
    public static void schedule(Event event) {
        if(event instanceof AirportEvent && (event.getType()!=AirportEvent.PLANE_LANDED)) {
            EventHandler tmp = event.getHandler();

            if(tmp instanceof Airport) {

                PriorityQueue<AirportEvent> tmpQueue =  ((Airport) tmp).getQueue();


                if(!tmpQueue.isEmpty()) {
                    double timeUsed = Math.max(tmpQueue.peek().queueTime, getSim().getCurrentTime());
                    event.setTime(timeUsed + event.getTime());
//                    event.setTime(event.getTime() + tmpQueue.peek().queueTime);
                    if(event.getType()==AirportEvent.PLANE_ARRIVES && tmpQueue.peek().queueTime > getSim().getCurrentTime()) {
                        ((AirportEvent) event).setM_circlingTime(tmpQueue.peek().queueTime - getSim().getCurrentTime());
                    }
                } else {
                    event.setTime(event.getTime() + getSim().getCurrentTime());
                }

                /**
                 * Queuing system use queueTime to order.
                 */
                if(event.getType()==AirportEvent.PLANE_ARRIVES) {
                    ((AirportEvent) event).queueTime = getSim().getCurrentTime() + event.getTime();
                } else {
                    ((AirportEvent) event).queueTime = getSim().getCurrentTime();
                }


                tmpQueue.offer((AirportEvent) event);                     //-------add this event to queue-----------------------
                getSim().schedule(event);
            }

        } else {
            event.setTime(event.getTime() + getSim().getCurrentTime());   //------currentTime I m not comfortable with that---------------
            getSim().schedule(event);
        }

    }

}

