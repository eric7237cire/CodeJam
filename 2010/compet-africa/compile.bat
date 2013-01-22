cls
@echo off
rem g++ -std=c++11 oddman-out.cpp -o oddman-out.exe 

rem oddman-out.exe A-small-practice.in > a_small.txt 

rem oddman-out.exe A-large-practice.in > a_large.txt


 g++ -std=c++11 get-to-work.cpp -o get-to-work.exe 
 rem 2>&1 | less
rem get-to-work.exe sample.in
  get-to-work.exe B-small-practice.in > b_small.txt
 get-to-work.exe B-large-practice.in > b_large.txt

rem g++ -std=c++11 t9-spelling.cpp -o t9-spelling.exe

rem t9-spelling.exe sample.in > sample.txt

rem t9-spelling.exe C-small-practice.in > c_small.txt

rem t9-spelling.exe C-large-practice.in > c_large.txt

