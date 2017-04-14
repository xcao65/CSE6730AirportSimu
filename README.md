# 6730Project2

To compile the java code in this folder, start a terminal session,
change directory to the Airport root directory, which should contain:

|---README
|---src
|---run.sh

To compile and run, simply run the 'run.sh' file. 

If you want to run the simulation using different arguments, you can use the following command:

java -cp bin AirportSim args[]

* args[]:
*    args[0] - number of airport;
*    args[1] - number of runways for each airport, default is 2;
*    args[2] - number of planes, default is 1000;
*    args[3] - total of minutes for simulation run, default is 2000;
*    args[4] - number of airwayCapacity between two airports, default is 0;
*    args[5] - random seed, default is 0.
*    args[6] - emergency event schedule flag 1 for yes, default is 0;
*    args[7] - emergency event scheduled airport index, default is 0;
*    args[8] - emergency event schedule start_time, default is 500;
*    args[9] - emergency event scheduled duration, default is 600;
**/

for example:

java -cp bin AirportSim 20 3 300 1000 5 0 0 0 500 600


