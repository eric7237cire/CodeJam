/**
 @Author  : Vu Van Tuyen
 @Content :  11547
 @Description:  
*/
 
#include<fstream>
#include<iostream>
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<string.h>
#define fi "11547.inp"
#define fo "11547.out"
using namespace std;
int main()
{
 //freopen(fi,"r",stdin);
 //freopen(fo,"w",stdout);
 long long int t,n,k;
 scanf("%lld",&t);
 while(t--)
 {
  scanf("%lld",&n);
  k=(n*567)/9;
  k=(k+7492)*235;
  k=(k/47)-498;
  if(k<0)
  k*=-1;
  k=(k/10)%10;
  printf("%lld\n",k);
 }
 return 0;
}