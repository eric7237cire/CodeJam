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
typedef vector<bool> vb;
typedef vector<vb> vvb;
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
        os << vec[i] << endl;
    }
    return os;
}

int main() 
{
#ifndef ONLINE_JUDGE
	freopen ("input.txt","r",stdin);
#endif

	string line; 
	std::getline(cin, line);

	int T;
	sscanf(line.c_str(), "%d", &T);

	std::getline(cin, line);
	
	FOR(t, 0, T)
	{
		int r = 1;
		vi seq;
		while(r == 1)
		{
			int h;
			if (!std::getline(cin, line))
				break;
			
			if (1 !=  sscanf(line.c_str(), "%d", &h))
				break;

			seq.pb(h);
		}
		
		if (seq.empty())
			return 0;

		if (t > 0)
			cout << endl;
	 
		//cout << "Sequence " << seq << endl;

	    vi lis;
	    lis.pb(seq[0]);
	    
	    vi dp(seq.size(), 0);
	    
		FOR(idx, 1, seq.size())
	    {
			//lower_bound means everything before is <=, upper bound means strictly greater
			vi::iterator it = upper_bound(all(lis), seq[idx], less<int>());
		
			int len = distance(lis.begin(), it);

			dp[idx] = len;
			
			if (it == lis.end())
			{
				lis.pb(seq[idx]);
			} else {
				//Found an lower element for position it
				assert(seq[idx] < *it );
				*it = seq[idx];
			}

			//cout << "After idx " << idx << " lis " << lis << endl;
		}
		
		int last = numeric_limits<int>::max();
		int cur = lis.size() - 1;
		
		
		//cout << dp << endl;
	
		//In order to have the seq latest in the input
		for(int i = seq.size() - 1; i >= 0; --i)
		{
			if (dp[i] == cur && seq[i] < last)
			{
				last = lis[ cur-- ] = seq[i];			
			}
		}

                        
		printf("Max hits: %d\n", lis.size());
		cout << lis;
	}

	return 0;
}
