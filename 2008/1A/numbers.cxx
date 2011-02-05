#include <fstream>
#include <iostream>
#include <vector>
#include <algorithm>
#include <limits>

using namespace std;

void do_test_case(int test_case, ifstream& input);

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
  
  for (int test_case = 0; test_case < T; ++test_case) 
  {
    do_test_case(test_case, input);  
  }

}

long long n_choose_k(unsigned n, unsigned k)
{
  long long r = 1;
  
  int n_k = n-k;
  
  int d1 = min<int>(k, n_k);
  int d2 = max<int>(k, n_k);
  
  for (int i = d2+1; i <= n; ++i) {
    r *= i;
  }
  
  for (int i = 1; i <= d1; ++i) {
    r /= i;
  }
  
  return r;
}

long long power(int base, int p)
{
  if (p == 0) {
    return 1;
  }
  long long sum = base;
  
  for (int i=1+1; i<=p; ++i) {
    sum *= base;
  }
  
  return sum;
}

//base is 5 ; only care about < 1000
int b_power(int p)
{
  switch (p) {
  case 0:
    return 1;
  case 2:
    return 5;
  case 4:
    return 25;
  default:
    return (p % 4 == 0) ? 625 : 125;
  }
}

typedef long long LONG_t;

void do_test_case(int test_case, ifstream& input)
{
  int N;
  input >> N;
  
  //initialize to (3 + sqrt(5) ) ^ 0, represented as integer_part * radical_coeff * sqrt(5)
  LONG_t integer_part = 1;
  LONG_t radical_coeff = 0;
  
  //17 same as 117, so pattern repeats
  
  if (N > 117) {
    N = 17 + (N - 17) % 100;
  }
  
  for (int i=1; i<=N; ++i)
  {
    LONG_t new_integer_part = 3*integer_part + 5 * radical_coeff;
    LONG_t new_radical_coeff = integer_part + 3 * radical_coeff;    
    
    integer_part = new_integer_part % 1000;
    radical_coeff = new_radical_coeff % 1000;
    
#if 0  
  //printf("%d: %lld + %lld * sqrt(5) \n", i, integer_part, radical_coeff);
  printf("%d: %lld\n", i, integer_part, radical_coeff);  
#endif

  }
#if 0  
  printf("%lld + %lld * sqrt(5) \n", integer_part, radical_coeff);
  printf("2 * X^n - 1 = %lld\n", 2*integer_part-1);
#endif

  printf("Case #%d: %03lld\n", test_case+1, (2*integer_part-1) % 1000 );
}


void do_test_case_not_working(int test_case, ifstream& input)
{
  int N;
  input >> N;
  
  //(3+√5)^9
  
  int a_exp = N;
  int b_exp = 0;
  const int a = 3;
  const int b2 = 5;
  
  int value = 0;
  long long co_eff = 1;
  
  for (int i=0; i <= N; ++i) {
    //printf("%d choose %d = %d\n", N, i, n_choose_k(N, i));
    
    if (b_exp % 2 == 0) {
      
      long long term = b_power(b_exp);
      
      //shortcut
      if (b_exp >= 6) 
      {
        if (a_exp % 2 != 0) {
          term = (term == 625 ) ? 875 : 375;
        }
      } else {
        term *= power(a, a_exp);
      }
        
      
#if 1
      //printf("A ^ %d * B ^ %d = %lld * %d\n", a_exp, b_exp, power(a, a_exp), b_power(b_exp));
      printf("Adding term %lld * (co_eff) %lld ; %lld \n", term, co_eff, co_eff % 8);
      //printf("Adding term %lld\n", (term % 1000 * (co_eff % 1000)) % 1000);
      //printf("Adding term %lld\n", (term % 1000 * (co_eff % 8)) % 1000);
#endif
      
      value += term % 1000 * (co_eff % 1000);
      value %= 1000;
    }
    

#if 0
    printf("Coeff %lld * %d / %d\n", co_eff, N-i, i+1);
#endif    
    co_eff *= (N-i);
    co_eff /= (i+1);
    
    if (b_exp >= 6) {
      //co_eff %= 8;
    }

    //co_eff %= 1000;
    
    a_exp--;
    b_exp++;
  }
  
  //cout << numeric_limits<long long>::max() << endl;
  printf("Case #%d: %03d\n", test_case+1, abs( (2*value - 1) % 1000 ) );
  
  
    
}
  
