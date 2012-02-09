#include <iostream>
#include <fstream>
#include <vector>
#include <set>
#include <queue>

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
  	
  for (int test_case = 0; test_case < T; ++test_case) {      
    do_test_case(test_case, input);   
  }
}

enum {
    NE,
    NW,
    SE,
    SW
};

int flipNS(int corner) {
    switch(corner) {
    case NE: return SE;
    case NW: return SW;
    case SE: return NE;
    case SW: return NW;
    }    
    return -1;
}

int flipEW(int corner) {
    switch(corner) {
    case NE: return NW;
    case NW: return NE;
    case SE: return SW;
    case SW: return SE;
    }    
    return -1;
}

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
    
    int next(int time, bool isNS) const {
        int t_mod = (time + cycle - NS_Start) % cycle;

        LOG_STR("T_mod nextEW " << t_mod << " Time " << time);
        
        if (isNS) {
            if (t_mod < NS_Duration) {
                return 1 + time;
            }
        
            //wait until next north / south light
            return 1 + time + (cycle - t_mod);
        }
        
        //east / west
        if (t_mod >= NS_Duration) {
            return 1 + time;
        }
        
        //wait until next north / south light
        return 1 + time + (NS_Duration - t_mod);
    }
    
    int nextNorth(int time) const {
        return next(time, true);
    }
    
    int nextEastWest(int time) const {
        return next(time, false);
    }
};

struct Location {
    int row;
    int col;
    int corner;
    
    Location(int r, int c, int corn) : row(r), col(c), corner(corn) {}
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
    Location endingLoc(0, M-1, NE);
        
    typedef priority_queue<TimeLocPair, vector<TimeLocPair>, greater<TimeLocPair> > Queue;
    
    Queue q;
    TimeLocPair start(0, Location(N - 1, 0, SW));
        
    q.push(start);
    
	typedef set<Location> LocSet;
	LocSet seen;

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

        if (seen.find(timeLoc.second) != seen.end()) {
            continue;
        }

        seen.insert(timeLoc.second);
        
        const Light& light = lights[timeLoc.second.row][timeLoc.second.col];
        
        //crossing street (north / south)
        Location newLoc(timeLoc.second);        
        newLoc.corner = flipNS(timeLoc.second.corner);
        
        q.push(TimeLocPair(light.nextNorth(timeLoc.first), newLoc) );
    
        //go north walking across block
        if ( timeLoc.second.row > 0 &&
            (timeLoc.second.corner == NE || timeLoc.second.corner == NW) )
        {
            q.push(
                TimeLocPair(timeLoc.first + 2, 
                    Location(timeLoc.second.row - 1, timeLoc.second.col, flipNS(timeLoc.second.corner) )
                ));
        }

        //south across block
        if ( timeLoc.second.row < N - 1 &&
            (timeLoc.second.corner == SE || timeLoc.second.corner == SW) )
        {
            q.push(
                TimeLocPair(timeLoc.first + 2, 
                    Location(timeLoc.second.row + 1, timeLoc.second.col, flipNS(timeLoc.second.corner) )
                ));
        }

        //Going east across block
        if (timeLoc.second.col < M - 1 && 
            (timeLoc.second.corner == NE || timeLoc.second.corner == SE) ) {
            q.push(
                TimeLocPair(timeLoc.first + 2, 
                    Location(timeLoc.second.row, timeLoc.second.col + 1, flipEW(timeLoc.second.corner) )
                ));
        }
        
        
	//Going west accross block
        if (timeLoc.second.col > 0 && 
            (timeLoc.second.corner == NW || timeLoc.second.corner == SW) ) {
            q.push(
                TimeLocPair(timeLoc.first + 2, 
                    Location(timeLoc.second.row, timeLoc.second.col - 1, flipEW(timeLoc.second.corner) )
                ));
        }

        //go east/west ; crossing street
        Location newLocEW(timeLoc.second);            
        newLocEW.corner = flipEW(timeLoc.second.corner); 
        
        q.push(TimeLocPair(light.nextEastWest(timeLoc.first), newLocEW) );
            
    }
    
    cout << "Case #" << (1+test_case) << ": " << lights.size() << endl;
    return;
        
    
}
  
