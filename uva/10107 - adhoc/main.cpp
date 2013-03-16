#include "stdio.h"
#include <algorithm>
#include <vector>
//#include <cstring>
#include <limits>
#include <list>
#include <iostream>
#include <iomanip>
#include <string>

#include <stdlib.h>
#include <ctype.h>
#include <cmath>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back 

const int MAX_SIZE = 10001;
int V[MAX_SIZE];

typedef list<int> li;

int N;

ostream& operator<<( ostream& os, const li& li)
{
	for(li::const_iterator it = li.begin(); it != li.end(); ++it)
		os << setw(5) << *it;
		
	return os;
}

int main()
{
	int i = 0;
	li L;
	
	
	while(1 == scanf("%d", &N))
	{
		li::iterator insPoint = upper_bound( L.begin(), L.end(), N );
		
		L.insert( insPoint, N );
				
		li::iterator medStart;
		li::iterator medEnd;
		if ( L.size() % 2 == 0)
		{
			medStart = L.begin();
			advance(medStart,  L.size()/2 - 1);
			medEnd = medStart;
			medEnd++;
		} else {
			medStart = L.begin();
			advance(medStart, ( L.size()+1) / 2 - 1);
			medEnd = medStart;
		}
		//printf("Med start %d end %d\n", *medStart, *medEnd);	
		//avoid overflow 
		int med = *medStart + (*medEnd - *medStart ) / 2;
		
		printf("%d\n", med);
	}
	
	return 0;
}
		