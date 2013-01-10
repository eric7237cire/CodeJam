/*
ID: eric7231
PROG: pprime
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
#include <cctype>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;

uint reverse(uint toRev) 
{
    uint rev = 0;
    while(toRev != 0) 
    {
        rev *= 10;
        rev += toRev % 10;
        toRev /= 10;
    }
    return rev;
}            

uint getDigitCount(uint num)
{
	uint ret = 0;
	while(num > 0)
	{
		++ret;
		num /= 10;
	}

	return ret;
}

bool miller_rabin_32(uint n);

bool isPrime(uint n) {
    return miller_rabin_32(n);
}
    
     int modular_exponent_32(int base, int power, int modulus) {
        unsigned long long result = 1;
        for (int i = 31; i >= 0; i--) {
            result = (result*result) % modulus;
            if ((power & (1 << i)) != 0) {
                result = (result*base) % modulus;
            }
        }
        return (int)result; // Will not truncate since modulus is an int
    }

    


     bool miller_rabin_pass_32(int a, int n) {
        int d = n - 1;
    int s = 0;
    
    while ((d % 2) == 0) {
        d /= 2;
        s++;
    }
    
        int a_to_power = modular_exponent_32(a, d, n);
        if (a_to_power == 1) return true;
        for (int i = 0; i < s-1; i++) {
            if (a_to_power == n-1) return true;
            a_to_power = modular_exponent_32(a_to_power, 2, n);
        }
        if (a_to_power == n-1) return true;
        return false;
    }

    bool miller_rabin_32(uint n) {
        if (n <= 1) return false;
        else if (n == 2) return true;
        else if (miller_rabin_pass_32( 2, n) &&
            (n <= 7  || miller_rabin_pass_32( 7, n)) &&
            (n <= 61 || miller_rabin_pass_32(61, n)))
            return true;
        else
            return false;
    }

int main() {
    
	ofstream fout ("pprime.out");
    ifstream fin ("pprime.in");
	
    uint a, b;
    fin >> a >> b;
    
    const uint digitsA = getDigitCount(a);
    const uint digitsB = getDigitCount(b);
    
    const int maxDigits = 9;
    
    int digitLB[maxDigits];
    
    //digit = index;  range LB <= num < UP
    int LB = 1;
    
    for(int i = 0; i < maxDigits; ++i) {
        digitLB[i] = LB;
        LB *= 10;
    }
    
    set<uint> palinPrimes;
    
    if (a <= 5)
        palinPrimes.insert(5);
    
    if (a <= 7)
        palinPrimes.insert(7);
        
    //cout << "Digits b " << digitsB << endl;
    
    for(uint d = max(2u, digitsA); d <= digitsB; ++d)
    {
        uint dHalf = d / 2;
        
        for(int half = digitLB[dHalf-1]; half < digitLB[dHalf-1] * 10; ++half)
        {
            //cout <<  "half " << half << endl;
            
            if (d % 2 == 0) {
                uint palin = half * digitLB[dHalf-1] + reverse(half);
               // cout << "Palin even " << palin << endl;
                if (a <= palin && b >= palin && isPrime(palin))
                    palinPrimes.insert(palin);
                
                continue;
            }
            
            uint revHalf = reverse(half);
            for(int m = 0; m <= 9; ++m)
            {
                uint left = half * 10 + m;   
                uint palin = left * digitLB[dHalf] + revHalf;
                
               // cout << "Palin odd " << palin << endl;
                
                if (a <= palin && b >= palin && isPrime(palin))
                    palinPrimes.insert(palin);
                
            }
        }
    }

	
	for(set<uint>::const_iterator it = palinPrimes.begin(); it != palinPrimes.end(); ++it) {
	     fout << *it << endl;   
	}
	
	return 0;
}
