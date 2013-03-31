#cmake  .
#python3 ../../lib/replaceInFile2.py main.cpp geom
make clean all
#g++ -O2 p2.cpp -o p2
#g++ -O2 p3.cpp -o p3
python3 gen_data.py > input.txt
cat input.txt | ./p3 > out3.txt
cat input.txt | ./p2 > out2.txt
cat input.txt | ./project > out.txt

diff out.txt out2.txt
diff out.txt out3.txt

#cat out.txt
