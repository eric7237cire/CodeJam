/*
ID: eric7231
PROG: humble
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
#include <iomanip>
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
    
	ofstream fout ("humble.out");
    ifstream fin ("humble.in");
	
    uint K, N;
    
    fin >> K >> N;
        
    typedef set<uu> Set_t;

    Set_t factPrime;
    uint prime;
    FOR(k, 0, K) 
    {
        fin >> prime;
        factPrime.insert( mp(prime, prime) );   
    }
    
    //uvi sequence(1, 1);
    set<uint> sequence;
    sequence.insert(1);
    uint iter = 0;
    while( iter < 1000) //sequence.size() < N + 1) 
    {
        ++iter;
        Set_t::iterator it = factPrime.begin();
        uu factPrimeElem = *it;
        uint lastFactor = factPrimeElem.first;
        cout << "Min prime factor " << factPrimeElem.second << " " << factPrimeElem.first << endl;
        
        factPrimeElem.first *= factPrimeElem.second;
        
        factPrime.erase(it);
        factPrime.insert(factPrimeElem);
        
        //uint curSize = sequence.size();
        uvi newElems;
        uint index = 0;
        bool stop = false;
        for(set<uint>::const_iterator it = sequence.begin(); it != sequence.end(); ++it) 
        {
            cout << *it << endl;
            newElems.pb( *it * factPrimeElem.second);
            //sequence.pb(sequence[n] * factPrimeElem.second);
            
            ++index;
            
            if (index == N+1 && lastFactor > *it)
                stop = true;
        }
        
        sequence.insert( all(newElems) );
        
        if (stop)
            break;
    }
    
    //sort( all(sequence) );
    //FOR(i, 0, sequence.size() )
      //  cout << sequence[i] << endl;
    
      uint index = 0;
      for(set<uint>::const_iterator it = sequence.begin(); it != sequence.end(); ++it) {
          ++index;
           //cout << *it << endl;
           if (index == N+1) {
    fout << *it << endl; //sequence[N] << endl;               
           }
      }
    
    
        
    return 0;
}
