#include <fstream>
#include <iostream>
#include <vector>
#include <algorithm>
#include <limits>
#include <string>

#include <stdio.h>
#include <time.h>
#include <assert.h>
#include <stdlib.h>
#include <boost/numeric/conversion/bounds.hpp>
#include <boost/limits.hpp>
#include <cmath>
#include "util.h" 


using namespace std;


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

  SHOW_TIME_BEGIN(g) 
  	
  for (int test_case = 0; test_case < T; ++test_case) 
  {
    try {
      do_test_case(test_case, input);
    } catch(...) {
      error("Error exception caught\n"); 
    }
  }
  
  SHOW_TIME_END(g)
}

unsigned int compute_rle(const string& str)
{
  int c = 0;
  unsigned int rle = 0;
  for(unsigned int i=0; i<str.size(); ++i) {
    if (str[i] != c) {
      ++rle;
    }
    c=str[i];
  }
  
  return rle;
}

string perm_string(const string& str, const vector<unsigned int>& perm)
{
  assert(str.size() % perm.size() == 0);
  
  string ret(str);
  
  for(unsigned int i=0; i <= str.size() - perm.size(); i+=perm.size())
  {
    for(unsigned int j=0; j<perm.size(); ++j) 
    {
      assert(i+j < ret.size());
      assert(i+perm[j] < str.size());
      assert(perm[j] < perm.size());
      ret[i+j] = str[i+perm[j]];
    }
  }
  
  return ret;
}

void do_test_case(int test_case, ifstream& input)
{
 
  assert(compute_rle("aabcaaaa") == 4);
  
  vector<unsigned int> p;
  p.push_back(0);
  p.push_back(3);
  p.push_back(2);
  p.push_back(1);
  info(perm_string("abcabcabcabc", p).c_str());
  assert(perm_string("abcabcabcabc", p) == string("aacbbbacccba"));
  
  unsigned int k;
  input >> k;
  
  string s;
  input >> s;
  
  vector<unsigned int> perm(k);
  for(unsigned int i=0; i<k; ++i) {
    perm[i] = i;
  }

  unsigned int lowest_size = s.size();

  do {
    #if DEBUG
    for(unsigned int i=0; i<k; ++i) {
      printf("%d, ", perm[i]);
    }
    printf("\n");
    #endif
    //cout << myints[0] << " " << myints[1] << " " << myints[2] << endl;
    debug(perm_string(s, perm).c_str());
    debug("\n");
    unsigned int rle = compute_rle(perm_string(s, perm));
    if (rle < lowest_size) {
      info("New string %s, rle %d\n", perm_string(s, perm).c_str(), rle);
      lowest_size = rle;
    }
  } while ( next_permutation (perm.begin(), perm.end()) );
  
   printf("Case #%d: %d\n", test_case+1, lowest_size);
   
  return;
    
}
  
