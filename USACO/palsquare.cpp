/*
ID: eric7231
PROG: palsquare
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
	ofstream fout ("palsquare.out");
    ifstream fin ("palsquare.in");

	
	int B;
    fin >> B;

	for(int i = 1; i <= 300; ++i)
	{
		string num = convert(i, B);
		string conv = convert(i*i, B);
		string rev = conv;
		reverse(rev.begin(), rev.end());

		if (conv == rev)
		{
			fout << num << " " << conv << endl;
		}
	}


    return 0;
}
