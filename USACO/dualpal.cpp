/*
ID: eric7231
PROG: dualpal
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
using namespace std;

typedef unsigned int uint;

string convert(int num, int base)
{
	string ret;

	while(num > 0)
	{
		int remainder = num % base;

		if (remainder <= 9) 
			ret.insert(ret.begin(), '0' + remainder);
		else
			ret.insert(ret.begin(), 'A' + remainder - 10);

		num /= base;
	}

	return ret;
}
int main() {
	ofstream fout ("dualpal.out");
    ifstream fin ("dualpal.in");

	
	uint N;
	uint S;
    fin >> N >> S;

	
	while(N > 0)
	{
		++S; 
		int palin = 0;
		for(int base = 2; base <= 10 && palin < 2; ++base)
		{
			string num = convert(S, base);
			string rev = num;
			reverse(rev.begin(), rev.end());

			if (rev == num)
				++palin;
		}

		if (palin >= 2) {
			fout << S << endl;
			
			--N;
		}
	}


    return 0;
}
