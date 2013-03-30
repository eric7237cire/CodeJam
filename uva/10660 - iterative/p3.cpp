#include <iostream>
#include <cstring>
#include <algorithm>
#include <limits.h>
using namespace std;
 
int nTest;
 
int nNonNull;
int areaPopulation[25], distanceSumMin;
int officeI = 0, officeArea[5], officeAreaSol[5];
 
void solve(int areaL) {
    if (officeI < 5) {
        for (int areaI = areaL; areaI < 25; ++areaI) {
            // backtrack
            officeArea[officeI++] = areaI;
            solve(areaI + 1);
            --officeI;
        }
    } else {
        // calculate min distances
        int distanceSum = 0;
        for (int areaI = 0; areaI < 25; ++areaI)
            if (areaPopulation[areaI] > 0) {
                int minDistance = INT_MAX;
                for (int officeI = 0; officeI < 5; ++officeI)
                    minDistance = min(minDistance, areaPopulation[areaI] * (abs(areaI % 5 - officeArea[officeI] % 5) + abs(areaI / 5 - officeArea[officeI] / 5)));
                distanceSum += minDistance;
            }
        // minimize
        if (distanceSum < distanceSumMin) {
            distanceSumMin = distanceSum;
            for (int i = 0; i < 5; ++i)
                officeAreaSol[i] = officeArea[i];
        }
    }
}
 
int main() {
    cin >> nTest;
    for (int testI = 0; testI < nTest; ++testI) {
        // nNonNull
        cin >> nNonNull;
        // areaPopulation
        for (int i = 0; i < 25; ++i) //reset
            areaPopulation[i] = 0;
        for (int areaI = 0; areaI < nNonNull; ++areaI) { // input
            int x, y;
            cin >> x;
            cin >> y;
            cin >> areaPopulation[x * 5 + y];
        }
        // solve
        distanceSumMin = INT_MAX;
        solve(0);
        for (int i = 0; i < 5; ++i)
            cout << officeAreaSol[i] << ((i == 4) ? "\n" : " ");
    }
    return 0;
}