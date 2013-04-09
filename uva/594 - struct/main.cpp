#include<iostream>
#include<cstring>
#include<iomanip>
#include<iostream>
#include<sstream>
#include<string>
#include<cstdio>
#include<cstdlib>
using namespace std;
int main()
{
  int num;
	while(scanf("%d",&num)!=EOF)
	{
		int x;
		x=((num<<8) & 0xFF00FF00) | ((num>> 8) & 0xFF00FF);
		x=(x<<16) | ((x>>16) & 0xFFFF);
		printf("%d converts to %d\n",num,x);
	}
	return 0;
}