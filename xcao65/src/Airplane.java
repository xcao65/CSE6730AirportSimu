//YOUR NAME HERE
import java.util.Random;

//TODO add number of passengers, speed

public class Airplane {
    private String m_name;
    private int m_numberPassengers;
    private int m_speed;
    private Airport[] m_destination; 
    Random rand = new Random();

    private double m_arriveTime;    //Time when arrive
    private double m_landingTime;   //Time when begin to land

    public Airplane(String name, int speed, int numberPassengers, Airport[] destination) {
        m_name = name;
        m_speed = speed;
        m_numberPassengers = numberPassengers;
        m_destination = destination;
    }

    public void setArriveTime(double arriveTime) {
        m_arriveTime = arriveTime;
    }

    public double getCircleTime(double landingTime) {
        m_landingTime = landingTime;
        return m_landingTime - m_arriveTime;
    }

    public String getName() {
        return m_name;
    }

    public int getSpeed() {
    	return m_speed;
    }

    public int getNumPassengers(){
    	return m_numberPassengers;
    }

    //get a random destination
    int i;
    public Airport getRandomAirport(){
        i = rand.nextInt(5);
        return m_destination[i];
    }

    public Airport[] getDestinationList(){
        return m_destination;
    }
}
