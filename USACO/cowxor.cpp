/*
ID: eric7231
LANG: C++
TASK: cowxor
*/

#include<iostream>
#include<fstream>
using namespace std;
ifstream fin("cowxor.in");
ofstream fout("cowxor.out");
 
const int MAX=100000*5+10;
int n;
int num[MAX],a[MAX];
 
int next[MAX][2],end[MAX],end_id[MAX],size=0;
int answer,answerx,answery;
 
int bit(int a,int i)
{return (a>>(i-1))&1;}
 
void init(int now)
{
	next[now][0]=next[now][1]=-1;
	end[now]=0,end_id[now]=0;
}
 
void insert(int k,int id)
{
	int now=0,idx,j;
	for(j=21;j>=1;--j)
	{
		idx=bit(k,j);
		if(next[now][idx]==-1)
		{
			init(++size);
			next[now][idx]=size;
		}
		now=next[now][idx];
	}
	end[now]=k;
	end_id[now]=id;
}
 
void check(int k,int id)
{
	int now=0,idx,j;
	for(j=21;j>=1;--j)
	{
		idx=bit(k,j)^1;
		if(next[now][idx]!=-1)
			now=next[now][idx];
		else now=next[now][idx^1];
	}
	if((k^end[now])>answer)answer=k^end[now],answerx=end_id[now]+1,answery=id;
}
 
int main()
{
	int i,j,k;
	fin>>n;
	for(i=1;i<=n;++i)
		fin>>num[i];
	for(i=1;i<=n;++i)
		a[i]=num[i]^a[i-1];
	init(0);
	for(i=1;i<=n;++i)
	{
		check(a[i],i);
		insert(a[i],i);
	}
	if(answerx==0)answerx=answery=1;
	fout<<answer<<" "<<answerx<<" "<<answery<<endl;
}