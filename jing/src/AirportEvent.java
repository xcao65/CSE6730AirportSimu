//YOUR NAME HERE
/**
 * Author: Jing Gu (jgu47)
 * Date: 02/13/2017
*/
public class AirportEvent extends Event {
    public static final int PLANE_ARRIVES = 0;
    public static final int PLANE_LANDED = 1;
    public static final int PLANE_DEPARTS = 2;
    public static final int PLANE_LEFT = 3;

    private static String[] events = {"ARRIVES", "LANDED", "DEPARTS", "LEFT"};

    private Airplane _plane;

    AirportEvent(double delay, EventHandler handler, int eventType, Airplane p) {
        super(delay, handler, eventType);
        _plane = p;
    }

    public Airplane getPlane() {
      return _plane;
    }

    public int next_type() {
      return getType() < PLANE_LEFT? (getType() + 1) : -1;
    }

    @Override
    public String toString() {
      return new StringBuilder("AirportEvent [m_time = ").append(getTime())
        .append(", airport = ").append(getHandler())
        .append(", plane = ").append(_plane)
        .append(", type = ").append(events[getType()])
        .toString();
    }
}
