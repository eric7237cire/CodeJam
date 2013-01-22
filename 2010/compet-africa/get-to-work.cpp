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
#include <array>
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

void do_test_case(int test_case, ifstream& input);

int main(int argc, char** args)
{
  if (argc < 2) {
    cerr << "Usage: <exe> input_file" << endl;
    return -1;
  }
  
  ifstream input;
  input.open(args[1]);
  
  int T;
  input >> T;
  
  //string line;
  //getline(input, line);
    
  for (int test_case = 0; test_case < T; ++test_case) 
  {
    do_test_case(test_case, input);  
  }

}


template <class K, class V> 
V getMapValue( const map<K,V>& aMap, const K& key, const V& defaultValue )
{
    typename map<K, V>::const_iterator it = aMap.find(key);
    if ( it == aMap.end() )
        return defaultValue;
    return it->second;
}

void do_test_case(int test_case, ifstream& in)
{
    uint N, T, E;
    
    in >> N >> T >> E;
    
    uvvi driverCap(N);
    
    uvi people(N, 0) ;
    
    uint H, P;
    
    FOR(e, 0, E)
    {
        in >> H >> P;
        
        if (H != T)
            people[ H-1 ]++;
        
        if ( P >= 1 )
            driverCap[ H- 1 ].pb( P );        
    }
    
    uvi minCars;
    bool impossible = false;
    
    FOR(n, 0, N)
    {
        sort( all(driverCap[n]) );
        reverse( all(driverCap[n]) );
        int peopleLeft = people[n];        
        //printf("People left %d = %d\n", n, peopleLeft);
        uint carIndex = 0;
        uint carsNeeded = 0;
        while(peopleLeft > 0 && carIndex < driverCap[n].size())
        {
            ++carsNeeded;
            peopleLeft -= driverCap[n][carIndex++];   
        }
        
        if (peopleLeft > 0)
            impossible = true;
        
        minCars.pb(carsNeeded);
    }
    
    cout << "Case #" << test_case+1 << ": ";
    
    if (impossible) {
        cout << "IMPOSSIBLE";
    } else {
        FOR(n, 0, N) {
            if (n>0)
                cout << " ";
            
            cout << minCars[n];
        }
    }
    cout << endl;
  
    
}
