#include <fstream>
#include <iostream>
#include <vector>
#include <algorithm>
#include <limits>
#include <string>
#include <bitset>
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

const unsigned int MAX_K = 4;
const unsigned int MAX_LETTER = 3;

class LetterCounter
{
  vector<unsigned int> counts;
  unsigned int k;
  
  /*
  String s = a b c c
  counts[0, 'a'] = 0001
  counts[1, 'b'] = 0010
  counts[2, 'c'] = 1100
  
  */
public:
  LetterCounter(const string& s) : counts(MAX_LETTER, 0), k(s.size()) 
  {
    for(unsigned int i=0; i<k; ++i) 
    {
      unsigned int charVal = s[i] - 'a' + 1;
      counts[charVal-1] = SetBit(counts[charVal-1], i+1);   
    }
  }
  
  unsigned int operator[](unsigned int idx) const
  {
    return counts[idx]; 
  }
  
  bitset<MAX_K> getCounts(unsigned int idx) const
  {
    return bitset<MAX_K>(counts[idx]); 
  }
};

typedef vector<unsigned int> VectorUI;

class Graph
{
  //connections[node i][node j] = weight
  vector<VectorUI> connections;

public:  
  Graph() : connections(MAX_K) {
    for(unsigned int i=0; i<MAX_K; ++i) {
      connections[i] = VectorUI(MAX_K, 0);
    }
  }
  
  void addCounts(const LetterCounter& lc) 
  {
    for(unsigned int ch = 0; ch < MAX_LETTER; ++ch) 
    {
      trace("Processing character %d\n", ch);
      bitset<MAX_K> counts = lc.getCounts(ch);
      vector<unsigned int> pos_hits;
      for(unsigned int pos = 0; pos < MAX_K; ++pos)
      {
        trace("Processing position %d\n", pos);
        if (counts[pos]) {
          trace("Hit! %d\n", pos);
          pos_hits.push_back( pos );          
        }
      }

      
      for(unsigned int i=0; i<pos_hits.size(); ++i) {
        for(unsigned int j=i+1; j<pos_hits.size(); ++j) {
          unsigned int pos1 = pos_hits[i];
          unsigned int pos2 = pos_hits[j];
          if (pos1==pos2) {
            continue;
          }
          trace("Adding connection %d %d\n", pos1, pos2);
          trace("Connections are %d %d\n", connections[pos2][pos1], connections[pos1][pos2]);
          
          connections[pos1][pos2] += 1;
          connections[pos2][pos1] += 1;
          
          trace("Connections are %d %d\n", connections[pos2][pos1], connections[pos1][pos2]); 
        }
      }
    }
  }
  
  unsigned int getEdgeWeight(unsigned int fromNode, unsigned int toNode)
  {
    assert(fromNode >=0 && fromNode < MAX_K);
    assert(toNode >= 0 && toNode < MAX_K);
    debug("connections[%d][%d] = %d\n", fromNode, toNode, connections[fromNode][toNode]);
    return connections[fromNode][toNode];
  }
  
  
};

class DP 
{
  unsigned int m[MAX_K][MAX_K][1 << MAX_K];
  
  const Graph& graph;
  
  DP() {
    info("Memsetting m %d\n", sizeof(m));
    memset(m, 0, sizeof(m));
  }
  
  unsigned int distance(unsigned int startNode, unsigned int endNode, unsigned int visited)
  {
    /*
    f(0, 1, 0001)
    f(2, 1, 0101)
    f(3, 1, 1101)    
    */
    
    bitset<MAX_K> visitedBits(visitedBits);    
    debug("Start Node: %d, End Node: %d, visited: %s\n", startNode, endNode, visitedBits.to_string());
    
    return 37;
    
    
    
  }
  
};

void test() 
{
  assert(compute_rle("aabcaaaa") == 4);
  
  vector<unsigned int> p;
  p.push_back(0);
  p.push_back(3);
  p.push_back(2);
  p.push_back(1);
  info(perm_string("abcabcabcabc", p).c_str());
  assert(perm_string("abcabcabcabc", p) == string("aacbbbacccba"));
  
  LetterCounter lc = LetterCounter("abcc");
  assert(lc[0] == 1);
  assert(lc[1] == 2);
  assert(lc[2] == 12);
  
  Graph g;
  
  LetterCounter lc2 = LetterCounter("baba");
  LetterCounter lc3 = LetterCounter("cacb");
  
  g.addCounts(lc);
  debug("Adding counts 2\n");
  g.addCounts(lc2);
  debug("Adding counts 3\n");
  g.addCounts(lc3);
  
  assert(lc.getCounts(0).to_string() == "0001");
  
  assert(lc2.getCounts(0).to_string() == "1010");
  assert(lc2.getCounts(1).to_string() == "0101");
  assert(lc2.getCounts(2).to_string() == "0000");
  
  assert(g.getEdgeWeight(0, 0) == 0);
  assert(g.getEdgeWeight(3, 1) == 1);
  assert(g.getEdgeWeight(0, 2) == 2);
  assert(g.getEdgeWeight(2, 0) == 2);
  assert(g.getEdgeWeight(2, 3) == 1);
}



void do_test_case(int test_case, ifstream& input)
{
 
  test();
  
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
  
