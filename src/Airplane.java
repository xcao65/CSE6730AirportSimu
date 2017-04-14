//insert your name
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
//TODO add number of passengers, speed

public class Airplane {
    private String m_name;
    private int passengerCapacity;
    private int m_numberPassengers;
    private double m_speed;
    private List<String> trace;
    private int startAirport;
    private int destination;

    public Airplane(String name, double speed, int num) {
        m_name = name;
        m_speed = speed;
        passengerCapacity = num;
        // add range of the number of passengers.
        int base = num/2;
        m_numberPassengers = new Random().nextInt(num-base); // should change seed here!
        m_numberPassengers = base + m_numberPassengers;
        trace = new ArrayList();
    }

    public String getName() {
        return m_name;
    }

    public double getSpeed() {
    	return m_speed;
    }

    public void setSpeed(double speed) {
    	m_speed = speed;
    }

    public int getCapacity() {
    	return passengerCapacity;
    }

    //TODO!!!! change this
    public void setCapacity(int cap) {
    	passengerCapacity = cap;
    }

    public int getPassengerNo() {
    	return m_numberPassengers;
	}

    //TODO!!!! change this
    public void setPassenger(int num) {
    	m_numberPassengers = num;
    }

    public void addTrace(String s) {
    	trace.add(s);
    }

    //setter and getter method for start and destination airport
    public void setStartAirport(int index) {
        startAirport = index;
    }

    public void setDestination(int index) {
        destination = index;
    }

    public int getStartAirport() {
        return startAirport;
    }

    public int getDestination() {
        return destination;
    }

    public void printTrace() {
    	System.out.println("--------------------------------------------------");
    	System.out.println(this.getName());
    	System.out.println("--------------------------------------------------");
    	for(String str: trace) {
    		System.out.println(str);
    	}
    	System.out.println("--------------------------------------------------");
    }

    public void outputTraceFile() {
    	try{
    	    PrintWriter writer = new PrintWriter("output/" + this.getName() + "_trace.txt", "UTF-8");
    	    for(String str: trace) {
        		writer.println(str);
        	}
    	    writer.close();
    	} catch (IOException e) {

    	}
    }
}
