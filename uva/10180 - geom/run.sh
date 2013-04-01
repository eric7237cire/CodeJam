#cmake  .
python3 ../../lib/replaceInFile2.py main.cpp geom
make clean all
cat input.txt | ./project
