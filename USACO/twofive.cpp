/*
ID: eric7231
PROG: twofive
LANG: C++
*/
#include<stdio.h>
#include<stdlib.h>
#include<memory.h>
FILE* fin=fopen("twofive.in","r");
FILE* fout=fopen("twofive.out","w");
long f[6][6][6][6][6],mx[6],my[6],lim[6],sum;
bool visited[50];
long count(long a, long b, long c, long d, long e, long character)
{
    if(f[a][b][c][d][e])
    {
        return f[a][b][c][d][e];
    }
    if(visited[character])
    {
        return count(a,b,c,d,e,character+1);
    }
    if(a<5 and character>mx[0] and character>my[a]) f[a][b][c][d][e]+=count(a+1,b,c,d,e,character+1);
    if(b<a and character>mx[1] and character>my[b]) f[a][b][c][d][e]+=count(a,b+1,c,d,e,character+1);
    if(c<b and character>mx[2] and character>my[c]) f[a][b][c][d][e]+=count(a,b,c+1,d,e,character+1);
    if(d<c and character>mx[3] and character>my[d]) f[a][b][c][d][e]+=count(a,b,c,d+1,e,character+1);
    if(e<d and character>mx[4] and character>my[e]) f[a][b][c][d][e]+=count(a,b,c,d,e+1,character+1);
    return f[a][b][c][d][e];
}
void solve_w()
{
    char input[30];
    fscanf(fin,"%s",input);
    for(int i=0;i<25;i++)
    {
        lim[i/5]++;
        long j;
        for(j=0;j<input[i]-'A';j++)
        {
            if(not visited[j] and j>mx[i/5] and j>my[i%5])
            {
                mx[i/5]=my[i%5]=j;
                memset(f,0,sizeof(f));
                f[5][5][5][5][5]=1;
                visited[j]=true;
                sum+=count(lim[0],lim[1],lim[2],lim[3],lim[4],0);
                visited[j]=false;
            }
        }
        visited[j]=true;
        mx[i/5]=my[i%5]=j;
    }
    fprintf(fout,"%d\n",sum+1);
}
void solve_n()
{
    char output[30];
    fscanf(fin,"%ld",&sum);
    memset(mx,-1,sizeof(mx));
    memset(my,-1,sizeof(my));
    for(int i=0;i<25;i++)
    {
        lim[i/5]++;
        int j;
        for(j=0;j<25;j++)
        {
            if(not visited[j] and j>mx[i/5] and j>my[i%5])
            {
                visited[j]=true;
                mx[i/5]=my[i%5]=j;
                memset(f,0,sizeof(f));
                f[5][5][5][5][5]=1;
                long temp=count(lim[0],lim[1],lim[2],lim[3],lim[4],0);
                if(sum<=temp)
                {
                    break;
                }
                else
                {
                    sum-=temp;
                }
                visited[j]=false;
            }
        }
        output[i]=j+'A';
    }
    output[25]=0;
    fprintf(fout,"%s\n",output);
}
int main()
{
    char way[10];
    fscanf(fin,"%s",way);
    if(*way=='W')
    {
        solve_w();
    }
    else
    {
        solve_n();
    }
    return 0;
}