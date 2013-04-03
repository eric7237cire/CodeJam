rem python c:\codejam\codejam\lib\replaceInFile.py %1.cpp math
cmake -G "MSYS Makefiles" . 
make all
cat %1.txt | %1
rem g++ p2.cpp -o p2.exe
rem cat %1.txt | p2