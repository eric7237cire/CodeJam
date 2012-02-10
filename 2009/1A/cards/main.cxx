#include <fstream>
#include <iostream>
#include <iomanip>
#include <vector>
#include <numeric>
#include <algorithm>
#include <limits>
#include <map>
#include <set>
#include <sstream>
#include <deque>
#include <bitset>

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

typedef vector<int> VecInt;

int fact(int x) {
    int f = 1;
    for(int i = 1; i <= x; ++i) {
        f *= i ;  
    }
    return f;
}

int nCk (int n, int k) {
  return fact(n) / ( fact(k) * fact(n-k) );  
}  

int countOnes(int x) {
    bitset<10> bs(x);
    return bs.count();
}

//C # of cards
//N # of cards in a set
void gen_combinations(VecInt& list, const int N, const int C) {
    int end = (1 << C) - 1;
    
    LOG(N);
    LOG(C);
    LOG(end);
    
    const unsigned int expectedCount = nCk(C, N);
    
    for(int i = 0; i <= end; ++i) {
        LOG_STR(i << " 1s " << countOnes(i) );
        if(countOnes(i) == N) {
            list.push_back(i);
            LOG_STR("Adding combin " << i);
        }
    }
    
    LOG(list.size());
    
    assert(expectedCount == list.size());
}

void do_test_case(const int test_case, ifstream& input)
{
    SHOW_TIME_BEGIN(tc);
    //LOG_ON();
    //LOG_OFF();
    
    int C, N;
    input >> C >> N;
    
    vector<int> trials;
    VecInt combin;
    gen_combinations(combin, N, C);
    
    const int NUM_TRIALS = 100000;
    
    LOG_OFF();
    
    for(int i = 0; i < NUM_TRIALS; ++i) {
        vector<bool> found;
        found.assign(C, false);
        int numPacks = 0;
        
        while(find(found.begin(), found.end(), false) != found.end()) {
            numPacks ++;
            int combinNum = rand() % combin.size();
            LOG(combinNum);
            
            int packCards = combin[combinNum];
            LOG(packCards);
            for(int c = 0; c < C; ++c) {
                if ( ( (packCards >> c) & 1 ) == 1) {
                    LOG_STR("Found " << c << " in " << packCards);
                    found[c] = true;   
                }
            }
        }
        
        LOG(numPacks);
        trials.push_back(numPacks);
    }
    
    int total =  accumulate(trials.begin(), trials.end(), 0);
    double average = (1.0 * total) / NUM_TRIALS;

    cout << "Case #" << (1+test_case) << ": " << average << endl;
        
    
}
  
