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
#define SHOW_TIME 0
#define DEBUG_OUTPUT 0

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

#if SHOW_TIME 
  clock_t begin_g=clock();
#endif
	
  for (int test_case = 0; test_case < T; ++test_case) 
  {
    do_test_case(test_case, input);  
  }
  
#if SHOW_TIME
  clock_t end_g=clock();
  cout << "Time elapsed: " << double(diffclock(end_g,begin_g)) << " ms"<< endl;
#endif
}


typedef long long LONG_t;

struct Node
{
public:
  Node* parent;
  int rank;
  
  Node() : parent(this), rank(0)
  {
    
  }
  
  
};

Node* find_root(Node* x)
{
  if (x->parent == x) {
    return x;
  } else {
    return find_root(x->parent);
  }
}

 
     
void merge(Node* a, Node* b)
{
  Node* a_root = find_root(a);
  Node* b_root = find_root(b);
  
  if (a_root->rank > b_root->rank) {
    b_root->parent = a_root;
  } else if (a_root != b_root) {
    a_root->parent = b_root;   
    if (a_root->rank == b_root->rank) {
      b_root->rank += 1;
    }
  }

    
}
  


void do_test_case(int test_case, ifstream& input)
{
  LONG_t A, B, P;
  input >> A >> B >> P;
  
  //First step, find  P <= primes <= interval
  LONG_t lower_bound_primes = P;
  unsigned upper_bound_primes = B-A;

SHOW_TIME_BEGIN(primes) 
    
  vector<bool> primes(upper_bound_primes, true);
  
  for(int i=2; i<primes.size(); ) {
    for(int j=i+i; j<primes.size(); j+=i) {
      primes[j] = false;
    }
    
    for(i=i+1 ; i < primes.size() && primes[i] == false; ++i) {
      
    }
    
    //cout << "I is " << i << endl;
    //++i;
  }
  
SHOW_TIME_END(primes)

SHOW_TIME_BEGIN(create_sets)
  vector<Node *> sets;
  for(LONG_t interval_p = A; interval_p <= B; ++interval_p) {
    sets.push_back(new Node());
  }
SHOW_TIME_END(create_sets)

#if SHOW_TIME 
  int cycle = 0;
  const int print_interval = 200;
  clock_t begin_pmain=clock();
#endif

SHOW_TIME_BEGIN(main_sieve)
  for(int prime=P; prime<primes.size(); ++prime) {
    if (primes[prime]) {
#if DEBUG_OUTPUT
      printf("Prime %d\n", prime);
#endif

#if SHOW_TIME 
  if (cycle == 0) {
    begin_pmain=clock();
  }
  ++cycle;  
#endif
      
      int aModPrime = A % prime;
      LONG_t interval_p = (aModPrime == 0) ? A : A + prime - aModPrime;
      
      //printf("First hit in interval %lld\n", interval_p);
      
      Node* set_to_merge = sets[interval_p - A];
      LONG_t set_to_merge_idx = interval_p;
      
      for(int i=interval_p - A + prime; i < sets.size(); i += prime)
      {
        merge(sets[i], set_to_merge);
#if DEBUG_OUTPUT
        printf("Merging sets  %lld with %d\n", set_to_merge_idx, i);
#endif
      }

#if SHOW_TIME
if (cycle == print_interval) {
  cycle = 0;
  clock_t end_pmain=clock();
  cout << "Time elapsed.  Cur prime (" << prime << ") primes: (" << print_interval << ") : " << double(diffclock(end_pmain,begin_pmain)) << " ms"<< endl;
}
#endif

    }
  }

SHOW_TIME_END(main_sieve)

SHOW_TIME_BEGIN(last_count)
  //vector<bool> visited(sets.size(), false);
  LONG_t set_count = 0;
  
  for(LONG_t i = 0; i < sets.size(); ++i) {
    //if (visited[i] == false) {
    if (sets[i]->parent == sets[i]) {
#if DEBUG_OUTPUT
      printf("Counting set %lld\n", i+A);
#endif
      ++set_count;
    }
    
    delete(sets[i]);
  }
  
SHOW_TIME_END(last_count)
  //printf("A=%lld B=%lld P=%lld\n", A, B, P);
  
  //cout << numeric_limits<long long>::max() << endl;
  printf("Case #%d: %lld\n", test_case+1, set_count );
  
  
    
}
  
