python3 ../../lib/replaceInFile2.py main.cpp geom
make clean all  > out.txt 2>&1
cat out.txt
# g++ -O2 p2.cpp -o p2.exe
g++ -O2 p3.cpp -o p3.exe

python3 gen_data.py > data.txt
cat data.txt | ./project > out.txt
#rem cat data.txt | p2 > out2.txt
cat data.txt | ./p3.exe > out3.txt
#rem diff out.txt out2.txt

diff out.txt out3.txt