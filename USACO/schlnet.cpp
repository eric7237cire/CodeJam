/*
ID: eric7231
LANG: C++
TASK: schlnet
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
//#include <complex>
#include <bitset>
#include <iomanip>
#include <cctype>
#include <limits>
#include <numeric>
#include <cmath>
#include <functional>
#include <queue>

using namespace std;
typedef vector<int> vi; 
typedef vector<bool> vb;
typedef vector<vb> vvb;

#define FORE(k,a,b) for(int k=(a); k <= (int)(b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (int) (b); ++k)

#define pb push_back 
#define mp make_pair 


void solve( vvb& g, int N){
    //Floyd warshalls
	FOR(k, 0, N) FOR(i, 0, N) FOR(j, 0, N)
	{
        if(g[i][k]&&g[k][j])g[i][j]=true;
    }
    
    //Initialize component numbers
    vi r(N, -1);
	FOR(i, 0, N)
	{
		if(r[i] != -1)
		    continue;
		r[i]=i;
		
		//Anything strongly connected to i also gets set to the component number
		FOR(j, 0, N)
		{
			if(g[i][j]&&g[j][i])
				r[j]=i;
		}
	}
	//n is a component count
	int n=0,NoIn=0,NoOut=0;
	FOR(i, 0, N)
	{
	    //ONly look at components
		if(r[i]!=i)
		    continue;
		++n;
		bool in=false,out=false;
		FOR(j, 0, N)
		{
			if(i==j)continue;
			//Only look at other strongly connected components
			if(r[j]!=j)continue;
			
			//There is a one way connection another the component
			if(g[i][j])out=true;
			
			//There is a one way connection to this component
			if(g[j][i])in=true;
		}
		if(!in)++NoIn;
		if(!out)++NoOut;
	}
	//R1 is the number of SCC that have no incoming connections from other SCC
	int R1, R2;
	
	if(n==1) {
		R1=1;
		R2=0;
	}
	else{
		R1=max(1, NoIn); //must be at least one
		R2=max(NoIn, NoOut);
	}
	
	freopen("schlnet.out","w",stdout);
	printf("%d\n%d\n",R1,R2);
}

int main(){
    
	freopen("schlnet.in","r",stdin);
	int N, i;
	scanf("%d",&N);
	vvb g(N, vb(N, false));
	i=0;
	while(i < N)
	{
		int x;
		scanf("%d",&x);
		if(!x) {
		    ++i;
		    continue;
		}
		//0 index the verticies
		g[i][x-1]=true;
	}	
	
	solve(g, N);
	
}
