#include "stdio.h"
#include <utility>
#include <algorithm>
using namespace std;

int N;
pair< int, char > charCount[26];

bool cmpPair( pair< int, char > p1, pair< int, char > p2 )
{
	if (p1.first != p2.first)
		return p2.first < p1.first;
	return p1.second < p2.second;
}

int main()
{
	while(1 == scanf("%d", &N))
	{
		for(int i = 0; i < 26; ++i)
			charCount[i] = pair<int, char>(0, 'A' + i);
			
		char c = fgetc(stdin); //chomp newline
		
		for(int newLineCount = 0; newLineCount < N; )
		{
			c = fgetc(stdin);  //printf("%d %c\n", c, c);
			
			if (c == '\n' || c == EOF)
				++newLineCount;
			else if ( c >= 'a' && c <= 'z')
				charCount[ c - 'a' ].first++;
			else if ( c >= 'A' && c <= 'Z')
				charCount[ c - 'A' ].first++;
		}
		
		sort( charCount, charCount + 26, cmpPair );
		
		for( int i = 0; i < 26 && charCount[i].first > 0; ++i )
			printf("%c %d\n", charCount[i].second, charCount[i].first);
		
	}
	return 0;
}