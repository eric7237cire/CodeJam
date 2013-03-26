/* @JUDGE_ID:  51710HU  576  C  "" */

#include<stdio.h>
#include<ctype.h>
#include<string.h>

int isaeiouy(char);

main()
{
    char sen[200];
    int  i,j,num[3];
    while(gets(sen))
    {
       if ( strcmp(sen,"e/o/i") == 0)
       break;
       for ( i=0 ; i < 3 ; i++)
        num[i]=0;
       j=0;
       for( i = 0 ; sen[i] != '\0'; i++ ) {
            if ( sen[i] == '/' )
             j++;
            if ( isaeiouy(sen[i]) == 1 && isaeiouy(sen[i+1]) != 1)
               num[j]++;
           }

       if ( num[0] != 5 )
        printf("1\n");
       else if ( num[1] != 7)
        printf("2\n");
       else if ( num[2] != 5)
        printf("3\n");
       else
        printf("Y\n");

   }
}

int isaeiouy(char c)
{
 if ( c == 'y' || c == 'a' || c == 'e' || c == 'o' || c == 'u' ||  c == 'A' || c == 'E' || c == 'O' || c == 'U' || c == 'I' || c == 'i')
 return 1;
 else
 return 0;
}


/* @END_OF_SOURCE_CODE */