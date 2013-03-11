#include "stdio.h"
//#include <algorithm>
//#include <vector>
#include <cstring>
//#include "string.h"
//#include <limits>
//#include <string>
#include <cassert>
//#include <iostream>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
int a, b;

int main()
{
	while(2 == scanf("%d%d", &a, &b) && (a || b))
	{
		int carry = 0;
		int ans = 0;
		
		while(a || b)
		{
			int d1 = a % 10;
			int d2 = b % 10;
			a /= 10;
			b /= 10;
			
			int r = d1 + d2 + carry;
			//printf("%d + %d   r=%d d1 %d d2 %d count %d\n", a, b, r, d1, d2, ans);
			if (r >= 10) {
				carry = 1;
				++ans;
			} else {
				carry = 0;
			}
		}
		
		if (ans == 0)
			printf("No carry operation.\n");		
		else if (ans == 1)
			printf("1 carry operation.\n");
		else 
			printf("%d carry operations.\n", ans);
	}

	return 0;
}