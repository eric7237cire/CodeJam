python c:\codejam\codejam\lib\replaceInFile.py main.cpp graph
make clean all  > out.txt 2>&1
cat out.txt

cat input.txt | project

goto end2

:end2

python gen_data.py > data.txt
cat data.txt | project > out.txt
cat data.txt | p2 > out2.txt
diff out.txt out2.txt

:end
