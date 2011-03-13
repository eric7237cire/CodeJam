// for BOOST testing
#define BOOST_TEST_MODULE GraphTest
#include <boost/test/unit_test.hpp>
using boost::unit_test_framework::test_suite;
using boost::unit_test_framework::test_case;

#include <fstream>
#include <iostream>
#include <vector>
#include <algorithm>
#include <limits>

#include <stdio.h>
#include <time.h>
#include <assert.h>
#include <stdlib.h>
#include <cmath>

#include "boost/bind.hpp"
// the class to be tested
#include "graph.h"
#include "bipartite.h"

using namespace std;

class GraphTestFixture 
{
   public:

   // constructor
   GraphTestFixture() 
   {
   }

   

};

BOOST_FIXTURE_TEST_SUITE(GraphTest, GraphTestFixture)

BOOST_AUTO_TEST_CASE(test_graph)
{
  LOG_OFF();
  using namespace CompleteGraph;
  Graph g(5);
  // 0 1 2 3 4
  g.addConnection(0, 1);
  g.addConnection(1, 2);
  g.addConnection(2, 3);
  g.addConnection(3, 4);
  g.inverse();
  
  set<int> a;
  g.findLargestSuperConnectedSubGraph(a);
  
  LOG_STR("The winner is " << a);
  
}

BOOST_AUTO_TEST_CASE(test_bipartite_isolated)
{
  using namespace Bipartite;
  Graph g;
  g.addNode(37);
  g.addNode(48);
  
  set<int> s;
  
  g.findMaximumIndependantSet(s);
  
  BOOST_CHECK(s.size() == 2);
}

BOOST_AUTO_TEST_CASE(test_bipartite)
{
//..........
//x.x.xx.x.x

//0134678013
//x2x5xx9x2x
  using namespace Bipartite;
  LOG_ON();
  LOG_STR("Starting");
  Graph g;
  
  for(int i = 0; i < 14; ++i)
  {
    g.addNode(i);
  }
  
  g.addConnection(0, 1);
  g.addConnection(0, 2);
  
  g.addConnection(3, 1);
  g.addConnection(3, 2);
  
  g.addConnection(3, 4);
  g.addConnection(3, 5);
  
  g.addConnection(6, 4);
  g.addConnection(6, 5);
  
  g.addConnection(6, 7);
  
  g.addConnection(8, 7);
  g.addConnection(9, 7);
  
  g.addConnection(8, 10);
  g.addConnection(9, 10);
  
  g.addConnection(10, 11);
  g.addConnection(10, 12);
  
  g.addConnection(13, 11);
  g.addConnection(13, 12);
  
  g.partition();
  
  cout << g << endl;
  
  set<int> nodeSet;
  g.findMaximumIndependantSet(nodeSet);
  
  cout << nodeSet << endl; 
  
}
  
BOOST_AUTO_TEST_SUITE_END()


