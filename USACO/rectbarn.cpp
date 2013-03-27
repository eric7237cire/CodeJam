/*
ID: eric7231
LANG: C++
TASK: rectbarn
*/

#include <iostream>
#include <fstream>
#include <sstream>
#include <iomanip>
#include <algorithm>
#include <string>
#include <queue>
#include <stack>
#include <set>
#include <cstdlib>
#include <cstring>
#include <climits>
#include <cassert>
#include <cmath>

using namespace std;
ifstream fin("rectbarn.in");
ofstream fout("rectbarn.out");
const int inf = INT_MAX / 4;
const int max_row = 3000;
const int max_col = 3000;
int n_row;
int n_col;
int n_damaged;

char field[max_row][max_col];

int u[max_col];
int l[max_col];
int r[max_col];

int gl[max_col];
int gr[max_col];

int max_barn;
void input(void)
{
	int i, j, k;
	assert(fout && fin);
	fin >> n_row >> n_col >> n_damaged;

	for (i = 0; i < n_row; i++)
		for (j = 0; j < n_col; j++)
			field[i][j] = '0';

	for (i = 0; i < n_damaged; i++) {
		int r, c;
		fin >> r >> c;
		field[r - 1][c - 1] = '1';
	}
}

void output(void)
{
	int i, j, k;
	fout << max_barn << endl;
}

void solve(void)
{
	int i, j, k;

	for (i = 0; i < n_col; i++)
		l[i] = r[i] = inf;

	for (i = 0; i < n_row; i++) {
		for (j = 0; j < n_col; j++) {
			if (field[i][j] == '1')
				gl[j] = 0;
			else if (j == 0)
				gl[j] = 1;
			else
				gl[j] = gl[j - 1] + 1;

			k = n_col - 1 - j;
			if (field[i][k] == '1')
				gr[k] = 0;
			else if (k == n_col - 1)
				gr[k] = 1;
			else
				gr[k] = gr[k + 1] + 1;
		}

		for (j = 0; j < n_col; j++) {
			if (field[i][j] == '1') {
				u[j] = 0;
				l[j] = inf;
				r[j] = inf;
			} else {
				u[j]++;
				l[j] = min(l[j], gl[j]);
				r[j] = min(r[j], gr[j]);
			}
			max_barn = max(max_barn, u[j] * (l[j] + r[j] - 1));
			// 	cout << i << " " << j << ": " << u[j] * (l[j] + r[j] - 1) << endl;
		}
	}
}

int main()
{
	input();
	solve();
	output();

	return 0;
}