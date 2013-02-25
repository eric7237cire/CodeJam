#include <iostream>

#include <vector>

#include <math.h>
#include <algorithm>
#include <cstdlib>
#include <complex>
#include<cstdio>
#include<cstring>

using namespace std;

struct point {
 int x, y;
};

struct line {
 vector<point> P;
 void add(const point &p) {
 P.push_back(p);
 }
 int size() {
 return P.size();
 }
 bool contain(const point &p) {
 int s = size(), c = 0;
 for (c = 0; c < s; c++)
 if (p.x == P[c].x && p.y == P[c].y)
 return true;
 return false;
 }
 void print() {
 int n = size();
 for (int i = 0; i < n; ++i)
 printf("(%4d,%4d)", P[i].x, P[i].y);
 }
};

bool collinear(const point &p1, const point &p2, const point &p3) {
 return (p1.y - p2.y) * (p1.x - p3.x) == (p1.y - p3.y) * (p1.x - p2.x);
}

int cmp(const point &p1, const point &p2) {
 if (p1.x == p2.x)
 return p1.y < p2.y;
 return p1.x < p2.x;
}

void print(vector<line> &v) {
 int n = v.size();
 if (n == 0)
 puts("No lines were found");
 else
 puts("The following lines were found:");
 for (int i = 0; i < n; i++) {
 v[i].print(), cout << endl;
 }
}

bool exist(vector<line> &v, const point &p1, const point &p2) {
 int c = v.size(), k = 0;
 for (k = 0; k < c; k++) {
 line L = v[k];
 if (L.contain(p1) && L.contain(p2))
 return true;
 }
 return false;
}

void solve(vector<point> &pts) {
 int i, j, k, n = pts.size();
 vector<line> lines;
 for (i = 0; i < n; i++) {
 for (j = i + 1; j < n; ++j) {
 if (exist(lines, pts[i], pts[j]))
 continue;
 line line;
 line.add(pts[i]), line.add(pts[j]);
 for (k = j + 1; k < n; k++)
 if (collinear(pts[i], pts[j], pts[k]))
 line.add(pts[k]);
 if (line.size() > 2)
 lines.push_back(line);
 }
 }
 print(lines);
}

int main() {

while (1) {
 vector<point> pts;
 point p;
 while (scanf("%d %d", &p.x, &p.y) == 2) {
 if (p.x == 0 && p.y == 0)
 break;
 pts.push_back(p);
 }
 if (pts.size() == 0)
 break;
 stable_sort(pts.begin(), pts.end(), cmp);
 solve(pts);
 }
 return 0;
}


