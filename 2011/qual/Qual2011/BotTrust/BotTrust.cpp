// BotTrust.cpp : Defines the entry point for the console application.
//

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

  	
  for (int test_case = 0; test_case < T; ++test_case) 
  {    
      do_test_case(test_case, input, output);   
  }
  
  input.close();
  output.close();
 
}

void do_test_case(int test_case, ifstream& input, ofstream& output) 
{
	int N;
	input >> N ;

	assert(N >= 0 && N <= 100);

	typedef pair<int,int> Pair_t;

	Pair_t oMove, bMove;

	vector<Pair_t> oMoves, bMoves;

	for(int i = 0; i < N; ++i) 
	{
		char hallway;
		input >> hallway;

		unsigned short position;
		input >> position;

		if (hallway == 'O') 
			oMoves.push_back(Pair_t(i,position));
		else
			bMoves.push_back(Pair_t(i,position));
	}

	int time = 0;
	int bluePos = 1, orangePos = 1;
	int curBlueIdx = 0, curOrangeIdx = 0;
	int sequenceNum = 0;

	while(true) 
	{
		bool moveSeq = false;

		for(int i = 0; i <= 1; ++i) 
		{
			int& curIdx = i == 0 ? curBlueIdx : curOrangeIdx;
			int& pos = i == 0 ? bluePos : orangePos;
			vector<Pair_t>& moves = i == 0 ? bMoves : oMoves;

			if(curIdx < moves.size()) 
			{
				Pair_t move = moves[curIdx];
				if(move.second == pos && sequenceNum == move.first) 
				{
					moveSeq = true;
					++curIdx;
				} else if(move.second < pos) 
				{
					--pos;
				} else if (move.second > pos)
				{
					++pos;
				}
			}
		}

		if (moveSeq)
			++sequenceNum;

		++time;

		if (curBlueIdx == bMoves.size() && curOrangeIdx == oMoves.size()) 
		{
			break;
		}
	}



	output << "Case #" << test_case+1 << ": " << time << endl;

}