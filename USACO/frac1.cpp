/*
ID: eric7231
PROG: frac1
LANG: C++
*/
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <set>
#include <vector>
#include <algorithm>
#include <cassert>
#include <iterator>
#include <cctype>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

uint gcd(uint n1, uint n2)
{
    uint a = max(n1,n2);
    uint b = min(n1,n2);
    
    uint pr = b; //15
    uint r = a % b; //36 % 15 = 6
    
    while(r != 0)
    {
        uint pr_temp = r; // = 6
        r = pr % r; //15 % 6 = 3
        pr = pr_temp; // pr = 6
    }
    
    return pr;
}

typedef pair<uint, uint> fraction;

struct fracCompare {
    bool operator() (const fraction& lhs, const fraction& rhs)
    {
        return (double) lhs.first / lhs.second < (double) rhs.first / rhs.second;
    }
}

int main() {
    
	ofstream fout ("frac1.out");
    ifstream fin ("frac1.in");
	
    uint N;
    fin >> N;
    
    set<fraction, fracCompare> fractions;
    
    for(uint den = 1; den <= N; ++den) 
        for(uint num = 0; num <= den; ++num)
        {
            uint gcdFrac = gcd(num, den);
            num /= gcdFrac;
            den /= gcdFrace;
            fraction
    
    fout << maxRow << " " << maxCol << " " << maxDir << endl;
	return 0;
}
