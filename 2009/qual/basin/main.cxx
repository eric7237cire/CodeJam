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


void do_test_case(int, ifstream& input);

int main(int argc, char** args)
{
    
  LOG_OFF();
    
  if (argc < 2) {
    cerr << "Usage: <exe> input_file" << endl;
    return -1;
  }
  
  ifstream input;
  input.open(args[1]);
  
  int T;
  input >> T;

  SHOW_TIME_BEGIN(g) 
  	
  for (int test_case = 0; test_case < T; ++test_case) {
    do_test_case(test_case, input);   
  }
      
  SHOW_TIME_END(g)
}

enum {
    NORTH,
    WEST,
    EAST,
    SOUTH
};

typedef pair<int, int> IntPair;

void do_test_case(const int test_case, ifstream& input)
{
    int H, W;
    
    input >> H;
    input >> W;
    
    int alt[H][W];
    char visited[H][W];
    
    for(int h = 0; h < H; ++h) {
        for(int w = 0; w < W; ++w) {
            alt[h][w] = 0;
            visited[h][w] = ' ';
            input >> alt[h][w];            
        }
    }
    
    char curLabel = 'a' - 1;
    
    //Label sinks
    for(int h = 0; h < H; ++h) {
        for(int w = 0; w < W; ++w) {
            
            pair<int, int> cur(h, w);
            
            set<IntPair> visitedList;
            
            while(true) {                
                LOG(cur);
                
                //If we have already been here, it's a new basin
                if (visitedList.find(cur) != visitedList.end()) {
                    LOG(cur);
                    LOG_STR("New basin!");
                    curLabel++;
                    
                    for(set<IntPair>::const_iterator it = visitedList.begin();
                        it != visitedList.end();
                        ++it) {
                        visited[it->first][it->second] = curLabel;
                    }
                    break;
                }
                
                //If current square has a label, then label all visited with that label
                if (visited[cur.first][cur.second] != ' ') {
                    for(set<IntPair>::const_iterator it = visitedList.begin();
                        it != visitedList.end(); ++it) {
                        visited[it->first][it->second] = visited[cur.first][cur.second];
                    }
                    break;
                }
                
                visitedList.insert(cur);
                
                int cur_alt = alt[cur.first][cur.second];
                
                vector<IntPair> nextSquares;
                
                if (cur.first > 0) { //north
                    nextSquares.push_back( IntPair(cur.first-1, cur.second) );
                }
                if (cur.second > 0) { //west
                    nextSquares.push_back( IntPair(cur.first, cur.second-1) );
                }
                if (cur.second < W-1) { //east
                    nextSquares.push_back( IntPair(cur.first, cur.second+1) );
                }
                if  (cur.first < H-1) { //south
                    nextSquares.push_back( IntPair(cur.first+1, cur.second) );
                }
                
                
                IntPair nextSquare(cur);
                
                for(vector<IntPair>::const_iterator it = nextSquares.begin();
                    it != nextSquares.end();
                    ++it) {
                    const IntPair& potenNextSquare = *it;
                    int nextAlt = alt[potenNextSquare.first][potenNextSquare.second];
                    
                    if (nextAlt < cur_alt) {
                        nextSquare = potenNextSquare;
                        cur_alt = nextAlt;
                    }
                }
                
                cur = nextSquare;         
                LOG(cur);
                
            }
        }
    }
        
        
    cout << "Case #" << (1+test_case) << ": " << endl;
    
    for(int h = 0; h < H; ++h) {
        for(int w = 0; w < W; ++w) {
            cout << visited[h][w] << " ";
        }
        cout << endl;
    }
      
    
}
  
