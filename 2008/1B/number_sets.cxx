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


typedef long long LONG_t;

struct Node
{
public:
  Node* parent;
  LONG_t value;
  
  Node(LONG_t value) : parent(this), value(value)
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

  a_root->parent = b_root;  
}
  
#define DEBUG_OUTPUT 0

void do_test_case(int test_case, ifstream& input)
{
  LONG_t A, B, P;
  input >> A >> B >> P;
  
  //First step, find  P <= primes <= interval
  LONG_t lower_bound_primes = P;
  unsigned upper_bound_primes = B-A;
  
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
  
  vector<Node *> sets;
  for(LONG_t interval_p = A; interval_p <= B; ++interval_p) {
    sets.push_back(new Node(interval_p));
  }
  
  for(int prime=P; prime<primes.size(); ++prime) {
    if (primes[prime]) {
#if DEBUG_OUTPUT
      printf("Prime %d\n", prime);
#endif
      LONG_t interval_p = A;
      //find 1st divisible
      for( ;interval_p <= B; interval_p += 1)
      {
        if (interval_p % prime == 0) {
          break;
        }
      }
      
      //printf("First hit in interval %lld\n", interval_p);
      
      Node* set_to_merge = sets[interval_p - A];
      LONG_t set_to_merge_idx = interval_p;
      
      interval_p += prime;
      for(; interval_p <= B; interval_p += prime)
      {
        merge(sets[interval_p - A], set_to_merge);
#if DEBUG_OUTPUT
        printf("Merging sets  %lld with %lld\n", set_to_merge_idx, interval_p);
#endif
      }
      
    }
  }
  
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
  }
  
  //printf("A=%lld B=%lld P=%lld\n", A, B, P);
  
  //cout << numeric_limits<long long>::max() << endl;
  printf("Case #%d: %lld\n", test_case+1, set_count );
  
  
    
}
  
