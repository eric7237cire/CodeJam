/*
 * File:   10610.cpp
 * Author: xerxes
 *
 * Created on September 16, 2011, 6:08 PM
 *
 * I have not failed, I have just found 10000 ways that won't work.
 */


#include <iostream>
#include <algorithm>
#include <vector>
#include <sstream>
#include <fstream>
#include <string>
#include <list>
#include <map>
#include <set>
#include <queue>
#include <stack>
#include <functional>
#include <bitset>
#include <iomanip>


#include <ctime>
#include <cassert>
#include <cstdio>
#include <cmath>
#include <cstring>
#include <climits>
#include <cstring>
#include <cstdlib>


using namespace std;


#define VI vector< int >
#define CI( x ) scanf("%d",&x)
#define CL( x ) scanf("%lld",&x)
#define CD( x ) scanf("%lf",&x )
#define fo(i,st,ed) for(int i = st ; i < ed ; ++i )
#define foE(i,st,ed) for(int i = st ; i <= ed ; ++i )
#define foit(i, x) for (typeof((x).begin()) i = (x).begin(); i != (x).end(); i++)
#define bip system("pause")
#define Int long long
#define pb push_back
#define SZ(X) (int)(X).size()
#define LN(X) (int)(X).length()
#define mk make_pair
#define f first
#define s second
#define SET( ARRAY , VALUE ) memset( ARRAY , VALUE , sizeof(ARRAY) )


inline void wait(double seconds) {
    double endtime = clock()+(seconds * CLOCKS_PER_SEC);
    while (clock() < endtime) {
        ;
    }
}


int speed, minute;
double d;
double sx, sy;
double tx, ty;
vector< pair<double, double> > points;


inline void Read() {
    d = speed * (minute * 60);
    string line;
    cin >> sx >> sy >> tx >> ty;




    points.clear();
    points.pb(mk(tx, ty));
    getline(cin, line);
    while (getline(cin, line)) {
        if (LN(line) < 1)return;
        double x, y;
        stringstream ss(line);
        ss >> x >> y;
        points.pb(mk(x, y));
    }
}


inline void Proc() {
    int total = SZ(points);
    VI vis(total + 5, (int) 1023456789);


    queue< pair<int, pair<double, double> > > q;


    q.push(mk(0, mk(sx, sy)));
    vis[0] = 0;
    if (sx == tx && sy == ty) {
        vis[1] = 1;
        goto L;
    }
    while (q.empty() == false) {
        int father = q.front().f;
        double x = q.front().s.f;
        double y = q.front().s.s;
        q.pop();


        for (int i = 0; i < total; ++i) {
            int id = i + 1;
            double dd = (x - points[i].f)*(x - points[i].f) + (y - points[i].s)*(y - points[i].s);
            dd = sqrt(dd);
            if (dd <= d && (vis[father] + 1 < vis[id])) {
                vis[id] = vis[father] + 1;
                q.push(mk(id, mk(points[i].f, points[i].s)));
            }
        }
    }


L:
    if (vis[1]<(int) 1023456789) {
        printf("Yes, visiting %d other holes.\n", vis[1] - 1);
    } else {
        printf("No.\n");
    }
}


int main() {


    while (cin >> speed >> minute) {
        if (speed == 0 && minute == 0)break;
        Read();
        Proc();
    }


    return 0;
}