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
	
	int sumDigits(int num)
	{
        int sum = 0;
	    while(num)
	    {
	        sum += num % 10;
	        num /= 10;
	    }
	    return sum;
	    
	}

}

#endif 
//STOPCOMMON

using namespace math;

const int MAX_NUM = 1000000;
vb inSeq(MAX_NUM+1, false);

int main() 
{

    FORE(i, 1, MAX_NUM)
    {
        if (inSeq[i])
            continue;
        printf("%d\n", i);
        
        int j = i;
        while( j <= MAX_NUM )
        {
            
            j = j + sumDigits(j);
            if (j > MAX_NUM)
                break;
            
            if (inSeq[j])
                break;
            
            inSeq[j] = true;
            
        }
    }
	
	return 0;
}
