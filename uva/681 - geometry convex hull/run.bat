python c:\codejam\codejam\lib\replaceInFile.py main.cpp
rem python gen_data.py > data.txt
rem g++ p2.cpp -o p2.exe
make all
cat input.txt | project > o.txt
cat input.txt | p2 > o2.txt
diff o.txt o2.txt
cat data.txt | project > o.txt
cat data.txt | p2 > o2.txt
diff o.txt o2.txt