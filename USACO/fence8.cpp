/*
ID: eric7231
PROG: fence8
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

bool debug = false;

template <class K, class V> 
V getMapValue( const map<K,V>& aMap, const K& key, const K& defaultValue )
{
    typename map<K, V>::const_iterator it = aMap.find(key);
    if ( it == aMap.end() )
        return defaultValue;
    return it->second;
}

//Minimum board index that can match a rail (boards and rails sorted asc)
vi minBoardForRail;

//Total length of rails from 0 to [ ]
vi totalRailLen;

bool dfs( vi& boards, int boardIdx, const vi& rails, 
    int railIdx, const int boardLenLeft)
{
    assert(boardLenLeft >= 0);
    int maxRails = 0;
    
    if (railIdx >= 0 && totalRailLen[railIdx] > boardLenLeft) {
        //cout << "Total rail len " << totalRailLen[railIdx] <<        " idx " << railIdx << endl;
        return false;
    }
    
    FOR(b, boardIdx, boards.size()) 
    {
        if (boards[b] < rails[railIdx])
            continue;
        
        //We are done
        if (railIdx == 0)
            return true;
        
        boards[b] -= rails[railIdx];
        int boardLenUsed = rails[railIdx];
                
        //If what is remaining is completely unusable, remove it
        if (boards[b] < rails[0])
            boardLenUsed += boards[b];
        
        bool found = false;
        
        if (rails[railIdx-1] == rails[railIdx]) {
            found = dfs(boards, b, rails, 
            railIdx-1, boardLenLeft-boardLenUsed);
        } else {
            found = dfs(boards, minBoardForRail[railIdx-1], rails, 
            railIdx-1, boardLenLeft-boardLenUsed);
        }
        
        if (found)
            return true;
        
        boards[b] += rails[railIdx];
                
        
    }
    
    return false;
}
    

int main() {
    
    
    
	ofstream fout ("fence8.out");
    ifstream fin ("fence8.in");

    int B;
    fin >> B;
    
    vi boards(B);
    
    FOR(b, 0, B)
        fin >> boards[b];
    
    int R;
    fin >> R;
    
    vi rail(R);
    
    FOR(r, 0, R)
        fin >> rail[r];
    
    sort( all( rail ) );
    sort( all( boards ) );
    
    //For each rail index, find the index of the board that can
    //fit
    minBoardForRail.resize(R, B);
    
    int minBoardIdx = 0;
    FOR(r, 0, R)
    {
        while(minBoardIdx < B && rail[r] > boards[minBoardIdx])
            ++minBoardIdx;
        
        minBoardForRail[r] = minBoardIdx;
    }        
    
    //Calculate lengths
    totalRailLen.resize(R);
    totalRailLen[0] = rail[0];
    
    FOR(r, 1, R)
        totalRailLen[r] = rail[r] + totalRailLen[r-1];
    
    int totalBoardLen = 0;
    FOR(b, 0, B)
        totalBoardLen += boards[b];
    
    cout << "Total board len " << totalBoardLen << endl;
    
    for(int startRailIdx = R - 1; startRailIdx >= 0; --startRailIdx)
    {
        bool possible = dfs(boards, minBoardForRail[startRailIdx], rail, startRailIdx, totalBoardLen);
        if (possible) {
            fout << 1+startRailIdx << endl;
            return 0;
        } else {
            cout << startRailIdx+1 << " not possible " << endl;   
        }
    }
    
    fout << 0 << endl;
    return 0;
    
}
