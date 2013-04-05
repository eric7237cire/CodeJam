#include<stdio.h>
#include<string.h>
int main()
{
long long a,b,c,d,e,i,l,leap,hulu,bulu,nl=0;
char s[100000];
while(gets(s))
{
a=0;
b=0;
c=0;
d=0;
e=0;
leap=0;
hulu=0;
bulu=0;
l=strlen(s);
for(i=0;i<l;i++)
    {
    a=a*10+(s[i]-'0');
    a=a%4;
    }
if(a==0)
    {
    for(i=0;i<l;i++)
        {
        b=b*10+(s[i]-'0');
        b=b%100;
        }
    if(b==0)
        {
        for(i=0;i<l;i++)
            {
            c=c*10+(s[i]-'0');
            c=c%400;
            }
        if(c==0)
        leap=1;
        }
    else 
    leap=1;
    }
for(i=0;i<l;i++)
    {
    d=d*10+(s[i]-'0');
    d=d%15;
    }
if(d==0)
hulu=1;
if(leap==1)
    {
    for(i=0;i<l;i++)
        {
        e=e*10+(s[i]-'0');
        e=e%55;
        }
    if(e==0)
    bulu=1;
    }
if(nl!=0)
printf("\n");
if(leap==1)
printf("This is leap year.\n");
if(hulu==1)
printf("This is huluculu festival year.\n");
if(bulu==1)
printf("This is bulukulu festival year.\n");
if(leap==0 && hulu==0 && bulu==0)
printf("This is an ordinary year.\n");
nl++;
}
return 0;
}