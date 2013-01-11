/*
ID: eric7231
PROG: sprime
LANG: C++
*/
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <cmath>
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
    
	ofstream fout ("sprime.out");
    ifstream fin ("sprime.in");
	
    uint N;
    fin >> N;
    
	vector<uint> sol;
	sol.push_back(2);
	sol.push_back(3);
	sol.push_back(5);
	sol.push_back(7);

    for(int n = 1; n < N; ++n)
	{
		vector<uint> next;

		for(uint solIdx = 0; solIdx < sol.size(); ++solIdx)
		{
			uint prev = sol[solIdx];

			for(uint digit = 1; digit <= 9; digit += 2)
			{
				uint candidate = prev * 10 + digit;

				if (!isPrime(candidate))
					continue;

				next.push_back(candidate);
			}
		}

		sol = next;
	}

	for(uint solIdx = 0; solIdx < sol.size(); ++solIdx)
	{
		cout << sol[solIdx] << endl;
		fout << sol[solIdx] << endl;
	}
    

	return 0;
}
