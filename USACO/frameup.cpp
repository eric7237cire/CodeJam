/*
ID: eric7231
PROG: frameup
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
#include <iomanip>
#include <sstream>
#include <bitset>
#include <limits>
#include <cctype>
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef long long ll;
typedef unsigned long long ull;

#define FORE(k,a,b) for(int k=(a); k <= (int)(b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (int)(b); ++k)

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
#define SZ(x) ((int) (x).size())

template <class K, class V> 
V getMapValue( const map<K,V>& aMap, const K& key, const V& defaultValue )
{
    typename map<K, V>::const_iterator it = aMap.find(key);
    if ( it == aMap.end() )
        return defaultValue;
    return it->second;
}


const int notConnected = numeric_limits<int>::max();


template<typename T>
ostream& operator<<( ostream& os, const vector<T>& vec )
{
    FOR(i, 0, vec.size())
    {
        os <<  vec[i] << endl;
    }
    return os;
}

int NumberOfSetBits(int i)
{
    i = i - ((i >> 1) & 0x55555555);
    i = (i & 0x33333333) + ((i >> 2) & 0x33333333);
    return (((i + (i >> 4)) & 0x0F0F0F0F) * 0x01010101) >> 24;
}

void backtrack( vi& curOrder, int usedLetters, const vi& placedBefore,
    int nFrames, int existMask,
    vector<string>& ans)
{
    int numSet = NumberOfSetBits(usedLetters);

    if (numSet == nFrames)
    {
        string str;
        FOR(i, 0, numSet)
        {
            str += 'A' + curOrder[i];
        }
        ans.pb(str);
        return;
    }
    
    FOR(let, 0, 26)
    {
        if ( (usedLetters & placedBefore[let]) != placedBefore[let])
            continue;
        
        if ( ( 1<<let & existMask ) == 0) 
            continue;
        
        //Letter already used
        if ( ( 1<<let & usedLetters ) != 0) 
            continue;
        
        //Here the frame with letter let exists and all the frames before it
        //have been placed
        curOrder[numSet] = let;
        backtrack(curOrder, usedLetters | 1 << let, placedBefore,
            nFrames, existMask, ans);
            
        
    }
    
}


int main() {
    
    
	ofstream fout ("frameup.out");
    ifstream fin ("frameup.in");
	
    int R,C;
    
    fin >> R >> C;
    
    vvi grid(R, vi(C, ' '));
    
    // max[letter]
    vi minMax(2);
    minMax[0] = numeric_limits<int>::max();
    minMax[1] = numeric_limits<int>::min();
    
    vvi frameRow(26, minMax);
    vvi frameCol(26, minMax);
    
    int existMask = 0;
    
    
    
    FOR(r, 0, R) 
    {
        FOR(c, 0, C)
        {
            char ch;
            fin >> ch;
            
            grid[r][c] = ch;
            
            if (ch == '.')
                continue;
            
            int let = ch - 'A';
            if (let < 0 || let >= 26)
                continue;
            
            frameRow[let][1] = max(frameRow[let][1], r);
            frameCol[let][1] = max(frameCol[let][1], c);
            
            frameRow[let][0] = min(frameRow[let][0], r);
            frameCol[let][0] = min(frameCol[let][0], c);
                 
            
            existMask |= 1 << let;
        }
    }
    
    int nFrames = NumberOfSetBits(existMask);
    
    //Walk each frame, computing placedBefore
    
    vi placedBefore(26, 0);
    
    FOR(let, 0, 26)
    {
		if ( (1 << let & existMask) == 0)
            continue;
        
        FORE(i, 0, 1)
        {
			FOR(c, frameCol[let][0], frameCol[let][1])
			{
				int r = i == 0 ? frameRow[let][0]  : frameRow[let][1];

				char ch = grid[r][c];
                int letAbove = ch - 'A';
                assert(letAbove >= 0 && letAbove < 26);
                        
                if (letAbove == let)
                    continue;
                        
                //The frame of 'let' must have been placed before
                //the letter found on it's perimiter
                placedBefore[letAbove] |= 1 << let;
                
			}
            
            FOR(r, frameRow[let][0], frameRow[let][1])
            {
				int c = i == 0 ? frameCol[let][0]  : frameCol[let][1];

                    
                
                char ch = grid[r][c];
                int letAbove = ch - 'A';
                assert(letAbove >= 0 && letAbove < 26);
                        
                if (letAbove == let)
                    continue;
                        
                //The frame of 'let' must have been placed before
                //the letter found on it's perimiter
                placedBefore[letAbove] |= 1 << let;
                
            }
            
        }
    }
    
    vector<string> ans;
    
    vi curOrder(26, -1);
    
    backtrack(curOrder, 0, placedBefore, nFrames, existMask, ans);
    
    sort( all(ans) );
    
    FOR(i, 0, ans.size())
    {
        fout << ans[i] << endl;
    }
    return 0;
}
