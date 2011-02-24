#include <fstream>
#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <limits>
#include <string>
#include <cstring>
#include <bitset>
#include <stdio.h>
#include <time.h>
#include <assert.h>
#include <stdlib.h>
#include <boost/numeric/conversion/bounds.hpp>
#include <boost/limits.hpp>
#include <boost/smart_ptr.hpp>
#include <cmath>
#include "util.h" 


using namespace std;


void do_test_case(int test_case, ifstream& input);

int main(int argc, char** args)
{
  if (argc < 2) {
    cerr << "Usage: <exe> input_file" << endl;
    return -1;
  }
  
  ifstream input;
  input.open(args[1]);
  
  int T;
  input >> T;

  SHOW_TIME_BEGIN(g) 
  	
  for (int test_case = 0; test_case < T; ++test_case) 
  {
    try {
      //SHOW_TIME_BEGIN(test_case)
      do_test_case(test_case, input);
      //SHOW_TIME_END(test_case)
    } catch(...) {
      error("Error exception caught\n"); 
    }
  }
  
  SHOW_TIME_END(g)
}

template <typename T> bool is_between(T n, T b1, T b2) {
  if (n >= b1 && n <= b2) {
    return true;
  }
  
  if (n >= b2 && n <= b1) {
    return true;
  }
  
  return false;
}

typedef pair<int, int> Point;
typedef pair< Point, Point > Segment;

int compare_x(const Segment& s1, const Segment& s2) {
  assert(s1.first.first == s1.second.first);
  assert(s2.first.first == s2.second.first);
  
  if (s1.first.first == s2.first.first) {
    return s1.first.second < s2.first.second;  
  } 
  return s1.first.first < s2.first.first;  
}

const int LIMIT = 100;

/*

-2 -1 is row/col -2
1 2 is row/col 2
0 -1 

*/
int get_row_or_col(int coord1, int coord2) 
{
  return LIMIT + (abs(coord1) > abs(coord2) ? coord1 : coord2);  
}


void do_test_case(int test_case, ifstream& input)
{
 
  unsigned int L;
  input >> L;
  
  int cur_x = 0;
  int cur_y = 0;
  int last_x = 0;
  int last_y = 0;
  unsigned int cur_direction = 0;
  //north 0, 1
  //west -1, 0
  //south 0, -1
  //east 1, 0
  typedef pair<int, int> Point;
  typedef pair< Point, Point > Segment;
  
  vector< Point > directions;
  
  directions.push_back(make_pair(0, 1));
  directions.push_back(make_pair(-1, 0));
  directions.push_back(make_pair(0, -1));
  directions.push_back(make_pair(1, 0));
  
  
  vector<Segment > segments;
  
  vector< vector<int> > horz(2 * LIMIT + 1);  //vec[0] is a list of all x coords in first row
  vector< vector<int> > vert(2 * LIMIT + 1);  //vec[0] is a list of all y coords in first column
  
  
  info("what\n");
  for(unsigned int i=0; i<L; ++i) {
    unsigned int T;
    string s;
    input >> s >> T;
    if (s == "S") {
      break;
    }
    info("S: %s, T: %d\n", s.c_str(), T);
    for(unsigned int t = 0; t < T; ++t) {
      for(string::const_iterator it = s.begin(); it != s.end(); ++it) {  
        switch(*it) {
        case 'L':
          cur_direction += 1;
          cur_direction = (cur_direction > 3 ? 0 : cur_direction);
          break;
        case 'R':
          cur_direction = (cur_direction == 0 ? 3 : cur_direction - 1);
          break;
        case 'F':
          debug("Last x %d, last y %d, cur x %d cur y %d, direction %d %d, cur_dir %d\n", last_x, last_y, cur_x, cur_y, directions[cur_direction].first, directions[cur_direction].second, cur_direction); 
          last_x = cur_x;
          last_y = cur_y;
          cur_x += directions[cur_direction].first;
          cur_y += directions[cur_direction].second;
          Point from = make_pair<int, int>(last_x, last_y);
          Point to = make_pair<int, int>(cur_x, cur_y);
          if (cur_x == last_x) {
            int row = get_row_or_col(cur_y, last_y);
            info("Adding row %d at %d\n", row, cur_x);
            horz[row].push_back(cur_x);
          }
          if (cur_y == last_y) {
            int col = get_row_or_col(cur_x, last_x);
            info("Adding col %d at %d\n", col, cur_y);
            vert[col].push_back(cur_y);
          }
          segments.push_back(make_pair<Point, Point>(from, to));
          info("%d, %d to %d, %d\n", segments.rbegin()->first.first, segments.rbegin()->first.second, segments.rbegin()->second.first, segments.rbegin()->second.second);
          break;
        }
      }
    }
  }
  
  set<pair<int, int> > pockets;
  
  for(unsigned int r = 0; r < horz.size(); ++r) {
    vector<int>& x_vals = horz[r];
    debug("Row %d\n", r);
    //cout << "X vals " << x_vals;
    sort(x_vals.begin(), x_vals.end());
    assert(x_vals.size() % 2 == 0);
    for(unsigned int x = 1; (x+1) < x_vals.size(); x+=2) {
      int start_outside_x = x_vals[x] ;
      int end_outside_x = x_vals[x+1] ;
      info("Outside row %d is between %d %d\n", r - LIMIT, start_outside_x, end_outside_x);
      for (int outside_x = start_outside_x; outside_x < end_outside_x; ++outside_x) {
        int col = get_row_or_col(outside_x, outside_x+1) - LIMIT;
        info("Adding %d %d\n", r-LIMIT, col);
        pockets.insert(make_pair(r-LIMIT, col));  
      }
    }
  }
  
  for(unsigned int c = 0; c < horz.size(); ++c) {
    vector<int>& y_vals = vert[c];
    debug("Col %d\n", c);
    //cout << "Y vals " << y_vals;
    sort(y_vals.begin(), y_vals.end());
    assert(y_vals.size() % 2 == 0);
    for(unsigned int y = 1; (y+1) < y_vals.size(); y+=2) {
      int start_outside_y = y_vals[y] ;
      int end_outside_y = y_vals[y+1] ;
      info("Outside col %d is between %d %d\n", c-LIMIT, start_outside_y, end_outside_y);
      for (int outside_y = start_outside_y; outside_y < end_outside_y; ++outside_y) {
        int row = get_row_or_col(outside_y, outside_y + 1) - LIMIT;        
        info("Adding %d %d\n", row, c-LIMIT);
        pockets.insert(make_pair(row, c-LIMIT));  
      }
    }
  }
  
  //print_cont( cout, pockets );
  #if 0
    for(int c = 0; c < vert.size(); ++c) {
      
  //for(int y = -LIMIT; y <= LIMIT; ++y) {
  for(int y = 0; y <= 6; ++y) {
    vector<Segment> row;
    //find all segments that intersect this y
    debug("Checking y %d\n", y);
    for(vector<Segment>::const_iterator it = segments.begin(); it != segments.end(); ++it)
    {
      if (it->first.first != it->second.first) {
        continue;
      }
      debug("Segment (%d, %d) to (%d, %d)\n", it->first.first, it->first.second, it->second.first, it->second.second );
      
      if (is_between(y, it->first.second, it->second.second) &&
          is_between(y+1, it->first.second, it->second.second)
        ) {
        info("Found matching segment!\n");
        row.push_back(*it);
      } 
    }
    
    int size_b = row.size();
    sort(row.begin(), row.end(), compare_x);
    assert(row.size() == size_b);
    
    assert(row.size() % 2 == 0);
    unsigned int poly_area = 0;  
    for(unsigned int i = 0; (i + 1) < row.size(); ++i) {
      info("%d\n", i);
      assert(i+1 < row.size());
      poly_area = row[i+1].first.first - row[i].first.first;
      debug("Poly area is now %d\n", poly_area);
    }
    
    if (row.size() > 0) {
      pocket_area += row.rbegin()->first.first - row[0].first.first - poly_area;
    }
    
    debug("Pocket area is now %d\n", pocket_area);
    
           
  }
  #endif
  printf("Case #%d: %d\n", test_case+1, pockets.size());
   
  return;
    
}
  
