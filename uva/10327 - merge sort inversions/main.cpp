#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
#include <stdlib.h>
#include <vector>
#include <iostream>
#include <iomanip>
using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 

typedef vector<int> vi;

ostream& operator<<( ostream& os, const vi& vec )
{
	for(int i = 0; i < vec.size(); ++i)
	{
		os << setw(5) << vec[i];
	}
	
	return os;
}

int merge( vi& arr, const vi& left, const vi& right) 
{
	int i = 0, j = 0, count = 0;
	while (i < left.size() || j < right.size()) 
	{
		if (i == left.size()) {
			arr[i+j] = right[j];
			j++;
		} else if (j == right.size()) {
			arr[i+j] = left[i];
			i++;
		} else if (left[i] <= right[j]) {
			arr[i+j] = left[i];
			i++;                
		} else {
			arr[i+j] = right[j];
			count += left.size()-i;
			j++;
		}
	}
	//cout << "merge " << arr << " ; " << left << " ; " << right << " ; " << count << endl;
	return count;
}

int invCount( vi& arr) 
{
	if (arr.size() < 2)
		return 0;

	vi orig(arr);
	//
	
	int m = (arr.size() + 1) / 2;
	vi left( arr.begin(), arr.begin() + m );
	vi right( arr.begin() + m, arr.end() );

	int invCountLeft = invCount(left);
	int invCountRight = invCount(right);
	int mergeCount = merge(arr, left, right);
	int ret = invCountLeft+invCountRight+mergeCount;
	
	//cout << "\nOrig array: (" << orig.size() << ") = " << orig << endl;
	//printf("Returning %d + %d + %d = %d\n Array after: ", invCountLeft, invCountRight, mergeCount, ret);
	//cout << arr << endl;
	
	return ret;
}

int main()
{
	int T;
	
	int N;
	while(1 == scanf("%d", &N))
	{
		vi input(N);
		for(int i = 0; i < N; ++i)
		{
			scanf("%d", &input[i]);
		}
			
		//cout << input << endl;
		int ans = invCount(input);
		//printf("N = %d\n", N);
		printf("Minimum exchange operations : %d\n", ans);

	}
	
	
	return 0;
}