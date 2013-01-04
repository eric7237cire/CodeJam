/*
ID: eric7231
PROG: beads
LANG: C++
*/
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <vector>
using namespace std;


int main() {
	ofstream fout ("beads.out");
    ifstream fin ("beads.in");
    int N;
    fin >> N;
    
	string str;
	fin >> str;

	cerr << str << " is the string " << endl;

	int maxCollected = 0;

	for(int cutPoint = 0; cutPoint < N; ++cutPoint)
	{
		//cut right before cutpoint
		int collectedForward = 0;
		int forwardPos = cutPoint; 
		int redBlue = 0;

		while(collectedForward < N && redBlue != 3) 
		{
			if (str[forwardPos] == 'b')
				redBlue |= 2;

			if (str[forwardPos] == 'r')
				redBlue |= 1;

			++collectedForward;

			++forwardPos;

			if (forwardPos == N) 
				forwardPos = 0;
		}

		redBlue = 0;
		int collectedBackward = 0;
		int backwardPos = cutPoint - 1;
		if (backwardPos < 0)
			backwardPos = N - 1;

		while(collectedBackward < N && redBlue != 3) 
		{
			if (str[backwardPos] == 'b')
				redBlue |= 2;

			if (str[backwardPos] == 'r')
				redBlue |= 1;

			++collectedBackward;

			--backwardPos;

			if (backwardPos < 0) 
				backwardPos = N - 1;
		}

		int collected = collectedForward - 1 + collectedBackward - 1;

		cout << "Cut point " << cutPoint << " Collected " << collected << endl;
		maxCollected = max(maxCollected, collected);
	}
    
	fout << min(N, maxCollected) << endl;

    return 0;
}
