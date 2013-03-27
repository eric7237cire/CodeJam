python c:\codejam\codejam\lib\replaceInFile.py main.cpp graph
make clean all  > out.txt 2>&1
python gen_data.py > data.txt
rem cat data.txt | project

cat input.txt | project