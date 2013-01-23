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

g++ -std=c++11 close-loop.cpp -o close-loop.exe

close-loop.exe sample.in 

close-loop.exe A-small-practice.in 

close-loop.exe A-large-practice.in 

