#include <fstream>
#include <iostream>
#include <iomanip>
#include <vector>
#include <algorithm>
#include <limits>
#include <map>
#include <set>
#include <sstream>
#include <deque>
#include <queue>

#include <stdio.h>
#include <time.h>
#include <assert.h>
#include <stdlib.h>

#include "util.h"
#include <cstring>

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

  string dummy;
  getline(input, dummy);
  SHOW_TIME_BEGIN(g) 
  	
  for (int test_case = 0; test_case < T; ++test_case) {
      
    do_test_case(test_case, input);   
  }
      
  SHOW_TIME_END(g)
}

typedef pair<int, int> IntPair;
typedef deque<int> BaseNum;

deque<int> baseConv(const int K, const int base) {
	deque<int> conNum;

	int k = K;

	do {
		conNum.push_front(k % base);
		k /= base;
	}
	while (k != 0);

	return conNum;
}

int squares[11] = { 0, 1, 4, 9, 16, 25, 36, 49, 64, 81, 100 };

int getSq(const deque<int>& convNum) {
    int r = 0;
    for(BaseNum::const_iterator it = convNum.begin();
        it != convNum.end();
        ++it) {
        r += squares[ (*it) ];
    }
    
    return r;
}

class Node {
    
public:
    bool happy ;
    bool init;
        
    Node() : happy(false), init(false) { 
           
    }
};

ostream& operator<<(ostream& os, Node* node) {
    os << (int) node;
    os << node->happy;
    return os;
}

template<typename T> ostream& operator<<(ostream& os, const vector<T>& vect)
{
  cout << "Size [" << vect.size() << "] " ;
  
  typename std::vector<T>::const_iterator it;
  
  for(it = vect.begin(); 
    it != vect.end(); ++it) {
    cout << *it << ", ";   
  }
  cout << endl;
  return os;
}

template std::ostream& operator<<(std::ostream& os, const std::vector<Node*>& vect);

typedef vector<Node*> VecNode;

VecNode nodes[9];

enum {
    NORTH,
    SOUTH,
    EAST,
    WEST
};

enum {
    NE,
    NW,
    SE,
    SW
};

struct Light {
    
private:
    int NS_Duration;
    int EW_Duration;
    int NS_Start;
    int cycle;

public:
    Light(int ns, int es, int start) : NS_Duration(ns), EW_Duration(es) {
        cycle = NS_Duration + EW_Duration;
        NS_Start = start % cycle;
    }
    
    int nextNorth(int time) const {
        int t_mod = (time + cycle - NS_Start) % cycle;

        LOG_STR("T_mod nextNS " << t_mod << " Time " << time);        

        if (t_mod < NS_Duration) {
            return 1 + time;
        }
        
        //wait until next north / south light
        return 1 + time + (cycle - t_mod);
    }
    
    int nextEastWest(int time) const {
        int t_mod = (time + cycle - NS_Start) % cycle;

        LOG_STR("T_mod nextEW " << t_mod << " Time " << time);
        
        if (t_mod >= NS_Duration) {
            return 1 + time;
        }
        
        //wait until next north / south light
        return 1 + time + (NS_Duration - t_mod);
    }
};

struct Location {
    int row;
    int col;
    int corner;
};

int operator<(const Location& lhs, const Location& rhs) {
    
    if (lhs.row != rhs.row) {
        return lhs.row < rhs.row;
    }
    
    if (lhs.col != rhs.col) {
        return lhs.col < rhs.col;
    }
    
    return lhs.corner < rhs.corner;
}

int operator>(const Location& lhs, const Location& rhs) {
    
    if (lhs.row != rhs.row) {
        return lhs.row > rhs.row;
    }
    
    if (lhs.col != rhs.col) {
        return lhs.col > rhs.col;
    }
    
    return lhs.corner > rhs.corner;
}


int operator==(const Location& lhs, const Location& rhs) {
    return (lhs.row == rhs.row && lhs.col == rhs.col && lhs.corner ==rhs.corner);    
}

typedef pair<int, Location> TimeLocPair;

ostream& operator<<(ostream& os, const TimeLocPair& tl) {
	string corner;
	switch (tl.second.corner) {
case SW:
corner = "SW";
break;
case SE:
corner = "SE";
break;
case NW:
corner = "NW";
break;
case NE:
corner = "NE";
break;
}
    os << tl.second.row << ", " << tl.second.col << " Corner " << corner << " Time " << tl.first;
    return os;
}

void do_test_case(const int test_case, ifstream& input)
{
    SHOW_TIME_BEGIN(tc);
    LOG_ON();
    LOG_OFF();
    
    int N, M; //N = rows (# of roads), M = cols (# of roads)
    input >> N >> M;

    typedef vector<Light> VL;
    typedef vector<VL> ML;
    
    ML lights;
    
    //0th is northernmost
    for(int i = 0; i < N; ++i) {
        VL row;
        for(int i = 0; i < M; ++i) {
            
            int ns, ew, start;
            input >> ns >> ew >> start;
            Light l(ns, ew, start);
            row.push_back(l);
        }        
        
        lights.push_back(row);
    }
    
    //Coord is the intersection, so NE means SW of the NE block
    Location startingLoc;
    Location endingLoc;
    
    startingLoc.row = N - 1;
    startingLoc.col = 0;
    startingLoc.corner = SW;
    
    endingLoc.row = 0;
    endingLoc.col = M - 1;
    endingLoc.corner = NE;
    
    
    
    typedef priority_queue<TimeLocPair, vector<TimeLocPair>, greater<TimeLocPair> > Queue;
    
    Queue q;
    TimeLocPair start;
    start.second = startingLoc;
    start.first = 0;
    
    q.push(start);
    
    set<TimeLocPair> seen;

    while(!q.empty()) {
        const TimeLocPair timeLoc = q.top();

        q.pop();
        
//        LOG_ON();
        LOG_STR("Processing " << timeLoc << " Queue size " << q.size()  );
//        LOG_OFF();
        
        if (timeLoc.second == endingLoc) {
            cout << "Case #" << (1+test_case) << ": " << timeLoc.first << endl;
	    return;
        }
        
        const Light& light = lights[timeLoc.second.row][timeLoc.second.col];
        
        //go north ; crossing street
        if (timeLoc.second.corner == SE || timeLoc.second.corner == SW)  {

            int time = light.nextNorth(timeLoc.first);
		LOG_STR("Going north across street.  New time " << time);
            Location newLoc(timeLoc.second);
            
            newLoc.corner = timeLoc.second.corner == SE ? NE : NW; 
            
            TimeLocPair newTL;
            newTL.first = time;
            newTL.second = newLoc;
            
            q.push(newTL);
        }
        
        //go north walking accross block
        if ( timeLoc.second.row > 0 &&
            (timeLoc.second.corner == NE || timeLoc.second.corner == NW) )
        {
            int time = timeLoc.first + 2;
		LOG_STR("Going north across block.  New time " << time);
            Location newLoc(timeLoc.second);
            
            newLoc.corner = (timeLoc.second.corner == NE) ? SE : SW; 
            newLoc.row -= 1;
            
            TimeLocPair newTL;
            newTL.first = time;
            newTL.second = newLoc;
            
            q.push(newTL);
        }


        if (timeLoc.second.corner == NE || timeLoc.second.corner == NW)  {

            int time = light.nextNorth(timeLoc.first);
		LOG_STR("Going south across street.  New time " << time);
            Location newLoc(timeLoc.second);
            
            newLoc.corner = timeLoc.second.corner == NE ? SE : SW; 
            
            TimeLocPair newTL;
            newTL.first = time;
            newTL.second = newLoc;
            
            q.push(newTL);
        }
        
        if ( timeLoc.second.row < N - 1 &&
            (timeLoc.second.corner == SE || timeLoc.second.corner == SW) )
        {
            int time = timeLoc.first + 2;
		LOG_STR("Going south across block.  New time " << time);
            Location newLoc(timeLoc.second);
            
            newLoc.corner = (timeLoc.second.corner == SE) ? NE : NW; 
            newLoc.row += 1;
            
            TimeLocPair newTL;
            newTL.first = time;
            newTL.second = newLoc;
            
            q.push(newTL);
        }

        //Going east accross block
        if (timeLoc.second.col < M - 1 && 
            (timeLoc.second.corner == NE || timeLoc.second.corner == SE) ) {

            int time = timeLoc.first + 2;
		LOG_STR("Going east across block.  New time " << time);
            Location newLoc(timeLoc.second);
            
            newLoc.corner = timeLoc.second.corner == SE ? SW : NW; 
            newLoc.col += 1;
            
            TimeLocPair newTL;
            newTL.first = time;
            newTL.second = newLoc;
            
            q.push(newTL);
        }

        //go east ; crossing street
        if (timeLoc.second.corner == SW || timeLoc.second.corner == NW)  {
            int time = light.nextEastWest(timeLoc.first);
		LOG_STR("Going east across street.  New time " << time );
            Location newLoc(timeLoc.second);
            
            newLoc.corner = (timeLoc.second.corner == SW) ? SE : NE; 
            
            TimeLocPair newTL;
            newTL.first = time;
            newTL.second = newLoc;

//		LOG_STR("Pushing after east walking street " << newTL);
            
            q.push(newTL);
        }

	//Going west accross block
        if (timeLoc.second.col > 0 && 
            (timeLoc.second.corner == NW || timeLoc.second.corner == SW) ) {

            int time = timeLoc.first + 2;
		LOG_STR("Going west across block.  New time " << time);
            Location newLoc(timeLoc.second);
            
            newLoc.corner = timeLoc.second.corner == SW ? SE : NE; 
            newLoc.col -= 1;
            
            TimeLocPair newTL;
            newTL.first = time;
            newTL.second = newLoc;
            
            q.push(newTL);
        }

        //go west ; crossing street
        if (timeLoc.second.corner == SE || timeLoc.second.corner == NE)  {
            int time = light.nextEastWest(timeLoc.first);
		LOG_STR("Going west across street.  New time " << time );
            Location newLoc(timeLoc.second);
            
            newLoc.corner = (timeLoc.second.corner == SE) ? SW : NW; 
            
            TimeLocPair newTL;
            newTL.first = time;
            newTL.second = newLoc;


            
            q.push(newTL);
        }
        
        seen.insert(timeLoc);

    }
    
    cout << "Case #" << (1+test_case) << ": " << lights.size() << endl;
    return;
        
    
}
  
