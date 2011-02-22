#include <fstream>
#include <iostream>
#include <vector>
#include <algorithm>
#include <limits>
#include <string>
#include <cstring>
#include <bitset>
#include <stdio.h>
#include <time.h>
#include <assert.h>
#include <stdlib.h>
#include <boost/numeric/conversion/bounds.hpp>
#include <boost/limits.hpp>
#include <boost/smart_ptr.hpp>
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
      //SHOW_TIME_BEGIN(test_case)
      do_test_case(test_case, input);
      //SHOW_TIME_END(test_case)
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

const unsigned int MAX_K = 16;
const unsigned int MAX_LETTER = 26;

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
      assert(charVal >= 1);
      trace("Charval %d, i %d, string %s\n", charVal, i, s.c_str());
      assert(charVal <= 26);
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
  
  /*
  abcc
  baba
  cacb
  
  tracks bonus from being the same from one line to the next
  
  */
  vector<VectorUI> beginEndBonuses;

public:  
  Graph() : connections(MAX_K), beginEndBonuses(MAX_K) {
    for(unsigned int i=0; i<MAX_K; ++i) {
      connections[i] = VectorUI(MAX_K, 0);
      beginEndBonuses[i] = VectorUI(MAX_K, 0);
    }
  }
  
  void addBeginingEndBonuses(const LetterCounter& lc, const LetterCounter& next_row_lc)
  {
    for(unsigned int ch = 0; ch < MAX_LETTER; ++ch) 
    {
      trace("Processing character %d\n", ch);
      bitset<MAX_K> row_counts = lc.getCounts(ch);
      bitset<MAX_K> next_row_counts = next_row_lc.getCounts(ch);
      
      vector<unsigned int> row_pos_hits;
      vector<unsigned int> next_row_pos_hits;
      
      for(unsigned int pos = 0; pos < MAX_K; ++pos)
      {
        trace("Processing position %d\n", pos);
        if (row_counts[pos]) {
          trace("Hit! %d\n", pos);
          row_pos_hits.push_back( pos );          
        }
        
        if (next_row_counts[pos]) {
          next_row_pos_hits.push_back(pos);
        }
      }

      
      for(unsigned int i=0; i<row_pos_hits.size(); ++i) {
        for(unsigned int j=0; j<next_row_pos_hits.size(); ++j) {
          unsigned int pos1 = row_pos_hits[i];
          unsigned int pos2 = next_row_pos_hits[j];
          if (pos1==pos2) {
            continue;
          }
          debug("Adding start end connection %d %d\n", pos2, pos1);
          
          beginEndBonuses[pos2][pos1] += 1;
           
        }
      }
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
  
  unsigned int getEdgeWeight(unsigned int fromNode, unsigned int toNode) const
  {
    assert(fromNode >=0 && fromNode < MAX_K);
    assert(toNode >= 0 && toNode < MAX_K);
    debug("connections[%d][%d] = %d\n", fromNode, toNode, connections[fromNode][toNode]);
    return connections[fromNode][toNode];
  }
  
  unsigned int getBoundaryBonus(unsigned int fromNode, unsigned int toNode) const
  {
    assert(fromNode >=0 && fromNode < MAX_K);
    assert(toNode >= 0 && toNode < MAX_K);
    debug("connections[%d][%d] = %d\n", fromNode, toNode, connections[fromNode][toNode]);
    return beginEndBonuses[fromNode][toNode];
  }
  
  
};

class DP 
{
public:
  typedef int(* M_ARRAY) [MAX_K][MAX_K][1 << MAX_K];
  typedef int(& M_ARRAY_REF) [MAX_K][MAX_K][1 << MAX_K];
  static M_ARRAY m_p;
  static M_ARRAY_REF m;
  const Graph& graph;
  
  unsigned int stop;
  unsigned int cache_hits;
  unsigned const int k;
  
  
public:
  
  ~DP() {
    //delete [] m_p;
  }
  
  DP(const Graph& graph, unsigned int k) : graph(graph), stop(0), cache_hits(0), k(k) {
    info("Memsetting m %d %d %d\n", sizeof(M_ARRAY_REF), sizeof(unsigned int), (1 << 3));
    
    if (!m_p) {
      m_p = (M_ARRAY) new int[MAX_K][MAX_K][1 << MAX_K];
    }
    
    //M_ARRAY_REF m = *m_p;
    //m = *m_p;
    memset(m_p, -1, sizeof(M_ARRAY_REF));
    
    assert(m[0][0][0] == -1);
    assert(m[MAX_K-1][MAX_K-1][0] == -1);
  }
  
  unsigned int distance(const unsigned int startNode, const unsigned int endNode, bitset<MAX_K> visitedBits) 
  {
    
    const unsigned int visited = visitedBits.to_ulong();
    int cached_value = m[startNode][endNode][visited];
    
    if (cached_value >= 0) {
      #if INFO
      ++cache_hits;
      //debug("CACHE RETURN Start Node: %d, End Node: %d, Visited: %d, returning %d\n", startNode, endNode, visitedBits.to_ulong(), cached_value);
      #endif
      return cached_value;
    }
    
    int boundaryBonus = graph.getBoundaryBonus(startNode, endNode);
    
    visitedBits.set(startNode, 1);    
    visitedBits.set(endNode, 1);
    
    debug("Start Node: %d, End Node: %d, visited: %s\n", startNode, endNode, visitedBits.to_string().c_str());
    unsigned int max_weight = 0;
    
    if (visitedBits.count() == k) {
      unsigned int ret = graph.getEdgeWeight(startNode, endNode);
      debug("END %d + %d\n", ret, boundaryBonus);
      m[startNode][endNode][visited] = ret+boundaryBonus;
      return ret+boundaryBonus;  
    }
  
    for(unsigned int pos = 0; pos < k; ++pos)
    {
      if (visitedBits.test(pos) == false) {
        //bitset<MAX_K> nextVisited(visitedBits);
        //nextVisited.set(pos, 1);
        
        //debug("Calling distance with nextVisited, pos %d: %s %d\n", pos, nextVisited.to_string().c_str(), static_cast<unsigned int>( nextVisited.to_ulong() ));
        
        const unsigned int weight = boundaryBonus + graph.getEdgeWeight(startNode, pos) + distance_nested(pos, endNode, visitedBits );
        
        if (weight > max_weight) {
          max_weight = weight;
        }
      }
    }
        
    debug("RETURN Start Node: %d, End Node: %d, Visited: %s, returning %d\n", startNode, endNode, visitedBits.to_string().c_str(), max_weight);
    m[startNode][endNode][visited] = max_weight;
    return max_weight;
  }
  
  unsigned int distance_nested(const unsigned int startNode, const unsigned int endNode, bitset<MAX_K> visitedBits) 
  {
    const unsigned int visited = visitedBits.to_ulong();
    int cached_value = m[startNode][endNode][visited];
    
    if (cached_value >= 0) {
      #if INFO
      ++cache_hits;
      //debug("CACHE RETURN Start Node: %d, End Node: %d, Visited: %d, returning %d\n", startNode, endNode, visitedBits.to_ulong(), cached_value);
      #endif
      return cached_value;
    }
    #if INFO
    ++stop;
    if (stop % 50000 == 0) {
      info("Distance, %d uncached iterations, cached: %d\n", stop, cache_hits);
    }
    if (stop > 9000000) {
      throw 3;
    }
    #endif
    /*
    f(0, 1, 0001)
    f(2, 1, 0101)
    f(3, 1, 1101)    
    */
    
    //debug("Start Node: %d, End Node: %d, visited: %d %s\n", startNode, endNode, visited, visitedBits.to_string().c_str());
    
    visitedBits.set(startNode, 1);
    
    //debug("Start Node: %d, End Node: %d, visited: %s, needToVisitBits: %s\n", startNode, endNode, visitedBits.to_string().c_str(), needToVisitBits.to_string().c_str());
    
    
    if (visitedBits.to_ulong() == (1 << k) - 1) {
      unsigned int ret = graph.getEdgeWeight(startNode, endNode);
      debug("END %d\n", ret);
      m[startNode][endNode][visited] = ret;
      return ret;  
    }
  
    unsigned int max_weight = 0;
    for(unsigned int pos = 0; pos < k; ++pos)
    {
      if (visitedBits.test(pos) == false) {
        //bitset<MAX_K> nextVisited(visitedBits);
        //nextVisited.set(pos, 1);
        
        //debug("Calling distance with nextVisited, pos %d: %s %d\n", pos, nextVisited.to_string().c_str(), static_cast<unsigned int>( nextVisited.to_ulong() ));
        
        const unsigned int weight = graph.getEdgeWeight(startNode, pos) + distance_nested(pos, endNode, visitedBits );
        
        if (weight > max_weight) {
          max_weight = weight;
        }
      }
    }
        
    debug("RETURN Start Node: %d, End Node: %d, Visited: %s, returning %d\n", startNode, endNode, visitedBits.to_string().c_str(), max_weight);
    m[startNode][endNode][visited] = max_weight;
    return max_weight;
  }
  
};

DP::M_ARRAY DP::m_p = (M_ARRAY) new int[MAX_K][MAX_K][1 << MAX_K];
DP::M_ARRAY_REF DP::m = *DP::m_p;

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
  g.addBeginingEndBonuses(lc, lc2);
  debug("Adding counts 3\n");
  g.addCounts(lc3);
  g.addBeginingEndBonuses(lc2, lc3);
  
  assert(lc.getCounts(0).to_string().substr(MAX_K-4, 4) == "0001");
  
  assert(lc2.getCounts(0).to_string().substr(MAX_K-4, 4) == "1010");
  assert(lc2.getCounts(1).to_string().substr(MAX_K-4, 4) == "0101");
  assert(lc2.getCounts(2).to_string().substr(MAX_K-4, 4) == "0000");
  
  assert(lc.getCounts(0).to_string().substr(MAX_K-4, 4) == "0001");
  
  info(LetterCounter("azezz").getCounts(25).to_string().substr(MAX_K-5, 5).c_str());
  assert(LetterCounter("azezz").getCounts(25).to_string().substr(MAX_K-5, 5) == "11010");
  
  assert(g.getEdgeWeight(0, 0) == 0);
  assert(g.getEdgeWeight(3, 1) == 1);
  assert(g.getEdgeWeight(0, 2) == 2);
  assert(g.getEdgeWeight(2, 0) == 2);
  assert(g.getEdgeWeight(2, 3) == 1);
  
  assert(g.getBoundaryBonus(1, 0) == 1);
  assert(g.getBoundaryBonus(3, 0) == 2);
  assert(g.getBoundaryBonus(1, 3) == 1);
  assert(g.getBoundaryBonus(3, 2) == 1);
  
  {
  DP dp(g, 4);
  
  assert(dp.distance(1, 0, 0) == 5);
  }
  
  
  {
  Graph g2;
  g2.addCounts(LetterCounter("azezz"));
  
  assert(g2.getEdgeWeight(3, 4) == 1);
  
  info("Testing #2\n");
  DP dp2(g2, 5);
  
  assert(dp2.distance(0, 4, 0) == 2);
  assert(dp2.distance(1, 4, 0) == 1);
  }
}



void do_test_case(int test_case, ifstream& input)
{
 
  test();
  
  unsigned int k;
  input >> k;
  
  string s;
  input >> s;
  
  Graph g;
  boost::scoped_ptr<LetterCounter> prev_lc;
  boost::scoped_ptr<LetterCounter> lc;
  
  
  if (k>MAX_K) {
    return;
  }
  
  const unsigned int segments = s.size() / k;
  assert(s.size() % k == 0);
  
  for(unsigned int i=0; i<segments; ++i) {
    lc.reset( new LetterCounter(string(s.begin() + k * i, s.begin() + k * i + k)) );   
    g.addCounts(*lc);
    
    if (i > 0) {
      g.addBeginingEndBonuses(*prev_lc, *lc);  
    }
    
    prev_lc.swap( lc );
  }
    
  DP dp(g, k);
  
  unsigned int max_reduction = 0;
  for(unsigned int i=0; i<k; ++i) {
    for(unsigned int j=0; j<k; ++j) {
      if(i==j) {
        continue;
      }
      
      unsigned int pathLength = dp.distance(i, j, 0);
      if (pathLength > max_reduction) {
        max_reduction = pathLength;
      }
    }
  }
  
  unsigned int min_rle_length = s.size() - max_reduction;
      
  printf("Case #%d: %d\n", test_case+1, min_rle_length);
  
  #if 0
  vector<unsigned int> perm(k);
  for(unsigned int i=0; i<k; ++i) {
    perm[i] = i;
  }

  unsigned int lowest_size = s.size();

  do {
    #if TRACE
    for(unsigned int i=0; i<k; ++i) {
      printf("%d, ", perm[i]);
    }
    printf("\n");
    #endif
    //cout << myints[0] << " " << myints[1] << " " << myints[2] << endl;
    //debug(perm_string(s, perm).c_str());
    //debug("\n");
    unsigned int rle = compute_rle(perm_string(s, perm));
    if (rle < lowest_size) {
      //info("New string %s, rle %d\n", perm_string(s, perm).c_str(), rle);
      lowest_size = rle;
    }
  } while ( next_permutation (perm.begin(), perm.end()) );
  
   printf("Case #%d: %d\n", test_case+1, lowest_size);
   #endif
   
  return;
    
}
  
