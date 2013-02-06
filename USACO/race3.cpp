/*
ID: eric7231
PROG: race3
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

typedef set<uint> si;
typedef vector<set<uint> > vs;
typedef vector<vs> vvs;


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
        os <<  setw(5) << vec[i]; // << endl;
    }
    return os;
}




int main()

{


	ofstream fout ("race3.out");
	ifstream fin ("race3.in");

	vvi adjList;
	
	int currentNode = 0;
	
	int input;
	vi adj;
	
	int iterCheck = 0;
	while(true) 
	{
	    iterCheck ++;
	    if (iterCheck > 1000)
	        break;
	    
	     fin >> input;
	     if (input == -1) {
	         break;
	     }
	     if (input == -2) {
	         ++currentNode;
	         adjList.pb(adj);
	         adj.clear();
			 continue;
	     }
	     
	     adj.pb(input);
	}
	
	int finishNode = adjList.size() - 1;
	
	vi unavoidable;
	vi splitting;
	
	for(int checkNode = 1; checkNode < finishNode; ++checkNode)
	{
	    bool isUnavoidable = false;
	    bool isSplitting = true;
	    
	    si visited;
	    queue<int> toVisit;
	    
	    toVisit.push(0);
	    /*
		Check if by removing the node the end becomes not reachable
		*/
	    while(!toVisit.empty())
	    {
	        int node = toVisit.front();
	        toVisit.pop();
	        
	        if (node == checkNode) {
	            continue;
	        }
	     
	        if (contains(visited, node))
	            continue;
	        
	        visited.insert(node);
	        
	        FOR(n, 0, adjList[node].size()) 
	        {
	            toVisit.push(adjList[node][n]);
	        }
	    }
	    
	    if( ! contains(visited, finishNode) )
	    {
	        unavoidable.pb(checkNode);
			isUnavoidable = true;
	    }

		/*
		Now check that starting from the node means we do not ever see the node again
		*/
		si visitedFromCheckPoint;
		assert(toVisit.empty());

		toVisit.push(checkNode);

		while(!toVisit.empty() && isSplitting)
	    {
	        int node = toVisit.front();
	        toVisit.pop();
	        
	        
	     
	        if (contains(visitedFromCheckPoint, node))
	            continue;
	        
	        visitedFromCheckPoint.insert(node);
	        
	        FOR(n, 0, adjList[node].size()) 
	        {
				if (adjList[node][n] == checkNode) {
				//	isSplitting = false;
					continue;
				}
	            toVisit.push(adjList[node][n]);
	        }
	    }

		FOR(node, 0, finishNode)
		{
			bool inFirst = contains(visited, node);

			FOR(n, 0, adjList[node].size()) 
			{
				if (checkNode == 4) {
					//cout << "Checknode " << checkNode << endl;
				}
				bool nInFirst = contains(visited, adjList[node][n]);

				if (inFirst && !nInFirst && adjList[node][n] != checkNode) {
					isSplitting = false;
					if (checkNode == 4) {			
						//cout << "Node " << node << " n " << n << " adj node " << adjList[node][n] << endl;
					}
					break;
				}

				if (!inFirst && nInFirst && adjList[node][n] != checkNode) {
					if (checkNode == 4) {
			
						//cout << "Not in first, neigh in first Node " << node << " n " << n << " adj node " << adjList[node][n] << endl;
					}
					isSplitting = false;
					break;
				}
			}
		}
				

		if (isSplitting) {
			splitting.pb(checkNode);
		}
	}
	
	fout << unavoidable.size(); 
	for(vi::const_iterator it = unavoidable.begin(); it != unavoidable.end(); ++it)
	{
	    
	    fout << " ";
	    
	    fout << *it;
	}
	fout << endl;

	fout << splitting.size(); 
	for(vi::const_iterator it = splitting.begin(); it != splitting.end(); ++it)
	{
	    
	    fout << " ";
	    
	    fout << *it;
	}
	fout << endl;
	return 0;
}



