//STARTCOMMON
#include <cmath>
#include <vector>
#include "stdio.h" 
#include <limits>
#include <cassert>
#include <string>
#include <iostream>
#include <cstring>
#include <algorithm>

using namespace std;

#define mp make_pair 
#define pb push_back 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define all(c) (c).begin(),(c).end() 
#define MIN(a,b) ((a)<(b)?(a):(b))
#define MAX(a,b) ((a)<(b)?(b):(a))

typedef unsigned int uint;
typedef long long ll;
typedef unsigned long long ull;

#define FORE(k,a,b) for(int k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (b); ++k)

typedef vector<int> vi; 
typedef vector<double> vd;
typedef vector<bool> vb;
typedef vector<vb> vvb;
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef vector<vd> vvd;
typedef pair<int,int> ii;
typedef pair<uint,uint> uu;
typedef vector<ii> vii;
typedef vector<vii> vvii;


const double tolerance = 0.000002;
template<typename T> 
int cmp(T a, T b, T epsilon = tolerance)
{
	T dif = a - b;
	if (abs(dif) <= epsilon)
	{
		return 0;
	}
	
	if (dif > 0)
	{
		return 1; //a > b
	}
	
	return -1;
}  


//STOPCOMMON

#define MAX_N 100010
char T[MAX_N], P[MAX_N];

int b[MAX_N], n, patLen;
// T = text, P = pattern
// b = back table, n = length of T, m = length of P
void kmpPreprocess() 
{
    memset(b, 0, sizeof b);
    
    // call this before calling kmpSearch()
    int i = 0, j = -1; b[0] = -1;
    // starting values

    while (i < patLen) 
    {
        // pre-process the pattern string P
        while (j >= 0 && P[i] != P[j])
            j = b[j]; // if different, reset j using b

        i++; j++;
        // if same, advance both pointers
        b[i] = j;
    }
}

/*
returns maximum number of characters matched
*/
int kmpSearch() 
{
    int ret = 0;
    
    // this is similar as kmpPreprocess(), but on string T
    int i = 0, j = 0;
    // starting values

    while (i < n) 
    {
        // search through string T
        while (j >= 0 && T[i] != P[j]) j = b[j]; // if different, reset j using b
        
        i++; j++;
       // printf("kmp i=%d j=%d\n", i, j);
       //KMP modification.  If all of remaining pattern matches, then count it
        int strLenRem = n - i - 1;
        if (strLenRem <= j)
            ret = max(ret, j);
        // if same, advance both pointers
        if (j == patLen) {
            // a match found when j == m
            //printf("P is found at index %d in T\n", i - j);
            j = b[j];
            // prepare j for the next possible match
        }
    }
    
    return ret;
}

int main() {

	
    
	while( gets(T) )
	{
		n = patLen = strlen(T);
		
		for(int i = n-1; i >= 0; --i)
            P[i] = T[n-i-1];
        
        P[n] = '\0';
        
        //cout << T << endl;
        //cout << P << endl;
        
        kmpPreprocess();
        int matching = kmpSearch();
        
        int neededChars = n - matching;
        //fprintf(stderr, "len %d matching %d\n", n, matching);     
        
        cout << T;
        if (matching < n)
            cout << &P[matching];
        cout << endl;
       #if 0
        string check;
        check += T;
        check += &P[matching];
        
        string revCheck(check);
        reverse(all(revCheck));
        //cout << check << endl;
        //cout << revCheck << endl;
        assert( check == revCheck );
		#endif
	}
	return 0;
}
