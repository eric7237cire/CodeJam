cls
@echo off

rem g++ -std=c++11 close-loop.cpp -o close-loop.exe

rem close-loop.exe sample.in

rem close-loop.exe A-small-practice.in 
rem close-loop.exe A-large-practice.in 

g++ -std=c++11 investing.cpp -o investing.exe

investing.exe sample.in

investing.exe B-small-practice.in 
investing.exe B-large-practice.in 

