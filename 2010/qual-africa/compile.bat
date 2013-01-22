cls
@echo off
rem g++ store-credit.cpp -o store-credit.exe 

rem store-credit.exe sample.in

rem store-credit.exe A-small-practice.in > a_small.txt 

rem store-credit.exe A-large-practice.in > a_large.txt
rem cat a_small.txt

rem g++ reverse-words.cpp -o reverse-words.exe
rem reverse-words.exe B-small-practice.in > b_small.txt
rem reverse-words.exe B-large-practice.in > b_large.txt

g++ -std=c++11 t9-spelling.cpp -o t9-spelling.exe

t9-spelling.exe sample.in > sample.txt

t9-spelling.exe C-small-practice.in > c_small.txt

t9-spelling.exe C-large-practice.in > c_large.txt

