#include <cstdio>
#include <cstring>
#include <cctype>
#include <vector>
#include <map>
#include <cmath>
#include <math.h>
#include <cstdlib>
#include <algorithm>
#include <iostream>
#define N 10000002
using namespace std;
 
bool mark [N];
 
void sieve ()
{
    memset (mark, true, N);
 
    mark [0] = mark [1] = false;
 
    for ( int i = 4; i < N; i += 2 )
        mark [i] = false;
 
    for ( int i = 3; i * i <= N; i += 2 ) {
        if ( mark [i] ) {
            for ( int j = i * i; j < N; j += 2 * i )
                mark [j] = false;
        }
    }
}
 
void find_prime (int x)
{
    if ( mark [x - 2] ) {
        printf ("2 %d", x - 2);
        return;
    }
 
    for ( int i = 3; i < N; i += 2 ) {
        if ( mark [x - i] && mark [i] ) {
            printf ("%d %d", i, x - i);
            return;
        }
    }
}
 
int main ()
{
    sieve ();
 
    int n;
 
    while ( scanf ("%d", &n) != EOF ) {
 
        if ( n < 8 ) {
            printf ("Impossible.\n");
            continue;
        }
 
        if ( n % 2 ) {
            printf ("2 3 ");
            find_prime (n - 5);
        }
 
        else {
            printf ("2 2 ");
            find_prime (n - 4);
        }
 
        printf ("\n");
    }
 
    return 0;
}