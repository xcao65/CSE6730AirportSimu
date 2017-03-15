# run from the Airport root directory, which should contain:
#    Airport.iml
#    commands.sh
#    result
#    src

# compile necessary java classes
echo "Compiling..."
mkdir -p out/debug
mkdir result
javac -d out/debug -sourcepath src  src/AirportSim.java

# run and redirect output, the parameters are:
# java -cp out/debug AirportSim <# of planes> <# of minutes to simulate> \
#     <random seed> <queuing strategy>

# Use landing first
echo Using \"landing first\" stragety
java -cp out/debug AirportSim 10 6000 20 0 > result/10_6000_20_l.txt
java -cp out/debug AirportSim 20 6000 20 0 > result/20_6000_20_l.txt
java -cp out/debug AirportSim 50 6000 20 0 > result/50_6000_20_l.txt
java -cp out/debug AirportSim 100 6000 20 0 > result/100_6000_20_l.txt
java -cp out/debug AirportSim 200 6000 20 0 > result/200_6000_20_l.txt
# java -cp out/debug AirportSim 15 2000 20 0 > result/15_2000_20_l.txt
# java -cp out/debug AirportSim 100 5000 20 0 > result/100_5000_20_l.txt

# Use departing first
echo Using \"departing first\" stragety
java -cp out/debug AirportSim 10 6000 20 1 > result/10_6000_20_d.txt
java -cp out/debug AirportSim 20 6000 20 1 > result/20_6000_20_d.txt
java -cp out/debug AirportSim 50 6000 20 1 > result/50_6000_20_d.txt
java -cp out/debug AirportSim 100 6000 20 1 > result/100_6000_20_d.txt
java -cp out/debug AirportSim 200 6000 20 1 > result/200_6000_20_d.txt
# java -cp out/debug AirportSim 20 2000 20 1 > result/20_2000_20_d.txt
# java -cp out/debug AirportSim 15 2000 20 1 > result/15_2000_20_d.txt
# java -cp out/debug AirportSim 100 5000 20 1 > result/100_5000_20_d.txt

# Use time_to_queue first
echo Using \"timestamp of queuing first\" stragety
java -cp out/debug AirportSim 10 6000 20 2 > result/10_6000_20_t.txt
java -cp out/debug AirportSim 20 6000 20 2 > result/20_6000_20_t.txt
java -cp out/debug AirportSim 50 6000 20 2 > result/50_6000_20_t.txt
java -cp out/debug AirportSim 100 6000 20 2 > result/100_6000_20_t.txt
java -cp out/debug AirportSim 200 6000 20 2 > result/200_6000_20_t.txt
# java -cp out/debug AirportSim 20 2000 20 2 > result/20_2000_20_t.txt
# java -cp out/debug AirportSim 15 2000 20 2 > result/15_2000_20_t.txt
# java -cp out/debug AirportSim 100 5000 20 2 > result/100_5000_20_t.txt
