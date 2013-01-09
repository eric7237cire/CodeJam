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

uint getUsedDigits(uint num);

void problem49() 
{
	vector<uint> primes;
	generatePrimes(10000, primes);

	for(vector<uint>::const_reverse_iterator largest = primes.rbegin();
		largest != primes.rend() && *largest >= 1000; ++largest)
	{
		for(vector<uint>::const_reverse_iterator middle = 1 + largest;
			middle != primes.rend() && *middle >= 1000;
			++middle)
		{
			if (getUsedDigits(*middle) != getUsedDigits(*largest))
				continue;
			
			for(vector<uint>::const_reverse_iterator smallest = 1 + middle;
				smallest != primes.rend() && *smallest >= 1000; ++smallest)
			{
				if (getUsedDigits(*middle) != getUsedDigits(*smallest) || *largest - *middle != *middle - *smallest)
					continue;
			
				cout << *smallest << " " << *middle << " " << *largest << endl;
			}

		}		
	}
}


void problem50()
{
	const int upperLimit = 1000000;
	vector<uint> primes;
	generatePrimes(upperLimit, primes);

	set<uint> primeSet;
	primeSet.insert(primes.begin(), primes.end());

	uint currentMaxTerms = 0;

	for(vector<uint>::const_iterator pStart = primes.begin();
		pStart != primes.end();
		++pStart)
	{
		uint sum = *pStart;
		uint terms = 1;

		for(vector<uint>::const_iterator pStop = 1 + pStart;
		pStop != primes.end();
		++pStop) 
		{
			++terms;
			sum += *pStop;

			if (sum >= upperLimit)
				break;

			if (terms > currentMaxTerms && primeSet.find(sum) != primeSet.end())
			{
				currentMaxTerms = terms;
				cout << "New max "<< currentMaxTerms << " sum " << sum 
					<< " adding " << *pStart << " to " << *pStop << endl;
			}
		}
	}
	
}

/*
I solved it for non consecutive primes!
*/

void problem50_wrong()
{
	const int upperLimit = 1000000;
	vector<uint> primes;
	generatePrimes(upperLimit, primes);

	vector<int> maxTerms(upperLimit+1, -1);
	vector<int> prevTerm(upperLimit+1, -1);

	//Print<uint, vector<uint>::iterator>(cout, prevTerm.begin(), prevTerm.end(), ", ");
	//Print<int, vector<int>::iterator>(cout, prevTerm.begin(), prevTerm.end(), ", ");

	prevTerm[0] = -1;
	maxTerms[0] = 0;
	
	for(vector<uint>::const_iterator pIt = primes.begin(); pIt != primes.end(); ++pIt)
	{
		vector<int> nextTerms(maxTerms);

		uint prime = *pIt;

		cout << "Processing prime " << prime << endl;

		for(int prev = 0; prev+prime <= upperLimit; ++prev) 
		{
			if(maxTerms[prev] < 0)
				continue;

			int terms = 1 + maxTerms[prev];

			if (terms > maxTerms[prev + prime])
			{
				nextTerms[prev + prime] = terms;
				prevTerm[prev + prime] = prev;
			}
		}

		maxTerms = nextTerms;
		/*
		cout << "\n\nAfter prime " << prime << endl;
		cout << "Max " << endl;
		Print<int, vector<int>::iterator>(cout, maxTerms.begin(), maxTerms.end(), ", ");
		cout << "\n\nPrev " << endl;
		Print<int, vector<int>::iterator>(cout, prevTerm.begin(), prevTerm.end(), ", ");
		*/
	}

	int maxTermCount = 0;

	cout << endl << endl;
	for(vector<uint>::const_iterator pIt = primes.begin(); pIt != primes.end(); ++pIt)
	{
		uint prime = *pIt;
		if (maxTerms[prime] > maxTermCount)
		{
			maxTermCount = maxTerms[prime];

			cout << "New maximum with " << maxTermCount << " terms. " << prime << " = ";
			int next = prevTerm[prime];
			int prev = prime;
			while(next >= 0)
			{
				cout << prev - next;
				prev = next;
				next = prevTerm[next];
				if (next >= 0) {
					cout << " + ";
				}
			}

			cout << endl;
		}
	}
}

uint replaceDigit(uint num, uint digitToReplace, uint newDigit)
{
	uint ret = 0;
	uint powTen = 1;

	while(num > 0)
	{
		uint digit = num % 10;
		
		digit = digit == digitToReplace ? newDigit : digit;

		ret += digit * powTen;

		powTen *= 10;
		num /= 10;
	}

	return ret;
}

void problem51() 
{
	const uint upperLimit = 1000000;
	const int replaceUpTo = 2;

	vector<uint> primes;
	generatePrimes(upperLimit, primes);

	set<uint> primeSet;
	primeSet.insert(primes.begin(), primes.end());

	uint currentMax = 0;

	uint maxCount = 0;

	for(vector<uint>::const_iterator pIt = primes.begin(); pIt != primes.end(); ++pIt)
	{
		const uint prime = *pIt;

		for(int digitToReplace = 0; digitToReplace <= replaceUpTo; ++digitToReplace)
		{
			uint count = 1;
			for(int newDigit = digitToReplace+1; newDigit <= 9; ++newDigit)
			{
				uint rep = replaceDigit(prime, digitToReplace, newDigit);

				if (rep == prime)
					break;

				if (primeSet.find(rep) != primeSet.end()) 
					++count;
			}

			if (count > maxCount) 
			{
				cout << "New max " << count << " with prime " << prime << " and digit " << digitToReplace << endl;
				maxCount = count;
			}
		}
	}
}

void problem52()
{
	uint multTarget = 6;

	for(uint num = 1; ; ++num)
	{
		uint digits = getUsedDigits(num);
		for(uint m = 2; m <= multTarget; ++m) 
		{
			uint digitsM = getUsedDigits(num * m);

			if (digitsM != digits) 
				break;

			if (m >= multTarget) 
			{
				cout << "num= " << num << " up to m " << m << endl;
				return;
			} 
		}
	}
}

void problem53()
{
	const int limit = 100;
	const int goal = 1000000;

	uint combinations[limit+1][limit+1];
    for(int n = 0; n <= limit; ++n)
        for(int k = 0; k <= limit; ++k)
    {
        if (n<k)
            combinations[n][k] =0;
        else if (n==k || k==0 )
            combinations[n][k] = 1;
        else
            combinations[n][k] = (combinations[n-1][k] + combinations[n-1][k-1]);

		combinations[n][k] = min(goal+1, combinations[n][k]);
    }

	uint count = 0;

	for(int n = 1; n <= limit; ++n)
		for(int r = 0; r <= n; ++r)
		{
			uint cr = combinations[n][r];
			//printf("%d C %d = %d\n", n, r, cr);
			if (cr > goal)
				++count;
		}

	cout << "Count is " << count << endl;
}

uint concatNums(uint left, uint right);

void problem60() 
{
	const int upperLimit = 99999999;

	vector<unsigned int> primes;
	generatePrimes(upperLimit, primes);

	map<uint, uint> primeToIdx;

	set<uint> primeSet(primes.begin(), primes.end());

	for(uint pIdx = 0; pIdx < primes.size(); ++pIdx) 
	{
		primeToIdx.insert( make_pair(primes[pIdx], pIdx) );
	}
	for(auto p = primeSet.begin(); p != primeSet.end(); ++p) {
		//cout << *p << endl;
	}

	vector<set<uint>> pairablePrimes(primes.size());

	for(auto p = primes.begin(); p != primes.end(); ++p)
	{
		pairablePrimes[ primeToIdx[*p] ].insert(*p);

		uint cutPoint = 10;

		while(*p > cutPoint)
		{
			uint left = *p / cutPoint;
			uint right = *p % cutPoint;

			//avoid leading zeros
			if (right < cutPoint / 10) {
				cutPoint *= 10;
				continue;
			}

			//% 10 >= 1
			//% 100 >= 10

			cutPoint *= 10;

			if (primeSet.find(left) == primeSet.end())
				continue;

			if (primeSet.find(right) == primeSet.end())
				continue;

			uint other = concatNums(right, left);

			if (primeSet.find(other) != primeSet.end()) {				
				pairablePrimes[ primeToIdx[left] ].insert(right);
				pairablePrimes[ primeToIdx[right] ].insert(left);
			}
		}
	}

	/*for(uint pIdx = 0; pIdx < pairablePrimes.size(); ++pIdx)
	{
		if (primes[pIdx] > 100)
			break;

		//printf("Prime %d.  Pairable -- ", primes[pIdx]);

		for(const uint& pairable : pairablePrimes[pIdx]) {
			cout << pairable << " ";
		}

		cout << endl;
	}*/

	/*
	const int kSize = 4;
	uint indexes[kSize] = {0, 1, 2, 3};

	const int primeLimit = 150;

	while(true) 
	{
		for(int toInc = 0; toInc < kSize; ++toInc)
		{
			indexes[toInc] ++;
			if ( primes[indexes[toInc] ] > primeLimit) {
				indexes[toInc] = 0;

			while( 
		}

	}*/

	/*
	pairablePrimes.erase(
		std::remove_if(pairablePrimes.begin(), 
		pairablePrimes.end(), 
		[](const set<uint>& pSet) { return pSet.size() < 4; }), 
						  pairablePrimes.end());

	
	*/

	int limit = 3;

	for(uint pIdx = 0; pIdx < pairablePrimes.size(); ++pIdx)
	{
		if (pairablePrimes[pIdx].size() < limit) {
			pairablePrimes[pIdx].clear();
			continue; 
		}
	}
	
	for(uint pIdx = 0; pIdx < pairablePrimes.size(); ++pIdx)
	{
		auto setIt = pairablePrimes[pIdx].begin();

		while(setIt !=  pairablePrimes[pIdx].end())
		{
			if ( pairablePrimes[ primeToIdx[*setIt] ].size() < limit ) {
				pairablePrimes[pIdx].erase(setIt++);
			} else {
				++setIt;
			}
		}
	}

	for(uint pIdx = 0; pIdx < pairablePrimes.size(); ++pIdx)
	{
		auto setIt = pairablePrimes[pIdx].begin();

		while(setIt !=  pairablePrimes[pIdx].end())
		{
			vector<uint> intersection;

			//Itersection of setIts pair list and this one must be size >= 3
			set_intersection(pairablePrimes[pIdx].begin(), pairablePrimes[pIdx].end(),
				pairablePrimes[ primeToIdx[*setIt] ].begin(), pairablePrimes[ primeToIdx[*setIt] ].end(),
				back_inserter(intersection));

			if ( intersection.size() < limit-1 ) {
				pairablePrimes[pIdx].erase(setIt++);
			} else {
				++setIt;
			}
		}
	}

	map<uint, vector<uint>> remaining;

	for(uint pIdx = 0; pIdx < pairablePrimes.size(); ++pIdx)
	{
		if (pairablePrimes[pIdx].size() < limit) {
			pairablePrimes[pIdx].clear();
			continue; 
		}

		vector<uint> vec(pairablePrimes[pIdx].begin(), pairablePrimes[pIdx].end());

		remaining.insert(make_pair(primes[pIdx], vec));

		/*printf("Prime %d.  Pairable -- ", primes[pIdx]);

		for(const uint& pairable : pairablePrimes[pIdx]) {
			cout << pairable << " ";
		}

		cout << endl;*/
	}

	int minsum = 1000000;

	for(auto it = remaining.begin(); it != remaining.end(); ++it)
	{
		const vector<uint>& pairs = it->second;
		
		for(int a = 0; a < pairs.size(); ++a) {
			for(int b = a+1; b < pairs.size(); ++b) {

				

				vector<uint> intAB;

				set_intersection(remaining[pairs[a]].begin(), remaining[pairs[a]].end(),
				remaining[pairs[b]].begin(), remaining[pairs[b]].end(),
				back_inserter(intAB));

				if (intAB.size() < 5) continue;

				for(int c = b+1; c < pairs.size(); ++c) {

					vector<uint> intABC;

					set_intersection(intAB.begin(), intAB.end(),
					remaining[pairs[c]].begin(), remaining[pairs[c]].end(),
					back_inserter(intABC));

					if (intABC.size() < 5) {
						continue;
						
					}

					for(int d = c+1; d < pairs.size(); ++ d) {
						vector<uint> intABCD;

					set_intersection(intABC.begin(), intABC.end(),
					remaining[pairs[d]].begin(), remaining[pairs[d]].end(),
					back_inserter(intABCD));

					if (intABCD.size() >= 4) {
						//cout << "Int " << " " << pairs[a] << " " << pairs[b] << " " << pairs[c] << " " << pairs[d] << endl;

					}

					if (intABCD.size() < 5) {
						continue;
					}

					for(int e = d+1; e < pairs.size(); ++e) {
							vector<uint> intABCDE;

					set_intersection(intABCD.begin(), intABCD.end(),
					remaining[pairs[e]].begin(), remaining[pairs[e]].end(),
					back_inserter(intABCDE));

					if (intABCDE.size() >= 5) {
						cout << "Int " << " " << pairs[a] << " " << pairs[b] << " " << pairs[c] << " " << pairs[d] << " "<< pairs[e] << endl;
						cout << pairs[a] + pairs[b] + pairs[c] + pairs[d] + pairs[e] << endl;

					}
					}

					}
				}
			}
		}


	}

	/*
	prime ==> pairable primes
	*/
	
	/*
	take prime from pairable prime,
	take next
	*/

}

int main() {
	ull start = GetTickCount64();
	problem60();
	ull end = GetTickCount64();

	cout << "Elapsed ms " << end-start << endl;
}

uint concatNums(uint left, uint right)
{
	//123 ; 456
	uint rightDigits = right;

	while(rightDigits > 0)
	{
		rightDigits /= 10;
		left *= 10;
	}

	return left+right;
}

uint getUsedDigits(uint num)
{
	uint ret = 0;
	while(num > 0)
	{
		uint digit = num % 10;
		ret |= 1 << digit;

		num /= 10;
	}

	return ret;
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

