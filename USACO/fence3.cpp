/*
ID: eric7231
PROG: theme
LANG: C++
*/
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <stack>
#include <set>
#include <list>
#include <vector>
#include <algorithm>
#include <cassert>
#include <iterator>
#include <sstream>
#include <complex>
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


int main() {
    
	ofstream fout ("theme.out");
    ifstream fin ("theme.in");

    int N;  
    fin >> N;
    
    vi music(N);
  
    int ans=0;  

    FOR(i, 0, N)
    {
        fin >> music[i];
    }
    
    FOR(i, 0, N-2*ans)
    { 
        int len = 0;
        int sub,temp;
        
        //Find start of at least one other sequence that is later
       for(int j=i+5;j<N-ans;j++)
       {  
           //Find length of match
          int p1=i;  
          int p2=j;  
            temp=0;  
            //Difference will stay the same throughout the 2 subsequences
            sub=music[p1]-music[p2];   
            while(p1<j&&p2<N&&sub==music[p1++]-music[p2++])  
                temp++;  
            len=max(len, temp);  
        }  
        ans=max(len, ans);  
    }     
    if(ans<5)  
        ans=0;  
    fout << ans << endl;  
    return 0;  
}  
