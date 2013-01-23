/*
ID: eric7231
PROG: camelot
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

vector<uint> knightBFS (int startR, int startC, const int nCols, const int nRows)
{
    std::queue<uint> toVisit;
        
    vector<bool> visited(nCols*nRows, false);
    vector<uint> dist(nCols*nRows, numeric_limits<uint>::max() );
    
    uint start = getIndex(startR, startC, nCols);
    
    dist[start] = 0;
    toVisit.push (start);

    while (!toVisit.empty())
    {
        uint sq = toVisit.front();
        toVisit.pop();

        if (visited[sq])
            continue;
        
        visited[sq] = true;
        
        int curRow = sq / nCols;
        int curCol = sq % nCols;
        
        for(int dC = -2; dC <= 2; ++dC)
        {
            for(int dR = -2; dR <= 2; ++dR)
            {
                if ( abs(dC) + abs(dR) != 3 )
                    continue;
                
                int row = curRow + dR;
                int col = curCol + dC;
                
                if (row < 0 || row >= nRows || col < 0 || col >= nCols)
                    continue;
                
                uint index = getIndex(row,col, nCols);
                
                dist[index] = min(dist[index], 1 + dist[sq]);
               
                toVisit.push(index);
            }
        }
    }     
    
    return dist;
}

uint kingDistance( int kRow, int kCol, int targetRow, int targetCol )
{
    return max( abs(kRow-targetRow), abs(kCol-targetCol) );   
}
int main() {
    
	ofstream fout ("camelot.out");
    ifstream fin ("camelot.in");
	
    uint R, C;

    fin >> R >> C;
    
    char col;
    uint row;
    
    uu kingLoc;
    fin >> col >> row;
    kingLoc.second = col-'A';
    kingLoc.first = row - 1;
    
    vector<uu> knightsLoc;
    
    uvvi knightDist;
    
    while(fin >> col >> row)
    {
        uu kLoc;
        knightsLoc.pb( mp( row-1, col-'A' ) );
    }
    
    uint minTotalMoves = numeric_limits<uint>::max();
    const int kingRadius = 5;
    
    FOR(r, 0, R)
        FOR(c, 0, C)
    {
        knightDist.pb(  knightBFS( r, c, C, R ) );
    }
    
    
    FOR(r, 0, R)
        FOR(c, 0, C)
    {
        uint goalIndex = getIndex(r, c, C);
        
        uint totalMoves = 0;
        FOR(n, 0, knightsLoc.size() )
        {
            uint knightIndex = getIndex(knightsLoc[n].first, knightsLoc[n].second, C);
			if (knightDist[knightIndex][goalIndex] == numeric_limits<uint>::max()) {
				totalMoves = numeric_limits<int>::max();
				break;
			}
            totalMoves += knightDist[knightIndex][goalIndex];
        }

		//if (totalMoves < 4500)
		//printf( "Total Moves %d = %d\n", goalIndex, totalMoves );
        
        int minKingCost = kingDistance( kingLoc.first, kingLoc.second, r, c );
        
        //Can we improve kingCost?        
		for(int dr = -kingRadius; dr <= kingRadius; ++dr)
			for(int dc = -kingRadius; dc <= kingRadius; ++dc)
        {
			int pickupRow = kingLoc.first + dr;
			int pickupCol = kingLoc.second + dc;
            
            if (pickupRow >= (int)R || pickupRow < 0 || pickupCol >= (int)C || pickupCol < 0)
                continue;
            
            uint pickupIndex = getIndex(pickupRow, pickupCol, C);
            
            uint kingMove = kingDistance( kingLoc.first, kingLoc.second, pickupRow, pickupCol );
            
            uint minKnightPickupCost = numeric_limits<int>::max() - kingMove;
            
            FOR(n, 0, knightsLoc.size() )
            {
                uint knightIndex = getIndex(knightsLoc[n].first, knightsLoc[n].second, C);
            
                //knight to square, square to goal - knight directly to goal
                uint knightToPickup = knightDist[knightIndex][pickupIndex];
                uint knightToGoal = knightDist[pickupIndex][goalIndex];

				//Unreachable
				if (knightToGoal == numeric_limits<uint>::max() ||  knightToPickup == numeric_limits<uint>::max())
					continue;
                
                //Must be more expensive to make a potential detour
                assert(knightToPickup + knightToGoal >= knightDist[knightIndex][goalIndex]);

                //Remove path to goal, and add path to pickup                                
                uint knightToPickupCost = knightToPickup + knightToGoal - knightDist[knightIndex][goalIndex];
                
                minKnightPickupCost = min(minKnightPickupCost, knightToPickupCost);
            }
            
            int kingCost = kingMove + minKnightPickupCost;
            
            minKingCost = min(kingCost, minKingCost);
        }
        
        totalMoves += minKingCost;
        
        minTotalMoves = min(minTotalMoves, totalMoves);
    }
    
    
    
    fout << minTotalMoves << endl;
	
    return 0;
}
