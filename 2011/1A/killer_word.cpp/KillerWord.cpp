#include <iostream>
#include <fstream>
#include <vector>
#include <string> 
#include <set>
#include <boost/assert.hpp> 
#include "boost/multi_array.hpp"
using namespace std;
using namespace boost;

int main(int argc, char** args)
{
	int T;
	cin >> T;
	for(int t = 1; t <= T; ++t) 
	{
		unsigned int N, M;
		cin >> N >> M;

		vector<string> words;
		vector<string> lists;
		boost::array<set<string>, 10> sizeToWords;
	  // Create a 3D array that is 3 x 4 x 2
  typedef boost::multi_array<set<string>, 2> array_type;
  typedef array_type::index index;
  array_type indexLetterWords(boost::extents[10][26]);

  
		for(unsigned int n = 0; n < N; ++n) 
		{
			string word;
			cin >> word;
			words.push_back(word);

			sizeToWords[word.size() - 1].insert(word);

			for(int i = 0; i < word.size(); ++i) 
			{
				indexLetterWords[i][word[i] - 'a'].insert(word);				
			}
		}

		cout << "Case #" << t << ": ";
		for(unsigned int m = 0; m < M; ++m) 
		{
			string list;
			cin >> list;
			
			string maxWord;
			int maxPointsLost = -100;

			for(int n = 0; n < N; ++n)
			{
				//cout << "word " << words[n] << endl;
				string word = words[n];
				set<string> possibleWords = sizeToWords[words[n].size()-1];

				
				
				vector<bool> blanks(word.size());
				blanks.assign(blanks.size(), true);
				int blanksLeft = word.size();
				int pointsLost = 0;

				for(int glIdx = 0; glIdx < 26; ++glIdx)
				{
					char guessLetter = list[glIdx];
					vector<string> possibleWordsForGuess;
					//intersection.assign(possibleWords.begin(), possibleWords.end());

					for(int b = 0; b < blanks.size(); ++b)
					{
						if(!blanks[b])
							continue;

						vector<string> temp;
						set_union( indexLetterWords[b][guessLetter-'a'].begin(), indexLetterWords[b][guessLetter-'a'].end(),
							possibleWordsForGuess.begin(), possibleWordsForGuess.end(), back_inserter(temp) );

						possibleWordsForGuess = temp;


					}

					vector<string> intersection;
					set_intersection(possibleWords.begin(), possibleWords.end(),
						possibleWordsForGuess.begin(), possibleWordsForGuess.end(),
						back_inserter(intersection));

					
					if  (intersection.empty()) 
					{
						continue;
					} else {
						//guess letter
						int lettersGuessed = 0;
						for(int b = 0; b < blanks.size(); ++b)
						{
							if (word[b] == guessLetter) 
							{
								blanks[b] = false;
								++lettersGuessed;

								//reduce possibleWords
								vector<string> temp;
								set_intersection( indexLetterWords[b][guessLetter-'a'].begin(), indexLetterWords[b][guessLetter-'a'].end(),
								possibleWords.begin(), possibleWords.end(), back_inserter(temp) );

								possibleWords = set<string>(temp.begin(), temp.end());
						
							} else {
								//eliminate words that have letter in that position
								vector<string> temp;
								set_difference( possibleWords.begin(), possibleWords.end(),
									indexLetterWords[b][guessLetter-'a'].begin(), indexLetterWords[b][guessLetter-'a'].end(),
								 back_inserter(temp) );

								possibleWords = set<string>(temp.begin(), temp.end());

							}
						}

						if (lettersGuessed == 0)
						{
							++pointsLost;
						}

						blanksLeft -= lettersGuessed;
						if (blanksLeft == 0) 
						{
							break;
						}
					}
				}

				if (pointsLost > maxPointsLost)
				{
					maxWord = word;
					maxPointsLost = pointsLost;
				}
				assert(blanksLeft == 0);
			}
			
			cout << "List " << list << endl;
			cout << "Possible " << endl << endl;
			set<string> possibleWords = sizeToWords[maxWord.size()-1];

			for(set<string>::const_iterator it = possibleWords.begin(); it != possibleWords.end(); ++it)
					cout << *it << ", " << endl;

			cout << maxWord << " ";
			cout << "[" << maxPointsLost << "]" << endl;
		}

		cout << endl;
	}
}