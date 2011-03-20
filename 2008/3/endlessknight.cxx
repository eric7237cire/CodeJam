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
#define SHOW_TIME 0
#include "util.h" 
#include "bipartite.h"

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

template<typename I> struct RowColGeneric
{
  I row;
  I col;
  
  RowColGeneric(I r, I c) : row(r), col(c) 
  {}
  
  int operator==(const RowColGeneric& rc) const
  {
    return row == rc.row && col == rc.col;
  }
  
  int operator!=(const RowColGeneric& rc) const
  {
    return !(*this == rc);
  }
};

  template<typename I> std::size_t hash_value(const RowColGeneric<I>& rc)
  {
      std::size_t seed = 0;
      boost::hash_combine(seed, rc.row);
      boost::hash_combine(seed, rc.col);
      return seed;
  }
  


template<typename I> ostream& operator<<(ostream& os, const RowColGeneric<I>& rc)
{
  os << rc.row+1 << ", " << rc.col+1;
  return os;
}

typedef RowColGeneric<unsigned int> RowCol;
typedef boost::unordered_set<RowCol> RockSet;
typedef set<pair<uint, uint> > RockLevelIndexSet;
typedef map<pair<uint, uint>, uint> RockMultipliers;
typedef unsigned int uint;

RowCol getRowCol(const unsigned int level, const unsigned int index)
{
  return RowCol(2 * level - index, level + index);
}

bool getLevelIndex(uint& level, uint& index, const RowCol& rc)
{
  level = (rc.row + rc.col) / 3;
  index = rc.col - level;
  
  if (getRowCol(level, index) != rc || index > level) {
    return false;
  } else {
    return true;
  }
}

struct Calc
{
  int count;
  const RockSet& rocks;
  typedef pair<uint, uint> PairUint;
  typedef boost::unordered_map<pair<uint, uint>, uint > Cache;
  Cache cache;
  
  Calc(const RockSet& rocks) : rocks(rocks), count(0) {}
  
  int calculate_unique_paths(const uint& level, const uint& index);
};

int Calc::calculate_unique_paths(const uint& level, const uint& index)
{
  
  
  Cache::iterator c_it = cache.find(PairUint(level, index));
  
  if (c_it != cache.end()) {
     return c_it->second;
  }
  
  ++count;
  
  if (count % 1000 == 0) {
    //LOG_ON();
    LOG(count);
    LOG_OFF();
  }
  
  //LOG_OFF();
  //LOG_STR(level << " " << index);
  
  RowCol rc = getRowCol(level, index);
  
  int r = 0;
  
  if (isMember(rocks, rc)) {
    r = 0;
  } else if (level == 0) {
    assert(index == 0);
    r = 1;
  } else { 
  
    assert(index >= 0 && index <= level);
    
    
    
    if (index > 0) {
      r += calculate_unique_paths(level - 1, index - 1);
    }
    
    if (index < level) {
      r += calculate_unique_paths(level - 1, index);
    }
  }
  
  r %= 10007;
  
  cache.insert(Cache::value_type(PairUint(level, index), r));
  return r;
}

void getRockLevelIndex(RockLevelIndexSet& rockLevelIndexSet, const RockSet& rockRowColSet)
{
  
  for(RockSet::const_iterator r_it = rockRowColSet.begin();
    r_it != rockRowColSet.end();
    ++r_it)
  {
    const RowCol& rockRC = *r_it;
    uint rockLevel, rockIndex;
    bool rockOnPath = getLevelIndex(rockLevel, rockIndex, rockRC);
    if (rockOnPath) {
      LOG_ON();
      LOG(rockLevel);
      LOG(rockIndex);
      rockLevelIndexSet.insert(RockLevelIndexSet::value_type(rockLevel, rockIndex));
    }
  }
}

uint getUniquePaths(uint targetLevel, uint targetIndex, const RockMultipliers& rockMult);

//Multiplier is where the value of the rock
void getRockMultipliers(const RockLevelIndexSet& rockLevelIndexSet, RockMultipliers& rockMult)
{
  LOG_STR("getRockMultipliers");
  
  assert(rockMult.empty());
  
  for(RockLevelIndexSet::const_iterator r_it = rockLevelIndexSet.begin();
    r_it != rockLevelIndexSet.end();
    ++r_it)
  {
    uint rockLevel = r_it->first;
    uint rockIndex = r_it->second;
    
    LOG(rockLevel);
    LOG(rockIndex);
    
    uint reduceByMult = getUniquePaths(rockLevel, rockIndex, rockMult);
    LOG(reduceByMult);
    
    rockMult.insert(RockMultipliers::value_type(*r_it, reduceByMult));
  }
}


uint getUniquePaths(uint targetLevel, uint targetIndex, const RockMultipliers& rockMult)
{
  LOG_ON();
  LOG_STR("getUniquePaths");
  RockSet empty;
  Calc c(empty);
  //start with target
  int num_paths = c.calculate_unique_paths(targetLevel, targetIndex);
  LOG(num_paths);
  for(RockMultipliers::const_iterator r_it = rockMult.begin();
    r_it != rockMult.end();
        ++r_it)
  {
    uint rockLevel = r_it->first.first;
    uint rockIndex = r_it->first.second;
    
    LOG(rockLevel);
    LOG(rockIndex);
    
    if (rockIndex > targetIndex) {
      LOG_STR("Ignoring rock, index too Far");
      continue;
    }
    
    assert(rockLevel <= targetLevel);
    assert(rockIndex <= targetIndex);

      
    uint reduceByMult = r_it->second;
    LOG(reduceByMult);
      
    int adjLevel = targetLevel - rockLevel;
    int adjIndex = targetIndex - rockIndex;
    LOG(adjLevel);
    LOG(adjIndex);
      
    if (adjIndex > adjLevel) {
      LOG_STR("Ignoring rock, doesn't touch final square");
      continue;
    }
      
    int reduceBy = c.calculate_unique_paths(adjLevel, adjIndex);
      
    LOG(reduceBy);
    LOG(num_paths);
      //assert(reduceBy * reduceByMult <= num_paths);
      //num_paths += 10007;
    num_paths -= (reduceBy * reduceByMult);
    num_paths %= 10007;
    if (num_paths < 0) {
      LOG(num_paths);
      num_paths += 10007;
    }
    LOG(num_paths);
    LOG_OFF();
  }
  
  return num_paths;
}
      
void do_test_case(int test_case, ifstream& input)
{
  
  LOG_OFF();
  unsigned int H, W, R;
  input >> H >> W >> R;
  
  RockSet rocks;
  
  for(int i = 0; i < R; ++i) {
    unsigned int r, c;
    input >> r >> c;
    RowCol rc(r -1, c - 1);
    rocks.insert(rc);
    LOG(rc);
  }
  
  RockSet testRocks;
  testRocks.insert(RowCol(3-1, 2-1));
  
  assert(isMember(testRocks, RowCol(2, 1)));
  LOG_ON();
  LOG_OFF();
  for(int level = 0; level < 20; ++level)
  {
    LOG(level);
    for(int i = 0; i <= level; ++i)
    {
      LOG_STR(level << ", " << i << " = rc: " << getRowCol(level, i));
    }
  }
  //return;
  RowCol target(H-1, W-1);

  uint level, index;  
  bool possible = getLevelIndex(level, index, target);
  
  LOG_ON();
  LOG_STR("Possible " << possible);
  LOG_STR("Final Level: " << level);
  LOG_STR("Final index: " << index);
  LOG_STR("Final Target: " << target);
  LOG_OFF();
  
  uint num_paths = 0;
  
  if (possible) {
    RockLevelIndexSet rockLevelIndexSet;
    
    getRockLevelIndex(rockLevelIndexSet, rocks);
    
    RockMultipliers rockMultipliers;
    
    getRockMultipliers(rockLevelIndexSet, rockMultipliers);
    LOG_STR("Final get unique");
    num_paths = getUniquePaths(level, index, rockMultipliers);
  }
  
  printf("Case #%d: %d\n", test_case+1, num_paths);
   
  return;    
}
  
