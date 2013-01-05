#include <Windows.h>
#include <boost/dynamic_bitset.hpp>
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <set>
#include <vector>
#include <algorithm>
#include <cassert>
#include <cmath>
using namespace std;
using namespace boost;

typedef unsigned long long ull;

ull getPentagonal(int n) 
{
	return 1ull * n * (3*n - 1) / 2;
}

bool isPentagonal(ull num)
{
	ull solveForN = 1 + 24ull * num;
	solveForN = (ull) sqrt(solveForN);

	solveForN = (1 + solveForN) / 6;

	//solveForN = floor(solveForN) + 0.5;

	int n = (int) solveForN;

	ull check_num = getPentagonal(n);

	return check_num == num;
}

void problem44() 
{
		int upperLimit = 30000;

		set<ull> pentNums;
		vector<ull> pentNumsList(upperLimit);

		for(int n = 1; n <= upperLimit; ++n)
		{
			ull pentNum = getPentagonal(n);
			pentNumsList[n-1] = pentNum;
			pentNums.insert(pentNum);
		}

		for(int j = 1; j <= upperLimit; ++j)
		{
			for(int k = 1; k < j; ++k)
			{
				ull pentJ = pentNumsList[j-1];
				ull pentK = pentNumsList[k-1];

				assert(pentJ > pentK);

				ull sum = pentJ+pentK;
				ull diff = pentJ - pentK;

				if (pentNums.find(sum) != pentNums.end() && pentNums.find(diff) != pentNums.end())
				{
					printf("Pair %llu %llu  Diff = %llu \n", pentJ, pentK, diff);
					return;
				}
			}
		}
	
}

void problem45() 
{
	ull hexIdx = 143+1;
	ull pentIdx = 165+1;
	ull triIdx = 285+1;

	ull hex = 3;
	ull pent = 2;
	ull tri = 1;

	while(true)
	{
		if (hex == pent && hex == tri) 
		{
			printf("Solution tri %llu pent %llu  hex %llu.  Num = %llu \n", triIdx, pentIdx, hexIdx, hex);
			return;
		}
		ull curMax = max( max(tri,pent), hex);

		if (tri < curMax) 
		{
			++triIdx;
			tri = triIdx*(triIdx+1) / 2;
		}

		if (pent < curMax) 
		{
			++pentIdx;
			pent = pentIdx * (3*pentIdx-1) /2;
		}

		if (hex < curMax) 
		{
			++hexIdx;
			hex = hexIdx * (2*hexIdx - 1);
		}
	}
}

template<typename T, typename InputIterator>  
void Print(std::ostream& ostr,   
           InputIterator itbegin,   
           InputIterator itend,   
           const std::string& delimiter)  
{  
    std::copy(itbegin,   
              itend,   
              std::ostream_iterator<T>(ostr, delimiter.c_str()));  
}  

void generatePrimes(int n, vector<unsigned int>& primes);

void problem46() 
{
	vector<unsigned int> primes;
	generatePrimes(1000000, primes);

	//cout << primes.size() << endl;

	for(unsigned int pUpperBoundIdx = 1; pUpperBoundIdx < primes.size(); ++pUpperBoundIdx)
	{
		//cout << "Prime upper bound  " << primes[pUpperBoundIdx] << endl;

		//Loop through all odd numbers between this prime and previous
		for(unsigned int odd = primes[pUpperBoundIdx-1] + 2; odd <= primes[pUpperBoundIdx] - 2; odd += 2)
		{
			//cout << "Odd number " << odd ;
			bool foundIt = true;

			//Go through all primes lower than pUpperbound
			for(int primeIdx = pUpperBoundIdx - 1; primeIdx >= 0; --primeIdx)
			{
				int prime = primes[primeIdx];

				int rest = odd  - prime;
				assert(rest > 0);

				int sq = 1;
				while( (2 * sq * sq) < rest)
				{
					++sq;
				}

				if ( (2 * sq * sq) == rest) 
				{
					//cout << " equals prime " << prime << " + 2 * " << sq << " squared " << endl;
					foundIt = false;
					break;
				}
			}
						
			if (foundIt) 
			{
				cout << "Found it " << odd << endl;
				return;
			}
			
		}
	}

}

typedef unsigned int uint; 

void problem47()
{
	//Factor n
	int conseqNums = 4;

	const int upperLimit = 1250000;

	vector<uint> primes;
	generatePrimes( static_cast<uint>(sqrt(upperLimit)), primes);

	for(int n = 1; n <= upperLimit; ++n)
	{
		bool foundIt = true;

		for(int cIdx = 0; cIdx < conseqNums; ++cIdx)
		{
			int factors = 0;
			for(vector<uint>::const_iterator it = primes.begin(); it != primes.end(); ++it)
			{
				if ( (n+cIdx) % *it == 0)
				{
					++factors;
				}
			}

			if (factors != conseqNums)
			{
				foundIt = false;
				break;
			}
		}

		if (foundIt)
		{
			cout << "Found it " << n << endl;
			return;
		}
		
	}
}
int main() {
	ull start = GetTickCount64();
	problem47();
	ull end = GetTickCount64();

	cout << "Elapsed ms " << end-start << endl;
}

void generatePrimes(int n, vector<unsigned int>& primes) 
{
	dynamic_bitset<> isPrime(n+1, 0); 
	isPrime.flip();
	isPrime[0] = 0;
	isPrime[1] = 0;
        
	primes.clear();

    //Since we are eliminating via prime factors, a factor is at most sqrt(n)
    unsigned int upperLimit = static_cast<unsigned int>(sqrt(n));
    
	for(unsigned int i = 2; i <= upperLimit; ++i) {
        if (!isPrime[i]) {
            continue;
        }
            
        //Loop through all multiples of the prime factor i.  Start with i*i, because the rest
        //were already covered by previous factors.  Ex, i == 7, we start at 49 because 7*2 through 7*6 
        //we already covered by previous prime factors.
        for(int j = i * i; j <= n; j += i) {
            isPrime.set(j, false);
        }
    }
        
    for (size_t i = isPrime.find_first(); i != boost::dynamic_bitset<>::npos; i = isPrime.find_next(i)) {
		primes.push_back(static_cast<unsigned int>(i));
    }
 
}