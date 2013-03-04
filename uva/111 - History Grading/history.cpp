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
        os << setw(5) << vec[i];
    }
    return os;
}

int main() 
{
#ifndef ONLINE_JUDGE
	freopen ("input.txt","r",stdin);
#endif

	int N;
	scanf("%d", &N);
	
	vi correct(N);
	vi ranks(N);
	
	int rank;
	FOR(i, 0, N)
	{
	    scanf("%d", &rank);
		correct [ rank - 1 ] = i;
		ranks[ i ] = rank - 1;
	}

	//cout << "Ranks correct " << ranks << endl;
	//cout << "Order correct " << correct << endl;

	while(true)
	{
	    vi seq(N);
	    FOR(i, 0, N)
	    {
	        int s = scanf("%d", &rank);

			if (s != 1)
	            return 0;

			//Input is the student given rank for event i
			//Assigned to the correct rank of event i
			seq[ rank - 1] = ranks [i];
	        
	    }
	 
		//cout << "Sequence " << seq << endl;

	    vi lis;
	    lis.pb(seq[0]);
	    
	   
	    
	    FOR(idx, 1, N)
	    {
            vi::iterator it = lower_bound(all(lis), seq[idx]);
		
            int len = distance(lis.begin(), it);

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
                        
		printf("%d\n", lis.size());
	}

	return 0;
}
