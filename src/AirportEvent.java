//YOUR NAME HERE

public class AirportEvent extends Event {
    public static final int PLANE_ARRIVES = 0;
    public static final int PLANE_LANDED = 1;
    public static final int PLANE_DEPARTS = 2;
    public static final int PLANE_TAKEOFF = 3;
    // add emergency event type;
    public static final int AIRPORT_EMER_START = 4;
    public static final int AIRPORT_EMER_END = 5;

    private Airplane airplane;
    private int runway;
    private int eventType;
    private double delay;
    private double duration;

    AirportEvent(double delay, EventHandler handler, int eventType, Airplane airplane) {
        super(delay, handler, eventType);
        this.airplane = airplane;
        this.eventType = eventType;
        this.runway = -1; //? what does it do?
        this.delay = delay;
        this.duration = 0;
    }
/*
    // add an airport event constructor
    AirportEvent(double delay, EventHandler handler, int eventType){
        super(delay, handler, evenType);
        this.runway = -1;
        this.eventType = eventType;
        this.airplane = null;
    }
*/
    public int getRunWay() {
        return this.runway;
    }

    public void setRunWay(int runway) {
        this.runway = runway;
    }

    public double getDelay() {
       return this.duration;
    }

    public void setDelay(double d){
       this.duration = d;
    }

    public Airplane checkFlight() {
      if (this.eventType > 3){
        return null;
      } else {
        return this.airplane;
      }
    }

}
