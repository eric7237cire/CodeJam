#cmake  .
#python3 ../../lib/replaceInFile2.py main.cpp graph
make clean all > out.txt 2>&1
cat input.txt | ./project
