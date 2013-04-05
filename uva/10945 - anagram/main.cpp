#include <cstdio>
#include <string>
#include <cstring>
#include <iostream>
#include <algorithm>
#include <cstdlib>
#include <cassert>
#include <cmath>
#include <vector>
#include <set>
#include <map>
#include <stack>
#include <queue>
#include <deque>
#include <bitset>
#include <fstream>
#include <iomanip>
#include <sstream>
#include <complex>
#include <cctype>
#include <ctime>

using namespace std;

//Commonly used macros
#define FOREACH(i,a)    for(typeof((a).begin()) i=(a).begin();i!=(a).end();i++)
#define SZ(a)           (int)(a.size())
#define ALL(a)          (a).begin(),(a).end()
#define SORT(a)         sort(ALL(a));
#define REVERSE(a)      reverse(ALL(a))
#define UNIQUE(a)       SORT(a);(a).resize(unique(ALL(a))-(a).begin())
#define IN(a,b)         ((b).find(a)!=(b).end())
#define fill(x,a)       memset(x, a, sizeof(x))
#define abs(a)          ((a)<0?-(a):(a))
#define maX(a,b)        ((a)>(b)?(a):(b))
#define miN(a,b)        ((a)<(b)?(a):(b))
#define checkbit(n,b)   ((n>>b)&1)

//Main code begins here
string s;
int n;

bool isLetter(char c){return ((c>='A' && c<='Z') || (c>='a' && c<='z'));}
bool isUpperCase(char c){return c>='A' && c<='Z';}
bool isLowerCase(char c){return c>='a' && c<='z';}
char toLowerCase(char c){return (isUpperCase(c))?(c+32):c;}

void solve()
{
	if(n==1) {printf("You won't be eaten!\n"); return;}
	bool f=true;
	char *start = &s[0];
	char *end = &s[n-1];
	while(start <= end)
	{
		while(!isLowerCase(*start)) start++;
		while(!isLowerCase(*end)) end--;
		if(*start!=*end) {f=false; printf("Uh oh..\n"); break;}
		else start++; end--;
	}
	if(f) printf("You won't be eaten!\n");
	return;
}

int main()
{
	while(1)
	{
		getline(cin,s);
		if(s=="DONE") return 0;
		n=SZ(s);
		for(int i=0; i<n; ++i)
		{
			if(!isLetter(s[i])) continue;
			s[i]=toLowerCase(s[i]);
		}
		solve();
	}
	return 0;
}