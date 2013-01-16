/*
ID: eric7231
PROG: comehome
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
    
	ofstream fout ("comehome.out");
    ifstream fin ("comehome.in");
	
    int N;
    fin >> N;
    
	const uint notConnected = 1000000000;
	int nodeCount = 52;
	
	uvvi dist(nodeCount, uvi(nodeCount, notConnected));
	
	char n1Ch, n2Ch;
	int n1, n2;
	uint d;
	FOR(node, 0, N)
	{
	    fin >> n1Ch >> n2Ch >> d;
	    n1 = isupper(n1Ch) ? n1Ch - 'A' + 26 : n1Ch - 'a'; 
	    n2 = isupper(n2Ch) ? n2Ch - 'A' + 26 : n2Ch - 'a';
	    
	   // printf("n1 = [%d] n2 = [%d] %c %c %c %c \n", n1, n2, n1Ch, tolower(n1Ch), n2Ch, tolower(n2Ch));
	    
	    dist[n1][n2] = min(dist[n1][n2], d);
	    dist[n2][n1] = min(dist[n2][n1], d);
	}
	
	
	uvi min_distance(nodeCount, notConnected);
	vi previous(nodeCount, -1);
	int barnNode = nodeCount - 1;
	
    min_distance[barnNode] = 0;
    
    uint ansDist = 9999;
    char ansChar;
    
    std::set< uu > vertex_queue;
    vertex_queue.insert( mp(min_distance[barnNode], barnNode));
 
    while (!vertex_queue.empty()) 
    {
        uint distToCur = vertex_queue.begin()->first;
        uint curNode = vertex_queue.begin()->second;
        
        if (curNode >= 26 && curNode != barnNode) {
            ansDist = distToCur;
            ansChar = (curNode-26) + 'A';
            break;
        }
        vertex_queue.erase(vertex_queue.begin());
 
        FOR(adjNode, 0, nodeCount)
        {
            uint weight = dist[curNode][adjNode];
            
            if (adjNode == curNode || weight == notConnected) 
                continue;
            
            //Distance to barn : curNode -> adjNode -> barn
            uint distToAdj = distToCur + weight;
            
            if (distToAdj < min_distance[adjNode])
            {
                vertex_queue.erase(mp(min_distance[adjNode], adjNode));
                min_distance[adjNode] = distToAdj;
                previous[adjNode] = curNode;
                vertex_queue.insert( mp(min_distance[adjNode], adjNode));
 
            }
            
 
	    
        }
    }
	    
	fout << ansChar << " " << ansDist << endl;
	
    return 0;
}
