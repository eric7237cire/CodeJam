#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
#include <stdlib.h>
#include <stack>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
char buf[130];

int main()
{
	gets(buf);
	int T = atoi(buf);
	
	while(T--)
	{
		gets(buf);
		int c1 = 0; // (
		int c2 = 0; // [ 
		
		int last = 0;
		
		char* s = buf;
		bool ok = true;
		
		stack<char> st;
		
		for( char* s = buf;  *s != '\0'; ++s )
		{
			if (*s == '(' || *s == '[')
			{
				st.push( *s );				
				continue;
			} else 						
			if (*s == ']')
			{
				if ( st.empty() || st.top() != '[')
				{
					ok = false;
					break;
				}
				
				st.pop();
			} else 			
			if (*s == ')')
			{
				if ( st.empty() || st.top() != '(')
				{
					ok = false;
					break;
				}
				
				st.pop();
			} else {
				ok = false;
			}
		}
		
		if (ok && st.empty())
			puts("Yes");
		else
			puts("No");
		
	}

	return 0;
}