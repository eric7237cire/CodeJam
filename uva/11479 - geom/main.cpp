/**
 @Author  : Vu Van Tuyen
 @Content :  11479
 @Description:  
*/
 
#include<fstream>
#include<iostream>
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<string.h>
#define fi "11479.inp"
#define fo "11479.out"
using namespace std;
int main()
{
 //freopen(fi,"r",stdin);
 //freopen(fo,"w",stdout); 
 long long int n,a,b,c,k,j=0;
 scanf("%lld\n",&n);
 while(n--)
 {
  j++;
  scanf("%lld %lld %lld\n",&a,&b,&c);
  if(a>b)
  {
   if(a>c)
   {
    k=c;
    c=a;
    a=k;
   }
  }
  else
  {
   if(b>c)
   {
    k=c;
    c=b;
    b=k;
   }
  }
 
  if(c>=(a+b))
   printf("Case %d: Invalid\n",j);
  else if(c==a && c==b && a==b)
   printf("Case %d: Equilateral\n",j);
  else if(a!=b && b!=c && c!=a)
   printf("Case %d: Scalene\n",j);
  else if(a==b || b==c || c==a)
   printf("Case %d: Isosceles\n",j);
 }
 return 0;
}