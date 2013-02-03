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
    
	//Make left most/most significant index 0
	reverse(all(digits));

    digits.pb(sum);
    
    return digits;
}

uvi primesWithSum;
uvvi primeDigits;
set<uint> primeSet;
vector<string> ans;


	
void checkValid(const vi& rows) {

	

	FOR(col, 0, 5)
	{
		uint num = 0;
		for(int row = 0; row < 5; row++)
		{
			num*=10;
			num += primeDigits[ rows[row] ] [ col ];
		}
		
		

		if (!contains(primeSet, num)) {
			
			return;
		}
	}


    cout << "\nValid\n " << endl;
    FOR(r, 0, 5)
        cout << primesWithSum[ rows[r] ] << endl;
}

/**
Put a prime in the top row, then try to fill in the columns
*/
void search( const uvi& fdDigits, const uvi& sdDigits,
    const uvi& leftColDigits, const uvi& rightColDigits,
    vi& rows, int currentRow)
{
    if (currentRow == 5)
    {
        checkValid(rows);
        return;
    }
    
        
	 
    for(uint p = 0; p < primesWithSum.size(); ++p)
    {
        int fdDigit = currentRow;
        int sdDigit = 4 - currentRow;
        
        const uvi& pd = primeDigits[p];

		uint prime = primesWithSum[p];
        
        if (pd[fdDigit] != fdDigits[fdDigit]) {
			continue;
		}
        
        if (pd[sdDigit] != sdDigits[sdDigit]) {
			continue;
		}
		
		if (pd[0] != leftColDigits[currentRow]) {
		    continue;
		}
		
		if (pd[4] != rightColDigits[currentRow]) {
		    continue;
		}
        
        rows[currentRow] = p;
		
		search(fdDigits, sdDigits, leftColDigits, rightColDigits, rows, currentRow+1);
        
    }
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
	
	
	uint digitSum;
	uint topLeft;
	
	fin >> digitSum >> topLeft;
	
	
	vvs posDigitPrime(5, vs(10, set<uint>()));
	
	
	

	FOR(i, 0, allPrimes.size()) {
	    if (allPrimes[i] > 10000) {
	        uint p = allPrimes[i];
	        
	        uvi digits = getDigits(p);
	        
	        //cout << p << " digit sum " << digits << endl;
	        
	        assert(digits.size() == 6);
	        
	        
	        if (digits[5] != digitSum) 
	            continue;
	        
	        primeDigits.pb ( digits) ;	        
	        primesWithSum.pb(p);

			

			primeSet.insert(p);
	        
	        FOR(d, 0, 5) {
	            //posDigitPrime[d][digits[d]].insert( p );
	        }
	        
	    }
	}
	
	
    FOR(firstDiag, 0, primesWithSum.size())
    {
        if (primeDigits[firstDiag][0] != topLeft)
            continue;
        
        const uvi& firstDigits = primeDigits[firstDiag];
        

        FOR(secondDiag, 0, primesWithSum.size())
        {
            const uvi& secondDigits = primeDigits[secondDiag];
            
            //Check center same
            if (firstDigits[2] != secondDigits[2])
                continue;
        
            //Left column
            FOR(col1, 0, primesWithSum.size())
            {
                const uvi& col1Digits = primeDigits[col1];
                
                if(col1Digits[0] != firstDigits[0])
                    continue;
                
                if(col1Digits[4] != secondDigits[0])
                    continue;
                
                //Right column
                FOR(col2, 0, primesWithSum.size())
                {
                    const uvi& col2Digits = primeDigits[col2];
                
                    if(col2Digits[4] != firstDigits[4])
                        continue;
                
                    if(col2Digits[0] != secondDigits[4])
                        continue;
                
                    vi rows(5, -1);
                    search( firstDigits,
                        secondDigits,
                        col1Digits,
                        col2Digits,
                        rows, 0);
                }
            }
        }
    }
	
	
	
	cout << primesWithSum.size() << endl;
	return 0;
}



