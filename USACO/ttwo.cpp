/*
ID: eric7231
PROG: ttwo
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

int main() {
    
	ofstream fout ("ttwo.out");
    ifstream fin ("ttwo.in");
	
    vector<string> grid(10);
    
    Location farmer;
    Location cow;
    
    farmer.dir = NORTH;
    cow.dir = NORTH;
    farmer.dirIndex = 0;
    cow.dirIndex = 0;
    
    FOR(i, 0, 10)
    {
        fin >> grid[i];
        size_t fCol = grid[i].find('F', 0);
        if (fCol != string::npos) {
            farmer.row = i;
            farmer.col = fCol;
            grid[i][fCol]='.';
        }
        
        
        size_t cCol = grid[i].find('C', 0);
        
        if (cCol != string::npos) {
            cow.row = i;
            cow.col = cCol;
            grid[i][cCol]='.';
        }
        
    }
    
    
    bool visited[10][10][4][10][10][4] = { 0 };
    
    
    uint minutes = 0;
    while(!visited[cow.row][cow.col][cow.dirIndex] [farmer.row][farmer.col][farmer.dirIndex] )
    {
     /*
        cout << "Min " << minutes << " Cow loc " << cow << endl; 
        cout <<  "Farmer loc " << farmer << endl;*/
        
        visited[cow.row][cow.col][cow.dirIndex][farmer.row][farmer.col][farmer.dirIndex]=true; 
        
        
        if (cow.row == farmer.row && cow.col == farmer.col) {
            fout << minutes << endl;
            cout << minutes << endl;
            return 0;
            break;
        }
        
        cow = getNewLoc(cow, grid);
        farmer = getNewLoc(farmer, grid);
        
        ++minutes;
        /*
        char C = 'C';
        if (cow.dir == NORTH)
            C = '^';
        else if (cow.dir == EAST)
            C = '>';
        else if (cow.dir == WEST)
            C = '<';
        else if (cow.dir == SOUTH)
            C = 'v';
        grid[cow.row][cow.col] = C;
        grid[farmer.row][farmer.col] = 'F';
        
        fout << minutes << endl;
        FOR(rr, 0, 10)
            fout << grid[rr] << endl;
        fout << endl;
        
        grid[cow.row][cow.col] = '.';
        grid[farmer.row][farmer.col] = '.';*/
    }
   
    fout << 0 << endl;
    
    
	return 0;
}
