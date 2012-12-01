// Magicka.cpp : Defines the entry point for the console application.
//



#include <iostream>
#include <fstream>
#include <vector>
#include <string> 
#include <set>
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

// print elements of an STL container
template <typename T>
void print (T const& coll)
{
    typename T::const_iterator pos;  // iterator to iterate over coll
    typename T::const_iterator end(coll.end());  // end position

    for (pos=coll.begin(); pos!=end; ++pos) {
        std::cout << *pos << ' ';
    }
    std::cout << std::endl;
}

template <typename T> std::ostream& operator<<(std::ostream& os, const pair<T,T>& obj) 
{ 
	os << obj.first << ", " << obj.second ;
  // write obj to stream
  return os;
} 

 std::ostream& operator<<(std::ostream& os, const pair<string,char>& obj) 
{ 
	os << obj.first << ", " << obj.second ;
  // write obj to stream
  return os;
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
		reverse(combin.begin(), combin.end());
		combinations[combin] = newElem;
		
	}

	cout << "Combinations ";
	print(combinations);

	int D;
	input >> D;

	set<string> opposed;

	for(int i = 0; i < D; ++i) 
	{
		string opp;
		input >> opp;

		opposed.insert(opp);
		reverse(opp.begin(), opp.end());
		opposed.insert(opp);
	}

	cout << "Opposed ";
	print(opposed);

	int N;
	input >> N;

	string elemToInvoke;
	input >> elemToInvoke;

	vector<char> elemList;

	cout << elemToInvoke << endl;
	for(string::const_iterator it = elemToInvoke.begin(); it != elemToInvoke.end(); ++it)
	{
		
		elemList.push_back(*it);
		
		//check combination
		if (elemList.size() >= 2)
		{
			string check( elemList.begin() + elemList.size()  - 2, elemList.end());
			//check += *it;

			assert(check.size() == 2);
			map<string, char>::const_iterator combCheck = combinations.find(check);

			if (combCheck != combinations.end() ) 
			{
			//	continue;

			elemList.pop_back();
			elemList.pop_back();

			elemList.push_back( combCheck->second );
			continue;
			}
		}

		//check opposition
		for(vector<char>::const_iterator eIt = elemList.begin(); eIt != elemList.end(); ++eIt)
		{
			string check(1, *eIt);
			check += *it;
			if (opposed.find(check) != opposed.end()) 
			{
				elemList.clear();
				break;
			}

		}

	}

	output << "Case #" << test_case+1 << ": [";
	for(vector<char>::const_iterator it = elemList.begin(); it != elemList.end(); ++it)
	{
		output << *it;
		if (it != --elemList.end() )
			output << ", ";

	}

	output << "]" << endl;
	
}