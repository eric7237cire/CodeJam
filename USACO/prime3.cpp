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

typedef map< int, int > DigitPrimeMap;

vector<int> digitPrimeMap(10000, 0);

string DecToBin(int number)
{
    if ( number == 0 ) return "0";
    if ( number == 1 ) return "1";

    if ( number % 2 == 0 )
        return DecToBin(number / 2) + "0";
    else
        return DecToBin(number / 2) + "1";
}

vi getDigits(uint num)
{
    int sum = 0;
    
    vi digits;
    
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

vi primesWithSum;
vvi primeDigits;
vvi ans;

int powTen[] = { 10000, 1000, 100, 10, 1 }; 

	



void idxToRowCol(const int idx, int& row, int& col)
{
    row = idx % 5;
    col = idx / 5;
}

int rowColToIdx(int row, int col)
{
    return col * 5 + row;
}

void printGrid(const vi& grid, ostream& os, bool byRow)
{
    FOR(r, 0, 5)
    {
        FOR(c, 0, 5)
        {
            //int sq = rowColToIdx(r,c);
            //The answer needs to be sorted row by row
            if (byRow)
            {int sq = r * 5 + c;
            os << grid[sq];
            } else {
                int sq = rowColToIdx(r,c);
                os << grid[sq];
            }
        }
        
        os << endl;
    }
}




/**
Put a prime in the top row, then try to fill in the columns
*/
void search( vi& grid, vi& prefix, int currentSquare)
{
    if (currentSquare == 25)
    {        
        //ans.pb(grid);
        vi gridRowCol;
        FOR(r, 0, 5)
        {
            FOR(c, 0, 5)
            {
                gridRowCol.pb( grid[ rowColToIdx(r,c) ] );
            }
        }
        ans.pb(gridRowCol);
        
        return;
    }
    
    int row, col;
    idxToRowCol( currentSquare, row, col);
    
    int colNum = 0;
    FOR(r, 0, row) {
        colNum *= 10;
        colNum += grid[ rowColToIdx(r,col) ];        
    }
    int p1 = digitPrimeMap[colNum];
    
    int rowNum = 0;
    FOR(c, 0, col) {
        rowNum *= 10;
        rowNum += (grid[ rowColToIdx(row, c)]);
    }
    int p2 = digitPrimeMap[rowNum];
    
    bool onDiag1 = row == col;
    
    int diag1 = 0;
    if (onDiag1) {
        FOR(i, 0, row) {
            diag1 *= 10;
            diag1 += ( grid[ rowColToIdx(i,i) ] );   
        }
    }
    
    int p3 = onDiag1 ? digitPrimeMap[diag1] : (1 << 11) - 1;
    
    bool onDiag2 = 4-col == row;
    int diag2 = 0;
    
    if (onDiag2) {
        FOR(c, 0, col) {
            int r = 4 - c;
            diag2 *= 10;
            diag2 += ( grid[ rowColToIdx(r,c)  ] );   
        }
    }
    
    int p4 = onDiag2 ? digitPrimeMap[diag2] : (1 << 11) - 1;
    
    int all = p1 & p2 & p3 & p4;
    /*
    if (grid[0] == 1 &&
        grid[1] == 3 &&
        grid[2] == 3 &&
        grid[3] == 1 &&
        grid[4] == 3 &&
        
        grid[5] == 1 &&
        grid[6] == 3 &&
        grid[7] == 0 &&
        grid[8] == 4 &&
        grid[9] == 3 &&
        
        grid[10] == 3 &&
        grid[11] == 2 &&
        grid[12] == 3 &&
        grid[13] == 0 &&
        grid[14] == 3 
        ) {
    printGrid(grid);
    cout << "P1 " << DecToBin(p1) << endl;
    cout << "P2 " << DecToBin(p2) << endl;
    cout << "P3 " << DecToBin(p3) << endl;
    cout << "P4 " << DecToBin(p4) << endl;
    cout << "Possible for next " << DecToBin(all) << endl;
        }
        */
    FORE(d, 0, 9) {
        if ( (all & (1 << d)) != 0) {
            grid[currentSquare] = d;
            search(grid, prefix,currentSquare+1);
            grid[currentSquare] = -1;
        }
    }
    
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
	
	
	
	
	

	FOR(i, 0, allPrimes.size()) {
	    if (allPrimes[i] < 10000) {
	        continue;
	    }
	    
        uint p = allPrimes[i];
        
        vi digits = getDigits(p);
        
        //cout << p << " digit sum " << digits << endl;
        
        assert(digits.size() == 6);
        
        if (digits[5] != digitSum) 
            continue;
        
        primeDigits.pb ( digits) ;	        
        primesWithSum.pb(p);

        //erase size
        digits.erase(digits.begin() + 5);
        
        digitPrimeMap[ 0 ] = digitPrimeMap[ 0 ] | (1 << digits[0] );
        
        int n = 0;
        FOR(d, 0, 4)
        {
            n *= 10;
            n += digits[d];
            
            digitPrimeMap[ n ] = digitPrimeMap[ n ] | (1 << digits[d+1] );
        }
        /*
        for a prime like 13304
        we have
        0 -> 1
        
        1 -> 3
        13 -> 3
        133 -> 0
        1330 -> 4
        */
        
        
    }
	
	
	
    FOR(firstRowIdx, 0, primesWithSum.size())
    {
        if (primeDigits[firstRowIdx][0] != topLeft)
            continue;
        
        const vi& firstDigits = primeDigits[firstRowIdx];
        
		vi grid(25, -1);
		
		vi prefix(12, 0);
		
		FOR(d, 0, 5)
		{
		   prefix[d] = firstDigits[d] * powTen[0];
		}

		copy( firstDigits.begin(), firstDigits.begin()+5, grid.begin() );
            
		
        search( grid, prefix, 5 );
    }
	/*
	vi test;
	cout << "Test " << endl;
	//cout << DecToBin(digitPrimeMap[test]) << endl;
	
	test.pb(3);
	//cout << DecToBin(digitPrimeMap[test]) << endl;
	
	test.pb(4);
	//cout << DecToBin(digitPrimeMap[test]) << endl;
	
	test.pb(3);
	cout << DecToBin(digitPrimeMap[test]) << endl;
	
	test.clear();
	test.pb(5);
	//cout << DecToBin(digitPrimeMap[test]) << endl;
	
	cout << "Contains " << contains(primeSet, 34301) << endl;
	
	cout << primesWithSum.size() << endl;
	*/
	
	sort(all(ans));
	
	if (ans.size() == 0)
	{
	    fout << "NONE" << endl;
	    return 0;
	}
	
	FOR(a, 0, ans.size())
	{
	    printGrid(ans[a], fout,true);
	    if (a < ans.size() - 1)
	        fout << endl;
	}
	
	return 0;
}



