#include <vector>
#include <list>
#include <map>
#include <set>
#include <deque>
#include <stack>
#include <bitset>
#include <algorithm>
#include <functional>
#include <numeric>
#include <utility>
#include <sstream>
#include <iostream>
#include <iomanip>
#include <cstdio>
#include <cmath>
#include <cstdlib>
#include <ctime>
#include <queue>
#include <cstring>

using namespace std;

typedef long long LL;
typedef pair <int,int> pii;
typedef vector <int> vi;
typedef vector <double> vd;
typedef vector <string> vs;
typedef vector <LL> vll;
typedef vector <char> vc;

vs token(string s, string d)
{
	int pos;
	string t;
	vs res;

	while (true)
	{
		pos = s.find(d);
		if (pos == -1) break;
		t = s.substr(0,pos);
		s = s.substr(pos+d.length());
		if (t != "") res.push_back(t);
	}
	if (s != "") res.push_back(s);
	return res;
}

vs token(string s)
{
	return token(s," ");
}

//////////////////////////////////////////////

int nid;
map <string, int> id;
int getid(string s)
{
	if (id.count(s)) return id[s];
	return id[s] = nid++;
}

int tc, ntc;
int m, n;
double gas; 
char buf[1000];

double px[100], py[100];
vector < pair<int,double> > isi[100];

double todouble(const string& s)
{
	double res;
	sscanf(s.c_str(),"%lf",&res);
	return res;	
}

struct tstate
{
	int id, mask;
	int perish;
};
bool operator<(const tstate& a, const tstate& b)
{
	return false;
}

#define INF 1e100

const double sqr(double x)
{
	return x*x;
}

double val[60][1<<15][2];
bool used[60][1<<15][2];
double dist[60][60];
bool ff[15];
priority_queue < pair<double,tstate> > pq;

void add(const tstate& st, double v)
{
	if (val[st.id][st.mask][st.perish] > v)
	{
		val[st.id][st.mask][st.perish] = v;
		pq.push( make_pair(-v, st) );
	}
}

double dijkstra()
{
	pq = priority_queue< pair<double,tstate> >();
	tstate cur, nex;
	cur.id = n-1;
	cur.mask = 0;
	cur.perish = 0;
	
	int i, j, k;
	for (i=0; i<n; i++) for (j=0; j<(1<<m); j++) for (k=0; k<2; k++)
	{
		used[i][j][k] = 0;
		val[i][j][k] = INF;
	}
	
	pair <double, tstate> xx;
	double cval;
	
	//for (i=0; i<n; i++) 
	//{
		//printf("%d:\n",i);
		//for (j=0; j<isi[i].size(); j++) printf("%d %lf\n",isi[i][j].first, isi[i][j].second);
	//}
	
	add(cur, 0);
	int a;
	while (!pq.empty())
	{
		xx = pq.top(); pq.pop();
		cur = xx.second;
		cval = -xx.first;
		if (used[cur.id][cur.mask][cur.perish]) continue;
		
		used[cur.id][cur.mask][cur.perish] = true;
		if (cur.id == n-1 && cur.mask == (1<<m)-1) return cval;
		
		
		// move
		if (cur.perish)
		{
			nex.id = n-1;
			nex.mask = cur.mask;
			nex.perish = 0;
			add(nex, cval + dist[cur.id][n-1] * gas);
		}
		else
		{
			for (i=0; i<n; i++)
			{
				nex.id = i;
				nex.mask = cur.mask;
				nex.perish = 0;
				add(nex, cval + dist[cur.id][i] * gas);
			}
		}
		
		// buy
		for (i=0; i<isi[cur.id].size(); i++)
		{
			a = isi[cur.id][i].first;
			if (cur.mask & (1<<a)) continue;
			
			nex.id = cur.id;
			nex.mask = cur.mask | ( 1<<a );
			nex.perish = cur.perish;
			if (ff[a]) nex.perish = true;
			add(nex, cval + isi[cur.id][i].second);
		}
	}
	
	return INF;
}

int main()
{
	scanf("%d",&ntc);
	int i, j;
	int len;
	vs t, t2;
	double res;
	for (tc=1; tc<=ntc; tc++)
	{
		nid = 0;
		id.clear();
		scanf("%d %d %lf",&m,&n,&gas);
		memset(ff,0,sizeof(ff));
		//printf("n=%d, m=%d\n",n,m);
		for (i=0; i<m; i++) 
		{
			scanf("%s",buf);
			len = strlen(buf);
			if (buf[len-1] == '!')
			{
				buf[--len] = 0;
				j = getid(buf);	
				ff[j] = true;
			}
			else
			{
				getid(buf);
			}
		}
		for (i=0; i<=n; i++) isi[i].clear();		
		
		for (i=0; i<n; i++)
		{
			scanf("%lf %lf",&px[i],&py[i]);
			gets(buf);
			t = token(buf);
			for (j=0; j<t.size(); j++)
			{
				t2 = token(t[j],":");				
				isi[i].push_back( make_pair( getid(t2[0]), todouble(t2[1]) ) );
			}
		}
					
		px[n] = 0;py[n] = 0;n++;
		for (i=0; i<n; i++) for (j=0; j<n; j++)
		{
			dist[i][j] = sqrt( sqr(px[i]-px[j]) + sqr(py[i]-py[j]) );
		}
		res = dijkstra();
		
		printf("Case #%d: ",tc);
		printf("%.7lf\n",res);
	}
}