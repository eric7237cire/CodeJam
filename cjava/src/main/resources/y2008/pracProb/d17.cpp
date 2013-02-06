#include <cstring>
#include <cstdio>
#include <cmath>
#include <map>
#include <queue>
#include <utility>
#include <string>
#include <sstream>
#include <functional>
using namespace std;

const double INF=1e23;

struct QueueItem {
  int s;
  int b;
  double p;
  QueueItem(int s, int b, double p): s(s), b(b), p(p) {}
  bool operator<(const QueueItem &o) const { return p>o.p; }
};

string it[15];
int av[51];
char bf[100008];
bool bn[51][1<<15];
double ct[51][15];
double dt[51][1<<15];
double tl[51][1<<15];
map<string, int> mp;

int main() {
  int i, j, k, m, b, t, T, I, S, h;
  double c, p, G, np;
  double xs[51], ys[51];
  scanf("%d", &T);
  for (t=1; t<=T; t++) {
    h=0;
    mp.clear();
    memset(av, 0, sizeof av);
    memset(ct, 0, sizeof ct);
    memset(tl, 0, sizeof tl);
    memset(bn, 0, sizeof bn);
    scanf("%d %d %lf", &I, &S, &G);
    for (i=0; i<I; i++) {
      scanf("%s", bf);
      it[i]=bf;
      if (*it[i].rbegin()=='!') {
        h|=(1<<i);
        it[i].erase(it[i].end()-1);
      }
      mp[it[i]]=i;
    }
    scanf("\n");
    xs[0]=ys[0]=0;
    for (i=1; i<=S; i++) {
      gets(bf);
      string tk;
      istringstream iss(bf);
      iss>>xs[i];
      iss>>ys[i];
      while (iss>>tk) {
        sscanf(tk.c_str(), "%[^:]:%lf", bf, &p);
        ct[i][mp[bf]]=p;
        av[i]|=(1<<mp[bf]);
      }
    }
    priority_queue<QueueItem> pq;
    pq.push(QueueItem(0, 0, 0));
    for (i=0; i<=S; i++)
      for (j=0; j<(1<<I); j++) {
        dt[i][j]=INF;
        for (k=0; k<I; k++)
          if ((j&(1<<k))!=0 && (av[i]&(1<<k))!=0)
            tl[i][j]+=ct[i][k];
      }
    dt[0][0]=0;
    while (!pq.empty()) {
      QueueItem qi=pq.top();
      pq.pop();
      if (bn[qi.s][qi.b])
        continue;
      bn[qi.s][qi.b]=1;
      for (i=0; i<=S; i++)
        if (i!=qi.s) {
          c=hypot(xs[qi.s]-xs[i], ys[qi.s]-ys[i])*G;
          m=(av[i]&~(qi.b));
          if (i==0) {
            if (dt[0][qi.b]>qi.p+c) {
              dt[0][qi.b]=qi.p+c;
              pq.push(QueueItem(0, qi.b, qi.p+c));
            }
          }
          else 
            for (b=m; b>0; b=(b-1)&m)
              if ((b&h)==0) {
                if (dt[i][qi.b|b]>qi.p+c+tl[i][b]) {
                  dt[i][qi.b|b]=qi.p+c+tl[i][b];
                  pq.push(QueueItem(i, qi.b|b, qi.p+c+tl[i][b]));
                }
              }
              else
                if (dt[0][qi.b|b]>(np=qi.p+c+tl[i][b]+G*hypot(xs[i], ys[i]))) {
                  dt[0][qi.b|b]=np;
                  pq.push(QueueItem(0, qi.b|b, np));
                }
        }
    }
    printf("Case #%d: %.7lf\n", t, dt[0][(1<<I)-1]);
    fprintf(stderr, "Case #%d: %.7lf\n", t, dt[0][(1<<I)-1]);
  }
  return 0;
}
