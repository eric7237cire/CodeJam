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
	
	
	string lineStr;

	getline(input, lineStr);

	cout << lineStr << endl;
	

string key = "ejp mysljylc kd kxveddknmc re jsicpdrysi rbcpc ypc rtcsra dkh wyfrepkym veddknkmkrkcd de kr kd eoya kw aej tysr re ujdr lkgc jvzq";
string value = "our language is impossible to understand there are twenty six factorial possibilities so it is okay if you want to just give upqz";

output << "Case #" << test_case+1 << ": ";

for(string::iterator it = lineStr.begin(); it != lineStr.end(); ++it) {
	size_t idx = key.find(*it, 0);
	if (idx == string::npos) {
		cout << *it << " not found " << endl;
		continue; 
	}
	output << value.at(idx);
}

	output << endl;
}