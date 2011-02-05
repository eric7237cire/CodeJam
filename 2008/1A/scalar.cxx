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

void do_test_case(int test_case, ifstream& input)
{
  int N;
  input >> N;
  vector<int> x(N), y(N);
  
  for (int i=0; i<N; ++i) {
    input >> x[i];
  }
  
  for (int i=0; i<N; ++i) {
    input >> y[i];
  }
  
  sort(x.begin(), x.end());
  sort(y.begin(), y.end(), greater<int>());
  
  long long sum = 0;
  for (int i=0; i<N; ++i) {
    long long add = static_cast<long long> (x[i]) * y[i];
    if ( (sum + add) - add != sum) {
      throw "prob";
    }
      
    sum += add;
    
  }
  
  /*
  cout << sum << endl;
  cout << numeric_limits<int>::max() << endl;
  cout << numeric_limits<long>::max() << endl;
  cout << 100000LL * 100000 << endl;
*/
  printf("Case #%d: %lli\n", test_case+1, sum);  
}
  
