make all
cat input.txt | project > o.txt
diff o.txt out.txt
cat o.txt