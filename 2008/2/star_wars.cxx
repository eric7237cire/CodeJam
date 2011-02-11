#include <fstream>
#include <iostream>
#include <vector>
#include <algorithm>
#include <limits>

#include <stdio.h>
#include <time.h>
#include <assert.h>
#include <stdlib.h>

#include "simplex.h"

using namespace std;

double diffclock(clock_t clock1,clock_t clock2)
{
	double diffticks=clock1-clock2;
	double diffms=(diffticks*1000)/CLOCKS_PER_SEC;
	return diffms;
} 

void do_test_case(int test_case, ifstream& input);

#define SHOW_TIME 0
#define DEBUG_OUTPUT 0
//#undef assert
//#define assert(x) ((void)0)

#if SHOW_TIME
#define SHOW_TIME_BEGIN(A) clock_t begin_##A=clock();
#define SHOW_TIME_END(A) clock_t end_##A=clock(); cout << "Time elapsed: " #A << " " << double(diffclock(end_##A,begin_##A)) << " ms"<< endl;
#else
#define SHOW_TIME_BEGIN(A) 
#define SHOW_TIME_END(A) 
#endif

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
    do_test_case(test_case, input);  
  }
  
  SHOW_TIME_END(g)
}

void PrintVector(const VecDouble& vec)
{
  for(VecDouble::const_iterator it = vec.begin(); it != vec.end(); ++it)
  {
    printf("%.2f ", *it);
  }
  printf("\n");
}

void do_test_case(int test_case, ifstream& input)
{
  
  unsigned int N;
  input >> N;
    
  Simplex simplex(4, 8*N);
  
  //minimize w = 0x + 0y + 0z + mother_ship_power(MSP)
  VecDouble min_eq;
  min_eq.push_back(0);
  min_eq.push_back(0);
  min_eq.push_back(0);
  min_eq.push_back(1);
  
  simplex.set_eq_to_minimize(min_eq);
  
  VecDouble verify_values;
  verify_values.push_back(1.5);
  verify_values.push_back(2);
  verify_values.push_back(0);
  verify_values.push_back(3.5);
  
  
  for (unsigned int i = 0; i < N; ++i) {
    int x, y, z, ship_power;
    input >> x >> y >> z >> ship_power;
    VecDouble constraint;
    for(unsigned signs = 0; signs <= 7; ++signs) {
      int x_sign = (signs & 1) ? 1 : -1;
      int y_sign = (signs & 2) ? 1 : -1;
      int z_sign = (signs & 4) ? 1 : -1;
      constraint.resize(0);
      constraint.push_back(x_sign);
      constraint.push_back(y_sign);
      constraint.push_back(z_sign);
      constraint.push_back(-ship_power);
      int d = x_sign * x + y_sign * y + z_sign * z;
      verify_values.push_back(d - verify_values[0] * constraint[0] - verify_values[1] * constraint[1] - verify_values[2] * constraint[2] - verify_values[3] * constraint[3]);
      //int d = x_sign * x + y_sign * y;
      //add x_ms + y_ms + z_ms + ship_power*MSP >= x_s + y_s + z_s
      simplex.add_constraint_lte(constraint, d);
    }
  }
  
  
  simplex.print();
  int steps = 0;
  //assert(simplex.verify_equations(verify_values));
  simplex.initial_simplex_tableau();
  //throw 3;
  //PrintVector(verify_values);
  //throw 3;
  
  while(!simplex.solved()) {
 
    //printf("Step: %d \n", ++steps);
    simplex.do_step();
    simplex.print();
    //assert(simplex.verify_equations(verify_values));
    if (steps > 20) {
      cout << "TOO FAR" << endl;
      throw 5;
    }
  }
  
  simplex.reduce();
  simplex.print();
  
  printf("Case #%d: %f\n", test_case+1, simplex.getVar(3));
   
  //printf("Case #%d: (x,y,z) = (%f, %f, %f), power = %f\n", test_case+1, simplex.getVar(0), simplex.getVar(1), simplex.getVar(2), simplex.getVar(3));
    
  //printf("Case #%d: %f\n", test_case+1, simplex.getVar(3));
  
  //throw 3;
  return;
    
}
  