//YOUR NAME HERE

public class AirportEvent extends Event {
    public static final int PLANE_ARRIVES = 0;
    public static final int PLANE_LANDED = 1;
    public static final int PLANE_DEPARTS = 2;
    public static final int PLANE_TAKEOFF = 3;
    private Airplane airplane;
    private int runway;
    AirportEvent(double delay, EventHandler handler, int eventType, Airplane airplane) {
        super(delay, handler, eventType);
        this.airplane = airplane;
        runway = -1;
    }
    public int getRunWay() {
        return runway;
    }
    
    public void setRunWay(int runway) {
        this.runway = runway;
    }
    
    public Airplane checkFlight() {
    	return airplane;
    }
    
}
