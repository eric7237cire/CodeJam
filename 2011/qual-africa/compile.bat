cls
@echo off

rm *.out

rm close-loop.exe
g++ -std=c++11 close-loop.cpp -o close-loop.exe

rem close-loop.exe sample.in


 close-loop.exe A-small-practice.in 
 close-loop.exe A-large-practice.in 

rm investing.exe
 g++ -std=c++11 investing.cpp -o investing.exe

rem investing.exe sample.in

 investing.exe B-small-practice.in 
 investing.exe B-large-practice.in 

rm build-house.exe
 g++ -std=c++11 build-house.cpp -o build-house.exe

 build-house.exe sample.in

 build-house.exe C-small-practice.in 
 build-house.exe C-large-practice.in 

