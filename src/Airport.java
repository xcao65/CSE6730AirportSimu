//YOUR NAME HERE

import java.util.Random;

public class Airport implements EventHandler {

    //TODO add landing and takeoff queues, random variables
    Random rand=new Random();
    static Airplane p747=new Airplane("747",220,"ord","lax");
    static Airplane p748=new Airplane("748",230,"las","sfo");
    static Airplane p749=new Airplane("749",225,"sea","ord");
    int rand1=rand.nextInt(300);
    int rand2=rand.nextInt(300);
    int rand3=rand.nextInt(300);

    private int m_inTheAir;
    private int m_onTheGround;

    private boolean m_freeToLand;


    private double m_flightTime;
    private double m_runwayTimeToLand;
    private double m_requiredTimeOnGround;

    private String m_airportName;

    public Airport(String name, double runwayTimeToLand, double requiredTimeOnGround, double flightTime) {
        m_airportName = name;
        m_inTheAir =  0;
        m_onTheGround = 0;
        m_freeToLand = true;
        m_runwayTimeToLand = runwayTimeToLand;
        m_requiredTimeOnGround = requiredTimeOnGround;
        m_flightTime = flightTime;
        p747.setM_numberPassengers(rand1);
        p748.setM_numberPassengers(rand2);
        p749.setM_numberPassengers(rand3);
    }

    public String getName() {
        return m_airportName;
    }

    public void handle(Event event) {
        AirportEvent airEvent = (AirportEvent)event;

        switch(airEvent.getType()) {
            case AirportEvent.PLANE_ARRIVES:
                m_inTheAir++;
                System.out.println(Simulator.getCurrentTime() + ": Plane arrived at airport");
                if(m_freeToLand) {
                    AirportEvent landedEvent = new AirportEvent(m_runwayTimeToLand, this, AirportEvent.PLANE_LANDED);
                    Simulator.schedule(landedEvent);
                }
                break;

            case AirportEvent.PLANE_DEPARTS:
                m_onTheGround--;
                System.out.println(Simulator.getCurrentTime() + ": Plane departs from airport");
                AirportEvent takeoffEvent = new AirportEvent(m_flightTime, this, AirportEvent.PLANE_ARRIVES);
                Simulator.schedule(takeoffEvent);
                break;

            case AirportEvent.PLANE_LANDED:
                m_inTheAir--;
                System.out.println(Simulator.getCurrentTime() + ": Plane lands at airport");
                AirportEvent departureEvent = new AirportEvent(m_requiredTimeOnGround, this, AirportEvent.PLANE_DEPARTS);
                Simulator.schedule(departureEvent);
                if(m_inTheAir != 0)
                {
                    AirportEvent landingEvent = new AirportEvent(m_runwayTimeToLand, this, AirportEvent.PLANE_LANDED);
                    Simulator.schedule(landingEvent);
                }
                else
                {
                    m_freeToLand = true;
                }
                break;
        }
    }
}
