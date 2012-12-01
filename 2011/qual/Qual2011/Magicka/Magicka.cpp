// Magicka.cpp : Defines the entry point for the console application.
//



#include <iostream>
#include <fstream>
#include <vector>
#include <string> 
#include <map>
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

void do_test_case(int test_case, ifstream& input, ofstream& output) 
{
	int C;
	input >> C ;

	map<string, char> combinations;

	for(int i = 0; i < C; ++i) 
	{
		string combin;
		input >> combin;
		char newElem = combin[2];
		combin.erase(2,1);

		combinations[combin] = newElem;
		cout << "Combo " << newElem << endl;
	}

	int D;
	input >> D;

	map<char, char> opposed;

	for(int i = 0; i < D; ++i) 
	{
		string opp;
		input >> opp;
		opposed[opp[0]] = opp[1];
		opposed[opp[1]] = opp[0];
	}

	int N;
	input >> N;

	string elemToInvoke;
	input >> elemToInvoke;

	cout << elemToInvoke << endl;
}