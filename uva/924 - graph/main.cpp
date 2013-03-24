#include <iostream>
#include <algorithm>
#include <vector>
#include <fstream>
#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <queue>

using namespace std;

typedef unsigned long long ULL;
typedef long long LL;

bool G[2501][2501];
bool visited[2501];
int  step[2501];
int  freq[2501];
int  nempl;

bool isisolated(int n) {
    int sum = 0;

    for(int i=0; i<nempl; i++)
        sum += (int) G[n][i];

    if (sum > 0) return false;
    else return true;
}

void BFS(int start) {
    memset(visited, 0, sizeof(visited));
    memset(step, 0, sizeof(step));
    memset(freq, -1, sizeof(freq));
    
    queue<int> Q;

    visited[start] = true;
    step[start] = 0;
    Q.push(start);

    while(!Q.empty()) {
        int num = Q.front(); Q.pop();

        for(int i=0; i<nempl; i++) {
            if (G[num][i] && !visited[i]) {
                visited[i] = true;
                step[i] = step[num] + 1;
                Q.push(i);
            }
        }
    }

    for(int i=0; i<nempl; i++) freq[step[i]]++;

    int i=1;
    int maxday=0, maxempl=0;
    while(freq[i] != -1) {
        if (freq[i]+1 > maxempl) {
            maxempl = freq[i] + 1;
            maxday = i;
        }
        i++;
    }

    cout << maxempl << " " << maxday << endl;
}

int main() {
    memset(G, 0, sizeof(G));

    cin >> nempl;

    for(int i=0; i<nempl; i++) {
        int nfriend;
        cin >> nfriend;

        for(int j=0; j<nfriend; j++) {
            int temp; cin >> temp;
            G[i][temp] = true;
        }
    }

    int T; cin >> T;

    while(T--) {
        int start; cin >> start;

        if (isisolated(start)) cout << '0' << endl;
        else BFS(start);
    }
    return 0;
}