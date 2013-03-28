#include <iostream>
#include <algorithm>
#include <vector>
#include <fstream>
#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <sstream>
#include <cmath>

using namespace std;

typedef unsigned long long ULL;
typedef long long LL;

#define MAX 1000000

bool isnprime[MAX];

void sieve() {
    isnprime[0] = isnprime[1] = true;

    for(int i=2; i<=sqrt(MAX); i++) {
        if (!isnprime[i]) {
            for(int j=i*i; j<MAX; j+=i)
                isnprime[j] = true;
        }
    }
}

int main() {
    sieve();

    int N;
    while(cin >> N) {
        int revN;
        stringstream ss; ss << N;
        string tempstr; ss >> tempstr; ss.clear();
        reverse(tempstr.begin(), tempstr.end());
        ss << tempstr; ss >> revN;

        if (isnprime[N])
            cout << N << " is not prime." << endl;
        else {
            if (N != revN) {
                if (!isnprime[N] && isnprime[revN])
                    cout << N << " is prime." << endl;
                else if (!isnprime[N] && !isnprime[revN])
                    cout << N << " is emirp." << endl;
            }
            else
                if (!isnprime[N])
                    cout << N << " is prime." << endl;
        }
    }

    return 0;
}