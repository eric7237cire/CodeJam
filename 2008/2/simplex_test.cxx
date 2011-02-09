// for BOOST testing
#define BOOST_TEST_MODULE SimplexTest
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
#include "simplex.h"

using namespace std;

class SimplexTestFixture
{
   public:

   // constructor
   SimplexTestFixture()
   {
   }

   

};

BOOST_FIXTURE_TEST_SUITE(SimplexTest, SimplexTestFixture)

BOOST_AUTO_TEST_CASE(test_simple_max_1)
/** non mixed program */
{
  cout << "Maximize z = 4x + 6y subject to -x + y <= 11, x + y <= 27, 2x + 5y <= 90" << endl;
  cout << "Optimal Solution: z = 132; x = 15, y = 12" << endl;
  
  Simplex simplex(2, 3);
  VecDouble z;
  z.push_back(4);
  z.push_back(6);
  simplex.set_z(z);
  
  VecDouble c1;
  c1.push_back(-1);
  c1.push_back(1);
  
  simplex.add_constraint(0, c1, 11);
  
  c1.resize(0);
  c1.push_back(1);
  c1.push_back(1);
  
  simplex.add_constraint(1, c1, 27);
  
  c1.resize(0);
  c1.push_back(2);
  c1.push_back(5);
  
  simplex.add_constraint(2, c1, 90);
  
  int steps = 0;
  simplex.print();
  while (!simplex.solved()) {
     	
    simplex.do_step();
    simplex.print();
    ++steps;
  }
  
  //BOOST_REQUIRE(steps == 2);
  BOOST_CHECK(steps == 3);
  BOOST_CHECK(simplex.getVar(0) == 15);
  BOOST_CHECK(simplex.getVar(1) == 12);
}

BOOST_AUTO_TEST_CASE(test_simple_max_2)
{
  Simplex simplex(3, 3);
  VecDouble z;
  VecDouble c1;
  
  z.push_back(2);
  z.push_back(-1);
  z.push_back(2);
  simplex.set_z(z);
  
  c1.resize(0);
  c1.push_back(2);
  c1.push_back(1);
  c1.push_back(0);
  simplex.add_constraint(0, c1, 10);
  
  c1.resize(0);
  c1.push_back(1);
  c1.push_back(2);
  c1.push_back(-2);
  simplex.add_constraint(1, c1, 20);
  
  c1.resize(0);
  c1.push_back(0);
  c1.push_back(1);
  c1.push_back(2);
  simplex.add_constraint(2, c1, 5);
  
  int steps = 0;
  simplex.print();
  
  while(!simplex.solved()) {
    printf("Step: %d \n", ++steps);
    simplex.do_step();
    simplex.print();
  }
  
  BOOST_CHECK(steps == 2);
  BOOST_CHECK(simplex.getVar(0) == 5);
  BOOST_CHECK(simplex.getVar(1) == 0);
  BOOST_CHECK(simplex.getVar(2) == 2.5);
  
  cout << "Maximize P = 2x - y + 2z subject to 2x + y <= 10, x + 2y -2z <= 20, y + 2z <= 5" << endl;
  cout << "Optimal Solution: p = 15; x = 5, y = 0, z = 2.5" << endl;
  //http://www.zweigmedia.com/RealWorld/simplex.html
  simplex.print_current_solution();
  
}

BOOST_AUTO_TEST_CASE(test_simple_min)
{
  
  VecDouble z;
  VecDouble c1;
  Simplex minplex = Simplex(2, 3);
  
  z.resize(0);
  //these are negative instead
  z.push_back(-.12);
  z.push_back(-.15);
  minplex.set_z(z);

  c1.resize(0);
  c1.push_back(60);
  c1.push_back(60);
  //>= and switched to <= by multiplying by -1 as that changes the sign of
  //the slack variable
  minplex.add_constraint(0, c1, -300);
  
  c1.resize(0);
  c1.push_back(12);
  c1.push_back(6);
  minplex.add_constraint(1, c1, -36);
  
  c1.resize(0);
  c1.push_back(10);
  c1.push_back(30);
  minplex.add_constraint(2, c1, -90);
    
  
  
  minplex.print();
  int steps = 0;
  while(!minplex.solved()) {
    printf("Step: %d \n", ++steps);
    minplex.do_step();
    minplex.print();
  }
  
  cout << "Minimize W = .12x + .15y subject to 60x+60y >= 300, 12x+6y >= 36, 10x + 30y >= 90" << endl;
  cout << "Optimal Solution: w = 0.66; x = 3, y = 2" << endl;
  minplex.print_current_solution();
  
  BOOST_CHECK(minplex.getVar(0) == 3);
  BOOST_CHECK(minplex.getVar(1) == 2);
}

BOOST_AUTO_TEST_SUITE_END()


