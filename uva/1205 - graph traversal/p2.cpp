//1205
//Color a Tree
//Graphs;Job Scheduling
#include <iostream>
#include <vector>
#include <set>
#include "stdio.h"
#include <cassert>
#define MAX 1008
using namespace std;

struct Cost {
    int a, b, t, v;
    Cost() { }
    Cost(int a, int b, int t, int v) : a(a), b(b), t(t), v(v) {}

    inline bool operator <(const Cost &c) const {
        int cra = a*c.t, crb = c.a*t;
        if (cra!=crb) return cra>crb;
        return v<c.v;
    }
};

//P is immediate parent P[nodeId] = parentId
int P[MAX];

//A union find type structure
int M[MAX];
set<Cost> S;
Cost C[MAX];

int findParent(int v) {
    if (M[v] == v) return v;
    return M[v] = findParent(M[v]);
}

int main() {
    int n, r;
    while(cin >> n >> r, n|r) {
        for(int i=1; i<=n; i++) {
            int a; cin >> a;
          
            P[i] = 0;
            M[i] = i;
            C[i] = *S.insert(Cost(a, a, 1, i)).first;
        }
        
        for(int i=1; i<=n-1; i++) {
            int u, v; cin >> u >> v;
            P[v] = u;
        }
       
        int total = 0, time = 0;
		/*
		Look for the most expensive node to color
		*/
        while(!S.empty()) 
		{
            Cost c = *S.begin();
			//printf("\nLooking at cost.  a=%d b=%d t=%d v=%d\n", c.a, c.b, c.t, c.v);
            int pid = findParent(P[c.v]);
			//printf("Parent id is pid = %d\n", pid);
            if (pid == 0) {
                total += time * c.a + c.b;
				//printf("Adding total time %d * c.a %d + c.b %d.  total = %d \n", time, c.a, c.b, total);
                time += c.t;
				//printf("Adding c.t %d .  time = %d \n", c.t, time);
                S.erase(c);
                M[c.v] = 0;
            } else {
				/*
				Here it means the parent has not yet been colored
				*/
                //Cost d = *S.find(C[pid]);
				Cost d = C[pid];
				//printf("Cost[%d] is a=%d b=%d t=%d v=%d\n", pid, C[pid].a, C[pid].b, C[pid].t, C[pid].v);
				//printf("Cost    d  a=%d b=%d t=%d v=%d\n", d.a, d.b, d.t, d.v);
				//assert( C[pid].a == d.a && C[pid].b == d.b && C[pid].t == d.t && C[pid].v == d.v);
				
                Cost e(
				//time per cost combined
				c.a + d.a, 
				//increase the fixed cost, pay parents time to color * this colors factor
				c.b + d.b + c.a * d.t, 
				//Time to color this merged node --> combined
				c.t + d.t, 
				//only keep parent node
				d.v);
                //printf("Cost e  a=%d b=%d t=%d v=%d\n", e.a, e.b, e.t, e.v);
				
				S.erase(c);
                S.erase(d);
                S.insert(e);

                M[c.v] = d.v;
                C[e.v] = e;
            }
        }
        cout << total << endl;

    }
}