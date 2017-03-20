//YOUR NAME HERE

//TODO add number of passengers, speed

public class Airplane {
    private String m_name;
    private int m_numberPassengers;
    private int m_minspeed;
    private String m_start;
    private String m_end;

    public Airplane(String name,int minspeed,String start,String end) {
        m_name = name;
        m_minspeed=minspeed;
        m_start=start;
        m_end=end;

    }
    public void setM_numberPassengers(int n){
        m_numberPassengers=n;
    }
    public int getM_minspeed(){
        return m_minspeed;
    }
    public String getM_name() {
        return m_name;
    }
    public String getM_start(){
        return m_start;
    }
    public String getM_end(){
        return m_end;
    }
}
