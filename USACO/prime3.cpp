/*
ID: eric7231
PROG: prime3
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
#include <iterator>
#include <iomanip>
#include <sstream>
#include <bitset>
#include <limits>
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

typedef set<uint> si;
typedef vector<set<uint> > vs;
typedef vector<vs> vvs;


#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 
#define SZ(x) ((int) (x).size())

template <class K, class V> 
V getMapValue( const map<K,V>& aMap, const K& key, const V& defaultValue )
{
    typename map<K, V>::const_iterator it = aMap.find(key);
    if ( it == aMap.end() )
        return defaultValue;
    return it->second;
}


const int notConnected = numeric_limits<int>::max();


template<typename T>
ostream& operator<<( ostream& os, const vector<T>& vec )
{
    FOR(i, 0, vec.size())
    {
        os <<  setw(5) << vec[i]; // << endl;
    }
    return os;
}



struct BigNum{
		
        vector<long> s;
        BigNum(long n=0) : s(200, 0){
                s[0]=n;
                //cout << "Constructor " << s << endl;
        }
        void smooth(){
                for(long i=0;i<199;++i){
                        s[i+1]+=s[i]/1000000;
                        s[i]%=1000000;
                }
        }
        void operator-=(BigNum b){
                for(long i=0;i<200;++i)
                        s[i]-=b.s[i];
                for(long i=0;i<199;++i){
                        while(s[i]<0){
                                s[i]+=1000000;
                                --s[i+1];
                        }
                        s[i+1]+=s[i]/1000000;
                        s[i]%=1000000;
                }
        }
        void operator+=(BigNum b){
                for(long i=0;i<200;++i)
                        s[i]+=b.s[i];
                smooth();
        }
        static void output(long n, ostream& os){
                string str;
				ostringstream ostr;
				ostr << n;
                str = ostr.str();
               // cout << "Str " << str << endl;
				for(long l=str.length();l<6;++l)
                        os << ('0');
                os << str;
        }
        
};



ostream& operator<<(ostream& os, const BigNum& bignum){
                long i=199;
                for(;bignum.s[i]==0 && i > 0;--i);
                os << bignum.s[i];
                for(--i;i>=0;--i)
                        bignum.output(bignum.s[i], os);
                
                return os;
}


uvi getDigits(uint num)
{
    int sum = 0;
    
    uvi digits;
    
    while(num > 0) {
        uint digit = num % 10;
        digits.pb(digit);
        sum += digit;
        num /= 10;
    }
    
    digits.pb(sum);
    
    return digits;
}

/**
Put a prime in the top row, then try to fill in the columns
*/
void search(uvvi& grid, uint digitSum, vector<string>& ans, const vvs& posDigitPrimes)
{
    /*int curColumn = columns.size();
    
    
    
    int digit = topRowDigits[curColumn];
    
    const set<uint>& primesStartingWith = posDigitPrimes[0][digit];
    
    for(si::iterator psIt = primesStartingWith.begin();
        psIt != primesStartingWith.end();
        ++psIt)
    {
        FORE(r, 1, 4)
        {
            grid[r][curColumn   
        }
    }*/
}
        
void generatePrimes(int n, vector<unsigned int>& primes) 
{
	vector<bool> isPrime(n+1, true); 
	
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
			isPrime[j] = false;
		}
	}

	FORE(i, 0, n) {
	    if (isPrime[i])
	        primes.push_back(static_cast<unsigned int>(i));
	}

}
     


int main()

{


	ofstream fout ("prime3.out");
	ifstream fin ("prime3.in");

	uvi allPrimes;
	generatePrimes(100000, allPrimes);
	
	uvi primes;
	
	uint digitSum;
	uint topLeft;
	
	fin >> digitSum >> topLeft;
	
	uvi fdPrimes;
	uvvi primeDigits;
	
	vvs posDigitPrime(5, vs(10, set<uint>()));
	
	FOR(i, 0, allPrimes.size()) {
	    if (allPrimes[i] > 10000) {
	        uint p = allPrimes[i];
	        
	        uvi digits = getDigits(p);
	        
	        //cout << p << " digit sum " << digits << endl;
	        
	        
	        
	        assert(digits.size() == 6);
	        
	        
	        if (digits[5] != digitSum) 
	            continue;
	        
	        primeDigits.insert( mp(p, digits) );	        
	        primes.pb(p);
	        
	        FOR(d, 0, 5) {
	            //posDigitPrime[d][digits[d]].insert( p );
	        }
	        
	    }
	}
	
	FOR(tr, 0, fdPrimes.size()) 
	{
	    FOR(fc, 0, fdPrimes.size())
	    {
	        FOR(diag, 0, fdPrimes.size())
	        {
	            search( primeDigits[tr],
	                primeDigits[fc],
	                primeDigits[diag], 1);
	            
	        }
	    }
	}
	
	
	cout << primes.size() << endl;
	return 0;
}



