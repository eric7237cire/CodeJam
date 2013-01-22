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
  
  string line;
  getline(input, line);
    
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

string getDigits(char letter)
{
    if (letter == ' ')
        return "0";

    map<uint,uint> indexKey;
    
    uint idx = (letter - 'a');
    
    array<int, 8> sizes{ { 3, 3, 3, 3, 3, 4, 3, 4 } };
    
    uint index = 0;
    FOR(size, 0, sizes.size()) 
    {
        index += sizes[size];
        indexKey.insert(mp(index, size+2));
    }
    
    auto it = indexKey.upper_bound( idx );
    
    uint key = it->second;
    uint times = sizes[key-2] - (it->first - idx) + 1;
    
    return string(times, '0' + key);
}
void do_test_case(int test_case, ifstream& in)
{
    string line;
    
    getline(in, line);
    
    string ans;
    
    for(uint c = 0; c < line.length(); ++c)
    {
        string keys = getDigits( line[c] );
        
        if (ans.length() > 0 && ans[ans.length() - 1] == keys[0])
            ans.insert(ans.end(), ' ');
        
        ans.insert(ans.end(), keys.begin(), keys.end());
    }
    cout << "Case #" << test_case+1 << ": ";
  cout << ans << endl;
  
    
}
