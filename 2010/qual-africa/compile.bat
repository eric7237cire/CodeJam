cls
@echo off
g++ store-credit.cpp -o store-credit.exe 

rem store-credit.exe sample.in

store-credit.exe A-small-practice.in > a_small.txt 

store-credit.exe A-large-practice.in > a_large.txt
rem cat a_small.txt
