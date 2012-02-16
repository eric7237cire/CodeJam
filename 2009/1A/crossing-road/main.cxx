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

struct Corner {
private:
    bool north;
    bool east;
    
    Corner(bool _isNorth, bool _isEast) : north(_isNorth), east(_isEast) {}
        
public:
    Corner flipEW() const {
        return Corner(north, !east);
    }
    
    Corner flipNS() const {
        return Corner(!north, east);
    }
    
    bool isNorth() const {
        return north;
    }
    
    bool isEast() const {
        return east;
    }
    
    int operator==(const Corner& rhs) const {
        return north == rhs.north && east == rhs.east;
    }
    
    int operator<(const Corner& rhs) const {
        return toInt() < rhs.toInt();
    }
    
    int toInt() const {
        return (east ? 1 : 0) + (north ? 2 : 0);
    }
    
    static Corner NE, NW, SE, SW;    
};

Corner Corner::NE(true, true);
Corner Corner::NW(true, false);
Corner Corner::SE(false, true);
Corner Corner::SW(false, false);
    
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
    Corner corner;
    
    Location(int r, int c, Corner _corner) : row(r), col(c), corner(_corner) {}
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

void do_test_case(const int test_case, ifstream& input)
{
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
            
            row.push_back( Light(ns, ew, start) );
        }
        lights.push_back(row);
    }
    
    //Coord is the intersection, so NE means SW of the NE block    
    Location endingLoc(0, M-1, Corner::NE);
        
    typedef priority_queue<TimeLocPair, vector<TimeLocPair>, greater<TimeLocPair> > Queue;
    
    Queue q;
    
    q.push(TimeLocPair(0, Location(N - 1, 0, Corner::SW)));
    
	typedef set<Location> LocSet;
	LocSet seen;

    while(!q.empty()) {
        const TimeLocPair timeLoc = q.top();

        q.pop();
   
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
        newLoc.corner = timeLoc.second.corner.flipNS();
        
        q.push(TimeLocPair(light.nextNorth(timeLoc.first), newLoc) );
        
        //go east/west ; crossing street
        Location newLocEW(timeLoc.second);            
        newLocEW.corner = timeLoc.second.corner.flipEW(); 
        
        q.push(TimeLocPair(light.nextEastWest(timeLoc.first), newLocEW) );
    
        //go north walking across block
        if ( timeLoc.second.row > 0 && timeLoc.second.corner.isNorth() )
        {
            q.push(
                TimeLocPair(timeLoc.first + 2, 
                    Location(timeLoc.second.row - 1, timeLoc.second.col, timeLoc.second.corner.flipNS()) )
                );
        }

        //south across block
        if ( timeLoc.second.row < N - 1 && !timeLoc.second.corner.isNorth() )
        {
            q.push(
                TimeLocPair(timeLoc.first + 2, 
                    Location(timeLoc.second.row + 1, timeLoc.second.col, timeLoc.second.corner.flipNS()) )
                );
        }

        //Going east across block
        if ( timeLoc.second.col < M - 1 && timeLoc.second.corner.isEast() ) {
            q.push(
                TimeLocPair(timeLoc.first + 2, 
                    Location(timeLoc.second.row, timeLoc.second.col + 1, timeLoc.second.corner.flipEW()) )
                );
        }
        
        
	//Going west accross block
        if (timeLoc.second.col > 0 &&  !timeLoc.second.corner.isEast() ) {
            q.push(
                TimeLocPair(timeLoc.first + 2, 
                    Location(timeLoc.second.row, timeLoc.second.col - 1, timeLoc.second.corner.flipEW()) )
                );
        }

        
            
    }
    
    cout << "Case #" << (1+test_case) << ": " << lights.size() << endl;
    return;
        
    
}
  
