#include "stdio.h"
#include <algorithm>
//#include <vector>
#include <cstring>
//#include "string.h"
//#include <limits>
//#include <string>
//#include <set>
#include <cassert>
#include <iostream>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 



int main()
{
	
	//same as !cin.fail()
	for (std::string line; getline(cin, line);) 
	{
		int ws = -1;
		
		for(int i = 0; i < line.length(); ++i)
		{
			//printf("Read %c at %d\n", line[i], i);
			
			bool isBoundary = line[i] == ' ';
			
			if (!isBoundary && ws == -1)
			{
				ws = i;
				//printf("starting word at %d\n", i);
			} else if (isBoundary && ws != -1)
			{
				reverse(line.begin() + ws, line.begin() + i );
				ws = -1;
			}			
		
		}
		
		if (ws != -1)
			reverse(line.begin() + ws, line.end() );
		
		cout << line << endl;
		
	}
	
	return 0;
}