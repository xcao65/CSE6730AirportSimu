//YOUR NAME HERE,it's totally a test
//Some one has touched here 
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

    public Airplane(String name, double speed, int num) {
        m_name = name;
        m_speed = speed;
        passengerCapacity = num;
        m_numberPassengers = new Random().nextInt(passengerCapacity);
        trace = new ArrayList<>();
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
    
    public void setCapacity(int num) {
    	passengerCapacity = num;
    }
    
    public int getPassengerNo() {
    	return m_numberPassengers;
	}
    
    public void setPassenger(int num) {
    	m_numberPassengers = num;
    }
    
    public void addTrace(String s) {
    	trace.add(s);
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
