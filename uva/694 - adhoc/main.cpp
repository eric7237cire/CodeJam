#include<stdio.h>
#include<stdlib.h>

typedef long long ll;
int numberOfTerms(ll A,ll L)
{
    int n=0;
    while(A<=L&& A!=1)
    {
        if(A&1)
            A=3*A+1;
        else
            A>>=1;
            n++;
    }
    if(A==1)n++;
    return(n);
}
int main()
{
long cases=1,n,A,L;

while(scanf("%ld%ld",&A,&L)==2 && A>0 && L>0)
{
    n=numberOfTerms(A,L);
    printf("Case %ld: A = %ld, limit = %ld, number of terms = %ld\n",cases++,A,L,n);
}
return(0);
}