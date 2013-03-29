/* @JUDGE_ID: 13160EW 555 C++ */
// 06/13/03 01:30:58
// Q555: Bridge Hands


//@BEGIN_OF_SOURCE_CODE


#include <stdio.h>
#include <stdlib.h>


#define S 0
#define W 1
#define N 2
#define E 3


char poker[4][13][3] = { 0 };


char alpha[256];


int poker_comp( const void* e1,const void* e2 ) 
{
        char* b1 = (char*)e1;
        char* b2 = (char*)e2;
        if( b1[0] == b2[0] ) 
                return alpha[ b1[1] ] - alpha[ b2[1] ];
        return alpha[ b2[0] ] - alpha[ b1[0] ];
}


void out()
{
        int i;
        // s
        printf("S:");
        for(i=0;i<13;i++)
                printf(" %s", poker[S][i] );
        printf("\n");
        // w
        printf("W:");
        for(i=0;i<13;i++)
                printf(" %s", poker[W][i] );
        printf("\n");
        // n
        printf("N:");
        for(i=0;i<13;i++)
                printf(" %s", poker[N][i] );
        printf("\n");
        // e
        printf("E:");
        for(i=0;i<13;i++)
                printf(" %s", poker[E][i] );
        printf("\n");
}
int main()
{
        char c;
        char buf[60];
        int start,i,k,t;
        alpha['C'] = 4;
        alpha['D'] = 3;
        alpha['S'] = 2;
        alpha['H'] = 1;


        alpha['2'] = 0; alpha['3'] = 1;
        alpha['4'] = 2; alpha['5'] = 3;
        alpha['6'] = 4; alpha['7'] = 5;
        alpha['8'] = 6; alpha['9'] = 7;
        alpha['T'] = 8; alpha['J'] = 9;
        alpha['Q'] = 10;        alpha['K'] = 11;
        alpha['A'] = 12;


        while(1) {
                scanf("%c\n" , &c);
                if( c == '#' ) break;
                switch( c ) {
                        case 'S': start = 1 ; break;
                        case 'W': start = 2; break;
                        case 'N': start = 3; break;
                        case 'E': start = 0; break;
                } // end switch
                k = 0;
                t = 0;
                // poker 1
                gets(buf);
                for( i = 0 ; i < 52 ; i+=2) {
                        poker[start][k][0] = buf[i];
                        poker[start][k][1] = buf[i+1];
                        poker[start][k][2] = 0;
                        start = (start+1)%4;
                        t++;
                        if(t==4) t = 0 , k++;
                }
                // poker 2
                gets(buf);
                for( i = 0 ; i < 52 ; i+=2) {
                        poker[start][k][0] = buf[i];
                        poker[start][k][1] = buf[i+1];
                        poker[start][k][2] = 0;
                        start = (start+1)%4;
                        t++;
                        if(t==4) t = 0 , k++;
                }
                for(i=0;i<4;i++) 
                        qsort( &poker[i] , 13,(sizeof(char)*3),poker_comp);                     
                out(); // show four poker
        } // end while
        
        return 0;
}


//@END_OF_SOURCE_CODE