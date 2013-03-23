//STARTCOMMON
#include <cmath>
#include "stdio.h" 
#define FOR(k,a,b) for(int k=(a); k < (int) (b); ++k)

typedef unsigned long long ull;

const double tolerance = 0.000002;
template<typename T> 
int cmp(T a, T b, T epsilon = tolerance)
{
	T dif = a - b;
	if (abs(dif) <= epsilon)
	{
		return 0;
	}
	
	if (dif > 0)
	{
		return 1; //a > b
	}
	
	return -1;
}  

using namespace std;
//STOPCOMMON

#include <stdio.h>
int main()
{
  int i,j,k,l,ar[500],N,U,D,countUP,countDOWN;
  int flagU,flagD,upON,downON,help;
  double resUP,resDOWN;
  
  while(scanf("%d",&ar[0])==1&&ar[0]!=0){
    i=1;
    while(scanf("%d",&ar[i])==1&&ar[i]!=0)i++;
 
    countDOWN=0;
    countUP=0;
    N=0;flagD=0;
    U=0;flagU=0;
    D=0;
    upON=0;
    downON=0;
    help=0;
    for(j=1;j<i;j++){
      if(ar[j]>ar[j-1]){
          if(downON==1 || help==0){
              countUP++;
            help=1;
          }
          upON=1;
          downON=0;
        flagU=1;
        flagD=0;
        U+=N+1;
        N=0;
 
      }
      if(ar[j]<ar[j-1]){
          if(upON==1 || help==0){
              countDOWN++;
            help=1;
          }
          upON=0;
          downON=1;
        flagU=0;
        flagD=1;
      D+=N+1;
      N=0;
      }
      if(ar[j]==ar[j-1]){
          if(flagD){
           D+=1;
           N=0;
          }
          else if(flagU){
          U+=1;
          N=0;
          }
      else N++;
      }
 
    }
    if(flagD)D+=N;
    else if(flagU)U+=N;
 
    printf("Nr values = %d:  ",i);
    if(countUP==0)printf("0.000000 ");
    else {
     resUP=(double)U/(double)countUP;
     printf("%.6lf ",resUP);
 
    }
    if(countDOWN==0)printf("0.000000\n");
    else {
      resDOWN=(double)D/(double)countDOWN;
      printf("%.6lf\n",resDOWN);
    }
 
    }
 
 
    return 0;
}