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
using namespace std;

struct param {
    int v, c, f;
    // f: -1 for wall, 0 for nothing, 1 for bumper
    param() : v(0), c(0), f(0) {}
};

const int MAX = 51;
param grid[MAX][MAX];

const int dir[4][2] = {
    {1, 0} /*right*/, {0, 1} /*up*/,
    {-1, 0} /*left*/, {0, -1} /*down*/
};


void turn_right(int &d)
{
    d = (d + 3) % 4;
}

int main()
{
    int m, n, c, p;
    cin >> m >> n >> c >> p;

    // setup wall
    for (int i = 1; i <= m; ++i) {
        grid[1][i].f = grid[n][i].f = -1;
        grid[1][i].c = grid[n][i].c = c;
    }
    for (int j = 1; j <= n; ++j) {
        grid[j][1].f = grid[j][m].f = -1;
        grid[j][1].c = grid[j][m].c = c;
    }

    int x, y;
    for (int i = 0; i < p; ++i) {
        cin >> x >> y;
        cin >> grid[y][x].v >> grid[y][x].c;
        grid[y][x].f = 1;
    }

    int d, l, pts, tot = 0;
    int nx, ny;
    while (cin >> x >> y >> d >> l) {
        pts = 0;
        for (int i = 0; i < l - 1; ++i) {
            // move
            nx = x + dir[d][0];
            ny = y + dir[d][1];

            if (-1 == grid[ny][nx].f) {
                // hit wall
                turn_right(d);
                i += grid[ny][nx].c;
            } else if (1 == grid[ny][nx].f) {
                // hit bumper
                turn_right(d);
                i += grid[ny][nx].c;
                pts += grid[ny][nx].v;
            } else {
                x = nx;
                y = ny;
            }
        }
        cout << pts << endl;
        tot += pts;
    }
    cout << tot << endl;

    return 0;
}
