#include "stdio.h"
//#include <algorithm>
//#include <vector>
#include <cstring>
//#include "string.h"
//#include <limits>
//#include <string>
#include <cassert>
#include <iostream>
#include <stdlib.h>

typedef long long ll;

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back

ll rev(ll n)
{
	ll r = 0;
	
	while(n != 0)
	{
		r *= 10;
		r += n % 10;
		assert(n != n / 10);
		n /= 10;
		//printf("rev r %lld n %lld\n", r, n);
		//cout << r << " " << n << endl;
	}
	return r;
}
 
int main()
{
	int T;
	scanf("%d", &T);
	
	while(T--)
	{
		ll cur;
		scanf("%llu", &cur);
		
		ll last = -1;
		ll iter = 0;
		while(cur != last && iter < 2000)
		{
			last = cur;
			ll curRev = rev(cur);
			if (iter && cur == curRev)
				break;
			cur = cur + curRev;
			++iter;
			//printf("last %lld cur %lld\n", last, cur);
			//cout << last << " " << cur << endl;
		}
		
		cout << iter << " " << cur << endl; //("%d %lld\n", iter, cur);		
	}

	return 0;
}