//YOUR NAME HERE

public class AirportEvent extends Event {
    public static final int PLANE_ARRIVES = 0;
    public static final int PLANE_LANDED = 1;
    public static final int PLANE_DEPARTS = 2;
    private Airplane m_plane;
    private double m_circlingTime = 0.0;
    public double queueTime = 0.0;

    AirportEvent(Airplane plane, double delay, EventHandler handler, int eventType) {
        super(delay, handler, eventType);
        m_plane = plane;
    }

    public Airplane getM_plane() {
        return m_plane;
    }

    public void setM_plane(Airplane m_plane) {
        this.m_plane = m_plane;
    }

    public double getM_circlingTime() {
        return m_circlingTime;
    }

    public void setM_circlingTime(double m_circlingTime) {
        this.m_circlingTime = m_circlingTime;
    }

}
