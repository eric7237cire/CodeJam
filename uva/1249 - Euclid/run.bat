g++ -DONLINE_JUDGE euclid.cpp -o p0.exe
cat input.txt | p0 > out.txt

diff output.txt out.txt
