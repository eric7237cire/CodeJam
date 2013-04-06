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
#define INT_MAX 2147483647
#define INT_MIN -2147483647
#define pi acos(-1.0)
#define N 1000000
#define LL long long
using namespace std;
 
char board [9 + 3] [9 + 3];
int freq [10 + 2];
int n;
 
void reset ()
{
    for ( int i = 0; i < 12; i++ ) {
        for ( int j = 0; j < 12; j++ ) board [i] [j] = '.';
    }
 
    memset (freq, 0, sizeof (freq));
}
 
void isSquare (int r, int c)
{
    int next_r = r + 1;
    int next_c = c + 1;
    int size = 0;
    bool complete;
 
    while ( next_r <= n && next_c <= n ) {
        size++;
        complete = true;
 
        if ( board [next_r - 1] [c] == 'H' || board [next_r - 1] [c] == '.' ) break;
        if ( board [r] [next_c - 1] == 'V' || board [r] [next_c - 1] == '.' ) break;
 
        for ( int i = c; i < c + size; i++ )
            if ( board [next_r] [i] != 'H' && board [next_r] [i] != 'B' ) complete = false;
 
        for ( int i = r; i < r + size; i++ )
            if ( board [i] [next_c] != 'V' && board [i] [next_c] != 'B' ) complete = false;
 
        if ( complete ) freq [size]++;
 
        next_r++;
        next_c++;
    }
}
 
int main ()
{
    int cases = 0;
    int asterisk = false;
 
    while ( scanf ("%d", &n) != EOF ) {
        reset ();
 
        int m;
        scanf ("%d", &m);
        getchar ();
 
        char line_info [50];
        char ch;
        int p, q;
 
        for ( int i = 0; i < m; i++ ) {
            gets (line_info);
            sscanf (line_info, "%c %d %d", &ch, &p, &q);
 
            if ( ch == 'H' ) {
                if ( board [p] [q] == 'V' ) board [p] [q] = 'B';
                else board [p] [q] = 'H';
            }
            else {
                if ( board [q] [p] == 'H' ) board [q] [p] = 'B';
                else board [q] [p] = 'V';
            }
        }
 
        for ( int i = 1; i < n; i++ ) {
            for ( int j = 1; j < n; j++ )
                if ( board [i] [j] == 'B' ) isSquare (i, j);
        }
 
        if ( asterisk ) printf ("\n**********************************\n\n");
        asterisk = true;
 
        printf ("Problem #%d\n\n", ++cases);
 
        if ( accumulate (freq, freq + 12, 0) == 0 ) printf ("No completed squares can be found.\n");
        else {
            for ( int i = 1; i < 12; i++ )
                if ( freq [i] ) printf ("%d square (s) of size %d\n", freq [i], i);
        }
    }
 
    return 0;
}
 
// @END_OF_SOURCE_CODE