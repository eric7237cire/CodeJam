#include <iostream>
#include <algorithm>
#include <cstring>
#include <cstdlib>
#include <vector>
#include <string>
#include <set>
#include <map>
#include <queue>
#include <cctype>
#include <cmath>
#include <sstream>
#include <ctime>

#include <stdio.h>
#include <time.h>
#include <assert.h>


#define int64 long long
#define ii pair<int,int>
#define vi vector<int>
#define mp make_pair
#define pb push_back
#define all(v) (v).begin(),(v).end()
#define sz(v) ((int)(v.size()))

using namespace std;

template<class T> T abs(T x){return x>0 ? x:(-x);}
template<class T> T sqr(T x){return x*x;}

int m,n;
double gp;

map<string,int> M;
bool heavy[100];
int X[100],Y[100];
vector<ii> p[100];
int av_mask[100];
int home;
double d[60][1<<15][2];
double Mc[60][1<<15];
double Pri[60][60];
int heavy_mask;

double dist(int x,int y)
{
	return sqrt(sqr(X[x]-X[y])+sqr(Y[x]-Y[y])+0.0)*gp;
}

int numCalls = 0;
int numAllCalls = 0;

double go(int w,int mask,int bought)
{
  ++numAllCalls;
  if(w==home && mask==(1<<m)-1) return 0.0;
	double& res=d[w][mask][bought];
	if(res>-0.01) return res;
	++numCalls;
	res=1E100;
	/*if(w!=home){
		int nm=(~mask)&av_mask[w];
		for(int buy=nm;buy;buy=(buy-1)&nm){
			double cost=Mc[w][buy];
			if(heavy_mask&buy){
				int to=home;
				res=min(res,cost+dist(w,to)+go(to,mask|buy));
			} else{
				for(int to=0;to<=n;to++) if(to!=w)
					res=min(res,cost+dist(w,to)+go(to,mask|buy));
			}
		}
	} else{
		for(int to=0;to<n;to++)
			res=min(res,dist(w,to)+go(to,mask));
	}*/
	if(w==home){
		for(int to=0;to<n;to++)
			res=min(res,dist(w,to)+go(to,mask,0));
		return res;
	}
	if(!bought){
		//mask = 1111 means bought everything
		// 1100 buy 0,1  1001  1000 0111/0001
		int nm=(~mask)&av_mask[w];
		for(int buy=nm;buy;buy=(buy-1)&nm){
			double cost=Mc[w][buy];
			if(heavy_mask&buy){
				int to=home;
				res=min(res,cost+dist(w,to)+go(to,mask|buy,0));
			} else{
				res=min(res,cost+go(w,mask|buy,1));
			}
		}
	} else{
		for(int to=0;to<=n;to++) if(to!=w)
			res=min(res,dist(w,to)+go(to,mask,0));
		return res;
	}
	return res;
}

int main()
{
#
std::clock_t start = std::clock();
#
/* Stuff to time here */
#
  int totalCalls = 0;
	int nc;
	cin >> nc;
	for(int ic=0;ic<nc;ic++){
		cin >> m >> n >> gp;
		memset(heavy,0,sizeof(heavy));
		heavy_mask=0;
		for(int i=0;i<m;i++){
			string s;
			cin >> s;
			if(s[sz(s)-1]=='!'){
				heavy[i]=true;
				heavy_mask|=(1<<i);
				s=s.substr(0,sz(s)-1);
			}
			M[s]=i;
		}
		memset(av_mask,0,sizeof(av_mask));
		for(int i=0;i<n;i++){
			cin >> X[i] >> Y[i];
			char ts[1000];
			gets(ts);
			string S=string(ts);
			stringstream ss(S);
			string s;
			while(ss >> s){
				string nm;
				int pr=0;
				int cur=0;
				while(s[cur]!=':'){
					nm+=s[cur];
					cur++;
				}
				cur++;
				while(cur<sz(s)){
					pr=pr*10+(int)(s[cur]-'0');
					cur++;
				}
				av_mask[i]|=(1<<M[nm]);
				p[i].push_back(ii(M[nm],pr));
				Pri[i][M[nm]]=pr;
			}
		}
		home=n;
		X[home]=Y[home]=0;
		for(int i=0;i<n;i++){
			for(int j=av_mask[i];j;j=(j-1)&av_mask[i]){
				int x=j;
				double& q=Mc[i][j];
				q=0.0;
				for(int k=0;k<m;k++)
					if((x>>k)&1)
						q+=Pri[i][k];
			}
		}
		memset(d,-1,sizeof(d));
		numCalls = 0;
		double res=go(home,0,0);
		totalCalls += numCalls;
		printf("Case #%d: %.7f\n",ic+1,res);
		//printf(" Number of calls %d\n", numCalls);
	}

	printf("total calls: %d\n", totalCalls);
	printf("all total calls: %d\n", numAllCalls);
	double totalTimeSec = ( std::clock() - start ) / (double)CLOCKS_PER_SEC ;


	 std::cout<< "Total time " << totalTimeSec <<'\n';
	std::cout << "cost per call " << (1000.0 * totalTimeSec / totalCalls) << " ms " << endl;
	std::cout << "cost per call " << (1E6 * totalTimeSec / totalCalls) << " ns " << endl;

	return 0;
}
