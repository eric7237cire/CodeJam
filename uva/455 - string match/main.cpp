// http://uva.onlinejudge.org/external/4/455.html
// Runtime : 0.012
// Tag: string, sub-string, compare
 
 
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
#define LL unsigned long long
using namespace std;
 
string str;
 
bool isRepeat (int l)
{
    int round = str.length () / l;
 
    if ( round * l != str.length () ) return false;
 
    string sub = str.substr (0, l);
    int start = 0;
 
    while ( round-- ) {
        if ( sub != str.substr (start, l) ) return false;
        start += l;
    }
 
    return true;
}
 
int main ()
{
    int testCase;
    scanf ("%d", &testCase);
    bool blank = false;
 
    while ( testCase-- ) {
        cin >> str;
        int len = str.length () / 2;
 
        if ( blank ) printf ("\n"); blank = true;
        bool printed = false;
 
        for ( int i = 0; i < len; i++ ) {
            if ( isRepeat (i + 1) ) { printf ("%d\n", i + 1); printed = true; break; }
        }
 
        if ( !printed ) printf ("%d\n", str.length ());
    }
 
    return 0;
}
 
// @END_OF_SOURCE_CODE