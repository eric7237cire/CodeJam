#include  <stdio.h>
#include <string.h>
#include <iostream>

using namespace std;

int main(){
 int len,i,j,k,a,b,hi;
 char str[100000],mirr[300];
 //freopen("i.txt","r",stdin);

 memset(mirr,NULL,sizeof(mirr));
 mirr['A']='A';
 mirr['E']='3';
 mirr['H']='H';
 mirr['I']='I';
 mirr['J']='L';
 mirr['L']='J';
 mirr['M']='M';
 mirr['O']='O';
 mirr['S']='2';
 mirr['T']='T';
 mirr['U']='U';
 mirr['V']='V';
 mirr['W']='W';
 mirr['X']='X';
 mirr['Y']='Y';
 mirr['Z']='5';
 mirr['1']='1';
 mirr['2']='S';
 mirr['3']='E';
 mirr['5']='Z';
 mirr['8']='8';
 while(scanf("%s",&str)==1){
  a=b=1;
  len=strlen(str);
  hi=len/2;
  if(len & 1)
   hi++;
  for(i = 0, j = len-1; i < hi; i++,j-- ){
   if(a){
    if(str[i]!=str[j])
     a=0;
   }
   if(b){
    if(mirr[str[i]]!=str[j])
     b=0;
   }
  }

  if (a && b)
   printf ("%s -- is a mirrored palindrome.",str);
  else if (a)
   printf ("%s -- is a regular palindrome.",str);
  else if (b)
   printf ("%s -- is a mirrored string.",str);
  else
   printf ("%s -- is not a palindrome.",str);
  printf("\n\n");
 }
 return 0;
}

