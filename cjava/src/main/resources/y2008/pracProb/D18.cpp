#include <iostream>
#include <sstream>
#include <vector>
#include <map>
#include <math.h>
#include <algorithm>
#include <numeric>
#include <bitset>
#include <stack>
#include <queue>
#include <set>
using namespace std;

int dr[]={0,1,0,-1,1,1,-1,-1};
int dc[]={1,0,-1,0,1,-1,1,-1};
#define zmax(a,b) (((a)>(b))?(a):(b))
#define zmin(a,b) (((a)>(b))?(b):(a))
#define zabs(a) (((a)>=0)?(a):(-(a)))
#define iif(c,t,f) ((c)?(t):(f))
template<class A, class B> A cvt(B x) {stringstream s;s<<x;A r;s>>r;return r;}

#include<fstream>
ifstream fin("input.txt");
ofstream fout("output.txt");

int items;
int stores;

double price_of_gas;
int perish[15];
double dst[60][60];
int cost[60][15];
int x[60];
int y[60];

double memo[1 << 15][60][2];
double solve(int st, int msk, int exp)
{
	if(msk == (1 << items) - 1) return cost[st][0];
	double & ref = memo[msk][st][exp];
	if(ref != 0) return ref;

	if(exp)
	{
		ref = price_of_gas * dst[st][0] + solve(0, msk, 0);
		for(int i = 0; i < items; i++)
			if((msk & (1 << i)) == 0)
				ref <?= cost[st][i] + solve(st, msk | (1 << i), 1);
	}
	else
	{
		ref = 1e300;
		for(int i = 1; i <= stores; i++)
			for(int j = 0; j < items; j++)
				if((msk & (1 << i)) == 0)
					ref <?= price_of_gas * dst[st][i] + cost[i][j] + solve(i, msk | (1 << j), perish[j]);
	}
	return ref;
}

char line[4000];

int main()
{
	int t; fin >> t;
	
	for(int k = 1; k <= t; k++)
	{
		fin >> items;
		fin >> stores;
		fin >> price_of_gas;

cout << items << ", " << stores << endl;
		map<string, int> lst;
		memset(&perish, 0, sizeof(perish));
		for(int i = 0; i < items; i++)
		{
			string s; fin >> s;
			if(s[s.size() - 1] == '!')
			{
				perish[i] = true;
				s = s.substr(0, s.size() - 1);
			}
			lst[s] = i;
		}

		memset(&cost, 1, sizeof(cost));
		x[0] = 0;
		y[0] = 0;
		for(int i = 1; i <= stores; i++)
		{
			fin.getline(line, 4000);
cout << line << endl;
			istringstream sin(line);
			sin >> x[i] >> y[i];
			string s;
			while(sin >> s)
			{
				string a = s.substr(0, s.find(':'));
				string b = s.substr(s.find(':') + 1);
				cost[i][lst[a]] = cvt<int>(b);
			}
		}

		for(int a = 0; a <= stores; a++)
			for(int b = 0; b <= stores; b++)
				dst[a][b] = sqrt((x[a] - x[b]) * (x[a] - x[b]) + (y[a] - y[b]) * (y[a] - y[b]));

		for(int c = 0; c <= stores; c++)
			for(int a = 0; a <= stores; a++)
				for(int b = 0; b <= stores; b++)
					dst[a][b] <?= dst[a][c] + dst[c][b];

		memset(&memo, 0, sizeof(memo));
		char out[40];
		sprintf(out, "Case #%d: %.7f", k, solve(0, 0, 0));
		cout << out << endl;
	}

	#ifdef NOTSUBMIT
	system("pause");
	#endif
	return 0;
}






