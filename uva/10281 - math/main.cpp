//STARTCOMMON
#define FOR(k,a,b) for(int k=(a); k < (int) (b); ++k)

typedef unsigned long long ull;

using namespace std;
//STOPCOMMON
#include <stdio.h>
#include <string.h>
int main()
{
char str[30];
int prev_h = 0, prev_m = 0, prev_s = 0, prev_speed = 0;
int h, m, s, speed;
double result = 0;

while(gets(str))
{
int len=strlen(str);
if(len==8)
{ sscanf(str,"%d:%d:%d",&h,&m,&s);
result += ((h * 3600 + m * 60 + s) - (prev_h * 3600 + prev_m * 60 + prev_s)) * (double)prev_speed / 3600.0;
prev_h = h;
prev_m = m;
prev_s = s;
printf("%d%d:%d%d:%d%d %.2lf km\n", h / 10, h % 10, m / 10, m % 10, s / 10, s % 10, result);
}
else
{
sscanf(str,"%d:%d:%d %d",&h,&m,&s,&speed);
result += ((h * 3600 + m * 60 + s) - (prev_h * 3600 + prev_m * 60 + prev_s)) * prev_speed / 3600.0;
prev_h = h;
prev_m = m;
prev_s = s;
prev_speed = speed;
}
}
return 0;
}