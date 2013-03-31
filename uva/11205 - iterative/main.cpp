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

int nBits;
int nSym;

int sym[100];
bool taken[1 << 15];

int main() {

	int T;
	scanf("%d", &T);

	while(T--)
	{
        scanf("%d%d", &nBits, &nSym);
        
        FOR(s, 0, nSym)
        {
            int theSym = 0;
            FOR(p, 0, nBits)
            {
                int bit;
                scanf("%d", &bit);
                if (bit == 0)
                    continue;
                theSym |= 1 << p;
            }
            sym[s] = theSym;
        }
        
        

        int minLen = nBits + 10;

        for(int ss = (1 << nBits) - 1; ss >= 0; --ss)
        {
            int popCount = 0;
            for(int bit = 0; bit < nBits; ++bit)
            {
                popCount += ( 1 << bit & ss ) != 0;   
            }
            if (popCount >= minLen)
                continue;
            
            memset( taken, 0, sizeof taken );
            
            bool ok = true;
            
            FOR(s, 0, nSym)
            {
                int read = ss & sym[s];
                if (taken[read])
                {
                    ok = false;
                    break;
                }
                
                taken[read] = true;
            }
            
            if (ok)
            {
               minLen = popCount;
            }
        }
        
        printf("%d\n", minLen);
    }
		
	return 0;
}
