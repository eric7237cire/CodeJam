#define USING_MATH 1

//STARTCOMMON

#include <cmath>
#include <vector>
#include "stdio.h" 
#include <limits>
#include <cassert>

using namespace std;

#define mp make_pair 
#define pb push_back 
#define contains(c,x) ((c).find(x) != (c).end()) 


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

#ifdef USING_MATH
namespace math
{
	vector<bool> isPrime;

	vector<int> primes;
	
	void generatePrimes( int maxPrime ) 
	{
		isPrime.assign(maxPrime + 1, true);
		isPrime[0] = false;
		isPrime[1] = false;
		
		primes.clear();

		//Since we are eliminating via prime factors, a factor is at most sqrt(n)
		int upperLimit = static_cast<int>(sqrt(maxPrime));

		for(int i = 2; i <= upperLimit; ++i) {
			if (!isPrime[i]) {
				continue;
			}

			//Loop through all multiples of the prime factor i.  Start with i*i, because the rest
			//were already covered by previous factors.  Ex, i == 7, we start at 49 because 7*2 through 7*6 
			//we already covered by previous prime factors.
			for(int j = i * i; j <= maxPrime; j += i) {
				isPrime[j] = false;
			}
		}

		for(int i = 0; i <= maxPrime; ++i) {
			if (isPrime[i])
				primes.push_back(i);
		}

	}
	
	int addFactors(int n, int maxPIdx)
	{
		for(int pIdx = 0; pIdx <= maxPIdx; ++pIdx)
		{
			while( n % primes[pIdx] == 0 )
			{
				//total[pIdx] ++;
				n /= primes[pIdx];
			}
		}
		
		return n;

	}

}

#endif 
//STOPCOMMON


using namespace math;


int main() {

	
	vector<int> seq; 
	seq.pb(1);
	
	//pairs curIndex, prime factor
	vii curFactorIndex;
	curFactorIndex.pb(mp(0, 2));
	curFactorIndex.pb(mp(0, 3));
	curFactorIndex.pb(mp(0, 5));
	curFactorIndex.pb(mp(0, 7));
	
	for(int i = 0; i < 5842; ++i)
	{
		int lowestIndex = -1;
		int lowestNext = numeric_limits<int>::max();
		
		FOR(j, 0, curFactorIndex.size())
		{
			//increase index until we have a candidate
			int next;
			while( (next = seq[ curFactorIndex[j].first ] * curFactorIndex[j].second) <= seq[i] )
				++curFactorIndex[j].first;
			
			
			//printf("Cur seq value %d.  Checking Used %d =  %d\n", lowestIndex, lowestNext );
			
			assert(next > seq[i]);
			
			if (next < lowestNext)
			{
				lowestNext = next;
				lowestIndex = j;
			}
		}
		
		curFactorIndex[lowestIndex].first++;
		seq.pb( lowestNext );
		
		//printf("%d =  %d\n", i+2, lowestNext );
		
		
	}

	int N;
	while(1 == scanf("%d", &N) && N)
	{
		
		printf("The %d%s humble number is %d.\n", N, 
		N % 100 != 11 && N % 10 == 1 ? "st" : 
		N % 100 != 12 && N % 10 == 2 ? "nd" : 
		N % 100 != 13 && N % 10 == 3 ? "rd" :
		"th", seq[N-1]);
		

		//scanf("%d", &nSeg);
		
	}
	return 0;
}
