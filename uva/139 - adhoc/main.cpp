#define USING_MATH 0

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

#include <iostream>
#include <string>
#include <vector>
#include <map>
#include <iomanip>
using namespace std;

int str2int(string s) {
    int result = 0;

    for (int i = 0, sz = s.size(); i < sz; i++) {
        result = result * 10 + (s[i] - '0');
    }

    return result;
}

int main(void) {
    size_t found;
    int duration, pos;
    string input, code, locality, number, subscriber;
    vector<string> codes;
    map< string, pair<string,int> > iddstd;

    while (cin >> code) {
        if (code == "000000")
            break;

        codes.push_back(code);
        cin.get();
        getline(cin, input);

        found = input.find('$');
        locality = string(input.begin(), input.begin() + found);

        iddstd[code] = make_pair(locality, str2int(string(input.begin() + found + 1, input.end())));
    }

    while (cin >> number) {
        if (number == "#")
            break;

        cin >> duration;

        pos = -1;

        for (int i = 0, sz = codes.size(); i < sz; i++) {
            if (int(number.find(codes[i])) == 0) {
                if (codes[i][1] == '0' && codes[i].size() > 2 && number.size() - codes[i].size() > 3 && number.size() - codes[i].size() < 11) {
                    pos = i;
                    break;
                } else if (codes[i][1] != '0' && number.size() - codes[i].size() > 3 && number.size() - codes[i].size() < 8) {
                    pos = i;
                    break;
                }
            }
        }

        if (pos != -1)
            subscriber = number.substr(codes[pos].size());

        if (pos == -1 && number.size() > 0 && number[0] == '0') {
            cout << left << setw(16) << number << setw(35) << "Unknown" << setw(5) << right << duration << setw(13) << "-1.00" << endl;
        } else if (pos == -1 && number.size() > 0 && number[0] != '0') {
            cout << left << setw(16) << number << "Local" << right << setw(30) << number << setw(5) << duration << setw(6) << "0.00" << setw(7) << "0.00" << endl;
        } else {
            cout << left << setw(16) << number << iddstd[codes[pos]].first << right << setw(35 - iddstd[codes[pos]].first.size()) << subscriber << setw(5) << duration << setw(6) << fixed << setprecision(2) << iddstd[codes[pos]].second / 100.0 << setw(7) << iddstd[codes[pos]].second / 100.0 * duration << endl;
        }
    }

    return 0;
}
