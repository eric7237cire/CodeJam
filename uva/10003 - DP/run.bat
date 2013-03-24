rem python c:\codejam\codejam\lib\replaceInFile.py main.cpp common
python gen_data.py > data.txt
make clean all  > out.txt 2>&1
g++ p2.cpp -o p2.exe
cat data.txt | project > o.txt
cat data.txt | p2 > o2.txt
diff o.txt o2.txt

cat input.txt | project > o3.txt
cat input.txt | p2 > o4.txt
diff o3.txt o4.txt