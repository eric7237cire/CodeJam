#include <iostream>
#include <fstream>
#include <vector>
#include <string> 
#include <set>
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

  for (int test_case = 0; test_case < T; ++test_case) 
  {    
      do_test_case(test_case, input, output);   
  }
  
  input.close();
  output.close();
 
}

int powTen[7] = {1, 10,100,1000,10000,100000,1000000};

int getDigits(int n) {
	for(int digits = 6; digits >= 0; --digits) {
		//cout << "pow ten " << powTen[digits] << endl;
		if (n >= powTen[digits] ) 
			return digits+1;
	}

	return 0;
}

void do_test_case(int test_case, ifstream& input, ofstream& output) {

	

	int A; int B;
	input >> A >> B;

	cout << "A " << A << " B " << B << endl;
	int digits = getDigits(A);
	int tenPowDigits = powTen[digits-1];

	cout << "Digits " << digits << " ten " << tenPowDigits << endl;
	int count = 0;
	
	for(int n = A; n <= B; ++n)
	{
		int mem = n;
		for(int shifts = 0; shifts < digits - 1; ++shifts)
		{
			int digit = mem % 10;
			int m = mem / 10;
			m += tenPowDigits * digit;

			mem = m;

			if (mem == n) {
					break;
				}
			
			if (m > n && m <= B) {
				++count;				
			}
		}
	}

	
	output << "Case #" << test_case+1 << ": " << count << endl;
}