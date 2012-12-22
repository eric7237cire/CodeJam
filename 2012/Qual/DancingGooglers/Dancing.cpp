#include <iostream>
#include <fstream>
#include <vector>
#include <string> 
#include "boost/algorithm/string/replace.hpp"

using namespace std;
using namespace boost;

void do_test_case(int test_case, ifstream& input, ofstream& output);

int main(int argc, char** args)
{
  if (argc < 2) {
    cerr << "Usage: <exe> input_file" << endl;
    return -1;
  }
  
  ifstream input;
  
  input.open(args[1]);
  cout << "Input file " << args[1] << endl;

  if (!input) 
  {
	  cerr << "Can't open input file " << args[1] << endl;
	  exit(1);
  }

  string outFileName = args[1];
  replace_last(outFileName,".in",".out");

  ofstream output;
  output.open(outFileName);

  int T;
  input >> T;

  cout << T << endl;

  input >> ws; 	

  for (int test_case = 0; test_case < T; ++test_case) 
  {    
      do_test_case(test_case, input, output);   
  }
  
  input.close();
  output.close();
 
}

void do_test_case(int test_case, ifstream& input, ofstream& output) {
	
	int N; int S; int p;

	input >> N >> S >> p;

	int count = 0;

	for(int i = 0; i < N; ++i) 
	{
		int t;
		input >> t;

		int maxNonSurp = (t - 1 + 3) / 3;

		if (maxNonSurp >= p) {
			count ++;
			continue;
		}

		if (t > 0 && maxNonSurp + 1 >= p && (t % 3 == 2 || t % 3 == 0) && S > 0) {
			--S;
			count ++;
		}
	}
	output << "Case #" << test_case+1 << ": " << count << endl;
}