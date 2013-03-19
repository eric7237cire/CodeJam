#include <stdio.h>
int main()
{
    unsigned long long int input,count,j;
    
	//Player can force 
	
    while(scanf("%llu",&input)==1){
      count=1;
      j=0;
      while(count<input){
        if(j%2==0)count=count*9;
        else count=count*2;
        j++;
      }
      if(j%2)printf("Stan wins.\n");
      else printf("Ollie wins.\n");
    }
 
 
 
    return 0;
}