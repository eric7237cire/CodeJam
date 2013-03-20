make all
g++ p2.cpp -o p2.exe
cat input.txt | bf > o2.txt
cat input.txt | p2 > o3.txt
cat input.txt | project > o.txt
rem cat sample_in.txt | project > so.txt
rem diff so.txt sample_out.txt
diff o.txt o3.txt