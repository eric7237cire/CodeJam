#include <algorithm>
#include <cassert>
#include <cstdio>
#include <map>
#include <string>
#include <vector>
using namespace std;
#define REP(i,n) for(int i=0;i<(n);++i)
template<class T> inline int size(const T&c) { return c.size();}
const int INF = 1000000000;

int n; // number of types of marbles
vector<vector<int> > where; // [n][2]
vector<int> marbles; // [2*n]

void readInput() {
  char buf[30];
  map<string,int> dict;
  scanf("%d", &n);
  marbles.clear(); marbles.reserve(2*n);
  where.clear(); where.resize(n);
  for(int i=0;i<2*n;++i) {
    scanf("%s",buf);
    string s = buf;
    map<string,int>::iterator it = dict.find(s);
    int m;
    if(it==dict.end()) {
      m = size(dict);
      dict[s] = m;
    } else {
      m = it->second;
    }
    marbles.push_back(m);
    where[m].push_back(i);
  }
}

struct Event {
  int x,t;
  // t=0 start top, 1 end top
  // t=2 start bot, 3 end bot
};

vector<int> vis;

bool cross(int m1,int m2) {
  return
      where[m1][0] < where[m2][0] &&
      where[m2][0] < where[m1][1] &&
      where[m1][1] < where[m2][1] ||
      where[m2][0] < where[m1][0] &&
      where[m1][0] < where[m2][1] &&
      where[m2][1] < where[m1][1];
}

void dfs(int m,int sign) {
  if(vis[m]==sign) return;
  if(vis[m]==-sign) throw 0;
  vis[m]=sign;
  REP(i,n) if(i!=m && cross(m,i)) dfs(i,-sign);
}

vector<vector<Event> > cacheCalcEvents;

const vector<Event> &calcEvents(int startx) {
  vector<Event> &res = cacheCalcEvents[startx];
  if(!res.empty()) return res;
  vis.assign(n,0);
  dfs(marbles[startx],1);
  REP(x,2*n) {
    int m = marbles[x];
    if(vis[m]==0) continue;
    int nr=0;
    if(where[m][nr] != x) ++nr;
    assert(where[m][nr]==x);
    Event e; e.x=x;
    e.t = (1-vis[m]) + nr;
    res.push_back(e);
  }
  return res;
}

vector<vector<int> > cacheCalcH2;

int calcH2(int a,int b,int h1) {
  if(h1<0) return INF;
  if(a==b) return 0;
  int &res = cacheCalcH2[a][h1];
  if(res!=-1) return res;
  const vector<Event> &events = calcEvents(a);
  res = INF;
  for(int mask = 0; mask<=2; mask+=2) {
    int top=0, bot=0;
    int h2 = 0;
    REP(i,size(events)+1) {
      int alpha = i==0 ? a : events[i-1].x + 1;
      int beta = i==size(events) ? b : events[i].x;
      h2 = max(h2, calcH2(alpha, beta, h1 - top) + bot);
      if(i!=size(events)) {
        switch(events[i].t ^ mask) {
          case 0: ++top; break;
          case 1: --top; break;
          case 2: ++bot; break;
          case 3: --bot; break;
        }
      }
    }
    res = min(res, h2);
  }
  return res;
}

int solve() {
  int res = INF;
  cacheCalcH2.assign(2*n, vector<int>(n+1,-1));
  cacheCalcEvents.clear(); cacheCalcEvents.resize(2*n);
  try {
    REP(h1,n+1) {
      res = min(res, h1 + calcH2(0,2*n,h1));
    }
    return res;
  } catch(int) { return INF; }
}

int main() {
  int ntc; scanf("%d", &ntc);
  REP(tc,ntc) {
    readInput();
    int res = solve();
    if(res==INF) res = -1;
    printf("Case #%d: %d\n", tc+1, res);
  }
}
