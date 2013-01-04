/*
ID: eric7231
PROG: milk2
LANG: C++
*/
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <vector>
#include <algorithm>
#include <cassert>
using namespace std;


int main() {
	ofstream fout ("milk2.out");
    ifstream fin ("milk2.in");

	assert(fin);

    int N;
    fin >> N;
    
	vector<int> start;
	vector<int> stop;

	for(int i = 0; i < N; ++i) 
	{
		int begin, end;
		fin >> begin  >> end;
		start.push_back(begin);
		stop.push_back(end);
	}

	sort(start.begin(), start.end());
	sort(stop.begin(), stop.end());

	int cowsMilked = 1;
	
	int pStart = 1;
	int pStop = 0;

	int longestNoMilking = 0;
	int longestMilking = 0;

	int lastChange = start[0];

	while(pStop < N)
	{
		if (pStart < N && start[pStart] <= stop[pStop]) 
		{
			if (cowsMilked == 0) 
			{
				cout << "End of no milking at time " << start[pStart] << endl;
				int noMilking = start[pStart] - lastChange;
				longestNoMilking = max(noMilking, longestNoMilking);
				lastChange = start[pStart];
			}

			++cowsMilked;
			++pStart;
		} else {

			--cowsMilked;

			if (cowsMilked == 0) 
			{
				cout << "Start of no milking at time " << stop[pStop] << endl;
				int milking = stop[pStop] - lastChange;
				longestMilking = max(milking, longestMilking);
				lastChange = stop[pStop];
			}

			++pStop;
		}
	}

	fout << longestMilking << " " << longestNoMilking << endl;

    return 0;
}
