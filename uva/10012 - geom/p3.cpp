#include<stdio.h>
#include<string.h>
#include<math.h>
int n,vis[10];
double a[10],A[10],c[10],ans;
void dfs(int cur)
{
    int i,j;
    double move,temp,res;
    if(cur==n)
    {
        move=0.0;
        for(i=0;i<n;i++)
        {
            temp=A[i]-c[i];
            if(temp>move)
                move=temp;
            for(j=0;j<n;j++)
                c[j]+=move;
        }
        res=0.0;
        for(i=0;i<n;i++)
        {
            temp=c[i]+A[i];
            if(temp>res)
                res=temp;
        }
        if(res<ans)
            ans=res;
        return;
    }
    for(i=0;i<n;i++)
        if(!vis[i])
        {
            A[cur]=a[i];
            c[cur]=0.0;
            for(j=0;j<cur;j++)
            {
                temp=c[j]+2*sqrt(A[cur]*A[j]);
                if(temp>c[cur])
                    c[cur]=temp;
            }
            vis[i]=1;
            dfs(cur+1);
            vis[i]=0;
        }
}
int main()
{
    int i,j,k,t;
    
    scanf("%d",&t);
    while(t--)
    {
        scanf("%d",&n);
        for(i=0;i<n;i++)
            scanf("%lf",&a[i]);
        ans=1000000000.0;
        memset(vis,0,sizeof(vis));
        for(i=0;i<n;i++)
        {
            A[0]=a[i];
            c[0]=a[i];
            vis[i]=1;
            dfs(1);
            vis[i]=0;
        }
        printf("%.3f\n",ans);
    }
    return 0;    
}