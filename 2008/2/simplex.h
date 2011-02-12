#ifndef SIMPLEX_H
#define SIMPLEX_H

#include <vector>
#include <map>

using namespace std;

typedef vector<int> VecInt;
typedef vector<VecInt> MatrixInt;

typedef vector<double> VecDouble;
typedef vector<VecDouble> MatrixDouble;

double round(double r, unsigned int prec);
void PrintVector(const VecDouble& vec);

class Simplex
{
protected:
  unsigned int num_non_basic_variables;
  unsigned int num_basic_variables; // slack variables
  unsigned int num_artificial_variables;
  
  bool finding_max;
  int rows;
  int cols;
  unsigned int cur_constraint;
  MatrixDouble data;
  unsigned int last_chosen_pivot_col;
  double last_max;
  bool initial_simplex_tableau_called;

  bool b_solved;
  
  map<unsigned int, unsigned int> basic_to_artificial;    
  
public:
  Simplex(int num_non_basic_variables, int num_basic_variables);
  void print();
  
  //z = z[0] * x_0 + z[1] * x_1 + ...
  void set_z(const VecDouble& z);
  
  void set_eq_to_minimize(const VecDouble& z);
  void set_eq_to_maximize(const VecDouble& z);
  
  bool is_feasible();
  bool initial_simplex_tableau();
  bool eliminate_neg_s_variable(unsigned int s_col, unsigned int s_row, unsigned int a_col);
  
  //a[1][0]*x[0] + a[1][1]*x[1] + s[1] == b[1]  
  void add_constraint(unsigned int cons_num, const VecDouble& co_eff, double b);
  
  void add_constraint_lte(const VecDouble& co_eff, double b);
  void add_constraint_gte(const VecDouble& co_eff, double b);
  
  void print_current_solution();
  bool verify_equations(const VecDouble& vars);
  
  //find value of all variables
  bool reduce();
  
  //s[i]
  double getBasicVar(unsigned int i);
  
  //x[i]
  double getVar(unsigned int i);
  
  bool solved();
  
  //returns true if optimal
  bool do_step();
  
};

class MinSimplex : public Simplex
{
public:
  //because of the transposition, invert the 2
  MinSimplex(int num_non_basic_variables, int num_basic_variables);
  
  void set_z(const VecDouble& z);
  
  void add_constraint(unsigned int cons_num, const VecDouble& co_eff, double b);
  
};

#endif
