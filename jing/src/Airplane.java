//YOUR NAME HERE
/**
 * Author: Jing Gu (jgu47)
 * Date: 02/13/2017
*/


//TODO add number of passengers, speed
import java.util.Random;
public class Airplane {
    private String m_name;
    private int m_numberPassengers;
    private double _speed;
    private int _max_cap;
    private Random _gen;

    public Airplane(String name, double s, int cap, Random gen) {
        m_name = name;
        _speed = s;
        _max_cap= cap;
        _gen = gen;
        board_passengers();
    }

    public void board_passengers() {
      int base = _max_cap / 2;
      m_numberPassengers = base + _gen.nextInt(_max_cap - base);
    }

    public String getName() {
        return m_name;
    }

    public int numPassengers() {
      return m_numberPassengers;
    }

    /**
     * Return minutes needed to complete the distance
     **/
    public double timeNeededForDistance(double d) {
      return d * 60 / _speed;
    }

    @Override
    public String toString() {
      return "Airplane [" + m_name + "]";
    }

    public void elaborate() {

    }
}
