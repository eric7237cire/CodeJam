rem python gen_data.py > in.txt

g++ -DONLINE_JUDGE -O2 lining-up.cpp -o p0.exe
rem g++ -DONLINE_JUDGE -O2 lining-up-slow.cpp -o p1.exe
g++ -DONLINE_JUDGE -O2 p2.cpp -o p2.exe
rem g++ -DONLINE_JUDGE -O2 p1.cpp -o p1.exe

cat in.txt  | p0 > o1.txt
rem cat in.txt  | p1 > o2.txt
cat in.txt  | p2 > o3.txt

rem cat in.txt | p1 > o2.txt


rem diff o1.txt o2.txt
diff o1.txt o3.txt
