/*
ID: eric7231
PROG: cowtour
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

const ii NORTH = mp(-1, 0);
const ii EAST = mp(0, 1);
const ii SOUTH = mp(1, 0);
const ii WEST = mp(0, -1);


double distance(int x1, int y1, int x2, int y2)
{
	return sqrt((x1-x2)*(x1-x2) + (y2-y1)*(y2-y1));
}

int main() {
    
	ofstream fout ("cowtour.out");
    ifstream fin ("cowtour.in");
	
    int N;
    fin >> N;
    
	uvi x(N);
	uvi y(N);

	FOR(n, 0, N) 
		fin >> x[n] >> y[n];

	const double notConnected = 1000000000 ;
	vvd dist(N, vd(N, notConnected));

	FOR(n, 0, N)
	    dist[n][n] = 0;
	
	FOR(nodeFrom, 0, N) FOR(nodeTo, 0, N)
	{
	    char connected;
	    fin >> connected;
	    if (connected != '1')
	        continue;
		dist[nodeFrom][nodeTo] = distance(x[nodeFrom],y[nodeFrom], x[nodeTo],y[nodeTo]);
	}
		
	FOR(k, 0, N) FOR(i, 0, N) FOR(j, 0, N)
	    dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j]);

	vd maxDist(N, 0);
	FOR(i, 0, N) FOR(j, 0, N)
	{
	    if(dist[i][j] + 0.001 >= notConnected)
	        continue;
	    
	    maxDist[i] = max(maxDist[i], dist[i][j]);
	    
	}
	
	double minConnectionDistance = notConnected;
	
	FOR(i, 0, N) FOR(j, 0, N)
	{
	    if (dist[i][j] + .000001 < notConnected)
	        continue;
	    
	    double distBet = maxDist[i] + maxDist[j] 
	    + distance(x[i], y[i], x[j], y[j]);
	    
	    cout << "Distance " << i << ", " << j << " : " << distance(x[i], y[i], x[j], y[j]) << endl;
	    cout << "Dist bet " << distBet << " maxdist i " << maxDist[i] << " maxj " << maxDist[j] << endl;
	    minConnectionDistance = min(minConnectionDistance, distBet);
	}
		
//FOR(i, 0, N) FOR(j, 0, N)
	//    cout << i << ", " << j << " dist " << dist[i][j] << endl;
	
	FOR(i, 0, N)
	    cout << "Max distance node = " << i << " = " << maxDist[i] << endl;
	
	fout.precision(6);
	fout << fixed << minConnectionDistance << endl;
	
    return 0;
}
