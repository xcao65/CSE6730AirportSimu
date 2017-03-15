//YOUR NAME HERE

public class Event implements Comparable<Event> {
    private EventHandler m_handler; //each event has a handler
    private double m_time;  //time this event takes, given at beginning for onGround and landing
    private int m_eventId;  //define automatically 1,2,3,4,5...
    private int m_eventType;   //defined when initiate: arriving, landing, departing
    static private int m_nextId = 0;

    Event() {
        m_eventId = m_nextId++;

    }

    Event(double delay, EventHandler handler, int eventType) {
        this();
        m_time = delay;
        m_handler = handler;
        m_eventType = eventType;
    }

    public int getId() {
        return m_eventId;
    }

    public double getTime() {
        return m_time;
    }

    public void setTime(double time) {
        m_time = time;
    }

    public EventHandler getHandler() {
        return m_handler;
    }

    public int getType() { return m_eventType; }

    public void setHandler(EventHandler handler) {
        m_handler = handler;
    }

    //Compare the event's order
    //Return the difference of order
    @Override
    public int compareTo(final Event ev) {
        int timeCmp = Double.compare(m_time, ev.getTime());
        if(timeCmp != 0) {
            return timeCmp;
        }
        else
            return Integer.compare(m_eventId, ev.getId());
    }
}
