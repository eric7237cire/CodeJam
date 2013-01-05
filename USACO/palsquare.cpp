/*
ID: eric7231
PROG: namenum
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

int main() {
	ofstream fout ("namenum.out");
    ifstream fin ("namenum.in");

	ifstream dictIn("dict.txt");

	string N;
    fin >> N;

	std::string line;
	bool any = false;

	while (std::getline(dictIn, line))
	{
		string orig = line;

		for(uint c = 0; c < line.length(); ++c)
		{
			if(line[c] >= 'Z')
				continue;

			if(line[c] == 'Q')
				continue;

			if(line[c] > 'Q')
				line[c]--;

			line[c] = '0' + 2 + (line[c] - 'A') / 3;
		}

		if (line == N) 
		{
			fout << orig << endl;
			any = true;
		}
	}

	if (!any)
	{
		fout << "NONE" << endl;
	}

    return 0;
}
