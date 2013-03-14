/*
ID:hzhua201
LANG:C++
PROB:betsy
*/
#include<stdio.h>
#include<stdlib.h>
#include<cstring>
#define mod 6133
struct node
	{
	int pid;
	int st;
	node *next;
	};
node *h[2][mod];
int pre,now;
long long num[2][40000];
int map[13][13];
int npid[2];
int ast[2][40000];
int ln,lm;
int n,m;
int tx,ty;
long long ans;
int get(int st,int pst)
	{
	int p=st%mod;
	for(node *t=h[pst][p];t;t=t->next)
		if(t->st==st)
			return t->pid;
	node *nt=new node();
	nt->st=st;
	nt->pid=++npid[pst];
	nt->next=h[pst][p];
	h[pst][p]=nt;
	num[pst][npid[pst]]=0;
	return npid[pst];
	}
void push(int st,long long delta)
	{
	if(tx==ln && ty==lm)
		{if(st==0)ans+=delta;return;}
	int pst=pre;
	int pid=get(st,pst);
	if(ty==m)st<<=2;
	num[pst][pid]+=delta;
	ast[pst][pid]=st;
	}
void clear()
	{
	for(int i=0;i<mod;i++)h[pre][i]=0;
	npid[pre]=0;
	memset(num[pre],0,sizeof(num[pre]));
	}
void run()
	{
	now=1;
	num[now][get(0,now)]=1;
	now=!now,pre=!pre;
	for(tx=1;tx<=n;tx++)
		for(ty=1;ty<=m;ty++)
			{
			pre=!pre,now=!now;
			clear();
			for(int ipos=1;ipos<=npid[now];ipos++)
				{
				int tst=ast[now][ipos];
				long long tnum=num[now][ipos];
				int py=(ty-1)*2;
				int up=(tst>>(py+2))&3;
				int left=(tst>>py)&3;
				if(!map[tx][ty])
					push(tst,tnum);
				else
					{
					if((!up && !left))
						{
						if(ty!=m && tx!=n && map[tx][ty+1] &&map[tx+1][ty])
							{
							int newst=tst+(2<<(py+2))+(1<<(py));
							push(newst,tnum);
							}
						}
					else if(up && left)
						{
						if(up==1 && left==1)
							{
							int tleft=1;
							int newst=tst^(up<<(py+2))^(left<<(py));
							for(int p=py+4;;p+=2)
								{
								int ist=(tst>>p)&3;
								if(ist==1)tleft++;
								else if(ist==2)tleft--;
								if(tleft==0)
									{newst=(newst^(2<<p))+(1<<p);break;}
								}
							push(newst,tnum);
							}
						else if(up==1 && left==2)
							{
							int newst=tst^((up<<(py+2)))^(left<<(py));
							push(newst,tnum);
							}
						else if(up==2 && left==1)
							{
							int newst=tst^((up<<(py+2)))^(left<<(py));
							if(tx==ln && ty==lm)
								push(newst,tnum);
							}
						else if(up==2 && left==2)
							{
							int tright=1;
							int newst=tst^((up<<(py+2)))^(left<<(py));
							for(int p=py-2;;p-=2)
								{
								int ist=(tst>>p)&3;
								if(ist==2)tright++;
								else if(ist==1)tright--;
								if(tright==0)
									{newst=newst^(1<<p)+(2<<p);break;}
								}
							push(newst,tnum);
							}
						}
					else if(up)
						{
						if(tx!=n && map[tx+1][ty])
							push(tst^(up<<(py+2))+(up<<(py)),tnum);
						if(ty!=m && map[tx][ty+1])
							push(tst,tnum);
						}
					else if(left)
						{
						if(tx!=n && map[tx+1][ty])
							push(tst,tnum);
						if(ty!=m && map[tx][ty+1])
							push(tst^(left<<(py))+(left<<(py+2)),tnum);
						}
					}
				}
			}
	}
int main()
	{
	freopen("betsy.in","r",stdin);
	freopen("betsy.out","w",stdout);
	//int casen;
		scanf("%d\n",&n);
		m=n;
		ans=0;
		for(int i=1;i<=3+n-1;i++)
			for(int j=1;j<=3+m+1;j++)
				map[i][j]=true;
		for(int i=3,ti=1;i<=3+n-1;i++,ti++)
			for(int j=3,tj=1;j<=3+m-1;j++,tj++)
				map[i][j]=true;
		for(int i=2;i<=3+m;i++)
			map[2][i]=false;
		for(int i=3;i<=3+n-2;i++)
			map[i][2]=map[i][3+m]=false;
		n+=2;m+=4;
		ln=n,lm=m;
		run();
		printf("%lld\n",ans);
	return 0;
	}