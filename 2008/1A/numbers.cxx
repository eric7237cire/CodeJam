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
  
  for (int i=0; i <= N; ++i) {
    //printf("%d choose %d = %d\n", N, i, n_choose_k(N, i));
    
    if (b_exp % 2 == 0) {
      long long co_eff = n_choose_k(N, i);
      long long term = power(a, a_exp) * power(b2, b_exp / 2);
      printf("A ^ %d * B ^ %d = %lld * %lld\n", a_exp, b_exp, power(a, a_exp), power(b2, b_exp / 2));
      printf("Adding term %lld * %lld\n", term, co_eff);
      //term *= co_eff;
      value += (term % 1000) * (co_eff % 1000);
      value %= 1000;
    }
    
    a_exp--;
    b_exp++;
  }
  
  cout << numeric_limits<long long>::max() << endl;
  printf("Case #%d: %03d\n", test_case+1, abs( (2*value - 1) % 1000 ) );
  
  
    
}
  
