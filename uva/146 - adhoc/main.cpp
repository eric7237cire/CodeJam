#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
#include <string>
#include <stdlib.h>
#include <iostream>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
int main()
{
	while(true)
	{
		string s;
		cin >> s;
		
		if (s == "#")
			break;
			
		bool ok = next_permutation(s.begin(), s.end());
		
		if (!ok)
			puts("No Successor");
		else
			cout << s << endl;
			
	}
	
	return 0;
}