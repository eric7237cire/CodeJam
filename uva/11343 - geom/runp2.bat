python c:\codejam\codejam\lib\replaceInFile.py main.cpp geom
make clean all  > out.txt 2>&1
cat out.txt
g++ -O2 p2.cpp -o p2.exe
rem g++ -O2 p3.cpp -o p3.exe
g++ -O2 p4.cpp -o p4.exe
rem gen_data.py > data.txt
cat data.txt | project > out.txt
cat data.txt | p2 > out2.txt
rem cat data.txt | p3 > out3.txt
cat data.txt | p4 > out4.txt
diff out.txt out2.txt
rem diff out.txt out3.txt
diff out.txt out4.txt