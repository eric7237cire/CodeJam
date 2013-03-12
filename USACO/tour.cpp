/*
ID: eric7231
PROG: tour
LANG: C++
*/
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <set>
#include <limits>
#include <vector>
#include <algorithm>
#include <cassert>
using namespace std;

typedef unsigned int uint;

int N;
int E;

const int MAX_N = 100;
bool conn[MAX_N][MAX_N];

int dp[MAX_N][MAX_N];

typedef map<string, int> mapsi;

mapsi nameIdxMap;

string s;
string s2;

int main() {
	ofstream fout ("tour.out");
    ifstream fin ("tour.in");
		
	fin >> N;
	fin >> E;

	for(int i = 0; i < N; ++i)
	{
		fin >> s;
		nameIdxMap[s] = i;
	}
	
	for(int i = 0; i < N; ++i)
	{
		fill(dp[i], dp[i] + N, numeric_limits<int>::min());
		fill(conn[i], conn[i] + N, false);
	}
	
	for(int i = 0; i < E; ++i)
	{
		fin >> s >> s2;
		int c1 = nameIdxMap[s];
		int c2 = nameIdxMap[s2];
		printf("%d %d connected\n", c1, c2);
		conn[c1][c2] = conn[c2][c1] = true;
	}
	
	

	//dp[x][y] = most nodes between 2 distinct paths from 0 to x and to y
	dp[0][0] = 1;
	
	for(int i = 1; i < N; ++i)
	{
		if (!conn[i][0])
			continue;
			
		dp[i][0] = dp[0][i] = 2;
		
		for(int j = 1; j < i; ++j)
		{
			if (!conn[j][0])
				continue;
			
			dp[i][j] = dp[j][i] = 3;
		}
	}
	
	for(int i = 0; i < N; ++i)
	{			
		for(int j = 1; j < N; ++j)
		{
			if (i==j || dp[i][j] < 0)
				continue;
			
			//try to extend the path, must either be to the right of i and j, or can share N-1
			int kStart = min( max(i, j) + 1, N - 1);
			
			for(int k = kStart; k < N; ++k)
			{
				printf("i %d j %d k %d\n", i, j, k);
				
				if (conn[i][k] && dp[k][j] < dp[i][j]+1)
				{
					dp[k][j] = dp[i][j] + 1;
					printf("dp[k %d][j %d] = %d\n", k, j, dp[k][j]);
				}
				if (conn[k][j] && dp[i][k] < dp[i][j] + 1)
				{
					dp[i][k] = dp[i][j] + 1;
					printf("dp[i %d][k %d] = %d\n", i, k, dp[i][k]);
				}
			}
		}
	}
			
	int ans = dp[N-1][N-1];
	
	if (ans > 0)
		fout << ans - 1;
	else 
		fout << 1;
		
	fout	<< endl; 
	
    return 0;
}
