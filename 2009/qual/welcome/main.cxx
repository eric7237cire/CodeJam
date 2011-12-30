#include <fstream>
#include <iostream>
#include <iomanip>
#include <vector>
#include <algorithm>
#include <limits>
#include <map>
#include <set>

#include <stdio.h>
#include <time.h>
#include <assert.h>
#include <stdlib.h>

#include "util.h"
#include <cstring>

using namespace std;


void do_test_case(int, ifstream& input);

int main(int argc, char** args)
{
    
    LOG_OFF();
    
  if (argc < 2) {
    cerr << "Usage: <exe> input_file" << endl;
    return -1;
  }
  
  ifstream input;
  input.open(args[1]);
  
  int T;
  input >> T;

  string dummy;
  getline(input, dummy);
  SHOW_TIME_BEGIN(g) 
  	
  for (int test_case = 0; test_case < T; ++test_case) {
    do_test_case(test_case, input);   
  }
      
  SHOW_TIME_END(g)
}

enum {
    NORTH,
    WEST,
    EAST,
    SOUTH
};

typedef pair<int, int> IntPair;

int searchSpace[19][500];
int searchCount;
string searchText = "welcome to code jam";
string text;

int search(unsigned int indexOfSearchString, unsigned int indexOfText) {
    
    
    if (indexOfSearchString >= 19 || indexOfText >= text.length() ) {
        return 0;
    }
    
    if (searchSpace[indexOfSearchString][indexOfText] >= 0 ) {
	assert(searchSpace[indexOfSearchString][indexOfText] >= 0);
        return searchSpace[indexOfSearchString][indexOfText];
    }

++searchCount;
    
    LOG_ON();
if (searchCount % 5000 == 0) {
LOG(searchCount);
}
//    LOG(indexOfSearchString);
//    LOG(indexOfText);
    LOG_OFF();
    
    char searchChar = searchText[indexOfSearchString];
    LOG(searchChar);
    int hits = 0;
    
    for(int i = indexOfText;  i < text.length(); ++i) {
        if (text[i] == searchChar) {
            LOG_STR("Hit " << hits << " I " << i);
            hits = hits + search(indexOfSearchString + 1, i + 1);   
            assert(hits >= 0);
            if (indexOfSearchString == 18) {
                ++hits;
		assert(hits >= 0);
                LOG_STR("HIT!");
            }
        }
    }
	assert(0 <= indexOfSearchString && 19 > indexOfSearchString);
assert(0 <= indexOfText && 500 > indexOfText);

assert(hits >= 0);
	hits %= 10000;
    searchSpace[indexOfSearchString][indexOfText] = hits;
    return hits;
}

void do_test_case(const int test_case, ifstream& input)
{
    //LOG_ON();
    
    memset( &searchSpace[0][0], -1, sizeof(searchSpace) );
	searchCount = 0;
        
    getline(input, text);
   
    //cout << text << endl;
        
    int hits = search(0, 0);
    cout << "Case #" << (1+test_case) << ": " << setfill('0') << setw(4) << hits % 10000 << endl;
}
  
