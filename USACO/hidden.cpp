/*
ID: eric7231
PROG: hidden
LANG: C++
*/
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
FILE* fin=fopen("hidden.in","r");
FILE* fout=fopen("hidden.out","w");
int n;
char s[200005],S[200005];
int main()
{
    fscanf(fin,"%d\n",&n);
    while(fscanf(fin,"%s",s)!=EOF)
    {
        strcat(S,s);
    }
    strcpy(s,S), strcat(S,s);
    int i=0,j=1,k=0;
    while(j<n)
    {
        for(k=0;k<n;k++)
        {
            if(S[i+k]!=S[j+k])
            {
                break;
            }
        }
        if(k==n)
        {
            break;
        }
        if(S[i+k]>S[j+k])
        {
            i+=k+1;
        }
        else
        {
            j+=k+1;
        }
        if(j==i)
        {
            j++;
        }
    }
    fprintf(fout,"%d\n",i);
    return 0;
}