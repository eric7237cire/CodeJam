#include <stdio.h>
#include <math.h>
static int list[105];
static int memo[10005];
int pow2(int x)
{
    return x*x;
}
int min(int a,int b)
{
return (a<b) ? a:b;
}
int main()
{
    int x,q,i,j,r,c,t,n;
    for(i=1;i<101;i++)
        list[i]=pow2(i);
    for(i=0;i<10005;i++)
        memo[i]=1<<25;
    memo[0]=0;
    for(i=1;i<101;i++)
        for(j=list[i];j<10005;j++)
            memo[j]=min(memo[j],1+memo[j-list[i]]);
    scanf("%d",&x);
    while(x-->0)
    {
        scanf("%d",&n);
        printf("%d\n",memo[n]);
    }
 
 
    return 0;
}