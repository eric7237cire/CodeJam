make all
g++ main2.cpp -o main2.exe
cat input.txt | main2.exe > o2.txt
cat input.txt | project > o.txt
diff o.txt o2.txt
cat o.txt