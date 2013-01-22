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
#include <numeric>
#include <array>
#include <queue>
#include "prettyprint.h"
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;
typedef long long ll;

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

void do_test_case(int test_case, ifstream& input, ofstream& output);

bool replace(std::string& str, const std::string& from, const std::string& to) {
    size_t start_pos = str.find(from);
    if(start_pos == std::string::npos)
        return false;
    str.replace(start_pos, from.length(), to);
    return true;
}

int main(int argc, char** args)
{
	/*
  if (argc < 2) {
    cerr << "Usage: <exe> input_file" << endl;
    return -1;
  }
  */
  ifstream input;
  string inputFilename = (argc < 2) ? "sample.in" : args[1];
      
   
  input.open(inputFilename);

  string outputFilename = inputFilename;
  replace(outputFilename, ".in", ".out");
  
  ofstream output;
  output.open(outputFilename);
  
  int T;
  input >> T;
  
  //string line;
  //getline(input, line);
    
  for (int test_case = 0; test_case < T; ++test_case) 
  {
    do_test_case(test_case, input, output);  
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

ll count_brute_force( vector<ll> S, uint C )
{
    ll total = 0;
    cout << "C " << C << endl;
    while(true)
    {
        sort( all(S) );
        //cout << "S " << S << endl;
        for(int i = S.size() - 1; i >= (int) (S.size() - C); --i)
        {
            if (S[i] == 0)
                return total;
            S[i]--;            
        }
        ++total;
    }
    
    return total;
}

void reduce( vector<ll>& S, ll total, uint probSetSize, uint start, uint stop )
{
    assert(total % probSetSize == 0);
    assert(total > 0);
    assert(stop >= start);
    
    uint blockSize = stop-start + 1;
    assert(blockSize >= probSetSize);
    
    ll itemReduce = total / blockSize;
    ll numRedExtra = total % blockSize;
    
    transform( S.begin()+start, S.begin() + stop + 1,
            S.begin()+start,
            [ itemReduce ] ( ll probRemaining ) -> ll {
                return probRemaining - itemReduce;
            }
            );
        
    transform( S.begin()+start, S.begin() + start + numRedExtra,
            S.begin()+start,
            [  ] ( ll probRemaining ) -> ll {
                return probRemaining - 1;
            }
            );
    
}

void do_test_case(int test_case, ifstream& in, ofstream& fout)
{
    uint P, C;
    
    
    
    in >> P >> C;
    
    vector<ll> S(P+1, 0);
    
    
    FOR(p, 0, P)
        in >> S[p];
    
    cout << "P " << P << endl;
    
    //fout << "bf " << count_brute_force(S, C) << endl;
    
    
    //vector<ll> buckets(C, 0);
    
    sort( all(S) );
    reverse( all(S) );
    
    ull total = 0;
    
    while( !S.empty() )
    {        
        cout << "S " << S << endl;
      //  cout << "Buckets " << buckets << endl;
      
      
      /*
      if (C > 1) {
        while( S[C-1] + S[C] <= S[C-2] && S[C] > 0)
        {
            //combine into C-1
            S[C-1] += S[C];
            S.erase( S.begin() + C );
        }
      }
      */
        
        
        //0 to left inclusive is set of max sized prob counts
      
        uint right = C - 1;
        
        
        while( right < S.size() - 1 && ( right < C - 1 || S[right] == S[right+1] ) )
            ++right;

		if ( S[right] == 0)
            break;
      
        uint left = right;
        while( left >= 1 && S[left] == S[right] )
            --left;
        
        //Reduce a maximum of diff * size taking set
        ll diffNext = S[right] - S[right+1];
        
        if (left < C-1 && left > 0) {
            ll diffMaxNext = S[left] - S[left+1];
            assert(diffMaxNext > 0);
            diffNext = min(diffNext, diffMaxNext);
        } 
        ll probSets = diffNext;
        uint maxSetSize = left + 1;
        
        uint forcedSetSize = maxSetSize > C ? 0 : C - maxSetSize;
        
        ll totalFromMaxSet = maxSetSize <= C ? probSets * maxSetSize : probSets * C;
        ll totalFromForcedSet = probSets * forcedSetSize;
                       
        assert(totalFromMaxSet + totalFromForcedSet == probSets * C );
        total += probSets;
        
        ll totalBefore = accumulate( all(S), (ll) 0 );
		reduce( S, totalFromMaxSet, maxSetSize > C ? C : maxSetSize, 0, left );
        ll totalAfter = accumulate( all(S), (ll) 0 );
        
        assert( totalBefore - totalFromMaxSet == totalAfter );
        
        if (forcedSetSize > 0)
        {
            totalBefore = accumulate( all(S), (ll) 0 );
            reduce( S, totalFromForcedSet, forcedSetSize, left+1, right );
            totalAfter = accumulate( all(S), (ll) 0 );
        
            assert( totalBefore - totalFromForcedSet == totalAfter );
        }
        
        sort( all(S) );
        reverse( all(S) );
    }
    
    fout << "Case #" << test_case+1 << ": "
    << total << endl;
    
  
    
}
