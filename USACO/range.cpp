/*
ID: eric7231
PROG: range
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
#include <sstream>
#include <bitset>
#include <iomanip>
#include <cctype>
#include <limits>
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(uint k=(a); k <= (b); ++k)
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
typedef pair<uint,uint> uu;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 


template <class K, class V> 
V getMapValue( const map<K,V>& aMap, const K& key, const K& defaultValue )
{
    typename map<K, V>::const_iterator it = aMap.find(key);
    if ( it == aMap.end() )
        return defaultValue;
    return it->second;
}

uint getIndex(int row, int col, int cols)
{
    return row*cols + col;
}

int main() {
    
	ofstream fout ("range.out");
    ifstream fin ("range.in");
	
    uint N;

    fin >> N;
    
    uvvi squares(N, uvi(N, 0) );
    uvi sqCount(N);
    
    char bit;

    FOR(r, 0, N)
        FOR(c, 0, N)
    {
    	fin >> bit;

        squares[r][c] = bit == '1' ? 1 : 0;
       // cout << squares[r][c];
    }
    
   /* FOR(r, 1, N)
        {
    		FOR(c, 0, N)
    		{
    			cout << setw(6) << squares[r][c];
    		}
    		cout << endl;
        }
*/
    FOR(r, 1, N)
        FOR(c, 1, N)
    {
        if (squares[r][c] == 0)
            continue;
        
        uint up = squares[r-1][c];
        uint upLeft = squares[r-1][c-1];
        uint left = squares[r][c-1];

        squares[r][c] = min(left, min(up, upLeft)) + 1;
    }

    FOR(r, 1, N)
		FOR(c, 1, N)
	{
    	uint size = squares[r][c];
    	FORE(s, 2, size)
    	{
    		sqCount[s-1]++;
    	}
	}

    for(uint size = 2; size <= N; ++size)
    {
    	if (sqCount[size-1] == 0)
    		break;

    	fout << size << " " << sqCount[size-1] << endl;
    }
    /*
    FOR(r, 1, N)
    {
		FOR(c, 0, N)
		{
			cout << setw(6) << squares[r][c];
		}
		cout << endl;
    }*/
            
	
    return 0;
}
