#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <stack>
#include <set>
#include <list>
#include <vector>
#include <algorithm>
#include <cassert>
#include <iterator>
#include <sstream>
#include <complex>
#include <bitset>
#include <iomanip>
#include <cctype>
#include <limits>
#include <numeric>
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(int k=(a); k <= (int)(b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (int) (b); ++k)

#define pb push_back 
#define mp make_pair 

typedef vector<int> vi; 
typedef vector<double> vd;
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef vector<vd> vvd;
typedef pair<int,int> ii;
typedef vector<pair<int,int> > vii;
typedef vector<vii> vvii;
typedef pair<uint,uint> uu;
typedef map<string, int> msi;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 

template<typename T>
ostream& operator<<( ostream& os, const vector<T>& vec )
{
    FOR(i, 0, vec.size())
    {
        os << setw(5) << vec[i];
    }
    return os;
}

int main2()
{
	int size = 4;
	int ints[] = {9, 8, 5, 2};
	
	int* lb = lower_bound( ints, ints+size, 1, greater<int>() );

	cout << *lb << endl;
	return 0;
}

int main() 
{
#ifndef ONLINE_JUDGE
	
	freopen ("input.txt","r",stdin);
#endif
	
	
	int n;
	
	
	while(true)
	{
		scanf("%d", &n);

		if (n == 0)
			break;

		vi deg;
		deg.reserve(n);

		int ok = 1;
		int sum = 0;

		FOR(i, 0, n)
		{
			int d;
			scanf("%d", &d);
			deg.pb(d);

			//Degree must be positive and less than n
			ok &= d >= 0 && d < n;

			//Use exclusive or to track if sum is even or odd
			sum ^= d & 1;
		}

		ok &= (sum == 0);

		sort(all(deg));
		reverse(all(deg));

		//In order to avoid looping from k+1 to n
		vi sumToEnd(n, 0);
		sumToEnd[deg.size() - 1] = deg[deg.size() - 1 ];
		for(int i = deg.size() - 2; i >= 0; --i)
		{
			sumToEnd[i] = sumToEnd[i+1] + deg[i];
		}
		
		sum = 0;
		for(int k = 0; k < n && ok; ++k)
		{
			sum += deg[k];
			//printf("k=%d\n", k);
			
			int rhs = k * (k+1);

			/*
			int checkSum = 0;
			for(int j = k+1; j < n; ++j)
			{
				checkSum += min(k+1, deg[j]);
			}*/

			int rhsSum = 0;

			if (k + 1 < n)
			{
				rhsSum = (n - k - 1) * (k+1);

				//Find first element not greater than k
				vi::iterator it = lower_bound( all(deg), k, greater<int>() );

				if (it != deg.end())
				{
					//Add degrees that are smaller than k

					//First remove the excess sum
					int idx = distance(deg.begin(), it);

					//Since we are summing [k+1, n) we must at least be at k+1
					idx = max(idx, k+1);
					
					int num = deg.size() - idx;
					//Subtract eveything up from [index, n)
					//k is 0 based, but the k we use is not
					rhsSum -= (k+1) * num;

					//Then add the actual degree sum 
					rhsSum += sumToEnd[idx];

					//assert(rhsSum == checkSum);	
				}
			}

			
			
			ok &= sum <= rhs + rhsSum;
		}

		
		


		//cout << deg << endl;
		cout << (ok ? "Possible" : "Not possible") << endl;
	}

	return 0;
}