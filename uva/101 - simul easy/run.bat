make all
cat input.txt | project > out.txt
cat input.txt | p3 > o3.txt
diff out.txt o3.txt
rm out.txt 
rm o3.txt