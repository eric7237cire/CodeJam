#include <algorithm>  
#include <iostream>  
#include <sstream>  
#include <string>  
#include <vector>  
#include <queue>  
#include <set>  
#include <map>  
#include <cstdio>  
#include <cstdlib>  
#include <cctype>  
#include <cmath>  
#include <list>  
using namespace std;  

#define PB push_back  
#define MP make_pair  
#define SZ(v) ((int)(v).size())  
#define FOR(i,a,b) for(int i=(a);i<(b);++i)  
#define REP(i,n) FOR(i,0,n)  
#define FORE(i,a,b) for(int i=(a);i<=(b);++i)  
#define REPE(i,n) FORE(i,0,n)  
#define FORSZ(i,a,v) FOR(i,a,SZ(v))  
#define REPSZ(i,v) REP(i,SZ(v))  
#define RESET(arr,val) memset(arr,val,sizeof(arr))
typedef long long ll;  


const int MAX_TEAM_NUM = 100;
char winLoss[MAX_TEAM_NUM][ MAX_TEAM_NUM+1];
int N;

void run(int casenr) {
	memset(winLoss, '.', sizeof(winLoss));
	scanf("%d", &N);
	
	FOR(i,0,N) scanf("%s", winLoss[i]);
	
	int wpNum[MAX_TEAM_NUM]; RESET(wpNum,0);
	int wpDenom[MAX_TEAM_NUM]; RESET(wpDenom, 0);

	//OWP [team] [opponnent]  is WP ignoring games against team
	double owpNum[MAX_TEAM_NUM]; RESET(owpNum, 0);
	double oowpNum[MAX_TEAM_NUM]; RESET(oowpNum, 0);
	
	char ch;
	FOR(i,0,N)
		FOR(j,i+1,N) 
			if ( (ch = winLoss[i][j]) != '.') 
			{
				wpNum[i] += ch == '1' ? 1 : 0; wpDenom[i] ++;
				wpNum[j] += ch == '0' ? 1 : 0; wpDenom[j] ++;
			}
	

	FOR(i,0,N)
	{
		int numOpp = 0;
		double avgOpp = 0;
		FOR(j,0,N) 
		{
			//team I, opponent is J.  They must have played against one another
			if (i==j || (ch = winLoss[j][i]) == '.') 
				continue;
			
			numOpp++;
			avgOpp += wpDenom[j] - 1 == 0 ? 0 : (double) (wpNum[j] - (ch == '1' ? 1 : 0))  / (wpDenom[j] - 1);			 
		}
		owpNum[i] = numOpp > 0 ? avgOpp / numOpp : 0;
	}

	FOR(i,0,N)
	{
		int numOpp = 0;
		double avgOowp = 0;
		FOR(j,0,N) 
		{
			if (i==j || winLoss[j][i] == '.') 
				continue;

			numOpp++;
			avgOowp += owpNum[j];
		}

		oowpNum[i] = numOpp > 0 ? avgOowp / numOpp : 0;		
	}
	
	printf("Case #%d:\n",casenr);
	FOR(i,0,N) 
	{
		double winPer = wpDenom[i] == 0 ? 0 : (double) wpNum[i]  / wpDenom[i];
		double owp = owpNum[i];
		double oowp = oowpNum[i];
		printf("%.6f\n", winPer * 0.25 + owp * 0.5 + oowp * 0.25);
	}
}

int main() {
	int n; scanf("%d",&n); FORE(i,1,n) run(i);
	return 0;
}

 
