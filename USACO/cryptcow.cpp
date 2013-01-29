/*
ID: eric7231
PROG: cryptcow
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

template <class K, class V> 
V getMapValue( const map<K,V>& aMap, const K& key, const V& defaultValue )
{
    typename map<K, V>::const_iterator it = aMap.find(key);
    if ( it == aMap.end() )
        return defaultValue;
    return it->second;
}


const int notConnected = numeric_limits<int>::max();

int op_decrease (int i) { return --i; }

template<typename T>
ostream& operator<<( ostream& os, const vector<T>& vec )
{
    FOR(i, 0, vec.size())
    {
        os << setw(5) << vec[i];
    }
    return os;
}


int main() {
    
    string finalMsg = "Begin the Escape execution at the Break of Dawn";
    
	ofstream fout ("cryptcow.out");
    ifstream fin ("cryptcow.in");
	
    string msg;
    getline(fin, msg);
    
    queue<string> toVisit;
    set<string> visited;
    
    toVisit.push(msg);
    
    while(!toVisit.empty()) {
        string cur = toVisit.front();
        toVisit.pop();
        
       // cout << "Visiting " << cur << endl;
        if (contains(visited, cur))
            continue;
        
        visited.insert(cur);
        
        if (cur == finalMsg)
        {
            int steps = (msg.length() - finalMsg.length()) / 3;
            fout << "1 " << steps << endl;
            return 0;
        }
        int cIndex = -1;
        
        while( (cIndex = cur.find_first_of('C', cIndex+1)) != string::npos)
        {
            int oIndex = cIndex;
            assert(cIndex >= 0 && cIndex < cur.length());
            
            while( (oIndex = cur.find_first_of('O', oIndex+1)) != string::npos)
            {
                assert(oIndex > cIndex);
                int wIndex = oIndex;
                
                while( (wIndex = cur.find_first_of('W', wIndex+1)) != string::npos)
                {
                    assert(cIndex < oIndex);
                    assert(oIndex < wIndex);
                    
                    //Before C
                    string copy(cur.begin(), cur.begin() + cIndex);
                    
                    //Between O and W
                    copy.append(cur.begin()+oIndex+1, cur.begin()+wIndex);
                    
                    //Between C and O
                    copy.append(cur.begin() + cIndex+1,
                        cur.begin()+oIndex);
                    
                    //After W
                    copy.append(cur.begin() + wIndex+1,
                        cur.end());
                    
                    //copy.erase(wIndex, 1);
                    //copy.erase(oIndex, 1);
                    //copy.erase(cIndex, 1);
                    
                    toVisit.push(copy);
                }
            }
            
            //break;
        }
            
    }
    
    fout << 0 << " " << 0 << endl;
	
    return 0;
}
