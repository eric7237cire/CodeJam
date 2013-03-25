/* @JUDGE_ID: 13160EW 642 C++ */
// 01/17/04 10:57:12
// 642 : Word Amalgamation 
//@BEGIN_OF_SOURCE_CODE 

#include <stdio.h> 
#include <string.h>

struct sPair {
        char org[10];
        char after[10];
} pair[101];

void DoSort(char* str) 
{
        int i,j;
        int n = strlen(str);
        for(i=0;i<n-1;i++)
                for(j=i+1;j<n;j++)
                        if( str[i] > str[j] )
                                str[i] ^= str[j] ^= str[i] ^= str[j] ;
}

int main()
{
        int n = 0 ;
        // read dictory 
        while( gets( pair[n].org ) ) {
                if( pair[n].org[0] == 'X' ) break;
                strcpy( pair[n].after , pair[n].org );
                DoSort( pair[n].after );
                n++;
        }
        int i,j,k;
        char buf[10];
        char answer[100][10];
        while( gets( buf ) ) {
                if( buf[0] == 'X' ) break;
                DoSort( buf );
                for( i = 0 ; i < n && strcmp( pair[i].after , buf) != 0 ; i++);
                if( i == n  ) {
                        printf("NOT A VALID WORD\n");
                } else {
                        j = 0 ;
                        strcpy( answer[j++] , pair[i++].org );
                        // find all answer
                        for( ; i < n ; i++)
                                if( strcmp( pair[i].after , buf) == 0) 
                                        strcpy( answer[j++] , pair[i].org );
                        // do sort each answer
                        for( i = 0 ; i < j-1 ; i++)
                                for( k = i+1 ; k < j ; k++)
                                        if( strcmp( answer[i] , answer[k] ) > 0  ) {
                                                strcpy( buf , answer[i] );
                                                strcpy(answer[i] , answer[k] );
                                                strcpy( answer[k] , buf );
                                        }
                        // show the answer
                        for(i=0;i<j;i++)
                                printf("%s\n" , answer[i] );
                }
                printf("******\n");

        }


        return 0;
}