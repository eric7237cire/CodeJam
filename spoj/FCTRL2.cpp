#include "stdio.h"

void factorial(int input);

int main()
{
	int n;
	int T;
	scanf("%d", &T);
	for(int t=1; t <= T; ++t)
	{
		scanf("%d", &n);
		factorial(n);
		printf("\n");
	}
	return 0;   
}

void factorial(int input){
     //This array stores the result 
     //Hence the result can be upto 200 digits.
     int arr[200]; 
     //This m keeps track of no. of digits in the result
     int m=0;      
     int i,index,tempresult,k;
     int temp=input;
     //after the end of this while loop the input 
     //will be stored as induvidual digits in the array
     //if 345 is the input , then array becomes {5,4,3}
     while(input>0){
      arr[++m]=input%10;
      input/=10;               
     }
     
     input=temp;
     temp=0;
     for(i=(input-1);i>=1;i--){
     //the equivalent operation performed here is result = result *i;
     //But using the pen paper algorithm
       for(index=1;index<=m;index++){
        tempresult = (arr[index]*i)+temp;
        arr[index] = tempresult%10;
        temp = tempresult/10;                              
       }
       
       while(temp>0){
         arr[++m]=temp%10;
         temp/=10;               
        }
                               
     }
     
     //Now it is time for the result to be printed
     //If you had followed the above code ,
     // then you would notice that the result
     //actually in the reverse order in the array
     for(i=m;i>=1;i--){
     printf("%d",arr[i]);                  
     }
}