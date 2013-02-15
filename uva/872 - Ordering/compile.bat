g++ -O2 -pipe -DONLINE_JUDGE main.cpp -o main.exe && cat input.txt | main > out.txt 
cat input.txt | m4.exe > out4.txt
rem diff out.txt out2.txt
rem diff out.txt out3.txt
diff out.txt out4.txt
