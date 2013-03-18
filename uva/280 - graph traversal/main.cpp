// @BEGIN_OF_SOURCE_CODE
 
#include <iostream>
#include <algorithm>
#include <cstring>
#include <string>
#include <cctype>
#include <vector>
#include "stdio.h"
#include <map>
#include <set>
#include <math.h>
#define For(a) for ( i = 0; i < a; i++ )
#define Rep(a, b) for ( i = a; i <= b; i++ )
#define N 1000000
using namespace std;
 
enum related_color {gray, white, black};
 
bool matrix [100 + 2] [100 + 2];
bool related_vertics [100 + 2];
related_color color [100 + 2];
int number_of_vertics_n;
 
void reset_all (int n)
{
    for ( int i = 0; i < n; i++ ) {
        for ( int j = 0; j < n; j++ )
            matrix [i] [j] = false;
    }
}
 
void dfs (int u)
{
    color [u] = gray;
 
    for ( int i = 0; i < number_of_vertics_n; i++ ) {
        if ( matrix [u] [i] ) {
            related_vertics [i] = true;
            if ( color [i] == white ) {
                related_vertics [i] = true;
                dfs (i);
            }
        }
    }
 
    color [u] = black;
}
 
int main ()
{
    while ( scanf ("%d", &number_of_vertics_n) && number_of_vertics_n ) {
        reset_all (number_of_vertics_n);
 
        int starting_vertex;
 
        while ( scanf ("%d", &starting_vertex) && starting_vertex ) {
            int series_of_edges;
            while ( scanf ("%d", &series_of_edges) && series_of_edges ) {
                matrix [starting_vertex - 1] [series_of_edges - 1] = true;
            }
        }
 
        int testCase;
        scanf ("%d", &testCase);
 
        while ( testCase-- ) {
            int query;
            scanf ("%d", &query);
 
            for ( int i = 0; i < number_of_vertics_n; i++ ) {
                related_vertics [i] = false;
                color [i] = white;
            }
 
            dfs (query - 1);
 
            vector <int> v;
 
            for ( int i = 0; i < number_of_vertics_n; i++ ) {
                if ( !related_vertics [i] )
                    v.push_back (i + 1);
            }
 
            printf ("%d", v.size ());
 
            for ( unsigned int i = 0; i < v.size (); i++ )
                printf (" %d", v [i]);
 
            printf ("\n");
        }
 
        /*
        for ( int i = 0; i < number_of_vertics_n; i++ ) {
            for ( int j = 0; j < number_of_vertics_n; j++ )
                related_vertics [j] = false;
 
            dfs (i);
 
            for ( int j = 0; j < number_of_vertics_n; j++ ) {
                if ( related_vertics [j] )
                    matrix [i] [j] = true;
            }
        }
        */
    }
 
    return 0;
}
 
// @END_OF_SOURCE_CODE