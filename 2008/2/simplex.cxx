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
#include <stdarg.h>

#include "boost/bind.hpp"
#include <boost/numeric/conversion/bounds.hpp>
#include <boost/limits.hpp>

#include "simplex.h"

using namespace std;

void PrintVector(const VecDouble& vec)
{
  for(VecDouble::const_iterator it = vec.begin(); it != vec.end(); ++it)
  {
    printf("%.2f ", *it);
  }
  printf("\n");
}


double round(double r) {
    return (r > 0.0) ? floor(r + 0.5) : ceil(r - 0.5);
}

double round(double r, unsigned int prec) {
      int power = 1;
      for (unsigned int i = 1; i <= prec; ++i) {
        power *= 10;
      }
      /*printf("%f\n", r);
      cout << prec << endl;
      cout << power << endl;
      cout << round(r*prec) << endl;
      cout << prec << endl;
      assert(round(r*power) == static_cast<long long>(round(r*power)));
      printf("%f\n", round(r*power));
      printf("%f\n", round(r*power) / power);*/
      return round(r*power) / power;
  }

namespace {
  
  double M = 1000000;
  
  double diffclock(clock_t clock1,clock_t clock2)
  {
    double diffticks=clock1-clock2;
    double diffms=(diffticks*1000)/CLOCKS_PER_SEC;
    return diffms;
  } 
  
  
  
  
  
  
  #define ERROR 0
  #define INFO 0
  #define DEBUG 0
  #define TRACE 0
  
  void error(const char* pMsg, ...)
  {
    #if ERROR
    va_list arg;
    va_start(arg, pMsg);
    vprintf(pMsg, arg);
    va_end(arg);
     #endif 
  }
  
  void info(const char* pMsg, ...)
  {
    #if INFO
    va_list arg;
    va_start(arg, pMsg);
    vprintf(pMsg, arg);
    va_end(arg);
     #endif 
  }
  
  void debug(const char* pMsg, ...)
  {
    #if DEBUG
    va_list arg;
    va_start(arg, pMsg);
    vprintf(pMsg, arg);
    va_end(arg);
     #endif 
  }
  
  void trace(const char* pMsg, ...)
  {
    #if TRACE
    va_list arg;
    va_start(arg, pMsg);
    vprintf(pMsg, arg);
    va_end(arg);
     #endif 
  }


}

//#define printf trace

//#undef assert
//#define assert(x) ((void)0)



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
  num_non_basic_variables(num_non_basic_variables), 
  num_basic_variables(num_basic_variables),
  num_artificial_variables(0),
  cur_constraint(0),
  last_max(boost::numeric::bounds<double>::lowest()),
  initial_simplex_tableau_called(false),
  b_solved(false)
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
    
    for(unsigned int i = 1; i <= num_artificial_variables; ++i) 
    {
      printf("a_%d ", i);
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
  
  void Simplex::set_eq_to_minimize(const VecDouble& z)
  {
    VecDouble z_neg(z.size());
    
    transform(z.begin(), z.end(), z_neg.begin(), negate<double>());
    set_z(z_neg);
    
    finding_max = false;
  }
  
  void Simplex::set_eq_to_maximize(const VecDouble& z)
  {
    set_z(z);
    finding_max = true;
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
  
  void Simplex::add_constraint_lte(const VecDouble& z, double d)
  {
    if (d < 0) {
      VecDouble z_neg(z.size());
      
      transform(z.begin(), z.end(), z_neg.begin(), negate<double>());
      add_constraint_gte(z_neg, -d);
      return;
    }
    
    
    
    add_constraint(cur_constraint++, z, d);
  }
  
  void Simplex::add_constraint_gte(const VecDouble& z, double d)
  {
    if (d < 0) {
      VecDouble z_neg(z.size());
      
      transform(z.begin(), z.end(), z_neg.begin(), negate<double>());
      add_constraint_lte(z_neg, -d);
      return;
    }
    
    //if (finding_max == false) {
      //set d to negative to switch s value
      //all problems are max problems
      d = -d;      
    //}
    
    add_constraint(cur_constraint++, z, d);
  }
  
  bool Simplex::verify_equations(const VecDouble& verify_vals)
  {
    for(unsigned int r = 0; r < data.size() - 1; ++r)
    {
      
      VecDouble& row = data[r];
      double tally = 0;
      assert(row.size() >= verify_vals.size() + 1);
      for(unsigned int c = 0; c < verify_vals.size(); ++c)
      {
        tally += row[c] * verify_vals[c];
      }
      
      if (tally != *row.rbegin()) {
        trace("False match, row %d, values: ", r);
        for(unsigned int c = 0; c < row.size() - 1; ++c)
        {
          trace("%f ", row[c]);
        }
        trace("\n");
        return false;
      }
      
      trace("row %d, tally: %f\n ", r, tally);
    }
    
    return true;
  }
  
  //a[1][0]*x[0] + a[1][1]*x[1] + s[1] == b[1]  
  void Simplex::add_constraint(unsigned int cons_num, const VecDouble& co_eff, double b)
  {
    assert(cons_num >= 0);
    assert(cons_num < num_basic_variables);
    assert(data.size() == num_basic_variables + 1);
    assert(data[cons_num].size() == num_basic_variables + num_non_basic_variables + num_artificial_variables + 1);
    
    copy(co_eff.begin(), co_eff.end(), data[cons_num].begin());
    
    *data[cons_num].rbegin() = abs(b);
    
    //slack variable
    if (b < 0) {
      const unsigned int slack_column = num_non_basic_variables + cons_num; 
      data[cons_num][slack_column] = -1;
      ++num_artificial_variables;
      for(unsigned int r=0; r<data.size(); ++r)
      {
        unsigned int new_size = num_artificial_variables+num_basic_variables+num_non_basic_variables + 1;
        assert(data[r].size() == new_size - 1);
        data[r].resize(new_size);
        *data[r].rbegin() = *(data[r].rbegin() + 1); //copy d
        *(data[r].rbegin() + 1) = 0;
      }
      
      const unsigned int artificial_column = num_non_basic_variables + num_basic_variables + num_artificial_variables - 1;
      trace("sc = %d, ac = %d\n", slack_column, artificial_column);
      basic_to_artificial.insert(make_pair(slack_column, artificial_column));
      
      // add artificial variable
      data[cons_num][artificial_column] = 1; 
      (*data.rbegin())[artificial_column] = M;
    } else {
      data[cons_num][num_non_basic_variables + cons_num] = 1;
    }
  }
  
  //s[i]
  double Simplex::getBasicVar(unsigned int i)
  {
    return getVar(i + num_non_basic_variables);    
  }
  
  
  //x[i]
  double Simplex::getVar(unsigned int i)
  {
    VecDouble& z_row = *data.rbegin();
    
    if (z_row[i] == 0) {
      for(unsigned int r=0; r<data.size() - 1; ++r) {
        if (data[r][i] == 1) {
          return round(*data[r].rbegin(), 6);            
        } else if (data[r][i] == -1) {
          return round(-*data[r].rbegin(), 6);            
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
  
  bool Simplex::initial_simplex_tableau()
  {
    initial_simplex_tableau_called = true;
    if (is_feasible())
    {
      return true;
    }
    
    unsigned int cur_artificial_variable = num_basic_variables + num_non_basic_variables;
    
    VecDouble& z_row = *data.rbegin();
    
    for (unsigned int c=num_non_basic_variables; c<num_non_basic_variables+num_basic_variables; ++c) {
      if (z_row[c] == 0) {
        for(unsigned int r=0; r<num_basic_variables; ++r) {
          if (data[r][c] < 0) {
            //surplus variable
            eliminate_neg_s_variable(c, r, cur_artificial_variable++);
            
          }
        }
      }
    }
    
    assert(cur_artificial_variable == z_row.size() - 1);
    
    return true;
      
  }
  
  bool Simplex::eliminate_neg_s_variable(unsigned int s_col, unsigned int s_row, unsigned int a_col)
  {
    #if TRACE
    printf("eliminate_neg_s_variable s_col=%d, s_row=%d, a_col=%d\n", s_col, s_row, a_col);
    print();
    #endif
    
    assert(data[s_row][s_col] < 0);
    
    VecDouble& z_row = *data.rbegin();
    
    assert(z_row[a_col] == M);
    assert(z_row.size() == data[s_row].size());
    
    transform(z_row.begin(), z_row.end(),
      data[s_row].begin(), 
      data.rbegin()->begin(),
      boost::bind(plus<double>(), _1,
        boost::bind(multiplies<double>(), _2, -M)));
          
    #if TRACE
    print();
    #endif
    
    assert(z_row[a_col] == 0);
    //assert(data[s_row][s_col] > 0);
    return true;
  }
  
  bool Simplex::reduce()
  {
    //blow away any artifical variables
    for(unsigned int r = 0; r<data.size(); ++r) {
      VecDouble& row = data[r];
      unsigned int new_row_size = num_basic_variables+num_non_basic_variables+1;
      row[new_row_size - 1] = *row.rbegin();
      row.resize(new_row_size);
    }
    print();
    
    unsigned int i = 0;
    unsigned int j = 0;
    const unsigned int m = data.size() - 2;
    const unsigned int n = num_basic_variables + num_non_basic_variables;
    
    while (i <= m && j <= n) 
    {
      //Find pivot in column j, starting in row i:
      unsigned int maxi = i;
      for( unsigned int k = i + 1; k <= m; ++k) 
      {
        if ( abs(data[k][j]) > abs(data[maxi][j]) ) {
          maxi = k;
        }          
      }
      
      if (data[maxi][j] != 0)
      {
        VecDouble i_row = data[i];
        VecDouble maxi_row = data[maxi];
        data[i] = maxi_row;
        data[maxi] = i_row;
        
        //Now A[i,j] will contain the old value of A[maxi,j].
        assert(equal(data[i].begin(), data[i].end(), maxi_row.begin()));
    
        //divide each entry in row i by A[i,j]
        transform(data[i].begin(), data[i].end(), data[i].begin(), boost::bind(divides<double>(), _1, data[i][j]));
        
        cout << "After row switch\n";
        print();
        
        //Now A[i,j] will have the value 1.
        assert(data[i][j] == 1);
        for(unsigned int u = i+1; u <= m; ++u) 
        {
          if (u == i) {
            continue;
          }
          if (data[u][j] == 0) {
            continue;
          }
          trace("Row %d, multiplier %f\n", u, data[u][j]);
          //subtract A[u,j] * row i from row u
          transform(data[u].begin(), data[u].end(), //input1 b-e 
            data[i].begin(), //input2
            data[u].begin(), //output
            boost::bind(minus<double>(), 
              boost::bind(multiplies<double>(), data[u][j], _2),
              _1)); 
        
          
          //Now A[u,j] will be 0, since A[u,j] - A[i,j] * A[u,j] = A[u,j] - 1 * A[u,j] = 0.
          assert(data[u][j] == 0);
        }
        assert(data[i][j] == 1);
        
        print();
        
        for(unsigned int u = 0; u < i; ++u) 
        {
          trace("u=%d, j=%d, i=%d, m=%d\n", u, j, i, m);
          if (data[u][j] == 0) {
            continue;
          }
          if (u==i) {
            continue;
          }
          trace("Row %d, multiplier rr %f\n", u, data[u][j]);
          //subtract A[u] = row i * A[u][j] + row u
          transform(data[u].begin(), data[u].end(), //input1 b-e 
            data[i].begin(), //input2
            data[u].begin(), //output
            boost::bind(plus<double>(), 
              boost::bind(multiplies<double>(), -data[u][j], _2),
              _1)); 
        
          
          //Now A[u,j] will be 0, since A[u,j] - A[i,j] * A[u,j] = A[u,j] - 1 * A[u,j] = 0.
          assert(data[u][j] == 0);
        }
        
        ++i;
      }
    ++j;
    }
    
    
  
    return true;
  }
  
  //initially
  bool Simplex::is_feasible()
  {
    //VecDouble& last_row = *data.rbegin();
    
    //all basic variables must be > 0 and have only 1 non_zero element
    for(unsigned int b_var = num_non_basic_variables; b_var < num_non_basic_variables + num_basic_variables; ++b_var)
    {
      bool found_non_zero = false;
      unsigned int non_zero_row = 0;
      
      
      unsigned int basic_col = b_var;
      
      map<unsigned int, unsigned int>::const_iterator b_to_a_it = basic_to_artificial.find(basic_col);
      if (b_to_a_it != basic_to_artificial.end()) {
        basic_col = b_to_a_it->second;
        assert(basic_col >= num_basic_variables+num_non_basic_variables);
        assert(basic_col < num_basic_variables+num_non_basic_variables+num_artificial_variables);
        assert(num_artificial_variables > 0);
      }
      
      for(unsigned int r=0; r<data.size(); ++r)
      {
        if (data[r][basic_col] != 0) {
          if (found_non_zero) {
            return false;
          }
          found_non_zero = true;
          non_zero_row = r;
          //break;
        }
      }
      
      assert(data[non_zero_row][basic_col] != 0);
      if (data[non_zero_row][basic_col] < 0)
      {
        return false;
      }
      
    }
    
    return true;
  }
  
  bool Simplex::solved()
  {
    return b_solved;    
  }
  
  //returns true if optimal
  bool Simplex::do_step()
  {
    assert(cur_constraint == num_basic_variables);
    assert(initial_simplex_tableau_called || num_artificial_variables == 0);
    
    #if DEBUG
    printf("STEP\n\n");
    print();
    #endif
    
    //find most negative value
    VecDouble& z_row = *data.rbegin();
    
    double min_value = 0;
    int pivot_col_idx = -1;
    const double zero_threshold = 0.00000001;
  
    assert(z_row.size() == num_non_basic_variables + num_basic_variables + num_artificial_variables + 1);
    info("rows %d, cols %d\n", data.size(), data[0].size());
    
    for(unsigned int i = 0; i < z_row.size() - 1; ++i) 
    {
      if (z_row[i] < min_value && z_row[i] != 0) { 
        pivot_col_idx = i;
        min_value = z_row[i];
      }
    }
    
    if (pivot_col_idx == -1) {
      trace("ret pivot col index false\n");
      return false;
    }
    
    last_chosen_pivot_col = pivot_col_idx;
    assert(pivot_col_idx >= 0);
    debug("Pivot col=%f, position=%d\n", min_value, pivot_col_idx);
    
    double min_ratio = boost::numeric::bounds<double>::highest();
    
    unsigned int pivot_row_idx = 10000;
    bool found_pivot_row = false;
    
    for(unsigned int r = 0; r < data.size() - 1; ++r)
    {
      if (data[r][pivot_col_idx] > zero_threshold) {
        //b[i] (end of row) / value in pivot column 
        double ratio = *data[r].rbegin() / data[r][pivot_col_idx];
        assert(ratio >= 0);
        if( ratio < min_ratio ) {
          pivot_row_idx = r;
          min_ratio = ratio;
          found_pivot_row = true;
        }
      }
    }
    
    debug("Pivot row min ratio=%f, position=%d\n", min_ratio, pivot_row_idx);
    
    #if ERROR
    if (!found_pivot_row) {
      error("Could not find pivot row\n");
      print();
    }
    #endif
    
    assert(found_pivot_row);
    assert(pivot_row_idx <= num_basic_variables);
    //divide pivot row by pivot column
    
    double pivot = data[pivot_row_idx][pivot_col_idx];
    trace("Pivot is %f\n", pivot);
    
    VecDouble& pivot_row = data[pivot_row_idx];
    
    #if TRACE
    printf("Pivot row %d was: ", pivot_row_idx);
    PrintVector(pivot_row);
    #endif
    
    for(VecDouble::iterator it = pivot_row.begin(); it != pivot_row.end(); ++it)
    {
      (*it) = (*it) / pivot;
    }
    
    #if TRACE
    printf("Pivot row %d is: ", pivot_row_idx);
    PrintVector(pivot_row);
    #endif
    
    //print();
    
    for(unsigned int r = 0; r < data.size() - 1; ++r)
    {
      if (r == pivot_row_idx) {
        continue;
      }
      
      VecDouble& row = data[r];
      VecDouble& pivot_row = data[pivot_row_idx];
      //trace("end of row %d was %f\n", r, *row.rbegin());
      const double multiple = -row[pivot_col_idx];
      trace("Multiple is %f\n", multiple);
      
      #if TRACE
      printf("Row %d was: ", r);
      PrintVector(data[r]);      
      #endif
      
      transform(row.begin(), row.end(), pivot_row.begin(), row.begin(),
        boost::bind(std::plus<double>(), _1,
          boost::bind(std::multiplies<double>(), multiple, _2)));
    
      #if TRACE
      printf("Row %d is now: ", r);
      PrintVector(data[r]);
      #endif
      
      //fix double inaccuracy
      //*row.rbegin() = round(*row.rbegin(), 12); 
      //trace("end of row %d is %f\n", r, *row.rbegin());
      if (*row.rbegin() < 0 && *row.rbegin() >= -zero_threshold) {
        *row.rbegin() = 0;
      }
      assert(r == data.size() - 1 || *row.rbegin() >= 0);
      
      #if ERROR
      if (r != data.size() - 1 && !(*row.rbegin() >= 0)) {
        error("end of row %d is %f\n", r, *row.rbegin());  
      }
      #endif
      
    }
    
    b_solved = true;
    unsigned int r = data.size() - 1;
    {
      
      VecDouble& row = data[r];
      VecDouble& pivot_row = data[pivot_row_idx];
      //trace("end of row %d was %f\n", r, *row.rbegin());
      const double multiple = -row[pivot_col_idx];
      trace("Multiple is %f\n", multiple);
      
      #if TRACE
      printf("Row %d was: ", r);
      PrintVector(data[r]);      
      #endif
      
      for(unsigned int c=0; c<row.size(); ++c) {
        row[c] = row[c] + multiple * pivot_row[c];
        if (row[c] < 0 && c < row.size() - 1) {
          b_solved = false;
        }
      }
      
      #if TRACE
      printf("Row %d is now: ", r);
      PrintVector(data[r]);
      #endif
      
      //fix double inaccuracy
      //*row.rbegin() = round(*row.rbegin(), 12); 
      //trace("end of row %d is %f\n", r, *row.rbegin());
      if (*row.rbegin() < 0 && *row.rbegin() >= -zero_threshold) {
        *row.rbegin() = 0;
      }
      assert(r == data.size() - 1 || *row.rbegin() >= 0);
      
      #if ERROR
      if (r != data.size() - 1 && !(*row.rbegin() >= 0)) {
        error("end of row %d is %f\n", r, *row.rbegin());  
      }
      #endif
      
    }
    
    #if ERROR
    double z_value = *z_row.rbegin();
    trace("%f is the current val\n", z_value);
    trace("%f is the current max\n", last_max);
    assert(z_value >= last_max);
    last_max = z_value;
    #endif
    
    return true;
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
  
  Simplex simplex(2, 3);
  VecDouble z;
  VecDouble c1;
  int steps = 0;
  
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
    trace("Step: %d \n", ++steps);
    minplex.do_step();
    minplex.print();
  }
  
  cout << "Minimize W = .12x + .15y subject to 60x+60y >= 300, 12x+6y >= 36, 10x + 30y >= 90" << endl;
  cout << "Optimal Solution: w = 0.66; x = 3, y = 2" << endl;
  minplex.print_current_solution();
  
  return 1;
  
}

  
