#include <cstdlib>
#include <list>
#include <iostream>
#include <stdio.h>
#include <memory.h>
#include <iterator>
#include <algorithm>
using namespace std;
int n,m;
#define MAXM 250005
#define MAXN 5005
struct edge
{
    int s,t,l;
    bool operator <(const edge &rhs) const
    {
        if (l<rhs.l) return true;
        else return false;
    }
};
edge e[MAXM];
list<int> LK[MAXN];
list<int>::iterator ita,itb;
int father[MAXN];
int ans;
int me[MAXN],ee[MAXN];
bool cnt[5005][5005];
int cha(int x)
{
    if (x!=father[x])
        father[x]=cha(father[x]);
    return father[x];
}
void bing(int a,int b)
{
    int i,j;
    a=cha(a);
    b=cha(b);
    if (a==b) {ee[a]++;return ;}
    father[b]=a;
    ee[a]+=ee[b]+1;
    me[a]+=me[b];
    for(ita=LK[a].begin();ita!=LK[a].end();ita++)
        for(itb=LK[b].begin();itb!=LK[b].end();itb++)
        {
            me[a]+=cnt[(*ita)][(*itb)];
        }
    LK[a].merge(LK[b]);
}
int calced[MAXN];
void calc_ans(int i)
{
    int a;
    a=cha(e[i].s);
    if (calced[a]==e[i].l) return ;
    if ( (ee[a]==me[a])&&(calced[a]!=e[i].l) )
    {
        ans+=LK[a].size();
        calced[a]=e[i].l;
    }
}
int main(int argc, char** argv) {
    int i,j,t,u,x,y,z;
    cin>>t;
    for(u=1;u<=t;u++)
    {
        cin>>n>>m;
        memset(cnt,0,sizeof(cnt));
        for(i=1;i<=m;i++)
        {
            cin>>e[i].s>>e[i].t>>e[i].l;
            cnt[e[i].s][e[i].t]=cnt[e[i].t][e[i].s]=1;
        }
        sort(e+1,e+m+1);
        e[0].l=-1;
        ans=0;
        memset(me,0,sizeof(me));
        memset(ee,0,sizeof(ee));
        memset(calced,-1,sizeof(calced));
        for(i=1;i<=n;i++)
        {
            father[i]=i;
            LK[i].clear();
            LK[i].push_back(i);
        }
        int lasti=m+1,ii;
        for(i=m;i>=1;i--)
        {
            if (e[i].l==e[i-1].l)
            {
                bing(e[i].s,e[i].t);
            }
            else
            {
                bing(e[i].s,e[i].t);
                for(ii=i;ii<lasti;ii++)
                calc_ans(ii);
                lasti=i;
            }
        }
        cout<<ans<<endl;

    }
    return 0;
}
