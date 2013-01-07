/*
ID: eric7231
PROG: calfflac
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


int main() {
	ofstream fout ("calfflac.out");
    ifstream fin ("calfflac.in");
		
    string line;
	string orig;
	
    while (std::getline(fin, line))
	{
	    copy(line.begin(), line.end(), back_inserter(orig));
	    orig.append(1, '\n');
	}
	
	string clean;
	vector<uint> cleanToOrig;

	for(uint origIdx = 0; origIdx < orig.length(); ++origIdx)
	{
		if (!isalpha(orig[origIdx])) 
			continue;

		cleanToOrig.push_back(origIdx);
		clean.append(1, tolower(orig[origIdx]));
	}

	int maxPalinLen = 0;
	string palin;
	
	vector<int> nextChange(clean.length());
	vector<int> nextChangeRev;
	
	nextChangeRev.push_back(0);
	char lastChar = tolower(clean[0]);
	
	for(uint p = 1; p < clean.length(); ++p) 
	{
	    if (clean[p] == lastChar) {
	         nextChangeRev.push_back(nextChangeRev[p-1]);   
	    } else {
	        nextChangeRev.push_back(p);
	        lastChar = clean[p];
	    }
	}
	
	lastChar = clean[clean.length() - 1];
	nextChange[clean.length() - 1] = clean.length() - 1;
	
	for(int p = clean.length() - 2; p >= 0; --p)
	{
	    if (clean[p] == lastChar) {
	         nextChange[p] = nextChange[p+1];   
	    } else {
	        nextChange[p] = p;
	        lastChar = clean[p];
	    }
	}
		
	for(uint start = 0; start < clean.length(); ++start)
	{
		for(int offset = 0; offset <= 1; ++offset)
		{
			uint left = start;
			uint right = start + offset;

			int palinLen = offset == 0 ? 1 : 2;

			while(true)
			{
				if (left < 0 || right >= clean.length()) {
					break;
				}

				if (clean[left] == clean[right]) {
					int blockRight = nextChange[right] - right;
					int blockLeft = left - nextChangeRev[left];

					int minBlock = min(blockRight, blockLeft);

					left -= minBlock;
					right += minBlock;
					palinLen+=2*minBlock;

					if (palinLen > maxPalinLen)
					{
						string substr = std::string(orig.begin() + cleanToOrig[left] ,
							orig.begin() + cleanToOrig[right]+1);
						maxPalinLen = palinLen;
						palin = substr;
					}
					
					left --;
					right ++;
					palinLen+=2;
					continue;
				}

				break;
			}


		}
	}

	fout << maxPalinLen << endl;
	fout << palin << endl;

	return 0;
}
