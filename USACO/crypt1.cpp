/*
ID: eric7231
PROG: crypt1
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
using namespace std;

typedef unsigned int uint;

bool isNotAlpha(char c) {
    return !( ('a' <= c && 'z' >= c) || ('A' <= c && 'Z' >= c) ); 
}

char toLowerCase(char c)
{
    return tolower(c);
}

uint getUsedDigits(uint num)
{
	uint ret = 0;
	while(num > 0)
	{
		uint digit = num % 10;
		ret |= 1 << digit;

		num /= 10;
	}

	return ret;
}

int main() {
	ofstream fout ("crypt1.out");
    ifstream fin ("crypt1.in");
		
    int N;
	fin >> N;

	uint digitsAllowed = 0;

	for(int i = 0; i < N; ++i)
	{
		uint d;
		fin >> d;
		digitsAllowed |= 1 << d;
	}

	vector<uint> usedDigitsPerNum(10000);

	for(int i = 0; i < 10000; ++i) {
		usedDigitsPerNum[i] = getUsedDigits(i);
	}

	uint sol = 0;

	for (int a = 100; a <= 999; ++a)
	{
		if ((digitsAllowed & usedDigitsPerNum[a]) != usedDigitsPerNum[a])
			continue;

		for(int b = 10; b <= 99; ++b)
		{
			if ((digitsAllowed & usedDigitsPerNum[b]) != usedDigitsPerNum[b])
				continue;

			int p1 = a * (b % 10);

			if (p1 >= 1000 || ((digitsAllowed & usedDigitsPerNum[p1]) != usedDigitsPerNum[p1]))
				continue;

			int p2 = a * (b / 10);

			if (p2 >= 1000 || ((digitsAllowed & usedDigitsPerNum[p2]) != usedDigitsPerNum[p2]))
				continue;

			int c = a * b;

			if (c >= 10000 || c < 1000 || ((digitsAllowed & usedDigitsPerNum[c]) != usedDigitsPerNum[c]))
				continue;

			//printf("Solution %d * %d = %d + %d = %d\n", a,b,p1,p2,c);
			++sol;
		}
	}
	
	fout << sol << endl;

	return 0;
}
