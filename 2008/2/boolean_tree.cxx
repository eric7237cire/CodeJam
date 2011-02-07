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
//#undef assert
//#define assert(x) ((void)0)

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


struct iNode {
  virtual bool getValue() = 0;
  virtual int getMinNumberChanges(bool desiredValue) = 0;  
};

const int AND = 1;
const int OR = 0;

struct BoolNode : public iNode
{
  int type;
  iNode* lChild;
  iNode* rChild;
  bool changeable;
  
  BoolNode(int type, int changeable) : type(type), changeable(changeable == 1), lChild(0), rChild(0)
  {
  }
  
  static int SafeAddition(int A, int B)
  {
    assert(A >= 0);
    assert(B >= 0);
    int sum = A + B;
    if (sum < 0) {
      return numeric_limits<int>::max();
    }
    return sum;
  }
  
  int getMinNumberChanges(bool desiredValue)
  {
    /*
    if (getValue() == desiredValue) {
      return 0;
    }*/
    
    if (!changeable) {
      if (desiredValue && type == AND) {
        return SafeAddition(lChild->getMinNumberChanges(true), rChild->getMinNumberChanges(true));
      }
      
      if (!desiredValue && type == AND) {
        return min<int>(lChild->getMinNumberChanges(false), rChild->getMinNumberChanges(false));
      }
      
      if (desiredValue && type == OR) {
        return min<int>(lChild->getMinNumberChanges(true), rChild->getMinNumberChanges(true));
      }
      
      if (!desiredValue && type == OR) {
        
        int ret = SafeAddition(lChild->getMinNumberChanges(false), rChild->getMinNumberChanges(false));
        //cout << "uh" << ret << endl;
        return ret;
      }
    } else {
      if (desiredValue ) {
        return min<int>( 
          SafeAddition(type==AND ? 0 : 1, 
            SafeAddition(lChild->getMinNumberChanges(true), rChild->getMinNumberChanges(true))),
          SafeAddition(type == OR ? 0 : 1, 
          min<int>(
            lChild->getMinNumberChanges(true), 
            rChild->getMinNumberChanges(true))
          ));
      } else {
        //want false
        int andCost = SafeAddition(type==AND ? 0 : 1, 
            min<int>(lChild->getMinNumberChanges(false), rChild->getMinNumberChanges(false)));

        int orCost = SafeAddition(type == OR ? 0 : 1,SafeAddition(lChild->getMinNumberChanges(false),
            rChild->getMinNumberChanges(false)));
        
        return min<int>(andCost, orCost); 
          
      }
    }
     
     
  }
  
  bool getValue() 
  {
    assert(lChild);
    assert(rChild);
    
    if (type == AND) {
      return lChild->getValue() && rChild->getValue();
    } else {
      return lChild->getValue() || rChild->getValue();
    }
  }
};

struct ValueNode : public iNode
{
  bool value;
  
  ValueNode(bool value) : value(value)
  {
  }
  
  int getMinNumberChanges(bool desiredValue)
  {
    if (value == desiredValue) {
      return 0;
    } else {
      return numeric_limits<int>::max();
    }
  }
  
  bool getValue() 
  {
    return value;
  }
};

void do_test_case(int test_case, ifstream& input)
{
  int M, V;
  input >> M >> V;
  
  vector<iNode*> nodes; //(M, static_cast<iNode*>(NULL) );
  
  for(int i = 0; i < (M-1) / 2; ++i)
  {
    int G, C;
    input >> G >> C;
    nodes.push_back( new BoolNode(G, C) );
    
    #if DEBUG_OUTPUT
    printf("Creating bool Node=%d, G=%d, C=%d\n", i+1, G, C);
    #endif
  }
  
  for(int i = 0; i < (M+1) / 2; ++i)
  {
    int I;
    input >> I;
    
    #if DEBUG_OUTPUT
    printf("Node=%d, value=%d\n", i+1, I);
    #endif
    nodes.push_back(new ValueNode(I));
  }
  
  assert(nodes.size() == M);
  
  #if DEBUG_OUTPUT
  printf("M=%d, V=%d\n", M, V);
  #endif
  for(int i = 1; i <= (M-1) / 2; ++i)
  {
    BoolNode* node = dynamic_cast<BoolNode*>(nodes[i-1]);
    node->lChild = nodes[2*i-1];
    node->rChild = nodes[2*i+1-1];
    
    #if DEBUG_OUTPUT
    printf("Node=%d, lc=%d, rc=%d\n", i, 2*i, 2*i+1);
    #endif
  }
  
  for(int i = 0; i < M; ++i)
  {
    #if DEBUG_OUTPUT
    printf("Node=%d, value=%d\n", i+1, nodes[i]->getValue());
    #endif
  }
  
  int minNum = nodes[0]->getMinNumberChanges(V);
  
  printf("Case #%d: ", test_case+1);
  if (minNum == numeric_limits<int>::max()) {
    printf("IMPOSSIBLE");
  } else {
    printf("%d", minNum);
  }
  
  printf("\n");
  
  
    
}
  
