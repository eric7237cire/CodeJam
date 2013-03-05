#include<iostream>
#include<vector>
#include<cstdio>
#include<cstring>
#define MAXNEG -2134483648
using namespace std;

vector< int > Seq;
vector< int > Len,Pre;

int LIS(int);
void PrintLIS(int n);

int main()
{
    int t,n,k=1,f=1;
    char inp[15];
    //freopen("in.txt","r",stdin);
    gets(inp);
    sscanf(inp,"%d",&t);
    gets(inp);
    
    for(k=1;k<=t;k++)
    {
        Seq.clear();
        int z = Seq.size();
        Seq.push_back(MAXNEG);
        z = Seq.size();
        while(gets(inp))
        {
            if(sscanf(inp,"%d",&n)==1)
                Seq.push_back(n);
            else
                break;
        }

        n = Seq.size();
        n = LIS(n);

        if(!f)printf("\n");
        else f = 0;

        printf("Max hits: %d\n",Len[n]);
        PrintLIS(n);
    }
    return 0;
}

int LIS(int n)
{
    int i,k,tmp;
    Len = vector < int >(n);
    Pre = vector < int >(n);
    for(i=0;i<n;i++)
        Len[i] = 0;

    for(k = 1;k < n; k++)
    {
        for(i = k-1; i>=0; i--)
        {
            tmp = 1 + Len[i];
            if(Seq[k] > Seq[i] && Len[k] < tmp)
                Len[k] = tmp,Pre[k] = i;
        }
    }

    tmp = Len[1],k = 1;
    for(i=2;i<n;i++)
        if(tmp < Len[i])
            tmp = Len[i], k = i;
    return k;
}

void PrintLIS(int n)
{
    if(Pre[n]==0)
    {
        printf("%d\n",Seq[n]);
        return;
    }
    PrintLIS(Pre[n]);
    printf("%d\n",Seq[n]);
}
