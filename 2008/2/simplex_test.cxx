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

BOOST_AUTO_TEST_CASE(test_non_mixed_max_1)
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
  
  simplex.add_constraint_lte(c1, 11);
  
  c1.resize(0);
  c1.push_back(1);
  c1.push_back(1);
  
  simplex.add_constraint_lte(c1, 27);
  
  c1.resize(0);
  c1.push_back(2);
  c1.push_back(5);
  
  simplex.add_constraint_lte(c1, 90);
  
  simplex.initial_simplex_tableau();
  
  simplex.print();
  
  BOOST_REQUIRE(simplex.is_feasible());
  
  int steps = 0;
  simplex.print();
  while (!simplex.solved()) {
     	
    BOOST_REQUIRE(simplex.do_step());
    simplex.print();
    ++steps;
  }
  
  //BOOST_uCHECK
  //BOOST_REQUIRE(steps == 2);
  BOOST_REQUIRE(steps == 3);
  BOOST_REQUIRE(simplex.getVar(0) == 15);
  BOOST_REQUIRE(simplex.getVar(1) == 12);
}

BOOST_AUTO_TEST_CASE(test_simple_max_2)
{
  Simplex simplex(3, 3);
  VecDouble z;
  VecDouble c1;
  
  z.push_back(2);
  z.push_back(-1);
  z.push_back(2);
  simplex.set_eq_to_maximize(z);
  
  c1.resize(0);
  c1.push_back(2);
  c1.push_back(1);
  c1.push_back(0);
  simplex.add_constraint_lte(c1, 10);
  
  c1.resize(0);
  c1.push_back(1);
  c1.push_back(2);
  c1.push_back(-2);
  simplex.add_constraint_lte(c1, 20);
  
  c1.resize(0);
  c1.push_back(0);
  c1.push_back(1);
  c1.push_back(2);
  simplex.add_constraint_lte(c1, 5);
  
  int steps = 0;
  simplex.print();
  
  while(!simplex.solved()) {
    printf("Step: %d \n", ++steps);
    BOOST_REQUIRE(simplex.do_step());
    simplex.print();
  }
  
  BOOST_REQUIRE(steps == 2);
  BOOST_REQUIRE(simplex.getVar(0) == 5);
  BOOST_REQUIRE(simplex.getVar(1) == 0);
  BOOST_REQUIRE(simplex.getVar(2) == 2.5);
  
  cout << "Maximize P = 2x - y + 2z subject to 2x + y <= 10, x + 2y -2z <= 20, y + 2z <= 5" << endl;
  cout << "Optimal Solution: p = 15; x = 5, y = 0, z = 2.5" << endl;
  //http://www.zweigmedia.com/RealWorld/simplex.html
  simplex.print_current_solution();
  
}

BOOST_AUTO_TEST_CASE(test_simple_min)
{
  
  VecDouble z;
  VecDouble c1;
  Simplex simplex = Simplex(2, 3);
  
  z.resize(0);
  z.push_back(.12);
  z.push_back(.15);
  simplex.set_eq_to_minimize(z);

  c1.resize(0);
  c1.push_back(60);
  c1.push_back(60);
  
  simplex.add_constraint_gte(c1, 300);
  
  c1.resize(0);
  c1.push_back(12);
  c1.push_back(6);
  simplex.add_constraint_gte(c1, 36);
  
  c1.resize(0);
  c1.push_back(10);
  c1.push_back(30);
  simplex.add_constraint_gte(c1, 90);
    
  
  
  simplex.print();
  
  simplex.initial_simplex_tableau();
  
  simplex.print();
  
  BOOST_REQUIRE(simplex.is_feasible());
  
  int steps = 0;
  while(!simplex.solved()) {
    printf("Step: %d \n", ++steps);
    bool step_worked = simplex.do_step();
    BOOST_REQUIRE(step_worked);
    if (!step_worked) {
      break;
    }
    
    simplex.print();
  }
  
  cout << "Minimize W = .12x + .15y subject to 60x+60y >= 300, 12x+6y >= 36, 10x + 30y >= 90" << endl;
  cout << "Optimal Solution: w = 0.66; x = 3, y = 2" << endl;
  simplex.print_current_solution();
  
  BOOST_REQUIRE(simplex.getVar(0) == 3);
  BOOST_REQUIRE(simplex.getVar(1) == 2);
}


BOOST_AUTO_TEST_CASE(test_mixed_max_1)
{
  
  Simplex simplex(3, 3);
  VecDouble z;
  z.push_back(1);
  z.push_back(1);
  z.push_back(2);
  simplex.set_eq_to_maximize(z);
  
  VecDouble c1;
  c1.push_back(2);
  c1.push_back(1);
  c1.push_back(1);
  
  simplex.add_constraint_lte(c1, 50);
  
  c1.resize(0);
  c1.push_back(2);
  c1.push_back(1);
  c1.push_back(0);
  
  simplex.add_constraint_gte(c1, 36);
  
  c1.resize(0);
  c1.push_back(1);
  c1.push_back(0);
  c1.push_back(1);
  
  simplex.add_constraint_gte(c1, 10);
  
  VecDouble verify_vals;
  verify_vals.push_back(0);
  verify_vals.push_back(36);
  verify_vals.push_back(14);
  verify_vals.push_back(50 - 36 - 14);
  verify_vals.push_back(36 - 36);
  verify_vals.push_back(14 - 10);
  
  simplex.initial_simplex_tableau();
  
  simplex.print();
  
  BOOST_REQUIRE(simplex.is_feasible());
  
  int steps = 0;
  simplex.print();
  //BOOST_REQUIRE(simplex.verify_equations(verify_vals));
  while (!simplex.solved()) {
     	
    BOOST_REQUIRE(simplex.do_step());
    simplex.print();
    //BOOST_REQUIRE(simplex.verify_equations(verify_vals));
    ++steps;
  }
  
  //BOOST_uCHECK
  //BOOST_REQUIRE(steps == 2);
  BOOST_REQUIRE(simplex.getVar(0) == 0);
  BOOST_REQUIRE(simplex.getVar(1) == 36);
  BOOST_REQUIRE(simplex.getVar(2) == 14);
}

BOOST_AUTO_TEST_CASE(test_mixed_max_2)
{
  
  Simplex simplex(3, 3);
  VecDouble z;
  z.push_back(3);
  z.push_back(2);
  z.push_back(4);
  simplex.set_eq_to_maximize(z);
  
  VecDouble c1;
  c1.push_back(3);
  c1.push_back(2);
  c1.push_back(5);
  
  simplex.add_constraint_lte(c1, 18);
  
  c1.resize(0);
  c1.push_back(4);
  c1.push_back(2);
  c1.push_back(3);
  
  simplex.add_constraint_lte(c1, 16);
  
  c1.resize(0);
  c1.push_back(2);
  c1.push_back(1);
  c1.push_back(1);
  
  simplex.add_constraint_gte(c1, 4);
  
  int steps = 0;
  simplex.print();
  
  simplex.initial_simplex_tableau();
  //BOOST_CHECK(simplex.verify_equations(verify_vals));
  while (!simplex.solved()) {
     	
    BOOST_REQUIRE(simplex.do_step());
    simplex.print();
    //BOOST_CHECK(simplex.verify_equations(verify_vals));
    ++steps;
  }
  
  //BOOST_uCHECK
  //BOOST_REQUIRE(steps == 2);
  BOOST_REQUIRE(simplex.getVar(0) == 0);
  BOOST_REQUIRE(simplex.getVar(1) == 13.0 / 2);
  BOOST_REQUIRE(simplex.getVar(2) == 1);
}

BOOST_AUTO_TEST_CASE(test_mixed_min_1)
{
  
  Simplex simplex(3, 3);
  VecDouble z;
  z.push_back(4);
  z.push_back(2);
  z.push_back(1);
  simplex.set_eq_to_minimize(z);
  
  VecDouble c1;
  c1.push_back(2);
  c1.push_back(3);
  c1.push_back(4);
  
  simplex.add_constraint_lte(c1, 14);
  
  c1.resize(0);
  c1.push_back(3);
  c1.push_back(1);
  c1.push_back(5);
  
  simplex.add_constraint_gte(c1, 4);
  
  c1.resize(0);
  c1.push_back(1);
  c1.push_back(4);
  c1.push_back(3);
  
  simplex.add_constraint_gte(c1, 6);
  
  simplex.initial_simplex_tableau();
  
  simplex.print();
  
  BOOST_REQUIRE(simplex.is_feasible());
  
  int steps = 0;
  simplex.print();
  //BOOST_CHECK(simplex.verify_equations(verify_vals));
  while (!simplex.solved()) {
    printf("Step %d\n", steps); 	
    BOOST_REQUIRE(simplex.do_step());
    simplex.print();
    //BOOST_CHECK(simplex.verify_equations(verify_vals));
    ++steps;
  }
  
  //BOOST_uCHECK
  //BOOST_REQUIRE(steps == 2);
  BOOST_REQUIRE(simplex.getVar(0) == 0);
  BOOST_REQUIRE(simplex.getVar(1) == 0);
  BOOST_REQUIRE(simplex.getVar(2) == 2);
}

BOOST_AUTO_TEST_CASE(test_mixed_max_3)
{
  cout << "test_mixed_max_3" << endl;
  cout << "test_mixed_max_3" << endl;
  cout << "test_mixed_max_3" << endl;
  cout << "test_mixed_max_3" << endl;
  cout << "test_mixed_max_3" << endl;
  cout << "test_mixed_max_3" << endl;
  cout << "test_mixed_max_3" << endl;
  cout << "test_mixed_max_3" << endl;
  cout << "test_mixed_max_3" << endl;
  cout << "test_mixed_max_3" << endl;
  cout << "test_mixed_max_3" << endl;
  cout << "test_mixed_max_3" << endl;
  
  Simplex simplex(2, 2);
  VecDouble z;
  z.push_back(2);
  z.push_back(1);
  simplex.set_eq_to_maximize(z);
  
  VecDouble c1;
  c1.push_back(1);
  c1.push_back(1);
  
  simplex.add_constraint_lte(c1, 10);
  
  c1.resize(0);
  c1.push_back(-1);
  c1.push_back(1);
  
  simplex.add_constraint_gte(c1, 2);
  
  simplex.print();
  
  BOOST_REQUIRE(!simplex.is_feasible());
  
  simplex.print();
  
  simplex.initial_simplex_tableau();
  
  simplex.print();
  
  BOOST_REQUIRE(simplex.is_feasible());
  
  int steps = 0;
  simplex.print();
  //BOOST_CHECK(simplex.verify_equations(verify_vals));
  while (!simplex.solved()) {
     	
    simplex.do_step();
    simplex.print();
    //BOOST_REQUIRE(simplex.is_feasible());
    //BOOST_CHECK(simplex.verify_equations(verify_vals));
    ++steps;
  }
  
  //BOOST_uCHECK
  //BOOST_REQUIRE(steps == 2);
  BOOST_REQUIRE(simplex.getVar(0) == 4);
  BOOST_REQUIRE(simplex.getVar(1) == 6);
  
}


BOOST_AUTO_TEST_SUITE_END()


