#include <fstream>
#include <iostream>
#include <vector>
#include <algorithm>
#include <limits>

#include <stdio.h>
#include <time.h>
#include <assert.h>

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
  vector<Bucket*>& child_list;
  
  //inclusive
  int from;
  int to;
  int count;
  int size;
  
public:
  
  int getCount()
  {
    return count;
  }
  
  Bucket(int _from, int _to, vector<Bucket*>& child_list) : child_list(child_list), parent(0), left_child(0), right_child(0), from(_from), to(_to), count(0)
  {
    assert(to >= from);
    size = to - from + 1;
    count = size;
    //printf("%d %d\n", from, to);
    assert(size >= 1);
  }
    
  void create_tree()
  {
    if (size > 1) {
      int middle = from + size / 2;
      assert(middle <= to);
      assert(middle > from);
      
      left_child = new Bucket(from, middle - 1, child_list);
      left_child->parent = this;
      
      right_child = new Bucket(middle, to, child_list);
      right_child->parent = this;
      
      left_child->create_tree();
      right_child->create_tree();
    } else {
      assert(size == 1);
      //printf("%d %d\n", from, to); 
      assert(child_list[from] == 0);
      child_list[from] = this;
    }
    
  }
  
  void mark() {
    --count;
    assert(count >= 0);
    if (parent != NULL) {
      parent->mark();
    }
  }
  
  //returns position reached
  static int advance(Bucket* root, int cur_pos, int steps_needed, const int max_pos)
  {
    //first find bucket whose left == cur_pos
    Bucket* curBucket = root;
    
    assert (steps_needed > 0); //|| root->child_list[cur_pos]->count == 0);
    
    int first_next_pos = cur_pos + 1;
    if (first_next_pos > max_pos) {
      first_next_pos = 0;
    }
    
    
    //printf("Begin advance %d - %d; count=%d, cur_pos=%d, steps_needed=%d\n", curBucket->from, curBucket->to, curBucket->count, cur_pos, steps_needed);
    while(first_next_pos != curBucket->from) {
      //printf("%d - %d; count=%d, steps_needed=%d\n", curBucket->from, curBucket->to, curBucket->count, steps_needed);
      assert(curBucket->size > 1);
      assert(curBucket->left_child != 0);
      assert(curBucket->right_child != 0);
      
      if (first_next_pos > curBucket->left_child->to) {
        assert(curBucket->right_child != curBucket);
        curBucket = curBucket->right_child;
      } else {
        assert(curBucket->left_child != curBucket);
        curBucket = curBucket->left_child;
      }
      
      if (curBucket->count == 0) {
        //advance to the end
        int retVal = advance(root, curBucket->to, steps_needed, max_pos);
        assert(retVal != curBucket->to);
        //printf("Returning %d\n", retVal);
        return retVal;
      }
    }
    
    assert(curBucket->from == first_next_pos);
    assert(curBucket->count >= 0);
    
    //printf("Found from %d - %d; count=%d, cur_pos=%d, steps_needed=%d\n", curBucket->from, curBucket->to, curBucket->count, cur_pos, steps_needed);
    
    if (curBucket->count >= steps_needed) {
      //curBucket or a child contains the position we want
      
      while(curBucket->size > 1) {
        if (curBucket->left_child->count >= steps_needed && curBucket->left_child->count > 0) {
          //left child
          curBucket = curBucket->left_child;
        } else {
          //right child has target cur_pos
          steps_needed -= curBucket->left_child->count;
          curBucket = curBucket->right_child;
        }
      }
      
      assert(curBucket->size == 1);
      //printf("Returning from in bucket %d\n", curBucket->from);
      return curBucket->from;
    } else {
      //inc cur pos
      /*
      cur_pos = curBucket->to + 1;
      if (cur_pos > max_pos) {
        cur_pos = 0;
      }*/
      assert(curBucket->count > 0);
      
      steps_needed = steps_needed - curBucket->count;
      
      
      assert(steps_needed >=  0);
      int retVal = advance(root, curBucket->to, steps_needed, max_pos);
      if (retVal == curBucket->to) {
        //printf("%d - %d; count=%d, steps_needed=%d, curPos=%d, max_pos=%d, cur_pos count=%d, bucket_count=%d\n", curBucket->from, curBucket->to, curBucket->count, steps_needed, cur_pos, max_pos, root->child_list[cur_pos]->count, root->count);
        //throw 3;
      }
      return retVal;
    }
        
    
  }
  
  Bucket* operator[](int idx) {
    return child_list[idx];
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
  
  Vec_UINT_t deck(K, 0);
  vector<Bucket*> buckets(K, static_cast<Bucket*>(0));
  Bucket rootBucket(0, K-1, buckets);
  rootBucket.create_tree();
  int cur_pos = 0;  
  deck[0] = 1;
  buckets[cur_pos]->mark();
  
  for(int k=2; k<=K; ++k) 
  {
    #if DEBUG_OUTPUT
    //printf("handling k=%d\n", k);
    #endif
    int steps = 0;
    assert(rootBucket.getCount() == K-k + 1);
    int steps_needed = 0;
    if ( k % rootBucket.getCount() == 0) {
      steps_needed = rootBucket.getCount();
    } else {
      steps_needed = k % rootBucket.getCount();
    }
    //int steps_needed = k % rootBucket.getCount();
    //cout << rootBucket.getCount() << endl;
    //assert(steps_needed > 0);
    //printf("cur_pos=%d, Starting Bucket\n", cur_pos);
    //printf("k=%d, cur_pos=%d, steps_needed=%d, bucket_count=%d\n", k, cur_pos, steps_needed, rootBucket.getCount());
    cur_pos = Bucket::advance(&rootBucket, cur_pos, steps_needed, K-1);
    
    //assert(bucketCurPos == cur_pos);
    //printf("Done Advance, main loop. k=%d, cur_pos=%d, steps_needed=%d, bucket_count=%d\n", k, cur_pos, steps_needed, rootBucket.getCount());
    assert(buckets[cur_pos] != 0);
    assert(deck[cur_pos] == 0);
    buckets[cur_pos]->mark();
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
  
