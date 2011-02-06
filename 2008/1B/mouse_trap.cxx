#include <fstream>
#include <iostream>
#include <vector>
#include <algorithm>
#include <limits>

#include <stdio.h>
#include <time.h>

using namespace std;

double diffclock(clock_t clock1,clock_t clock2)
{
	double diffticks=clock1-clock2;
	double diffms=(diffticks*1000)/CLOCKS_PER_SEC;
	return diffms;
} 

void do_test_case(int test_case, ifstream& input);
#define SHOW_TIME 1
#define DEBUG_OUTPUT 1

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


typedef long long LONG_t;

struct Bucket
{

private:

  Bucket* parent;
  Bucket* left_child;
  Bucket* right_child;
  Bucket* root;
  
  //inclusive
  int from;
  int to;
  int count;
  int size;
  
public:
  
  Node(int from, int to) : root(this), parent(0), left_child(0), right_child(0), from(from), to(to), count(0)
  {
    size = from - to + 1;
    count = size;
  }
  
  void create_tree()
  {
    if (size > 1) {
      int middle = from + size / 2;
      left_child = new Bucket(from, middle - 1);
    
  }
};


void do_test_case(int test_case, ifstream& input)
{
  int K, n;
  input >> K >> n;
  
  typedef vector<unsigned> Vec_UINT_t; 
  Vec_UINT_t indices(n, 0);
  
  for( Vec_UINT_t::iterator it = indices.begin(); it != indices.end(); ++it) 
  {
    input >> *it;
  }
  
  Vec_UINT_t deck(K-1, 0);
  int cur_pos = 0;  
  deck[0] = 1;
  
  for(int k=2; k<=K; ++k) 
  {
    int steps = 0;
    while(steps < k) {
      ++cur_pos;
      if (cur_pos >= K) {
        cur_pos = 0;
      }
      if (deck[cur_pos] == 0) {
        ++steps;
      }
    }
    
    deck[cur_pos] = k;
  }
  #if DEBUG_OUTPUT
  for(int k=1; k<=K; ++k) {
    printf("Card# %d is %d\n", k, deck[k-1]);
  }
  #endif
  
  printf("Case #%d: ", test_case+1);
  
  for (int i=0; i<indices.size(); ++i) {
    printf("%d ", deck[indices[i]-1]);
  }
  
  printf("\n");
    
}
  
