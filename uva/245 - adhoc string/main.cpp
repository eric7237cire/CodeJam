#include <list>
#include <string>

#include <stdio.h>
#include <string.h>
#include <ctype.h>
using namespace std;

#define size 10000
list<string> table;

void find_str_in_table(int n)
{
        int i = 1;
        list<string>::iterator 
                begin = table.begin(),
                end = table.end();
        // find n-th word
        while( i!=n && begin!= end ) {i++, begin++;}

        printf("%s",begin->c_str() );
        string temp = *begin;
        table.erase( begin );
        table.push_front( temp );
}

int main()
{
	string buf;
	buf.resize(51);
	int bi;
	char line[size];
	char* pline;
	int n;
	while( gets(line) ) 
	{
		pline= line;
		if( *pline=='0' && *(pline+1)==0) break;
		bi = 0;
		while( *pline ) 
		{
			if( isalpha(*pline) ) 
			{
				// find the word
				bi = 0;
				while( *pline &&  isalpha(*pline) ) buf[bi++] = *pline++;
				buf[bi] = 0;
				printf("%s" , buf.c_str() );
				table.push_front( buf );
			} else if( *pline >= '0' && *pline <= '9' ) 
			{
				// find the number
				n = 0;
				while( *pline &&  *pline >= '0' && *pline <= '9' ) {
						n = n*10 + (*pline-'0');
						pline++;
				}
				// find it from string table
				find_str_in_table( n );
			} else
					printf("%c",*pline++ );                 
		}
		printf("\n");
	}       
	return 0;
}
