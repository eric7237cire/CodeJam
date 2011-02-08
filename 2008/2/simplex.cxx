#include <fstream>
#include <iostream>
#include <vector>
#include <algorithm>
#include <limits>

#include <stdio.h>
#include <time.h>
#include <assert.h>
#include <stdlib.h>

#include "boost/bind.hpp"

using namespace std;

double diffclock(clock_t clock1,clock_t clock2)
{
	double diffticks=clock1-clock2;
	double diffms=(diffticks*1000)/CLOCKS_PER_SEC;
	return diffms;
} 

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


//Maximize z = 4x + 6y subject to -x + y <= 11, x + y <= 27, 2x + 5y <= 90

//Maximize z = c[0]*x[0] + c[1]*x[1] + ...
//a[0][0]*x[0] + a[0][1]*x[1] <= b[0]
//a[1][0]*x[0] + a[1][1]*x[1] <= b[1]

// ==>

//a[0][0]*x[0] + a[0][1]*x[1] + s[0] == b[0]
//a[1][0]*x[0] + a[1][1]*x[1] + s[1] == b[1]

//x[i] >= 0
//b[i] >= 0
//s[i] >= 0

typedef vector<int> VecInt;
typedef vector<VecInt> MatrixInt;

typedef vector<double> VecDouble;
typedef vector<VecDouble> MatrixDouble;


class Simplex
{
  int num_non_basic_variables;
  int num_basic_variables; // slack variables
  
  int rows;
  int cols;
  MatrixDouble data;

public:
  Simplex(int num_non_basic_variables, int num_basic_variables) : 
  num_non_basic_variables(num_non_basic_variables), num_basic_variables(num_basic_variables)
  {
    rows = num_basic_variables + 1;
    cols = num_non_basic_variables + num_basic_variables + 1;
    data.resize(rows);
    
    for(MatrixDouble::iterator it = data.begin(); it != data.end(); ++it)
    {
      (*it).resize(cols, 0);
    }
      
  }
  
  
  
  void print()
  {
    for(int i = 1; i <= num_non_basic_variables; ++i) 
    {
      printf("x_%d ", i);
    }
    
    for(int i = 1; i <= num_basic_variables; ++i) 
    {
      printf("s_%d ", i);
    }
    
    printf("  b\n");
    
    for(MatrixDouble::iterator it = data.begin(); it != data.end(); ++it)
    {
      VecDouble& row = *it;
      for(VecDouble::iterator col_it = row.begin(); col_it != row.end(); ++col_it)
      {
        printf("%3.2f ", *col_it);  
      }
      printf("\n");
    }
    
    printf("\n");
  }

  //z = z[0] * x_0 + z[1] * x_1 + ...
  void set_z(const VecInt& z)
  {
    VecDouble& z_row = *data.rbegin();
    
    assert(z.size() == num_non_basic_variables);
    assert(z_row.size() == num_non_basic_variables + num_basic_variables + 1);
    transform(z.begin(), z.end(), z_row.begin(), negate<int>());
    assert(z_row.size() == num_non_basic_variables + num_basic_variables + 1);
  }
  
  //a[1][0]*x[0] + a[1][1]*x[1] + s[1] == b[1]  
  void add_constraint(int cons_num, const VecInt& co_eff, int b)
  {
    assert(cons_num >= 0);
    assert(cons_num < num_basic_variables);
    assert(data.size() == num_basic_variables + 1);
    assert(data[cons_num].size() == num_basic_variables + num_non_basic_variables + 1);
    
    copy(co_eff.begin(), co_eff.end(), data[cons_num].begin());
    
    *data[cons_num].rbegin() = b;
    
    //slack variable
    data[cons_num][num_non_basic_variables + cons_num] = 1;
  }
  
  //returns true if optimal
  bool do_step()
  {
    //find most negative value
    VecDouble& z_row = *data.rbegin();
    
    double min_value = 0;
    int pivot_col_idx = -1;
    
    for(int i = 0; i < num_basic_variables; ++i) 
    {
      if (z_row[i] < min_value) {
        pivot_col_idx = i;
        min_value = z_row[i];
      }
    }
    
    if (pivot_col_idx == -1) {
      return true;
    }
    
    assert(pivot_col_idx >= 0);
    printf("Pivot col=%f, position=%d\n", min_value, pivot_col_idx);
    
    double min_ratio = numeric_limits<double>::max();
    
    int pivot_row_idx = -1;
    
    for(int r = 0; r < num_basic_variables; ++r)
    {
      //b[i] (end of row) / value in pivot column 
      double ratio = *data[r].rbegin() / data[r][pivot_col_idx]; 
      if( ratio < min_ratio ) {
        pivot_row_idx = r;
        min_ratio = ratio;
      }
    }
    
    printf("Pivot row min ration=%f, position=%d\n", min_ratio, pivot_row_idx);
    
    assert(pivot_row_idx >= 0);
    assert(pivot_row_idx <= num_basic_variables);
    //divide pivot row by pivot column
    
    double pivot = data[pivot_row_idx][pivot_col_idx];
    
    VecDouble& pivot_row = data[pivot_row_idx];
    
    for(VecDouble::iterator it = pivot_row.begin(); it != pivot_row.end(); ++it)
    {
      (*it) = (*it) / pivot;
    }
    
    print();
    
    for(int r = 0; r < data.size(); ++r)
    {
      if (r == pivot_row_idx) {
        continue;
      }
      
      VecDouble& row = data[r];
      VecDouble& pivot_row = data[pivot_row_idx];
      
      double multiple = -row[pivot_col_idx];
      printf("Multiple is %f\n", multiple);
      
      transform(row.begin(), row.end(), pivot_row.begin(), row.begin(),
        boost::bind(std::plus<double>(), _1,
          boost::bind(std::multiplies<double>(), multiple, _2)));
    }
  }
};


int main(int argc, char** args)
{
  
  SHOW_TIME_BEGIN(g) 
 
  Simplex simplex(2, 3);
  VecInt z;
  z.push_back(4);
  z.push_back(6);
  simplex.set_z(z);
  
  VecInt c1;
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
  
  simplex.print(); 	
  
  simplex.do_step();
  simplex.print();
  
  return;
  
  simplex.do_step();
  simplex.print();
  
  simplex.do_step();
  simplex.print();
  
  simplex.do_step();
  simplex.print();
  
  SHOW_TIME_END(g)
}

  
