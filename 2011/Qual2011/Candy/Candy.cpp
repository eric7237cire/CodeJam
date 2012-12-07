

#include <iostream>
#include <fstream>
#include <vector>
#include <string> 
#include <set>
#include <map>

using namespace std;

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
  
  outFileName.replace(outFileName.begin() + outFileName.size() - 3, outFileName.end(), ".out");

  ofstream output;
  output.open(outFileName);

  int T;
  input >> T;

  cout << T << endl;

  	
  for (int test_case = 0; test_case < T; ++test_case) 
  {    
      do_test_case(test_case, input, output);   
  }
  
  input.close();
  output.close();
 
}

void do_test_case(int test_case, ifstream& input, ofstream& output) 
{
	unsigned int n;
	input >> n;

	unsigned int min = 10000000;
	unsigned int sum = 0;
	unsigned int xOr = 0;

	unsigned int candyValue;

	for(unsigned int i = 0; i < n; ++i) {
		input >> candyValue;
		xOr ^= candyValue;
		sum += candyValue;
		min = std::min(min,candyValue);
	}

	output << "Case #" << test_case+1 << ": ";
	if (xOr != 0) {
		output << "NO" << endl;
	} else {
		output << sum - min << endl;
	}
}