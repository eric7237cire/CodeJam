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
    
    const int INT_MAX = 99999999;
    
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
            
            //visitedList.insert(cur);
            
            while(true) {
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
                        it != visitedList.end();
                        ++it) {
                        visited[it->first][it->second] = visited[cur.first][cur.second];
                    }
                    break;
                }
                
                visitedList.insert(cur);
                
                int cur_alt = alt[cur.first][cur.second];
                int flow_dir = -1;
                
                int northAlt = (cur.first > 0) ? alt[cur.first-1][cur.second] : INT_MAX;
                
                if (northAlt < cur_alt) {
                    flow_dir = NORTH;
                    cur_alt = northAlt;
                }
                int westAlt = (cur.second > 0) ? alt[cur.first][cur.second-1] : INT_MAX;
                if (westAlt < cur_alt) {
                    flow_dir = WEST;
                    cur_alt = westAlt;
                }
                int eastAlt = (cur.second < W-1) ? alt[cur.first][cur.second+1] : INT_MAX;
                if (eastAlt < cur_alt) {
                    flow_dir = EAST;
                    cur_alt = eastAlt;
                }
                int southAlt = (cur.first < H-1) ? alt[cur.first+1][cur.second] : INT_MAX;
                if (southAlt < cur_alt) {
                    flow_dir = SOUTH;
                    cur_alt = southAlt;
                }
                
                LOG(flow_dir);
                
                switch(flow_dir) {
                case NORTH:
                    cur.first --;
                    break;
                case SOUTH:
                    cur.first++;
                    break;
                case EAST:
                    cur.second++;
                    break;
                case WEST:
                    cur.second--;
                    break;
                }
                
            
                
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
  
  
  //bool isOn = (n_sq & K) == n_sq;
      
//
    
  return;
    
}
  
