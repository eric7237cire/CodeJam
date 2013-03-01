#include <algorithm>
#include <cassert>
#include <cstdio>
#include <cstring>
#include <iostream>
#include <map>
using namespace std;

int N;
pair<int, double> in[1024];

bool can(double r) {
  double beg = in[0].second - r, end = in[0].second + r;
  for (int i = 1; i < N; ++i) {
    double c = in[i].second, d = in[i].first - in[i-1].first;
    beg -= d; end += d;
    double newbig = c - r, newend = c + r;
    beg = max(beg, newbig);
    end = min(end, newend);
    if (end < beg) return false;
  }
  return true;
}

int main() {
  //freopen("C-large.in", "r", stdin);
 // freopen("C-large.out", "w", stdout);
  int K;
  cin >> K;
  for (int kase = 1; kase <= K; ++kase) {
    cin >> N;
    for (int c = 0; c < N; ++c)
      cin >> in[c].second >> in[c].first;
    sort(in, in+N);
    int iter = 400;
    double beg = 0.0, end = 1e9;
    while (iter--) {
      double r = (beg+end) / 2;
      if (can(r)) end = r;
      else beg = r;
    }
    printf("Case #%d: %.12lf\n", kase, end);
  }
  return 0;
}

