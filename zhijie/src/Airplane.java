//YOUR NAME HERE

//TODO add number of passengers, speed

public class Airplane {
    private String m_name;
    private int m_maxNumberPassengers;
    private int m_numberPassengers;
    private int m_speed;    // -------km/h-----------

    public Airplane(String name, int m_capacity, int actual, int speed) {
        m_name = name;
        m_maxNumberPassengers = m_capacity;
        m_numberPassengers = actual;
        m_speed = speed;
    }

    public String getName() {
        return m_name;
    }

    public int getSpeed() {
        return m_speed;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public void setSpeed(int speed) {
        this.m_speed = speed;
    }

    public int getM_maxNumberPassengers() {
        return m_maxNumberPassengers;
    }

    public void setM_maxNumberPassengers(int m_maxNumberPassengers) {
        this.m_maxNumberPassengers = m_maxNumberPassengers;
    }

    public int getM_numberPassengers() {
        return m_numberPassengers;
    }

    public void setM_numberPassengers(int m_numberPassengers) {
        this.m_numberPassengers = m_numberPassengers;
    }
}
