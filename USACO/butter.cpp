/*
ID: eric7231
PROG: butter
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
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(uint k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(uint k=(a); k < (b); ++k)

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


int main() {
    
	ofstream fout ("butter.out");
    ifstream fin ("butter.in");
	
    uint N, P, C;
    
    fin >> N >> P >> C;
    
	const uint notConnected = 1000000000;
	int nodeCount = P;
	
	uvvi dist(nodeCount, uvi(nodeCount, notConnected));
	
	uvi cowLocs(N, 0);
	
	FOR(cowIdx, 0, N)
	{
	    fin >> cowLocs[cowIdx];    
	}
	
	uint p1, p2, d;
	
	FOR(conPath, 0, C)
	{
	    fin >> p1 >> p2 >> d;
	    dist[p1-1][p2-1] = d;
	    dist[p2-1][p1-1] = d;
	}
	
	FOR(n, 0, P)
	    dist[n][n] = 0;
	
cout << "Start " << endl;	
FOR(k, 0, P) {
    cout << "k: " << k << endl;
    FOR(i, 0, P) FOR(j, 0, P) {
	    if (j==k || j==i)
	        continue;
	    dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j]);
	}
}

cout << "Start 2" << endl;	
	FOR(i, 0, P) FOR(j, 0, P) 
	{
	    //cout << "Distance " << i+1 << " " << j+1 << " dist " << dist[i][j] << endl;    
	}
	
	uint minTotal = notConnected*10;
	
	//for all pastures, calculate max distance to cows
	FOR(p, 0, P)
	{
	    uint total = 0;
	    //cout << "Pasture " << p << endl;
	    FOR(cowLocIdx, 0, N)
	    {
	        uint distToPast = dist[p][ cowLocs[cowLocIdx] - 1 ];
	        total += distToPast;
	        
	        //if (p==3)
	        {
	         //   cout << " cow " << cowLocIdx+1 << " dist " << distToPast << endl;
	        }
	    }
	    
	    minTotal = min(minTotal, total);
	}
	
	fout << minTotal << endl;
	
	
    return 0;
}
