#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <set>
#include <vector>
#include <algorithm>
#include <cassert>
#include <iterator>
#include <stack>
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


void updateColumnOnesToRight( uvi& columnOnesToRight, uint col, const uvvi& land)
{
    for(uint r = 0; r < land.size(); ++r)
    {
        if (land[r][col] != 0)
            columnOnesToRight[r]++;
        else
            columnOnesToRight[r] = 0;
    }
}

int area( ii lowerLeft, ii upperRight )
{
   return (upperRight.first - lowerLeft.first + 1)
   * (upperRight.second - lowerLeft.second + 1);
}
        
void do_test_case(int test_case, ifstream& in, ofstream& fout)
{
    uint L, W;
       
    
    in >> W >> L ;

	uint H = L;
    
    uvvi land(L, uvi(W, 0));
    
    char sq;
    FOR(r, 0, L)
        FOR(c, 0, W)
        {
            in >> sq;
            switch(sq) {
            case 'G':
            case 'S':
            land[r][c] = 1;
            }
        }
    
        
    ii best_ll(0,0);
    ii best_ur(-1,-1);
    
    uvi columnOnesToRight(H+1, 0);
    
    /*
    for( lowerLeftX = W-1; lowerLeftX >= 0; --lowerLeftX)
    {
        updateColumnOnes(columnOnesToRight);
        
        for( lowerLeftY = 0; lowerLeftY < L; ++lowerLeftY)
        {
            uu ur = grow_ones( mp(lowerLeftX, lowerLeftY) );
            
            if (area(ll,up) > area(best_ll, best_ur) ) {
                best_ll = ll;
                best_ur = ur;
            }
        }
    }*/

	 FOR(r, 0, L)
    {
        FOR(c, 0, W)
        {
            cout << land[r][c];
        }
        cout << endl;
    }
cout << endl;
   
    
    for( int x = W-1; x >= 0; --x)
    {
        updateColumnOnesToRight(columnOnesToRight, x, land);
		 stack<uu> widthStack;
        uint width = 0;
        for( int y = 0; y <= (int)H; ++y )
        {
            if ( columnOnesToRight[y] > width )
            {
                widthStack.push( mp(y, width) );
				cout << "X= " << x << " Y= " << y <<  "Push " << width << endl;
                width = columnOnesToRight[y];
				 
            }
            if ( columnOnesToRight[y] < width )
            {
                uint y0;
                do
                {
					uu rowWidth = widthStack.top();
                    widthStack.pop();
					cout << "X= " << x << " Y= " << y <<  "Pop " << rowWidth.second << " " << columnOnesToRight[y] << endl;
                    y0 = rowWidth.first;
                    if (width * (y-y0) > 
                        area(best_ll, best_ur) )
                    {
                        best_ll = mp (x, y0);
                        best_ur = mp(x+width-1, y-1);
                    }
                    width = rowWidth.second;
                } while( columnOnesToRight[y] < width );
                
                width = columnOnesToRight[y];
    
                if (width != 0)
                    widthStack.push( mp(y0, width) );
            }
        }
    }
    
   

cout << "Case #" << test_case+1 << ": " 
    << area(best_ll, best_ur)
     << endl;

    fout << "Case #" << test_case+1 << ": " 
    << area(best_ll, best_ur)
     << endl;
    
       
		

}

