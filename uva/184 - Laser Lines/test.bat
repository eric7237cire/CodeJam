python gen_data.py > in.txt

g++ -DONLINE_JUDGE -O2 p4.cpp -o p4.exe
cat in.txt | p4 > o.txt

cat in.txt | "..\VisualSolution\Release\184 - Laser Lines.exe" > o2.txt

diff o.txt o2.txt
