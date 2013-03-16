make all
python gen_data.py
cat input.txt | project > out.txt
diff out.txt correct.txt