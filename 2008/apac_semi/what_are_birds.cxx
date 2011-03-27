//http://en.wikipedia.org/wiki/Modular_multiplicative_inverse
//Lucas's theorum
//
#include <fstream>
#include <iostream>
#include <vector>
#include <set>
#include <map>
#include <deque>
#include <queue>
#include <sstream>
#include <time.h>
#include <assert.h>
#include <boost/smart_ptr.hpp>
#include <boost/numeric/conversion/bounds.hpp>
#include <boost/limits.hpp>
#define SHOW_TIME 0
#include "util.h" 
#include <boost/math/common_factor.hpp>

#include <boost/shared_ptr.hpp>

using namespace std;


//#define LOG_OFF LOG_OFF

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
    //try 
    {
      do_test_case(test_case, input);
    } 
    //catch(...) 
    {
      //error("Error exception caught\n"); 
    }
  }
  
  SHOW_TIME_END(g)
}

typedef unsigned int uint;
typedef pair<uint, uint> HW;



ostream& operator<<(ostream& os, const HW& hw) {
  os << "H: " << hw.first << " W: " << hw.second;
  return os;
}

template<typename T> bool isBetween(T a, T b, T n);

/*
if potential_nb is farther away for h and w, it is also a 
not bird.  Assumes there exists a valid range in the middle
*/
bool isOutside(const HW& potential_nb, const HW& nb, const HW& minValid, const HW& maxValid) {
  if (nb.first > maxValid.first) {
    if (potential_nb.first < nb.first) {
      return false;
    }
  }
  if (nb.first < minValid.first) {
    if (potential_nb.first > nb.first) {
      return false;
    }
  }
  if (nb.second > maxValid.second) {
    if (potential_nb.second < nb.second) {
      return false;
    }
  }
  if (nb.second < minValid.second) {
    if (potential_nb.second > nb.second) {
      return false;
    }
  }
  
  return true;
}

void do_test_case(int test_case, ifstream& input)
{
  LOG_OFF();
  //LOG_ON();
  int N;
  
  vector<HW> birds;
  set<HW> not_birds;
  input >> N;
  for(int i = 0; i < N; ++i) {
    uint h, w;
    string s;
    input >> h >> w;
    getline( input, s);
    trim(s);
    HW hw(h, w);
    if (s == "BIRD") {
      birds.push_back(hw);
    } else if (s == "NOT BIRD") {
      not_birds.insert(hw);
    } else {
      LOG_STR("[" << s << "]");
      throw s;
    }
  }
  
  HW min_valid(boost::numeric::bounds<uint>::highest(), boost::numeric::bounds<uint>::highest());
  HW max_valid(boost::numeric::bounds<uint>::lowest(), boost::numeric::bounds<uint>::lowest());
  
  for(vector<HW>::const_iterator b = birds.begin(); b != birds.end(); ++b) {
    min_valid.first=min(min_valid.first, b->first);
    min_valid.second=min(min_valid.second, b->second);
    max_valid.first=max(max_valid.first, b->first);
    max_valid.second=max(max_valid.second, b->second);
  }
  
  LOG(min_valid);
  LOG(max_valid);
  
  //  0....nb_end .... min_width .. max_width .... nb_start
  HW nb_end(boost::numeric::bounds<uint>::lowest(), boost::numeric::bounds<uint>::lowest());
  HW nb_start(boost::numeric::bounds<uint>::highest(), boost::numeric::bounds<uint>::highest());
  
  for(set<HW>::const_iterator nb = not_birds.begin(); nb != not_birds.end(); ++nb) {
    LOG(*nb);
    if(nb->second >= min_valid.second && nb->second <= max_valid.second) {
      if(nb->first > max_valid.first) {
        nb_start.first = min(nb_start.first, nb->first);
      }
      if (nb->first < max_valid.first) {
        nb_end.first = max(nb_end.first, nb->first);
      }
    }
    if(nb->first >= min_valid.first && nb->first <= max_valid.first) {
      if(nb->second > max_valid.second) {
        nb_start.second = min(nb_start.second, nb->second);
      }
      if (nb->second < max_valid.second) {
        nb_end.second = max(nb_end.second, nb->second);
      }
    }
    LOG(nb_start);
    LOG(nb_end);
  
  }
  
  LOG(nb_start);
  LOG(nb_end);
  
  set<HW> outside_birds;
  
  for(set<HW>::const_iterator nb = not_birds.begin(); nb != not_birds.end(); ++nb) {
    
  }
  
  uint M;
  
  input >> M;
  
  cout << "Case #" << (test_case+1) << ":" << endl;
  for(int i = 0; i < M; ++i) {
    uint h, w;
    input >> h >> w;
    HW hw(h, w);
    LOG(hw);
    if (isBetween(min_valid.first, max_valid.first, h) && 
      isBetween(min_valid.second, max_valid.second, w)) {
      cout << "BIRD" << endl;
      continue;
    }
    
    if (h >= nb_start.first || h <= nb_end.first ||
      w >= nb_start.second || w <= nb_end.first || 
      isMember(not_birds, hw) || isMember(outside_birds, hw)
    ) {
      cout << "NOT BIRD" << endl;
      continue;      
    } 
    
    bool found = false;
    for(set<HW>::const_iterator nbi = not_birds.begin(); nbi != not_birds.end(); ++nbi) {
      if (isOutside(hw, *nbi, min_valid, max_valid)) {
        cout << "NOT BIRD" << endl;
        found = true;
        break;    
      }
    }
    
    if (!found) {
      cout << "UNKNOWN" << endl;
    }
    
  }
  
  return;    
}


  
