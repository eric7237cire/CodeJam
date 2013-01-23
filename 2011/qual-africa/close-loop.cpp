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

void do_test_case(int test_case, ifstream& in, ofstream& fout)
{
    uint N;
       
    
    in >> N;
    uint len;
    char color;
    
    vector<uint> red;
    vector<uint> blue;
    
    FOR(n, 0, N)
    {
        in >> len >> color;
        if (color == 'R')
            red.pb(len);
        
        if (color == 'B')
            blue.pb(len);
    }
    
    
    sort(all(red));
    reverse(all(red));
    
    sort(all(blue));
    reverse(all(blue));
    
    uint totalLen = 0;
    
    while(!red.empty() && !blue.empty())
    {
        
        totalLen += red[0];
        red.erase(red.begin());
    
        totalLen += blue[0];
        blue.erase(blue.begin());
    
        totalLen -= 2;
    }
    
    
    fout << "Case #" << test_case+1 << ": "
    << totalLen << endl;
    
    cout << "Case #" << test_case+1 << ": "
    << totalLen << endl;
		

}

