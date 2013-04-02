python c:\codejam\codejam\lib\replaceInFile.py %1.cpp common
cmake -G "MSYS Makefiles" . 
make all
cat %1.txt | %1
g++ p2.cpp -o p2.exe
cat %1.txt | p2