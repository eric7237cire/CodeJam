#include <Windows.h>

#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <set>
#include <vector>
#include <algorithm>
#include <cassert>
#include <cmath>
using namespace std;

typedef unsigned long long ull;

ull getPentagonal(int n) 
{
	return 1ull * n * (3*n - 1) / 2;
}

bool isPentagonal(ull num)
{
	ull solveForN = 1 + 24ull * num;
	solveForN = sqrt(solveForN);

	solveForN = (1 + solveForN) / 6;

	//solveForN = floor(solveForN) + 0.5;

	int n = (int) solveForN;

	ull check_num = getPentagonal(n);

	return check_num == num;
}

void problem44() 
{
		int upperLimit = 30000;

		set<ull> pentNums;
		vector<ull> pentNumsList(upperLimit);

		for(int n = 1; n <= upperLimit; ++n)
		{
			ull pentNum = getPentagonal(n);
			pentNumsList[n-1] = pentNum;
			pentNums.insert(pentNum);
		}

		for(int j = 1; j <= upperLimit; ++j)
		{
			for(int k = 1; k < j; ++k)
			{
				ull pentJ = pentNumsList[j-1];
				ull pentK = pentNumsList[k-1];

				assert(pentJ > pentK);

				ull sum = pentJ+pentK;
				ull diff = pentJ - pentK;

				if (pentNums.find(sum) != pentNums.end() && pentNums.find(diff) != pentNums.end())
				{
					printf("Pair %llu %llu  Diff = %llu \n", pentJ, pentK, diff);
					return;
				}
			}
		}
	
}

int main() {
	ull start = GetTickCount64();
	problem44();
	ull end = GetTickCount64();

	cout << "Elapsed ms " << end-start << endl;
}