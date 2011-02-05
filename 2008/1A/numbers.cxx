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
 

void do_test_case(int test_case, ifstream& input)
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
      
      long long term = power(a, a_exp) * b_power(b_exp);
#if 1
      printf("A ^ %d * B ^ %d = %lld * %d\n", a_exp, b_exp,
        power(a, a_exp), b_power(b_exp));
      printf("Adding term %lld * %lld\n", term, co_eff);
#endif
      
      value += (term % 1000) * (co_eff % 1000);
      value %= 1000;
    }
    

#if 1
    printf("Coeff %lld * %d / %d\n", co_eff, N-i, i+1);
#endif    
    co_eff *= (N-i);
    co_eff /= (i+1);
    

    //co_eff %= 1000;
    
    a_exp--;
    b_exp++;
  }
  
  //cout << numeric_limits<long long>::max() << endl;
  printf("Case #%d: %03d\n", test_case+1, abs( (2*value - 1) % 1000 ) );
  
  
    
}
  
