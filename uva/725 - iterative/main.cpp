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
#include <bitset>
#include <utility>
#include <set>
#define pi acos(-1.0)
#define N 1000000
using namespace std;
 
bool avail [12];
char a [10 * 9 * 8 * 7 * 6 + 100] [5 + 3];
char temp [5 + 3];
int length;
bool printed;
 
void generateAll (int n)
{
    if ( n == 5 ) {
        temp [5] = 0;
        strcpy (a [length], temp);
        length++;
        return;
    }
 
    for ( int i = 0; i <= 9; i++ ) {
        if ( avail [i] ) {
            avail [i] = false;
            temp [n] = i + '0';
            generateAll (n + 1);
            avail [i] = true;
        }
    }
}
 
void check_print (char *p, int b, int n)
{
    bool available [10 + 2];
    memset (available, false, sizeof(available));
 
    char pch [10];
    sprintf (pch, "%d", b);
 
    for ( int i = 0; i < 5; i++ ) {
        available [p [i] - '0'] = true;
        available [pch [i] - '0'] = true;
    }
 
    for ( int i = 0; i < 10; i++ ) {
        if ( !available [i] ) return;
    }
 
    printed = true;
    printf ("%s / %s = %d\n", pch, p, n);
 
}
 
int main ()
{
    memset (avail, true, sizeof(avail));
    length = 0;
    generateAll (0);
 
    int n;
    bool blank = false;
 
    while (scanf ("%d", &n) && n) {
        printed = false;
        if ( blank )
            printf ("\n");
        blank = true;
        for ( int i = 0; i < 30240; i++ ) {
            int num = atoi (a [i]);
            if ( num * n < 100000 )
                check_print (a [i], num * n, n);
        }
 
        if ( !printed )
            printf ("There are no solutions for %d.\n", n);
    }
 
    return 0;
}
 
// @END_OF_SOURCE_CODE