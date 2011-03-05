// for BOOST testing
#define BOOST_TEST_MODULE UtilTest
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
#include "util.h"

using namespace std;

class UtilTestFixture 
{
   public:

   // constructor
   UtilTestFixture() 
   {
   }

   

};

BOOST_FIXTURE_TEST_SUITE(UtilTest, UtilTestFixture)

BOOST_AUTO_TEST_CASE(test_set_bit)
{
  BOOST_CHECK(SetBit(0, 1) == 1);
  BOOST_CHECK(SetBit(0, 2) == 2);
  BOOST_CHECK(SetBit(0, 3) == 4);
  BOOST_CHECK(SetBit(0, 4) == 8);
  
  BOOST_CHECK(SetBit(1, 1) == 1);
  BOOST_CHECK(SetBit(1, 2) == 3);
  BOOST_CHECK(SetBit(1, 3) == 5);
  BOOST_CHECK(SetBit(1, 4) == 9);
}
  
BOOST_AUTO_TEST_SUITE_END()


