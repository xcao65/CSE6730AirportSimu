//YOUR NAME HERE

public class AirportEvent extends Event {
    public static final int PLANE_ARRIVES = 0;
    public static final int PLANE_LANDED = 1;
    public static final int PLANE_DEPARTS = 2;
    public Airplane m_airplane;
    AirportEvent(double delay, EventHandler handler, int eventType) {
        super(delay, handler, eventType);

    }
    public void setM_airplane(Airplane airplane){
        m_airplane=airplane;
    }
    public Airplane getM_airplane(){
        return m_airplane;
    }

}
