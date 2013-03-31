// http://uva.onlinejudge.org/external/116/11678.html
 
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
#define pi acos(-1.0)
#define N 1000000
using namespace std;
 
int x [100000 + 10];
int y [100000 + 10];
 
int main ()
{
    int a, b;
 
    while ( scanf ("%d %d", &a, &b) ) {
        if ( a == 0 && b == 0 ) break;
 
        memset (x, 0, sizeof (x));
        memset (y, 0, sizeof (y));
 
        int input;
 
        for ( int i = 0; i < a; i++ ) {
            scanf ("%d", &input);
            x [input]++;
        }
        for ( int i = 0; i < b; i++ ) {
            scanf ("%d", &input);
            y [input]++;
        }
 
        int cntA = 0;
        int cntB = 0;
 
        for ( int i = 1; i <= 100000; i++ ) {
            if ( x [i] && !y [i] ) cntA++;
            if ( y [i] && !x [i] ) cntB++;
        }
 
        printf ("%d\n", min (cntA, cntB));
    }
 
    return 0;
}