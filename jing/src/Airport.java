//YOUR NAME HERE
/**
 * Author: Jing Gu (jgu47)
 * Date: 02/13/2017
*/
import java.util.ArrayList;
import java.util.Random;
import java.util.Queue;
import java.util.PriorityQueue;

public class Airport implements EventHandler {

    //TODO add landing and takeoff queues, random variables

    private double m_runwayTimeToLand;
    private double m_requiredTimeOnGround;
    private double _time_to_take_off;

    private String m_airportName;

    private double _lat;
    private double _lon;
    private boolean _busy;
    private double _cir_sum;
    private int _in_passengers;
    private int _out_passengers;
    private Queue<AirportEvent> _circling; // Those events are just records
    private Queue<AirportEvent> _departing; // for convenience, won't schedule
    private Queue<AirportEvent> _pending;

    private static ArrayList<Airport> ports = new ArrayList<Airport>();
    private static double R =  6371;
    private static Random gen;
    public static void initRandomGenerator(Random val) {
      gen = val == null? new Random() : val;
    }

    /**
     * Return the distance (in km) between 2 airports
     *
     **/
    public static double dist(Airport a, Airport b) {
      double d1 = b.getLattitude() - a.getLattitude();
      double d2 = b.getLongitude() - a.getLongitude();
      double a0 = Math.sin(d1 / 2) * Math.sin(d1 / 2)
            + Math.cos(a.getLattitude()) * Math.cos(b.getLattitude())
            * Math.sin(d2 / 2) * Math.sin(d2 / 2);
      double c = 2 * Math.atan2(Math.sqrt(a0), Math.sqrt(1 - a0));
      return R * c;
    }

    public static void print_circling_time() {
      for(Airport ap: ports) {
        System.out.println(ap.elaborate());
      }
    }

    /**
     * Pick a remote airport that is differnet from the current airport (p)
     *
     **/
    public static Airport pickOtherThan(Airport p) {
      while(true) {
        int idx = gen.nextInt(ports.size());
        if(ports.get(idx) != p) {
          // System.out.println(p + " - > " + ports.get(idx));
          return ports.get(idx);
        }
      }
    }

    public Airport(String name, double runwayTimeToLand,
            double requiredTimeOnGround, double lat, double lon, int stg) {
        m_airportName = name;
        m_runwayTimeToLand = runwayTimeToLand;
        _time_to_take_off = runwayTimeToLand; // TODO: if use a different value
        m_requiredTimeOnGround = requiredTimeOnGround;
        if(stg == 1)
          _pending = new PriorityQueue<AirportEvent>(1, new DepartingFirst());
        else if(stg == 2)
          _pending = new PriorityQueue<AirportEvent>(1, new TimeFirst());
        else // default is landing first
          _pending = new PriorityQueue<AirportEvent>(1, new LandingFirst());
        _lat = Math.toRadians(lat);
        _lon = Math.toRadians(lon);
        _busy = false;
        _cir_sum = 0;
        // Keep track of each airport for remote selection.
        ports.add(this);
    }

    public String getName() {
        return m_airportName;
    }

    public double getLattitude() {
      return _lat;
    }

    public double getLongitude() {
      return _lon;
    }

    public void handle(Event event) {
        AirportEvent airEvent = (AirportEvent)event;

        switch(airEvent.getType()) {
            case AirportEvent.PLANE_ARRIVES:
                System.out.println(Simulator.getCurrentTime() + ": " + airEvent.getPlane() + " arrived at " + this);
                if(_busy) push(airEvent);
                else Simulator.schedule(new AirportEvent(m_runwayTimeToLand, this, AirportEvent.PLANE_LANDED, airEvent.getPlane()));
                _busy = true;
                break;

            case AirportEvent.PLANE_DEPARTS:
                System.out.println(Simulator.getCurrentTime() + ": " + airEvent.getPlane() + " request departing from " + this);
                airEvent.getPlane().board_passengers();
                if(_busy) push(airEvent);
                else Simulator.schedule(new AirportEvent(_time_to_take_off, this, AirportEvent.PLANE_LEFT, airEvent.getPlane()));
                _busy = true;
                break;

            case AirportEvent.PLANE_LANDED:
                System.out.println(Simulator.getCurrentTime() + ":" + airEvent.getPlane()  + " lands at " + this);
                // Adding departs (request) event, no matter what
                _in_passengers += airEvent.getPlane().numPassengers();
                Simulator.schedule(new AirportEvent(m_requiredTimeOnGround, this, AirportEvent.PLANE_DEPARTS, airEvent.getPlane()));
                _busy = false; // could be overriden by check_pending()
                // run way is released for possible pending planes
                pop();
                break;
            case AirportEvent.PLANE_LEFT:
                // Generate event of remote arrival, no matter what
                _out_passengers += airEvent.getPlane().numPassengers();
                Airport dest = pickOtherThan(this);
                double delay = airEvent.getPlane().timeNeededForDistance(dist(this, dest));
                System.out.println(Simulator.getCurrentTime() + ": " + airEvent.getPlane() + " has left " + this + " for " + dest + ", total flying time " + delay);
                Simulator.schedule(new AirportEvent(delay, dest, AirportEvent.PLANE_ARRIVES, airEvent.getPlane()));
                _busy = false; // could be overriden by check_pending()
                // run way is released for possible pending planes
                pop();
                break;
        }
    }

    private void push(AirportEvent cur) {
      int next_type = cur.next_type();
      if(next_type == -1) return;
      // Use absolute time for event to be queued, will be corrected later
      // depending on the eventType
      _pending.offer(new AirportEvent(Simulator.getCurrentTime(), this,
                      next_type, cur.getPlane()));
    }

    private void pop() {
      _busy = _pending.peek() != null;
      if(!_busy) return;
      AirportEvent next = _pending.poll();
      if(next.getType() == AirportEvent.PLANE_LANDED)
        _cir_sum += (Simulator.getCurrentTime() - next.getTime());
      next.setTime(next.getType() == AirportEvent.PLANE_LANDED?
                    m_runwayTimeToLand : _time_to_take_off);
      Simulator.schedule(next);
    }

    public String elaborate() {
      return new StringBuilder(this.toString())
        .append(" - timeToLand = ").append(m_runwayTimeToLand)
        .append(", timeToTakeOff = ").append(_time_to_take_off)
        .append(", timeOnGround = ").append(m_requiredTimeOnGround)
        .append(", in traffic = ").append(_in_passengers)
        .append(", out traffic = ").append(_out_passengers)
        .append(", circling time = ").append(_cir_sum)
        .toString();
    }

    @Override
    public String toString() {
      return "Airport [" + m_airportName + "]";
    }
}
