/*
ID: eric7231
PROG: humble
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
#include <iomanip>
#include <iterator>
#include <sstream>
#include <bitset>
#include <cctype>
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(uint k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(uint k=(a); k < (b); ++k)

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


uint getNext( const uvi& sequence, uint lastIndex, uint prime) 
{
        int idxMin = lastIndex;
        int idxMax = sequence.size() - 1;
        
        ull last = *sequence.rbegin();
        
        //printf("getNext min %d max %d last %d lastindex %d\n ",idxMin,idxMax, last,lastIndex);
        
        if ( (sequence[idxMin] * prime) > last)
            return idxMin;

        // prime * idxMin always lower, prime * idxMax always higher
        while (true) {
            int idxMid = idxMin + (idxMax - idxMin) / 2;

            ull num = (ull) sequence[idxMid] * prime;

            if (num > last) {
                idxMax = idxMid;
            } else {
                idxMin = idxMid;
            }

            if (idxMax - idxMin <= 1)
                break;
        }
        
        return idxMax;
}


int main() {
    
	ofstream fout ("humble.out");
    ifstream fin ("humble.in");
	
    uint K, N;
    
    fin >> K >> N;
        

    uvi primes;
    uint prime;
    FOR(k, 0, K) 
    {
        fin >> prime;
        primes.pb(prime);
    }
    
    uvi lastIndex(K, 0);
    
    uvi sequence(1, 1);
    
    FORE(n, 0, N)
    {
        uint last = sequence[sequence.size() - 1];
        uint pIndex = getNext(sequence, lastIndex[0], primes[0]);
        //cout << "PIndex k0 " << pIndex << endl;
        lastIndex[0] = pIndex;
        uint minNext = sequence[pIndex] * primes[0]; 
        
        FOR(k, 1, K) {
            pIndex = getNext(sequence, lastIndex[k], primes[k]);
           // cout << "PIndex k " << pIndex << endl;
            uint nextForPrime = sequence[pIndex] * primes[k];
            lastIndex[k] = pIndex;
            assert(nextForPrime > last);
            //printf( "Next prime %d = %d \n", primes[k], nextForPrime);
            minNext = min(minNext, nextForPrime);
        }
        
        assert(minNext > last);
        
        sequence.pb(minNext);
        //printf("%d: %d\n", n+1, minNext);
    }
    
    
    fout << sequence[N] << endl;
        
    return 0;
}
