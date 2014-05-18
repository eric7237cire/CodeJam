#include <iostream>
#include <sstream>
#include <fstream>
#include <string>
#include <vector>
#include <deque>
#include <queue>
#include <stack>
#include <set>
#include <map>
#include <algorithm>
#include <functional>
#include <utility>
#include <bitset>
#include <cmath>
#include <cstdlib>
#include <ctime>
#include <cstdio>

using namespace std;

#define REP(i,n) for((i)=0;(i)<(int)(n);(i)++)
#define snuke(c,itr) for(__typeof((c).begin()) itr=(c).begin();itr!=(c).end();itr++)

int N,K;
string s;

double p[210][210][210]; // len, cnt1, cnt2

void pre(void){
	int i,j,k;
	
	for(i=1;i<=205;i++){
		double prob1 = 1.0 / i;
		double prob2 = (i - 1.0) / i;
		
		REP(j,205) REP(k,205) p[i][j][k] = 0.0;
		p[i][0][0] = 1.0;
		REP(j,205) REP(k,205){
			p[i][j+1][k+1] += p[i][j][k] * prob1;
			p[i][j+1][k] += p[i][j][k] * prob2;
		}
	}
}

double dp[210][210][210];

double func1(void){
	int i,j,k;
	
	REP(i,N+1) REP(j,K+1) REP(k,K-j+1) dp[i][j][k] = 0.0;
	
	dp[0][0][K] = 1.0;
	REP(i,N) REP(j,K+1) REP(k,K-j+1) if(dp[i][j][k] > 1.0E-15){
		int x;
		int center = k / (N-i);
		
		for(x=center;x>=0;x--){
			double prob = dp[i][j][k] * p[N-i][k][x];
			if(prob < 1.0E-15) break;
			int i2 = i+1, j2 = max(j+x - ((s[i]=='.')?1:0), 0), k2 = k-x;
			dp[i2][j2][k2] += prob;
		}
		
		for(x=center+1;x<=k;x++){
			double prob = dp[i][j][k] * p[N-i][k][x];
			if(prob < 1.0E-15) break;
			int i2 = i+1, j2 = max(j+x - ((s[i]=='.')?1:0), 0), k2 = k-x;
			dp[i2][j2][k2] += prob;
		}
	}
	
	double ans = 0.0;
	REP(j,K+1) REP(k,K-j+1) ans += j * dp[N][j][k];
	return ans;
}

void main2(void){
	int i,j;
	
	cin >> s;
//	s = "";
//	REP(i,200) s += ".";
	
	N = s.length();
	K = 0;
	REP(i,N) if(s[i] == '.') K++;
	
	double ans = 0.0;
	string t = s;
	REP(i,N){
		t = t.substr(1) + t[0];
		s = t;
		ans += func1();
	}
	
//	cout << ans << endl;
	printf("%.12f\n", (double)N * (double)K - ans);
}

int main(void){
	pre();
	int TC,tc;
	cin >> TC;
	REP(tc,TC){
		printf("Case #%d: ", tc+1);
		main2();
	}
	return 0;
}
