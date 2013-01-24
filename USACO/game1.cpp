/*
ID: eric7231
PROG: game1
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

#define FORE(k,a,b) for(int k=(int)(a); k <= (int)(b); ++k)
#define FOR(k,a,b) for(int k=(int)(a); k < (int) (b); ++k)

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
    
	ofstream fout ("game1.out");
    ifstream fin ("game1.in");
	
    uint N;

    fin >> N;
    
    uvi board(N, 0);
    
    FOR(n, 0, N)
        fin >> board[n];
    
    //First is score for player to act, second is the score for following player
    vector< vector< ii > > score(N, vector< ii >(N, mp(-1,-1) ));
    
    //Init DB with pairs remaining
    for(int i = 1; i < (int)N; ++i)
    {
       score[ i - 1 ][ i ] = 
        mp( max(board[i], board[i-1]),
            min(board[i], board[i-1]) );
    }
    
    for(int size = 3; size <= (int)N; ++size)
    {
        for(int right = size-1; right < (int) N; ++right)
        {
            int left = right - size + 1;
            
            ii leftBranch = score[ left ][right - 1];
            ii rightBranch = score[ left+1 ][right];
            
            //Take the right number goes to left branch
            int leftBranchScore = board[right] + leftBranch.second;
            
            int rightBranchScore = board[left] + rightBranch.second;
            
            if (rightBranchScore >= leftBranchScore)
            {
                score[left][right] = mp( rightBranchScore, rightBranch.first );   
            }
            else 
            {
                score[left][right] = mp( leftBranchScore, leftBranch.first );
            }
        }
        
    }
            
    fout << score[0][N-1].first << " " << score[0][N-1].second << endl;
	
    return 0;
}
