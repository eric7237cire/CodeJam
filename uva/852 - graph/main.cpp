// http://uva.onlinejudge.org/external/8/852.html
// Runtime: 0.016s
// Tag: DFS
 
 
// @BEGIN_OF_SOURCE_CODE
 
#include <iostream>
#include <cstdio>
#include <algorithm>
#include <cstring>
#include <string>
#include <cctype>
#include <stack>
#include <queue>
#include <list>
#include <vector>
#include <map>
#include <sstream>
#include <cmath>
#include <bitset>
#include <utility>
#include <set>
#include <numeric>
 
#define INF_MAX 2147483647
#define INF_MIN -2147483647
#define pi acos(-1.0)
#define N 1000
#define LL long long
 
#define For(i, a, b) for( int i = (a); i < (b); i++ )
#define Fors(i, sz) for( size_t i = 0; i < sz.size (); i++ )
#define Fore(it, x) for(typeof (x.begin()) it = x.begin(); it != x.end (); it++)
#define Set(a, s) memset(a, s, sizeof (a))
#define Read(r) freopen(r, "r", stdin)
#define Write(w) freopen(w, "w", stdout)
 
using namespace std;
 
char board [9 + 3] [9 + 3];
bool vis [9 + 3] [9 + 3];
int dr [] = {-1, 0, 1, 0};
int dc [] = {0, 1, 0, -1};
bool res;
 
void dfs (int x, int y, char c)
{
    if ( x < 0 || y < 0 || x == 9 || y == 9 || vis [x] [y] || board [x] [y] == c ) return;
    if ( board [x] [y] != '.' && board [x] [y] != c ) { res = false; return; }
 
    vis [x] [y] = true;
 
    for ( int i = 0; i < 4; i++ ) {
        dfs ( dr [i] + x, dc [i] + y, c );
    }
}
 
void fillItUp (int x, int y, char c)
{
    if ( x < 0 || y < 0 || x == 9 || y == 9 || board [x] [y] != '.' ) return;
 
    board [x] [y] = c;
 
    for ( int i = 0; i < 4; i++ ) {
        fillItUp ( dr [i] + x, dc [i] + y, c );
    }
}
 
void print ()
{
    for ( int i = 0; i < 9; i++ ) {
        for ( int j = 0; j < 9; j++ )
            printf ("%c", board [i] [j]);
        printf ("\n");
    }
}
 
int main ()
{
    //Read ("in.in");
 
    int testCase; scanf ("%d", &testCase);
 
    while ( testCase-- ) {
        for ( int i = 0; i < 9; i++ ) scanf ("%s", board [i]);
 
        for ( int i = 0; i < 9; i++ ) {
            for ( int j = 0; j < 9; j++ ) {
                if ( board [i] [j] == '.' ) {
                    Set (vis, false);
                    res = true; dfs (i, j, 'X');
                    if ( res ) fillItUp (i, j, 'X');
                    Set (vis, false);
                    res = true; dfs (i, j, 'O');
                    if ( res ) fillItUp (i, j, 'O');
                    //print ();
                }
            }
        }
 
        int black = 0;
        int white = 0;
 
        for ( int i = 0; i < 9; i++ ) {
            for ( int j = 0; j < 9; j++ ) {
                if ( board [i] [j] == 'X' ) black++;
                else if ( board [i] [j] == 'O' ) white++;
            }
        }
 
        printf ("Black %d White %d\n", black, white);
    }
 
    return 0;
}
 
// @END_OF_SOURCE_CODE