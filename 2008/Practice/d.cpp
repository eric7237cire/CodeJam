#include <cstdio>
#include <algorithm>
#include <string>
#include <cassert>
#include <vector>
#include <cmath>
#include <queue>
using namespace std;

#define PB push_back
#define SZ(v) ((int)(v).size())
#define MP make_pair
#define FOR(i,a,b) for(int i=(a);i<(b);++i)
#define REP(i,n) FOR(i,0,n)
#define FORE(i,a,b) for(int i=(a);i<=(b);++i)
#define REPE(i,n) FORE(i,0,n)

typedef long long ll;
typedef vector<int> VI;
typedef vector<VI> VVI;
typedef vector<VVI> VVVI;
typedef vector<ll> VLL;
typedef vector<VLL> VVLL;


int nitems,nstores,gasprice;

char buff[200000];
char tmp[2000];
char name[15][2000];
int len[15];
int perishable[15];

int x[51],y[51];
int price[50][15];
double d[51][51];

double best[51][1<<15][2];

typedef struct S { int at,have,rethome; double cost; } S;
bool operator<(const S &a,const S &b) { return a.cost>b.cost; }

void run(int casenr) {
	scanf("%d%d%d",&nitems,&nstores,&gasprice);
	REP(i,nitems) {
		scanf("%s",name[i]);
		len[i]=strlen(name[i]);
		if(name[i][len[i]-1]=='!') perishable[i]=1,name[i][--len[i]]='\0'; else perishable[i]=0;
	}
	gets(buff);
	memset(price,-1,sizeof(price));
	REP(i,nstores) {
		gets(buff); char *s=buff;
		int cnt;
		sscanf(s,"%d%d%n",&x[i],&y[i],&cnt); s+=cnt;
		while(sscanf(s,"%s%n",tmp,&cnt)==1) {
			s+=cnt;
			int nfound=0;
			REP(j,nitems) if(strncmp(name[j],tmp,len[j])==0&&tmp[len[j]]==':') {
				price[i][j]=atoi(tmp+len[j]+1);
				++nfound;
			}
			assert(nfound==1);
		}
	}
	x[nstores]=y[nstores]=0;
	REPE(i,nstores) REPE(j,nstores) d[i][j]=gasprice*hypot(x[i]-x[j],y[i]-y[j]);
//	REP(i,nstores) REP(j,nitems) if(price[i][j]!=-1) printf("%d %d = %d\n",i,j,price[i][j]);
	
	priority_queue<S> q;
	REPE(i,nstores) REP(j,1<<nitems) REP(k,2) best[i][j][k]=1e200;
	#define ADD(i,j,k,c) if((c)<best[i][j][k]) best[i][j][k]=c,q.push((S){i,j,k,c});
	ADD(nstores,0,0,0);
	while(!q.empty()) {
		int at=q.top().at,have=q.top().have,rethome=q.top().rethome; double cost=q.top().cost; q.pop();
		if(cost>best[at][have][rethome]+1e-12) continue;
		REPE(to,nstores) if(to==nstores||!rethome) ADD(to,have,0,cost+d[at][to]);
		if(at!=nstores) REP(i,nitems) if(!(have&(1<<i))) if(price[at][i]!=-1) ADD(at,have|(1<<i),rethome||perishable[i],cost+price[at][i]);
	}
	printf("Case #%d: %.7lf\n",casenr,best[nstores][(1<<nitems)-1][0]);
}


int main() {
		
	int n; scanf("%d",&n); FORE(i,1,n) run(i);
}
