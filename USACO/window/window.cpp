/*
 ID: eric7231
 PROG: window
 LANG: C++
 */
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <stack>
#include <set>
#include <list>
#include <vector>
#include <algorithm>
#include <cassert>
#include <iterator>
#include <sstream>
#include <complex>
#include <bitset>
#include <iomanip>
#include <cctype>
#include <limits>
#include <cmath>
#include <functional>
#include <queue>
#include <cstring>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(int k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (int) (b); ++k)

#define pb push_back
#define mp make_pair

typedef vector<int> vi;
typedef vector<double> vd;
typedef vector<vi> vvi;
typedef vector<uint> uvi;
typedef vector<uvi> uvvi;
typedef vector<vd> vvd;
typedef pair<int, int> ii;
typedef pair<uint, uint> uu;
#define sz(a) int((a).size())
#define pb push_back
#define all(c) (c).begin(),(c).end()
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++)
#define contains(c,x) ((c).find(x) != (c).end())
#define cpresent(c,x) (find(all(c),x) != (c).end())


const int maxn=256;

int head,tail;
int prev[maxn];
int next[maxn];

inline void addhead(int x){
	prev[x]=-1;
	next[x]=head;
	if(head!=-1)prev[head]=x;
	head=x;
	if(tail==-1)tail=x;
}
inline void addtail(int x){
	next[x]=-1;
	prev[x]=tail;
	if(tail!=-1)next[tail]=x;
	tail=x;
	if(head==-1)head=x;
}
inline void remove(int x){
	if(x!=tail)prev[next[x]]=prev[x];
	if(x!=head)next[prev[x]]=next[x];
	if(x==tail)tail=prev[x];
	if(x==head)head=next[x];
}

char s[maxn];
int L[maxn],R[maxn],U[maxn],D[maxn];


int cut(int l,int r,int u,int d,int k){
	//Find the first rectangle that intersects the check coordinates
	while(k!=-1&&(R[k]<=l||L[k]>=r||U[k]>=d||D[k]<=u))k=next[k];
	if(k==-1)return (r-l)*(d-u);
	int res=0;
	if(L[k]>l){
		res+=cut(l,L[k],u,d,next[k]);
		l=L[k];
	}
	if(R[k]<r){
		res+=cut(R[k],r,u,d,next[k]);
		r=R[k];
	}
	if(U[k]>u)
		res+=cut(l,r,u,U[k],next[k]);
	if(D[k]<d)
		res+=cut(l,r,D[k],d,next[k]);
	return res;
}

inline void solve_w(int n){

	//Read left, up, right, down
	sscanf(s+4,"%d,%d,%d,%d",L+n,U+n,R+n,D+n);
	//Swap if necessary
	if(R[n]<L[n])swap(R[n],L[n]);
	if(D[n]<U[n])swap(U[n],D[n]);
	addtail(n);
}
inline void solve_t(int n){
	remove(n);
	addtail(n);
}
inline void solve_b(int n){
	remove(n);
	addhead(n);
}
inline void solve_d(int n){
	remove(n);
}
inline void solve_s(int n){
	int m=cut(L[n],R[n],U[n],D[n],next[n]);
	printf("%.3lf\n",100.0*(double)m/((R[n]-L[n])*(D[n]-U[n])));
}

inline void solve(int I){
	switch(s[0]){
		case 'w':
			solve_w(I);
			break;
		case 't':
			solve_t(I);
			break;
		case 'b':
			solve_b(I);
			break;
		case 'd':
			solve_d(I);
			break;
		case 's':
			solve_s(I);
			break;
	}
}

int main(){
	head=tail=-1;
	memset(prev,-1,sizeof(prev));
	memset(next,-1,sizeof(next));

	freopen("window.in","r",stdin);
	freopen("window.out","w",stdout);

	do{
		scanf("%s\n",s);
		//Returns identifier of window
		int I = s[2];
		solve(I);
	}while(!feof(stdin));
}
