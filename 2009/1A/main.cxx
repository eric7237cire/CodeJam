#include <fstream>
#include <iostream>
#include <iomanip>
#include <vector>
#include <algorithm>
#include <limits>
#include <map>
#include <set>
#include <sstream>
#include <deque>

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

typedef pair<int, int> IntPair;
typedef deque<int> BaseNum;

deque<int> baseConv(const int K, const int base) {
	deque<int> conNum;

	int k = K;

	do {
		conNum.push_front(k % base);
		k /= base;
	}
	while (k != 0);

	return conNum;
}

int getSq(const deque<int>& convNum) {
    int r = 0;
    for(BaseNum::const_iterator it = convNum.begin();
        it != convNum.end();
        ++it) {
        r += (*it) * (*it);
    }
    
    return r;
}

void do_test_case(const int test_case, ifstream& input)
{
    //LOG_ON();
    string text;
    getline(input, text);

    LOG(text);
    stringstream iStr(text);

    vector<int> bases;

    while(iStr) {
        int b;
        iStr >> b;
        if (iStr) {
            bases.push_back(b);
            LOG(b);
        }
    }
   
    
    assert(bases.size() > 0);
    //cout << text << endl;

    int K = 2;

    while(true) {
        int allHappy = true;
        
        for(vector<int>::const_iterator it = bases.begin();
            it != bases.end();
            ++it) {
            int base = *it;
            LOG(base);
            
            int num = K;
    
            set<int> visited;
                        
            while (num != 1) {
                
                deque<int> conNum = baseConv(num, base);
                LOG_STR("Starting inner loop");
                LOG(num);
                LOG(base);
                LOG(conNum);
                
                num = getSq(conNum);
                
                LOG(num);
                
                if (visited.find(num) != visited.end()) {
                    LOG_STR("loop detected");
                    allHappy = false;
                    break;
                }
                
                visited.insert(num);
                
                
            }
            
            if (!allHappy) {
                break;
            }
        }		
        
        if (allHappy) {
            cout << "Case #" << (1+test_case) << ": " << K << endl;    
            return;
        } else {
            allHappy = true;
        }
        ++K;
        LOG(K);

    }
        
    
}
  
