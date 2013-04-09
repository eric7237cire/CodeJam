#include <iostream>
#include <algorithm>
#include <vector>
#include <fstream>
#include <cstdio>
#include <cstring>
#include <cstdlib>

using namespace std;

typedef unsigned long long ULL;
typedef long long LL;

char G[55][55];
bool visited[55][55];
int ctr=0;
int C, R;

void DFS(int i, int j, char c) {
    if (i < 0 || j < 0 || i >= R || j >= C || visited[i][j] || G[i][j] != c)
        return;

    visited[i][j] = true;
    ctr++;

    DFS(i, j+1, c); DFS(i, j-1, c);
    DFS(i+1, j, c); DFS(i-1, j, c);
}

bool comp(pair<char, int> A, pair<char, int> B) {
    if (A.second == B.second)
        return (A.first < B.first);
    else
        return (A.second > B.second);
}

int main() {
    int t=0;
    while(cin >> R >> C && R && C) {
        vector<pair<char, int> > ans;

        memset(G, 0, sizeof(G));
        memset(visited, 0, sizeof(visited));

        for(int i=0; i<R; i++) {
            for(int j=0; j<C; j++)
                cin >> G[i][j];
        }
        
        for(int i=0; i<R; i++) {
            for(int j=0; j<C; j++) {
                if (G[i][j] >= 'A' && G[i][j] <= 'Z' && !visited[i][j]) {
                    ctr=0;

                    DFS(i, j, G[i][j]);
                    ans.push_back(make_pair(G[i][j], ctr));
                }
            }
        }
        
        sort(ans.begin(), ans.end(), comp);

        cout << "Problem " << ++t << ":" << endl;

        for(int i=0; i<ans.size(); i++)
            cout << ans[i].first << " " << ans[i].second << endl;
    }

    return 0;
}