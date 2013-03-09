python gen_data.py > data.txt
make all
cat data.txt | project > o1.txt
cat data.txt | p2 > o2.txt
diff o1.txt o2.txt

rem cat data.txt | project 

