python c:\codejam\codejam\lib\replaceInFile.py main.cpp common
make clean all  > out.txt 2>&1
cat input.txt | project > out.txt
diff out.txt correct.txt