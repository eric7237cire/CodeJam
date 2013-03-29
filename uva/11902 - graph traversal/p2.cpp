#include <iostream>
#include <string>
#include <cstring>

using namespace std;

int graph[110][110];
int doms[110][110];
int visited[110];
int first[110];
int n;

void dfs(int from, int disabled, int vis[110]) {
    if (from == disabled)
        return ;
    vis[from] = 1;

    for (int to = 0; to < n; ++to) {
        if (graph[from][to] && !vis[to])
            dfs(to, disabled, vis);
    }
}

int main() {
    int t;
    cin >> t;
    for (int x = 1; x <= t; x++) {
        cin >> n;
        memset(graph, 0, sizeof(graph));
        memset(doms, 0, sizeof(doms));
        memset(first, 0, sizeof(first));
        memset(visited, 0, sizeof(visited));

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                cin >> graph[i][j];
            }
        }

        dfs(0, -1, first);

        for (int i = 0; i < n; ++i) {
            memset(visited, 0, sizeof(visited));
            dfs(0, i, visited);
            for (int j = 0; j < n; ++j) {
                if (!visited[j] && first[j])
                    doms[i][j] = 1;
                else
                    doms[i][j] = 0;
            }

            doms[i][i] = first[i];
        }

        cout << "Case " << x << ":\n";

        string str(2 * n - 1, '-');

        cout << "+" << str << "+" << endl;

        for (int i = 0; i < n; ++i) {
            cout << "|";
            for (int j = 0; j < n; ++j) {
                if (doms[i][j])
                    cout << "Y|";
                else
                    cout << "N|";
            }
            cout << endl;
            cout << "+" << str << "+" << endl;
        }

    }

    return 0;
}