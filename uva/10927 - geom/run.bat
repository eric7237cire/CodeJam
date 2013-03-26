python c:\codejam\codejam\lib\replaceInFile.py main.cpp geom
rem python gen_data.py > data.txt
make clean all
cat data.txt | project > output.txt
rem cat input.txt | project