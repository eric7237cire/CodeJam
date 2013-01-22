cls
@echo off
rem g++ -std=c++11 oddman-out.cpp -o oddman-out.exe 

rem oddman-out.exe A-small-practice.in > a_small.txt 

rem oddman-out.exe A-large-practice.in > a_large.txt


 rem g++ -std=c++11 get-to-work.cpp -o get-to-work.exe 
 rem 2>&1 | less
rem get-to-work.exe sample.in
 rem get-to-work.exe B-small-practice.in > b_small.txt
 rem get-to-work.exe B-large-practice.in > b_large.txt

g++ -std=c++11 qual-round.cpp -o qual-round.exe

qual-round.exe sample.in > sample.txt

qual-round.exe C-small-practice.in > c_small.txt

rem qual-round.exe C-large-practice.in > c_large.txt

