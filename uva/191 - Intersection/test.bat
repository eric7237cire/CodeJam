rem python gen_data.py > in.txt

g++ -DONLINE_JUDGE -O2 intersection.cpp -o p0.exe

g++ -DONLINE_JUDGE -O2 p1.cpp -o p1.exe

cat in.txt | p0 > o.txt
cat in.txt | p1 > o2.txt


diff o.txt o2.txt
