/*
ID: eric7231
PROG: rockers
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
#include <iomanip>
#include <cctype>
#include <limits>
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(int k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (int) (b); ++k)

#define pb push_back 
#define mp make_pair 

typedef vector<int> vi; 
typedef vector<double> vd;
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef vector<vd> vvd;
typedef pair<int,int> ii;
typedef pair<uint,uint> uu;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 

bool debug = false;


int main() {
    
	ofstream fout ("rockers.out");
    ifstream fin ("rockers.in");

    int N; int T; //cd length
    int M; //# of cds
    
    fin >> N >> T >> M;
    
    vi songLen(N+1, 0);
    
    FORE(i, 1, N)
        fin >> songLen[i];
    
    int limit[21][21][21];
    FOR(i,0,21) FOR(j,0,21) FOR(k,0,21) limit[i][j][k] = 0;
    
    int ans = 0;
        
    FOR(cdLimit, 0, M) FORE(timeUsed, 0, T)
        FOR(lastSong, 0, N)
        FORE(newSong, lastSong+1, N)
    {
		int withNewSong = limit[cdLimit][timeUsed][lastSong] + 1;

        if (timeUsed + songLen[newSong] <= T)
        {            
            int& previous = limit[cdLimit][timeUsed + songLen[newSong]][newSong];
            
            if (withNewSong
                > previous)
            {
                previous = withNewSong; 
            }
		} else if (cdLimit + 1 <= M) 
		{

            int& usingNewCd = limit[cdLimit+1][songLen[newSong]][newSong];
            if (withNewSong > usingNewCd)
                usingNewCd = withNewSong;
        }
            
            
        
        
        ans = max(ans, limit[cdLimit][timeUsed][newSong]);
    }
    //limit[0][4][1] == 1
	//limit[1][3][2] == 2
    fout << ans << endl;
    
    return 0;
}
