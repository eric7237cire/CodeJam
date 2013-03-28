#include <iostream>
#include <algorithm>
#include <vector>
#include <fstream>
#include <cstdio>
#include <cstring>
#include <cstdlib>

using namespace std;

#define MAX 1000001

bool isnprime[MAX];
vector<int> prime;

void sieve() {
    for(int i=2; i<MAX; i++) {
        if (!isnprime[i]) {
            for(int j=i+i; j<MAX; j+=i)
                isnprime[j] = true;
            prime.push_back(i);
        }
    }
}

typedef unsigned long long ULL;
typedef long long LL;

int main() {
    sieve();
    
    int N;

    while(cin >> N && N) {
        int idx=0;
        int ans1, ans2;
        bool found = false;

        while(prime[idx] <= N/2 && !found) {
            if (binary_search(prime.begin(), prime.end(), N-prime[idx])) {
                ans1 = prime[idx];
                ans2 = N-prime[idx];
                found = true;
            }
            idx++;
        }

        cout << N << ":" << endl;
        if (found)
            cout << ans1 << "+" << ans2 << endl;
        else
            cout << "NO WAY!" << endl;

    }

    return 0;
}