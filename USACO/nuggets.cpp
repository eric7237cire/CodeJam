/*
ID: eric7231
PROG: nuggets
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

int gcd(int a, int b)
{
    int r_2 = max(a,b);
    int r_1 = min(a,b);
    
    while(r_1 != 0)
    {
        int t = r_1;
        r_1 = r_2 % t;
        r_2 = t;
    }
    
    return r_2;
}
    

int main() {
    
    cout << "a" << endl;
    
	ofstream fout ("nuggets.out");
    ifstream fin ("nuggets.in");

    int n;
    fin >> n;
    vi values(n);
    
    FOR(i, 0, n)
        fin >> values[i];
    
    bool foundCoPrime = false;
 
    FOR(i, 0, n)
        FOR(j, i+1, n)
        if (gcd(values[i], values[j]) == 1) {
            foundCoPrime = true;
            break;
        }
        
    if (!foundCoPrime)
    {
        fout << 0 << endl;
        return 0;
    }
                
    
    int max = 100000;
        
    vector<bool> canMakeChange(max+1, false);
    
    
    canMakeChange[0] = true;
    FORE(x, 1, max)
    {
        //cout << "x  " << x << endl;
        
        canMakeChange[x] = false;
        //return 0;
        FOR(i, 0, n)
        {
            //cout << "i " <<  i << endl;
            //if (i==0) return 0;
            if (x>=values[i] && canMakeChange[x-values[i]])
            {
              //  cout << "x " << x << endl;
                canMakeChange[x]=true;
                break;
            }
            
        }
    }
        
    
    int ans = 0;
    for(int i = max; i >= 0; --i)
    {
        if (!canMakeChange[i]) {
            ans = i;
            break;
        }
    }
    //cout << "ans " << ans << endl;
    fout << ans << endl;
	
    return 0;
}
