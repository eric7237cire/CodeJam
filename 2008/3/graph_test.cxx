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
  Graph g(5);
  // 0 1 2 3 4
  g.addConnection(0, 1);
  g.addConnection(1, 2);
  g.addConnection(2, 3);
  g.addConnection(3, 4);
  g.inverse();
  
  set<int> a;
  g.findLargestSuperConnectedSubGraph(a);
  
  cout << "The winner is " << a << endl;
  
}
  
BOOST_AUTO_TEST_SUITE_END()


