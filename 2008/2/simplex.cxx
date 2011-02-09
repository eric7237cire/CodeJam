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

#include "simplex.h"

using namespace std;

namespace {
double diffclock(clock_t clock1,clock_t clock2)
{
	double diffticks=clock1-clock2;
	double diffms=(diffticks*1000)/CLOCKS_PER_SEC;
	return diffms;
} 

double round(double r) {
    return (r > 0.0) ? floor(r + 0.5) : ceil(r - 0.5);
}

double round(double r, unsigned int prec) {
    int power = 1;
    for (unsigned int i = 1; i <= prec; ++i) {
      power *= 10;
    }
    
    return round(r*prec) / prec;
}


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

Simplex::Simplex(int num_non_basic_variables, int num_basic_variables) : 
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
  
  
  
  void Simplex::print()
  {
    for(unsigned int i = 1; i <= num_non_basic_variables; ++i) 
    {
      printf("x_%d ", i);
    }
    
    for(unsigned int i = 1; i <= num_basic_variables; ++i) 
    {
      printf("s_%d ", i);
    }
    
    printf("  b\n");
    
    for(MatrixDouble::iterator it = data.begin(); it != data.end(); ++it)
    {
      VecDouble& row = *it;
      for(VecDouble::iterator col_it = row.begin(); col_it != row.end(); ++col_it)
      {
        
        if (*col_it == 0) {
          printf("0 ");
        } else {
          printf("%.2f ", *col_it);
        }
      }
      printf("\n");
    }
    
    printf("\n");
    print_current_solution();
  }

  //z = z[0] * x_0 + z[1] * x_1 + ...
  void Simplex::set_z(const VecDouble& z)
  {
    VecDouble& z_row = *data.rbegin();
    
    assert(z.size() == num_non_basic_variables);
    assert(z_row.size() == num_non_basic_variables + num_basic_variables + 1);
    transform(z.begin(), z.end(), z_row.begin(), negate<double>());
    assert(z_row.size() == num_non_basic_variables + num_basic_variables + 1);
  }
  
  //a[1][0]*x[0] + a[1][1]*x[1] + s[1] == b[1]  
  void Simplex::add_constraint(unsigned int cons_num, const VecDouble& co_eff, double b)
  {
    assert(cons_num >= 0);
    assert(cons_num < num_basic_variables);
    assert(data.size() == num_basic_variables + 1);
    assert(data[cons_num].size() == num_basic_variables + num_non_basic_variables + 1);
    
    copy(co_eff.begin(), co_eff.end(), data[cons_num].begin());
    
    *data[cons_num].rbegin() = abs(b);
    
    //slack variable
    data[cons_num][num_non_basic_variables + cons_num] = b >= 0 ? 1 : -1;
  }
  
  //s[i]
  double Simplex::getBasicVar(int i)
  {
    return getVar(i + num_non_basic_variables);    
  }
  
  
  //x[i]
  double Simplex::getVar(int i)
  {
    VecDouble& z_row = *data.rbegin();
    
    if (z_row[i] == 0) {
      for(unsigned int r=0; r<num_basic_variables; ++r) {
        if (data[r][i] == 1) {
          cout << (*data[r].rbegin()) << endl;
          return round(*data[r].rbegin(), 2);            
        } else if (data[r][i] == -1) {
          return round(-*data[r].rbegin(), 2);            
        }
      }
    } else {
      return 0;
    }
    
    throw "Error";
    
  }
  
  
  
  void Simplex::print_current_solution()
  {
    printf("(");
    VecDouble& z_row = *data.rbegin();
    for (unsigned int c=0; c<z_row.size() - 1; ++c) {
      if (z_row[c] == 0) {
        for(unsigned int r=0; r<num_basic_variables; ++r) {
          if (data[r][c] == 1) {
            printf("%f, ", *data[r].rbegin());
            break;
          } else if (data[r][c] == -1) {
            printf("%f, ", -*data[r].rbegin());
            break;
          }
        }
      } else {
        printf("0, ");        
      }
    }
    
    printf(")\n");
      
  }
  
  bool Simplex::solved()
  {
    //find most negative value
    VecDouble& z_row = *data.rbegin();
    
    double min_value = 0;
    int pivot_col_idx = -1;
    
    assert(z_row.size() == num_basic_variables + num_non_basic_variables + 1);
    
    for(unsigned int i = 0; i < z_row.size() - 1; ++i) 
    {
      printf("Checking solved, col=%d, value=%f, min_value=%f\n", i, z_row[i], min_value);
      if (z_row[i] < min_value) {
        pivot_col_idx = i;
        min_value = z_row[i];
      }
    }
    
    for (unsigned int c=0; c<z_row.size() - 1; ++c) {
      if (z_row[c] == 0) {
        for(unsigned int r=0; r<num_basic_variables; ++r) {
          if (data[r][c] == -1) {
            //surplus variable
            return false;
          }
        }
      }
      
    }
    
    if (pivot_col_idx == -1) {
      return true;
    }
    return false;
  }
  
  //returns true if optimal
  bool Simplex::do_step()
  {
    //find most negative value
    VecDouble& z_row = *data.rbegin();
    
    double min_value = 0;
    int pivot_col_idx = -1;
  
    bool neg_surplus_variable = false;
    
    for (unsigned int c=0; c<z_row.size() - 1; ++c) {
      if (z_row[c] == 0) {
        for(unsigned int r=0; r<num_basic_variables; ++r) {
          if (data[r][c] == -1) {
            //surplus variable
            neg_surplus_variable = true;
            break;
          }
        }
      } else {
        printf("0, ");        
      }
      
      if (neg_surplus_variable) {
        break;
      }
    }
    
    if (neg_surplus_variable) {
      min_value = numeric_limits<double>::max();
    }
    
    for(unsigned int i = 0; i < num_non_basic_variables + num_basic_variables; ++i) 
    {
      if (z_row[i] < min_value && z_row[i] != 0) { //not sure about the !=0
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
    
    unsigned int pivot_row_idx = 10000;
    bool found_pivot_row = false;
    
    for(unsigned int r = 0; r < num_basic_variables; ++r)
    {
      //b[i] (end of row) / value in pivot column 
      double ratio = *data[r].rbegin() / data[r][pivot_col_idx]; 
      if( ratio < min_ratio && ratio > 0 ) {
        pivot_row_idx = r;
        min_ratio = ratio;
        found_pivot_row = true;
      }
    }
    
    printf("Pivot row min ration=%f, position=%d\n", min_ratio, pivot_row_idx);
    
    assert(found_pivot_row);
    assert(pivot_row_idx <= num_basic_variables);
    //divide pivot row by pivot column
    
    double pivot = data[pivot_row_idx][pivot_col_idx];
    
    VecDouble& pivot_row = data[pivot_row_idx];
    
    for(VecDouble::iterator it = pivot_row.begin(); it != pivot_row.end(); ++it)
    {
      (*it) = (*it) / pivot;
    }
    
    //print();
    
    for(unsigned int r = 0; r < data.size(); ++r)
    {
      if (r == pivot_row_idx) {
        continue;
      }
      
      VecDouble& row = data[r];
      VecDouble& pivot_row = data[pivot_row_idx];
      
      double multiple = -row[pivot_col_idx];
      //printf("Multiple is %f\n", multiple);
      
      transform(row.begin(), row.end(), pivot_row.begin(), row.begin(),
        boost::bind(std::plus<double>(), _1,
          boost::bind(std::multiplies<double>(), multiple, _2)));
    }
    
    return false;
  }


  //because of the transposition, invert the 2
  MinSimplex::MinSimplex(int num_non_basic_variables, int num_basic_variables) : Simplex(num_basic_variables, num_non_basic_variables)
  {
    
  }
  
  void MinSimplex::set_z(const VecDouble& z)
  {
    //cout << "MinSimplex\n";
    assert(data.size() == z.size() + 1);
    
    unsigned int col = data[0].size() - 1;
    assert(data[0].size() >= 1);
    assert(col > 0);
    for(unsigned int i = 0; i < z.size(); ++i)
    {
      assert(data[i].size() == col + 1);
      data[i][col] = z[i];
      data[i][col - z.size() + i] = 1;
    }
    
    data[z.size()][col] = 0;
  }
  
  void MinSimplex::add_constraint(unsigned int cons_num, const VecDouble& co_eff, double b)
  {
    //cout << "MinSimplex\n";
    unsigned int col = cons_num;
    assert(col >= 0);
    assert(col < data[0].size() - 1);
    assert(data.size() == co_eff.size() + 1); 
    for(unsigned int i = 0; i < co_eff.size(); ++i)    
    {
      //assert(data[i].size() == col + 1);
      data[i][col] = co_eff[i];
    }
    data[ co_eff.size() ][col] = -b; //negative because interpret as max problem z - blah = 0
  }


int simplex_test()
{
  
  SHOW_TIME_BEGIN(g) 
 
  Simplex simplex(2, 3);
  VecDouble z;
  
  
  MinSimplex minplex(2, 3);
  
  z.resize(0);
  z.push_back(.12);
  z.push_back(.15);
  minplex.set_z(z);

  c1.resize(0);
  c1.push_back(60);
  c1.push_back(60);
  minplex.add_constraint(0, c1, 300);
  
  c1.resize(0);
  c1.push_back(12);
  c1.push_back(6);
  minplex.add_constraint(1, c1, 36);
  
  c1.resize(0);
  c1.push_back(10);
  c1.push_back(30);
  minplex.add_constraint(2, c1, 90);
    
  
  
  minplex.print();
  steps = 0;
  while(!minplex.solved()) {
    printf("Step: %d \n", ++steps);
    minplex.do_step();
    minplex.print();
  }
  
  cout << "Minimize W = .12x + .15y subject to 60x+60y >= 300, 12x+6y >= 36, 10x + 30y >= 90" << endl;
  cout << "Optimal Solution: w = 0.66; x = 3, y = 2" << endl;
  minplex.print_current_solution();
  
  return 1;
  
  
  
  SHOW_TIME_END(g)
}

  
