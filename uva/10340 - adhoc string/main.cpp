#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
#include <stdlib.h>
#include <cmath>
#include <string> 
#include <iostream>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 

int main()
{
	
	while(cin)
	{
		string s1;
		string s2;
		
		cin >> s1 >> s2;
		
		if (cin.fail())
			break;
		
		string::iterator it1 = s1.begin();
		string::iterator it2 = s2.begin();
	
		bool ok = true;
		
		while(it1 != s1.end())
		{
			while( *it2 != *it1 && it2 != s2.end())
				++it2;
				
			if ( *it2 != *it1 )
			{
				ok = false;
				break;
			}
			
			++it1;
			
			if (it2 == s2.end()) {
				ok = false;
				break;
			}
			++it2;
		}
		
		if (ok)
			puts("Yes");
		else
			puts("No");
		
	}

	return 0;
}