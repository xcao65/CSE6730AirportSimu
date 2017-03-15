This is the README file for CSE-6730-Spring 2017 Project 1
Author by: Jing Gu
Date: 02/13/2017

[!NOTE!] The corresponding JAVA code, if not specified, compiles and runs in bash.

Part 0 - File Hierarchy

|---Project1_jgu47.pdf
|---README
|---commands.sh
|---src (folder for .java files)
    |---Airplane.java
    |---Airport.java
    |---AirportEvent.java
    |---DepartingFirst.java
    |---Event.java
    |---EventHandler.java
    |---LandingFirst.java
    |---Simulator.java
    |---SimulatorEngine.java
    |---SimulatorEvent.java
    |---TimeFirst.java
|---result (folder for .txt outputs)
|---out (folder for java class files)
|---Airport.iml


Part 1 - Compilation

To compile the java code in this folder, start a terminal session,
change directory to the Airport root directory, which should contain:
|---Project1_jgu47.pdf
|---README
|---commands.sh
|---src

To compile necessary java classes:
mkdir -p out/debug
javac -d out/debug -sourcepath src  src/AirportSim.java


Part 2 - Run

To run and redirect output, the parameters are:
java -cp out/debug AirportSim <# of planes> <# of minutes to simulate> <random seed> <queuing strategy>

<queuing strategy> : 0 for land first, 1 for departing first, 2 for time first

Example:

1. To simulate 10 aircrafts in a landing first queuing system for 600 minutes using seed 20.
java -cp out/debug AirportSim 10 600 20 0

2. To generate .txt results in result folder 
./commands.sh
