//YOUR NAME HERE

import java.util.*;

public class Airport implements EventHandler {

    //TODO add landing and takeoff queues, random variables
    private int[][] m_distance;
    Airport[] m_airports;
    private int m_index; //------------calculate out the distance------------------

    private int m_inTheAir;
    private int m_onTheGround;

    private boolean m_freeToLand;
    private PriorityQueue<AirportEvent> m_queue = new PriorityQueue<>(new Comparator<AirportEvent>() {
        @Override
        public int compare(AirportEvent a1, AirportEvent a2) {
            return Double.compare(a2.queueTime, a1.queueTime);
        }
    });  //------plane to be landed-----------------

    //private double m_flightTime;
    private double m_runwayTimeToLand;
    private double m_requiredTimeOnGround;

    private String m_airportName;

    private double m_sumCirclingTime = 0.0;

    private int m_incoming = 0;     //------------passangers arriving and departing---------
    private int m_outcoming = 0;

    public Airport(String name, int index, double runwayTimeToLand, double requiredTimeOnGround, int[][] distance, Airport[] airports) {
        m_index = index;
        m_distance = distance;
        m_airportName = name;
        m_inTheAir =  0;
        m_onTheGround = 0;
        m_freeToLand = true;
        m_runwayTimeToLand = runwayTimeToLand;
        m_requiredTimeOnGround = requiredTimeOnGround;
        m_airports = airports;
    }

    public String getName() {
        return m_airportName;
    }

    public PriorityQueue<AirportEvent> getQueue()  { return m_queue; }

    public double getM_sumCirclingTime() {
        return m_sumCirclingTime;
    }

    public void setM_sumCirclingTime(double m_sumCirclingTime) {
        this.m_sumCirclingTime = m_sumCirclingTime;
    }

    public int getM_incoming() {
        return m_incoming;
    }

    public void setM_incoming(int m_incoming) {
        this.m_incoming = m_incoming;
    }

    public int getM_outcoming() {
        return m_outcoming;
    }

    public void setM_outcoming(int m_outcoming) {
        this.m_outcoming = m_outcoming;
    }

    public void handle(Event event) {
        AirportEvent airEvent = (AirportEvent)event;
        Airplane plane = airEvent.getM_plane();

        switch(airEvent.getType()) {
            case AirportEvent.PLANE_ARRIVES:
                m_queue.remove(airEvent);    //------remove from the queueing system of this airport------------------
                m_inTheAir++;
                System.out.println(Simulator.getCurrentTime() + ": Plane "+plane.getName()+" arrived at Airport "+m_airportName);
                System.out.println("     Planned circling time is "+airEvent.getM_circlingTime());
                m_sumCirclingTime += airEvent.getM_circlingTime();      //-------add circling time to this airport--------

                AirportEvent landedEvent = new AirportEvent(plane, m_runwayTimeToLand, this, AirportEvent.PLANE_LANDED);
                Simulator.schedule(landedEvent);

                break;

            case AirportEvent.PLANE_DEPARTS:
                m_queue.remove(airEvent);
                m_onTheGround--;

                Random random = new Random();
                plane.setM_numberPassengers(random.nextInt(plane.getM_maxNumberPassengers()));   //---------give random number passagers departing
                System.out.println(Simulator.getCurrentTime() + ": Plane "+plane.getName()+" departs from airport "+m_airportName);
                System.out.println("      "+plane.getM_numberPassengers()+" passangers are departing");
                m_outcoming += plane.getM_numberPassengers();

                //----calculate flighttime by randomly choosing destination-------
                int destination_index=random.nextInt(5);
                while(destination_index==m_index) {
                    destination_index=random.nextInt(5);
                }
                double m_flightTime = m_distance[m_index][destination_index] / plane.getSpeed();
                AirportEvent takeoffEvent = new AirportEvent(plane, m_flightTime, m_airports[destination_index], AirportEvent.PLANE_ARRIVES);
                Simulator.schedule(takeoffEvent);
                break;

            case AirportEvent.PLANE_LANDED:
                m_inTheAir--;
                System.out.println(Simulator.getCurrentTime() + ": Plane "+plane.getName()+" lands at airport "+m_airportName);
                System.out.println("      "+plane.getM_numberPassengers()+" passangers are arriving");
                m_incoming += plane.getM_numberPassengers();
                AirportEvent departureEvent = new AirportEvent(plane, m_requiredTimeOnGround, this, AirportEvent.PLANE_DEPARTS);
                Simulator.schedule(departureEvent);

                break;
        }
    }
}
