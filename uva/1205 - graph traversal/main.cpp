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

struct Cost 
{
    int costPerUnitTime, fixedCost, timeNeeded, componentId;
    Cost() { }
    Cost(int costPerUnitTime, int fixedCost, int timeNeeded, int componentId) : costPerUnitTime(costPerUnitTime), fixedCost(fixedCost), timeNeeded(timeNeeded), componentId(componentId) {}

    inline bool operator <(const Cost &c) const {
        int cra = costPerUnitTime*c.timeNeeded;
		int crb = c.costPerUnitTime*timeNeeded;
		
		/*Order by the one that is most expensive to do second
		The time needed by component B * cost / time of component A
		*/
        if (cra!=crb) return cra>crb;
		
		//In order to break ties 
        return componentId<c.componentId;
    }
};

//P is immediate parent P[nodeId] = parentId
int P[MAX];

//A union find type structure
int M[MAX];
set<Cost> S;

//Store iterators as they are never invalidated
set<Cost>::iterator C[MAX];

//Union find type function.  Find component id containing vertex v / component id v 
int findParent(int v) {
    if (M[v] == v) return v;
	
	//Save time by setting all members of the component to the component id 
    return M[v] = findParent(M[v]);
}

int main() {
    int n, r;
    while(cin >> n >> r, n|r) {
        for(int i=1; i<=n; i++) {
            int costPerUnitTime; cin >> costPerUnitTime;
			//Initialize parent to 0, note nodes start at 1 so root will have P[root] == 0
            P[i] = 0;
			
			//Component id 
            M[i] = i;
			
			//Initial fixed cost is costPerUnitTime * 1 == costPerUnitTime
            C[i] = S.insert(Cost(costPerUnitTime, costPerUnitTime, 1, i)).first;
        }
        
		//Read in the tree
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
            const Cost& c = *S.begin();
			//printf("\nLooking at cost.  a=%d fixedCost=%d timeNeeded=%d v=%d\n", c.a, c.fixedCost, c.timeNeeded, c.v);
            int pid = findParent(P[c.componentId]);
			//printf("Parent id is pid = %d\n", pid);
            if (pid == 0) {
				//This means this component includes the root or a colored component connected directly/indirectly to root
				
                total += time * c.costPerUnitTime + c.fixedCost;
				//printf("Adding total time %d * c.costPerUnitTime %d + c.fixedCost %d.  total = %d \n", time, c.costPerUnitTime, c.fixedCost, total);
                time += c.timeNeeded;
				//printf("Adding c.timeNeeded %d .  time = %d \n", c.timeNeeded, time);
                S.erase(c);
				
				//Make it so future components detect that they are connected to the colored root 
                M[c.componentId] = 0;
            } else {
				/*
				Here it means the parent has not yet been colored
				*/
                
				const Cost& d = *C[pid];
				//printf("Cost[%d] is costPerUnitTime=%d fixedCost=%d timeNeeded=%d componentId=%d\n", pid, C[pid].costPerUnitTime, C[pid].fixedCost, C[pid].timeNeeded, C[pid].componentId);
				//printf("Cost    d  costPerUnitTime=%d fixedCost=%d timeNeeded=%d componentId=%d\n", d.costPerUnitTime, d.fixedCost, d.timeNeeded, d.componentId);
				//assert( C[pid].costPerUnitTime == d.costPerUnitTime && C[pid].fixedCost == d.fixedCost && C[pid].timeNeeded == d.timeNeeded && C[pid].componentId == d.componentId);
				
                Cost e(
				//time per cost combined
				c.costPerUnitTime + d.costPerUnitTime, 
				//increase the fixed cost, pay parents time to color * this colors factor
				c.fixedCost + d.fixedCost + c.costPerUnitTime * d.timeNeeded, 
				//Time to color this merged node --> combined
				c.timeNeeded + d.timeNeeded, 
				//only keep parent node
				d.componentId);
                //printf("Cost e  costPerUnitTime=%d fixedCost=%d timeNeeded=%d componentId=%d\n", e.costPerUnitTime, e.fixedCost, e.timeNeeded, e.componentId);
				
				//Erase current node 'c' which is the beginning of the set 
				S.erase(S.begin());
                S.erase(C[pid]);
                
				//Merge node with parent
                M[c.componentId] = d.componentId;
				
				//Store iterator of new merged node
                C[e.componentId] = S.insert(e).first;
            }
        }
        cout << total << endl;

    }
}