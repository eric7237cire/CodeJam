cls
@echo off

rem g++ -std=c++11 close-loop.cpp -o close-loop.exe

rem close-loop.exe sample.in

rem close-loop.exe A-small-practice.in 
rem close-loop.exe A-large-practice.in 

rem g++ -std=c++11 investing.cpp -o investing.exe

rem investing.exe sample.in

rem investing.exe B-small-practice.in 
rem investing.exe B-large-practice.in 

rm build-house.exe
 g++ -std=c++11 build-house.cpp -o build-house.exe

 build-house.exe sample.in

 build-house.exe C-small-practice.in 
 build-house.exe C-large-practice.in 

