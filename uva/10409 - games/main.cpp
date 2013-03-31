/* @JUDGE_ID: 13160EW 10409 C++ */
// 06/13/03 00:27:15
// Q10409: Die Game
//@BEGIN_OF_SOURCE_CODE

#include <stdio.h>
#include <string.h>
int top,north,west;

void init()
{
        top = 1;
        north = 2;
        west = 3;
}

int main()
{ 
        int i,n;
        int temp;
        char dir[10];
        while(1) {
                scanf("%d",&n);
                if( n<=0 ) break;
                init();
                for(i=0;i<n;i++) {
                        scanf( "%s" , dir );
                        if( strcmp(dir,"north") == 0 ) {
                                temp = 7 - north;
                                north = top;
                                top = temp;
                                
                        } else if( strcmp(dir,"west") == 0 ) {
                                
                                temp = 7 - west;
                                west = top;
                                top = temp;
                        } else if( strcmp(dir,"south") == 0 ) {
                                
                                temp = 7 - top;
                                top = north;
                                north = temp;
                        } else if( strcmp(dir,"east") == 0 ) {
                                
                                temp = 7 - top;
                                top = west;
                                west = temp;
                        }
                } // end for
                printf("%d\n",top);
        } // end while



        return 0;
}