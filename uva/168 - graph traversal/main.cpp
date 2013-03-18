#include<vector>
#include<iostream>
#include<string.h>
#include<algorithm>
using namespace std;
#define MAX 300
 
const int N = 26;
int K;
 
//Found not really easy understood
 
int go(int M, int T, int step, vector<int> adj[MAX], bool candle[MAX]) {
    candle[M] = !((++step) % K);
    for (size_t v = 0; v < adj[M].size(); v++)
        if (!candle[adj[M][v]] && adj[M][v] != T) {
            if (candle[M]) cout << char(M) << " ";
            return go(adj[M][v], M, step, adj, candle);
        }
    cout << "/" << char(M) << endl;
    return 0;
}
int main() {
    string line;
    while (getline(cin, line) && line != "#") {
        bool vis[MAX];
        vector<int> adj[MAX];
        size_t s, t, i, ei = 0; int e[2];
        string tmp = "";
        for (i = 0; i < line.size(); i++) {
            if (line[i] == '.') break;
            if (line[i] == ':' || line[i] == ';') ei = line[i] == ':' ? 1 : 0;
            else {
                e[ei] = line[i];
                if (ei == 1) adj[e[0]].push_back(e[1]);
            }
        }
        {
            i++;
            while (line[i] == ' ') i++;  s = line[i++];
            while (line[i] == ' ') i++;  t = line[i++];
            while (line[i] == ' ') i++;  K = 0;
            while (i < line.size()) K = K * 10 + (line[i++] - '0');
        }
        memset(vis, 0, sizeof(vis));
        go(s, t, 0, adj, vis);
    }
    return false;
}