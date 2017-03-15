/**************************
* To run this program,
first, compile all java file:

javac *.java

Second, run AirportSim.class this main class by inputing an argument parameter, this parameter is number of planes for each airport:

java AirportSim 5

Last, it will generate a file named output.txt in the same directory with source code, which has number of passengers and circle time for each airport.

-------------------------------------------------------------------------------------------------------------------
This program is to model plane landing and takeoff operations on airport.
It is a type of discrete event simulation, which is driven by event. In other words

The airport has three types of events:
1.plane arrive
2.plane land
3.plane takeoff

The java code has following main parts:
1. Eventhandler which includes:
	- Eventhandler Interface: Implemented by airport class and simulator enginee, providing an interface for them.

	- Airport.class(key class): It implements logic function needed for three kinds of airport event, by calling Simulator Class to schedule event.

	- SimulatorEngine.class: It is called by simulator class to schedule events. It has a Priority Queue(TreeSet in Java) to schedule all events.

2. Simulator includes:
	- AirportSim.class: main function to get input and call simulator
	
	- Simulator.class: called by AirportSim and calls simulator enginee to 	schedule events

3. Event Class:
	- Event.class: It implements common variable needs for airport events and simulator enginee event.
	- AirportEvent.class: It inherites Event.class and has more variable for airport event type.
	- SimulatorEvent.class: It inherites Event.class and has added stop variable for simulator enginee event(which controls simulation ending process).

4. Airplane Class:
	- airplane.class: It implements variable needed for a typical flight (Capacity, Speed, Name...)
*
**********/