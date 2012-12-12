#include <fstream>
#include <iostream>
#include <iomanip>

using namespace std;

void do_test_case(int test_case, istream& input);

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

const int MAX_ROUNDS = 15;
// [rounds left] [ steps ]
double roundProb[MAX_ROUNDS+1][ (1 << 15) + 1];

void do_test_case(int test_case, istream& input)
{
  int M;
  double P;
  int X;
  input >> M >> P >> X;
    
  memset(roundProb,0,sizeof(roundProb));

  roundProb[0][1] = 1;
  roundProb[0][0] = 0;

  for(int m = 1; m <= M; ++m) 
  {
	  const unsigned int lastRoundMax = (1 << m-1) + 1;
	  //Combine rounds
	  for(unsigned int highIndex = 0; highIndex < lastRoundMax; ++highIndex)	  
	  {
		  //Copy over rounds
		  roundProb[m][highIndex * 2] = max(roundProb[m][highIndex * 2], roundProb[m-1][highIndex]);

		  for(unsigned int lowIndex = 0; lowIndex < highIndex; ++lowIndex)
		  {
			  unsigned int thisRoundIdx = highIndex + lowIndex;
			  roundProb[m][thisRoundIdx] = max(roundProb[m][thisRoundIdx], P * roundProb[m-1][highIndex] + (1-P) * roundProb[m-1][lowIndex]);
		  }
	  }	  
  }
      
  cout << "Case #" << (test_case+1) << ": " 
  << std::setprecision(6) << roundProb[M][(unsigned int) (X / (1000000.0 / (1 << M)))] << endl;
  
  return;    
}


  
