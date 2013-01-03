/*
ID: eric7231
PROG: ride
LANG: C++
*/
#include <iostream>
#include <fstream>
#include <string>

using namespace std;

int findNumber(const string& str) 
{
    int prod = 1;
    for(int d = 0; d < str.length(); ++d) 
    {
        prod *= str[d] - 'A' + 1;
    }
    return prod;
}

int main() {
    ofstream fout ("ride.out");
    ifstream fin ("ride.in");
    string name, dest;
    fin >> name >> dest;
    bool ok = findNumber(name) % 47 == findNumber(dest) % 47;
    fout << (ok ? "GO" : "STAY") << endl;
    return 0;
}
