#include "stdio.h"
 
int num;

int main() 
{
	while( 1==scanf("%d", &num) && num != 42) 
	{
		printf("%d\n", num);
	}
 
	return 0;	
}