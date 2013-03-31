#cmake  .
#python3 ../../lib/replaceInFile2.py main.cpp common
make clean all > out.txt 2>&1 
cat out.txt
cat input.txt | ./project
