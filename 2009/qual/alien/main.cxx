#include <fstream>
#include <iostream>
#include <iomanip>
#include <vector>
#include <algorithm>
#include <limits>
#include <map>
#include <set>

#include <stdio.h>
#include <time.h>
#include <assert.h>
#include <stdlib.h>

#include "util.h"

using namespace std;


void do_test_case(int, int, int, ifstream& input);

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
    
    LOG_OFF();
    
  if (argc < 2) {
    cerr << "Usage: <exe> input_file" << endl;
    return -1;
  }
  
  ifstream input;
  input.open(args[1]);
  
  
  int L, D, N;
  input >> L >> D >> N;

  SHOW_TIME_BEGIN(g) 
  	
  
    do_test_case( L, D, N, input);  
  
  
  SHOW_TIME_END(g)
}


void do_test_case(const int L, const int D, const int N, ifstream& input)
{
  vector<string> words;
  
  //letter -> set<words index>
  
  typedef map<int, set<int> > LetterToWordsMap;
  
  //Index is the position in the word
  vector<LetterToWordsMap> letters_words;
  for(int i = 0; i < L; ++i) {
    letters_words.push_back(LetterToWordsMap());   
  }
  
  for(int i = 0; i < D; ++i) {
      string word;
      input >> word;
      words.push_back(word);
      
      for(int j=0; j < L; ++j) {
        letters_words[j][ word[j] ].insert(i);        
      }
  }
  
  for(int j=0; j < L; ++j) {
        LOG(letters_words[j]);        
      }
  
  for(int test_case = 0; test_case < N; ++test_case) {
      LOG(test_case);
    string test_word;
    input >> test_word;
    
    set<int> possibleMatches;
    set<int> parenMatches;
    
    for(int i = 0; i < D; ++i) {
        possibleMatches.insert(i);
    }
    
    LOG(possibleMatches);
    
    int currentPosition = 0;
    bool inParen = false;
    
    for(string::const_iterator it = test_word.begin();
        it != test_word.end();
        ++it) {
            if ( (*it) == '(' ) {
                inParen = true;
                parenMatches.clear();
                continue;
            } else if ( (*it) == ')' ) {
                inParen = false;
                ++currentPosition;
                
                set<int> intersection;
                
                set_intersection(possibleMatches.begin(), possibleMatches.end(),
               parenMatches.begin(), parenMatches.end(),  
               insert_iterator< set<int> > (intersection, intersection.begin()));
                
                
            possibleMatches = intersection;
            
                continue;
            }
            
            set<int> intersection;
            
            set<int> currPosWordMatches = letters_words[currentPosition][ (*it) ] ;
            
            LOG(currPosWordMatches);
            
            if (inParen) {
                set_union(parenMatches.begin(), parenMatches.end(),
               currPosWordMatches.begin(), currPosWordMatches.end(),  
               insert_iterator< set<int> > (intersection, intersection.begin()));
                
                parenMatches = intersection;
                
                LOG(parenMatches);
            } else {
            set_intersection(possibleMatches.begin(), possibleMatches.end(),
               currPosWordMatches.begin(), currPosWordMatches.end(),  
               insert_iterator< set<int> > (intersection, intersection.begin()));
            
            
                possibleMatches = intersection;
                
                
                LOG(possibleMatches);
            }
            
            
            
            if (!inParen) {
                ++currentPosition;
            }
            
            
        }
        
        cout << "Case #" << (1+test_case) << ": " << possibleMatches.size() << endl;
  }
  
  //bool isOn = (n_sq & K) == n_sq;
      
//
    
  return;
    
}
  
