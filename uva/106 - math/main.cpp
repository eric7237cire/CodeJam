
// Fermat vs. Pythagoras
// Time-stamp: <2012-08-16 15:07:00 gongzhitaao>

#include <cmath>
#include <iostream>
using namespace std;

int gcd(int a, int b)
{
	int t;
	while (b) {
		t = b;
		b = a % b;
		a = t;
	}
	return a;
}

bool relative_prime(int a, int b)
{
	return gcd(a, b) == 1;
}

bool relative_prime(int a, int b, int c)
{
	int ab = gcd(a, b);
	return 1 == ab ? true : gcd(ab, c) == 1;
}

int main()
{
	const int MAX = 1000000;
	int N;
	int a, b, c;
	bool counted[MAX+1];
	while (cin >> N) {

		for (int i = 0; i <= N; ++i)
			counted[i] = false;
		int prime_tri = 0, p = N;
		int m, n;
		int k;

		int u = (int)sqrt(N) + 1;
		for (m = 1; m < u; ++m) {
			for (n = 1; n < m; ++n) {

				c = m*m + n*n;
				if (c > N) break;

				a = m*m - n*n;;
				b = (m*n) << 1;

				k = N / c + 1;
				for (int i = 1; i < k; ++i) {
					if (!counted[a*i]) counted[a*i] = true, --p;
					if (!counted[b*i]) counted[b*i] = true, --p;
					if (!counted[c*i]) counted[c*i] = true, --p;
				}

				if (relative_prime(a, b, c)) ++prime_tri;
			}
		}

		cout << prime_tri << ' ' << p << endl;
	}
	return 0;
}