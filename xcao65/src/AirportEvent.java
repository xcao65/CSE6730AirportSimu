//YOUR NAME HERE

public class AirportEvent extends Event {
    public static final int PLANE_ARRIVES = 0;	//Class Variable
    public static final int PLANE_LANDED = 1;
    public static final int PLANE_DEPARTS = 2;
    public static final int PLANE_START = 3;
    private Airplane m_flight;

    AirportEvent(double delay, EventHandler handler, int eventType, Airplane flight) {
        super(delay, handler, eventType);
        m_flight = flight;
    }

    //return the flight object
    public Airplane getFlight(){
    	return m_flight;
    }

}

