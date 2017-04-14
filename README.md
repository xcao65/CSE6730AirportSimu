# 6730Project2


** Run:
  There is no argument for this program right now.

** Features:
  1. Airway Capacity:
    each airway has a capacity.
    The capacity matrix is not symetric, which means capacity of A to B has nothing to do with capacity from B to A
  2. Multiple runway


** After Adding Emergency

To compile the java code in this folder, start a terminal session,
change directory to the Airport root directory, which should contain:

|---README
|---src

To compile necessary java classes:

mkdir -p out/debug
javac -d out/debug -sourcepath src  src/AirportSim.java

To Run

java -cp out/debug AirportSim

After Run

rm -rf out/
