#include <iostream>
#include <sstream>
#include <vector>
#include <string>
#include <cstdio>
#include <cstring>
#include <algorithm>
#include <queue>
#include <set>
#include <stack>
#include <map>

using namespace std;

typedef pair<int,int>   ii;
typedef vector<int>     vi;
typedef vector<ii>      vii;

int places[54000][5];
int place_n = 0;

vi S;
void backTrack(int n){
    if(5 - S.size() > 25 - n)   return;
    S.push_back(n);
    if(S.size() == 5){
        for(int i=0;i<5;i++){
            places[place_n][i] = S[i];
        }
        place_n++;
    } else{
        for(int i=n+1;i<25;i++){
            backTrack(i);
        }
    }
    S.pop_back();
}

int TC,N,M;
int res[5];
int mm[5][5];

int rr[] = {-1,0,1,0}, cc[] = {0,1,0,-1};

int getCost(int p){
    // BFS to build the cost map
    int costMap[5][5];
    for(int i=0;i<5;i++)    for(int j=0;j<5;j++)    costMap[i][j] = -1;
    priority_queue<ii,vii,greater<ii> > q;
    for(int i=0;i<5;i++)    q.push(ii(0,places[p][i]));
    while(!q.empty()){
        ii t = q.top(); q.pop();
        int r = t.second / 5, c = t.second % 5;
        if(costMap[r][c] != -1) continue;   // visited
        costMap[r][c] = t.first;
        for(int i=0;i<4;i++){
            int nr = r + rr[i], nc = c + cc[i];
            if(nr < 0 || nr >= 5 || nc < 0 || nc >= 5)  continue;
            if(costMap[nr][nc] != -1)   continue;
            q.push(ii(t.first+1, nr * 5 + nc));
        }
    }
    int sum = 0;
    for(int i=0;i<5;i++)    for(int j=0;j<5;j++)    sum += costMap[i][j] * mm[i][j];
    return sum;
}

int main(){
    for(int i=0;i<25;i++){
        backTrack(i);
    }

    int TC;
    cin >> TC;
    while(TC--){
        cin >> M;
        memset(mm,0,sizeof mm);
        int a,b,c;
        for(int i=0;i<M;i++){
            cin >> a >> b >> c;
            mm[a][b] = c;
        }
        
        int minCost = 0x7fffffff;
        for(int i=0;i<place_n;i++){
            c = getCost(i);
            if(c < minCost){
                minCost = c;
                for(int j =0;j<5;j++)   res[j] = places[i][j];
            }
        }
        cout << res[0];
        for(int i=1;i<5;i++)    cout << " " << res[i];
        cout << endl;
    }

    return 0;
}