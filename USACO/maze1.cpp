/*
ID: eric7231
PROG: maze1
LANG: C++
*/
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <set>
#include <vector>
#include <algorithm>
#include <cassert>
#include <iterator>
#include <sstream>
#include <bitset>
#include <cctype>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(uint k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(uint k=(a); k < (b); ++k)

#define pb push_back 
#define mp make_pair 

typedef vector<int> vi; 
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef pair<int,int> ii;
typedef pair<uint,uint> uu;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 

const ii NORTH = mp(-1, 0);
const ii EAST = mp(0, 1);
const ii SOUTH = mp(1, 0);
const ii WEST = mp(0, -1);

ii directions[4] = { NORTH, EAST, SOUTH, WEST };
//01
//02
//20
//10
struct Location {
    ii dir;
    int dirIndex;
    int row;
    int col;
};

ostream& operator<<(ostream& os, const Location& loc) {
    os << "(" << loc.dir.first << ", " << loc.dir.second << ") row " << loc.row << " col " << loc.col;   
}

Location getNewLoc(Location curLoc, const vector<string>& grid)
{
    int dRow = curLoc.dir.first;
    int dCol = curLoc.dir.second;
    
    Location nextLoc(curLoc);
    
    //turn col, -row
    int nextRow = curLoc.row + dRow;
    int nextCol = curLoc.col + dCol;
    
    if (nextRow < 0 || nextRow >= 10 ||
        nextCol < 0 || nextCol >= 10 ||
        grid[nextRow][nextCol] == '*') 
    {
        nextLoc.dir.first = curLoc.dir.second;
        nextLoc.dir.second = 
        -curLoc.dir.first;
        
        nextLoc.dirIndex++;
        if (nextLoc.dirIndex > 3)
            nextLoc.dirIndex = 0;
        
    } else {
        nextLoc.row += dRow;
        nextLoc.col += dCol;
    }
    
    return nextLoc;
}

uint getIndex(int row, int col, uint width)
{
    //Taking into account 2 extra rows and columns
    return (1+row) * (width + 2) + col+1;   
}
int main() {
    
	ofstream fout ("maze1.out");
    ifstream fin ("maze1.in");
	
    vector<string> grid(10);
    
    int W, H;
    fin >> W;
    fin >> H;
    
    //Add nodes for just outside of maze
    uint numNodes = (H+2)*(W+2);
    uvvi connected(numNodes, uvi(numNodes, 0));
    
    string lineStr;
    FOR(line, 0, 2 * H + 1) 
    {
        
        fin >> lineStr;
        cout << "Line " << line << " " << lineStr << endl;
        if (line % 2 == 0) {
            //0 -1 0
            //2 0 1
            //4 1 2
            //6 2 3
            int rowAbove = line / 2;
            int rowBelow = rowAbove - 1;
            FOR(col, 0, W) {
                char wallCh = lineStr[1+2*col];
                if (wallCh == '-')
                    continue;
                
                assert(wallCh == ' ');
                connected[ getIndex(rowBelow, col, W) ]
                [ getIndex(rowAbove, col, W) ] = 1;
                connected
                [ getIndex(rowAbove, col, W) ]
                [ getIndex(rowBelow, col, W) ]= 1;
            }
        } else {
            int row = line / 2;
            FOR(col, 0, W) {
                char wallCh = lineStr[2 * col];
                if (wallCh == '-')
                    continue;
                
                assert(wallCh == '|');
                
                connected[ getIndex(row, col-1, W) ]
                [ getIndex(row, col, W) ] = 1;
                connected
                [ getIndex(row, col, W) ]
                [ getIndex(row, col-1, W) ]= 1;
                
            }
        }
    }
  
   
    fout << 0 << endl;
    
    
	return 0;
}
