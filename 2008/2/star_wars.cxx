#include <fstream>
#include <iostream>
#include <vector>
#include <algorithm>
#include <limits>

#include <stdio.h>
#include <time.h>
#include <assert.h>
#include <stdlib.h>
#include <boost/numeric/conversion/bounds.hpp>
#include <boost/limits.hpp>
#include <cmath>
#include "util.h" 

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
    try {
      do_test_case(test_case, input);
    } catch(...) {
      error("Error exception caught\n"); 
    }
  }
  
  SHOW_TIME_END(g)
}

void find_mother_ship_min(const int inputData[1000][4], const vector<int>& selectedShips, double mother_ship[4])
{
  double averages[3] = {0, 0, 0};
  
  for (unsigned int sn = 0; sn < selectedShips.size(); ++sn) {
    unsigned int i = selectedShips[sn]; 
    int x, y, z, ship_power;
    x = inputData[i][0];
    y = inputData[i][1];
    z = inputData[i][2];
    ship_power = inputData[i][3];
    
    averages[0] += x;
    averages[1] += y;
    averages[2] += z;
  }
  
  for(int i=0; i<3; ++i) {
    averages[i] /= selectedShips.size();
  }
  
  double max_cost = boost::numeric::bounds<double>::lowest();
  unsigned int furthest_ship = boost::numeric::bounds<unsigned int>::highest();
  
  for (unsigned int sn = 0; sn < selectedShips.size(); ++sn) {
    double cost = 0;
    unsigned int i = selectedShips[sn]; 
    for(unsigned int comp = 0; comp < 3; ++comp) {
      cost += abs(averages[comp] - inputData[i][comp]) / inputData[i][3];
    }
    if (cost > max_cost) {
      max_cost = cost;
      furthest_ship = i;
    }
       
  }
  
  info("Furthest ship index %d\n", furthest_ship);
  info("Starting mother ship pos %f %f %f\n", averages[0], averages[1], averages[2]);
  info("Furthest ship %d %d %d, power=%d\n", inputData[furthest_ship][0], inputData[furthest_ship][1], inputData[furthest_ship][2], inputData[furthest_ship][3]);
  
  //minimize w = 0x + 0y + 0z + mother_ship_power(MSP)
  VecDouble min_eq;
  min_eq.push_back(-1);
  min_eq.push_back(-1);
  min_eq.push_back(-1);
  min_eq.push_back(20000);
  
  
  vector<VecDouble> constraints;
  vector<double> d_vals;
  
  for (unsigned int sn = 0; sn < selectedShips.size(); ++sn) {
    unsigned int i = selectedShips[sn];
    int x, y, z, ship_power;
    x = inputData[i][0];
    y = inputData[i][1];
    z = inputData[i][2];
    ship_power = inputData[i][3];
    
    VecDouble constraint;
    for(unsigned signs = 0; signs <= 7; ++signs) {
      int x_sign = (signs & 1) ? 1 : -1;
      int y_sign = (signs & 2) ? 1 : -1;
      int z_sign = (signs & 4) ? 1 : -1;
      
      if (x > averages[0] && x > inputData[furthest_ship][0] && x_sign == 1) {
        debug("Skipping constraint.  x val = %d, x_sign = %d, aveg x = %f, furthest_x = %d\n", x, x_sign, averages[0], inputData[furthest_ship][0]);
        continue;
      }
      
      if (y > averages[1] && y > inputData[furthest_ship][1] && y_sign == 1) {
        debug("Skipping constraint.  x val = %d, x_sign = %d, aveg x = %f, furthest_x = %d\n", x, x_sign, averages[0], inputData[furthest_ship][0]);
        continue;
      }
      
      if (z > averages[2] && z > inputData[furthest_ship][2] && z_sign == 1) {
        debug("Skipping constraint.  x val = %d, x_sign = %d, aveg x = %f, furthest_x = %d\n", x, x_sign, averages[0], inputData[furthest_ship][0]);
        continue;
      }
      
      if (x < averages[0] && x < inputData[furthest_ship][0] && x_sign == -1) {
        debug("Skipping constraint.  x val = %d, x_sign = %d, aveg x = %f, furthest_x = %d\n", x, x_sign, averages[0], inputData[furthest_ship][0]);
        continue;
      }
      
      if (y < averages[1] && y < inputData[furthest_ship][1] && y_sign == -1) {
        debug("Skipping constraint.  x val = %d, x_sign = %d, aveg x = %f, furthest_x = %d\n", x, x_sign, averages[0], inputData[furthest_ship][0]);
        continue;
      }
      
      if (z < averages[2] && z < inputData[furthest_ship][2] && z_sign == -1) {
        debug("Skipping constraint.  x val = %d, x_sign = %d, aveg x = %f, furthest_x = %d\n", x, x_sign, averages[0], inputData[furthest_ship][0]);
        continue;
      }
      
      constraint.resize(0);
      constraint.push_back(x_sign);
      constraint.push_back(y_sign);
      constraint.push_back(z_sign);
      constraint.push_back(-ship_power);
      int d = x_sign * x + y_sign * y + z_sign * z;
      //add x_ms + y_ms + z_ms + ship_power*MSP >= x_s + y_s + z_s
      
      constraints.push_back(constraint);
      d_vals.push_back(d);
    }
  }
  
  Simplex simplex(4, constraints.size());
  simplex.set_eq_to_minimize(min_eq);
  
  for (unsigned int i=0; i < constraints.size(); ++i) {
    simplex.add_constraint_lte(constraints[i], d_vals[i]);
  }
  
  
  
  //simplex.print();
  int steps = 0;
  //assert(simplex.verify_equations(verify_values));
  simplex.initial_simplex_tableau();
  
  while(!simplex.solved()) {
    ++steps;
    if (steps % 40 == 0) {
      info("Step: %d \n", steps);
    }
    simplex.do_step();
    //simplex.print();
    //assert(simplex.verify_equations(verify_values));
    if (steps > 2000) {
      cout << "TOO FAR" << endl;
      throw 5;
    }
  }
  
  //simplex.reduce();
  //simplex.print();
  #if INFO
    simplex.print_current_solution();
  #endif
  //assert(simplex.verify_equations(verify_values));
  for (unsigned int i=0; i<4; ++i) {
    mother_ship[i] = simplex.getVar(i);
  }
  info("Steps: %d\n", steps);
}

double calculate_power(const int inputData[1000][4], unsigned int i, const double mother_ship[4])
{
  int x, y, z, ship_power;
  x = inputData[i][0];
  y = inputData[i][1];
  z = inputData[i][2];
  ship_power = inputData[i][3];
  
  double power_req = abs(x-mother_ship[0]) / ship_power + 
  abs(y-mother_ship[1]) / ship_power + 
  abs(z-mother_ship[2]) / ship_power ;
  
  return power_req;
}

struct SortFuncObj
{
  const int (*inputData)[4];
  const double* mother_ship;
  
  int operator()(const int& lhs, const int& rhs)
  {
    
    double power_req = calculate_power(inputData, lhs, mother_ship);
    
    double power_req2 = calculate_power(inputData, rhs, mother_ship);
    
    return power_req > power_req2;
  }
};

unsigned int find_unreached_ships(const int inputData[1000][4], vector<int>& selectedShips, const unsigned int start, const unsigned int stop, const double mother_ship[4])
{
  const unsigned int LIMIT = 10;
  
  SortFuncObj s;
  s.inputData = inputData;
  s.mother_ship = mother_ship;
  unsigned int stopped_at = stop+1;
  const unsigned int sel_ships = selectedShips.size();
  
  for(unsigned int i = start; i <= stop; ++i)
  {
    int x, y, z, ship_power;
    x = inputData[i][0];
    y = inputData[i][1];
    z = inputData[i][2];
    ship_power = inputData[i][3];
    
    double power_req = abs(x-mother_ship[0]) / ship_power + 
    abs(y-mother_ship[1]) / ship_power + 
    abs(z-mother_ship[2]) / ship_power ;
    
    if (power_req > mother_ship[3]) {
      selectedShips.push_back(i);
      info("Adding ship %d power_req %f  ms power %f\n", i, power_req, mother_ship[3]);
      if (selectedShips.size() > LIMIT + sel_ships) {
        stopped_at = i+1;
        break;
      }
    } else {
      //info("Skipping ship %d power_req %f  ms power %f\n", i, power_req, mother_ship[3]);
    }
  }
  
  sort(selectedShips.begin(), selectedShips.end(), s);
  
  assert(calculate_power(inputData, selectedShips[0], mother_ship) >= calculate_power(inputData, selectedShips[selectedShips.size() - 1], mother_ship));
  
  
  if (selectedShips.size() > LIMIT) {
    //selectedShips.resize(LIMIT);
  }
  
  return stopped_at;
}

void do_test_case(int test_case, ifstream& input)
{
  
  unsigned int N;
  input >> N;
  
  int inputData[1000][4];
  vector<int> selectedShips;
  
  for (unsigned int i = 0; i < N; ++i) {
    int x, y, z, ship_power;
    input >> x >> y >> z >> ship_power;
    inputData[i][0] = x;
    inputData[i][1] = y;
    inputData[i][2] = z;
    inputData[i][3] = ship_power;
    if (i < 10) {
      selectedShips.push_back(i);
    }
  }
  
  double mother_ship[4];
  unsigned int iter = 0;
  unsigned int stop = 0;
  while (stop < N) {
    info("test\n");
    find_mother_ship_min(inputData, selectedShips, mother_ship);
    
    stop = find_unreached_ships(inputData, selectedShips, 0, N-1, mother_ship);
    info("Stopped at %d with mother ship %f %f %f, %f\n", stop, mother_ship[0], mother_ship[1], mother_ship[2], mother_ship[3]);
    ++iter;
    info("After iteration %d stop is %d of %d\n", iter, stop, N);
    if (stop >= N) {
     // break;
    }
    if (iter > 50) {
      error("Too many iters\n");
      throw 3;
    }
  }
  
  find_mother_ship_min(inputData, selectedShips, mother_ship);
  
  printf("Case #%d: %f\n", test_case+1, mother_ship[3]);
   
  //printf("Case #%d: (x,y,z) = (%f, %f, %f), power = %f\n", test_case+1, simplex.getVar(0), simplex.getVar(1), simplex.getVar(2), simplex.getVar(3));
    
  //printf("Case #%d: %f\n", test_case+1, simplex.getVar(3));
  
  //throw 3;
  return;
    
}
  
