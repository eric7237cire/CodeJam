#include<cstdio>

#include<cstring>
#include<queue>
#include<map>
#include<string>
using namespace std;
#define M 2020
#define INF 1<<27
int n,m,s,t,k,l;
int head[M],end[M];
int flow[M][M],cap[M][M],a[M],p[M];

int bfs()
{
    queue<int>q;
    memset(flow,0,sizeof(flow));
    int f = 0;
    while(1)
    {
        memset(a,0,sizeof(a));
        a[s] = INF;
        q.push(s);
        while(!q.empty())
        {
            int u = q.front();
            q.pop();
            for(int v = 1; v <= l; v++)
                if(!a[v]&&cap[u][v]>flow[u][v])
                {
                    p[v] = u;
                    q.push(v);
                    a[v] = min(a[u],cap[u][v]-flow[u][v]);
                }
        }
        if(!a[t]) break;
        for(int u = t; u != 0; u = p[u])
        {
            flow[p[u]][u] += a[t];
            flow[u][p[u]] -= a[t];
        }
        f += a[t];

    }
    return f;
}
int main()
{
    int te;
    char s1[403],s2[430],s3[100],s4[100];
    scanf("%d",&te);
    while(te--)
    {
        map<string,int> str;
        str.clear();
        scanf("%d",&n);
        getchar();
        s = 0;
        t = 1;
        l = 2;
        for(int i = 1; i <= n; i++)
        {
            gets(s1);
            if(!str[s1]) str[s1] = l++;
            cap[str[s1]][t]++;
        }
        scanf("%d",&m);
        getchar();
        for(int i = 1; i <= m; i++)
        {
            scanf("%s %s",s2,s3);
            if(!str[s3]) str[s3] = l++;
            cap[s][str[s3]]++;
            getchar();
        }
        scanf("%d",&k);
        int u,v;
        for(int i = 1; i <= k; i++)
        {
            scanf("%s %s",s2,s4);
            if(!str[s2]) str[s2] = l++;
            u = str[s2];
            if(!str[s4]) str[s4] = l++;
            v = str[s4];
            cap[u][v] = INF;
        }
        printf("%d\n",m-bfs());
        if(te) printf("\n");
        memset(cap,0,sizeof(cap));
        memset(p,0,sizeof(p));
    }
    return 0;
}