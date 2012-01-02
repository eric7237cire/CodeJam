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
string searchText = "welcome to code jam";
string text;

int search(unsigned int indexOfSearchString, unsigned int indexOfText) {
    
    LOG(searchSpace[indexOfSearchString][indexOfText]);
    
    if (indexOfSearchString >= 19 || indexOfText >= text.length() ) {
        return 0;
    }
    
    if (searchSpace[indexOfSearchString][indexOfText] >= 0 ) {
        return searchSpace[indexOfSearchString][indexOfText];
    }
    
   // LOG_ON();
    LOG(indexOfSearchString);
    LOG(indexOfText);
    LOG_OFF();
    
    int r = 0; 
    if (searchText[indexOfSearchString] == text[indexOfText]) {
        if (indexOfSearchString == 18) {
            r = 1;
        }
        
        r += search(indexOfSearchString + 1, indexOfText + 1) + search(indexOfSearchString, indexOfText + 1);
        
    } else {
        r = search(indexOfSearchString, indexOfText + 1);   
    }
    
    r %= 10000;
    searchSpace[indexOfSearchString][indexOfText] = r;
    return r;
}

void do_test_case(const int test_case, ifstream& input)
{
    //LOG_ON();
    
    memset( &searchSpace[0][0], -1, sizeof(searchSpace) );
        
    getline(input, text);
   
    //cout << text << endl;
        
    int hits = search(0, 0);
    cout << "Case #" << (1+test_case) << ": " << setfill('0') << setw(4) << hits % 10000 << endl;
}
  
