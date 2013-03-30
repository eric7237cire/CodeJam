#include <iostream>
#include <algorithm>
#include <cstdio>
#include <cstdlib>
#include <vector>
#include <cstring>

using namespace std;

typedef unsigned long long ULL;
typedef long long LL;

vector<int> V;
int res[6];

void result(int index, int pos) {
    if (pos == 6) {
        cout << res[0];
        for(int i=1; i<6; i++)
            cout << " " << res[i];
        cout << endl;
        return;
    }

    for(int i=index; i<V.size(); i++) {
        res[pos] = V[i];
        result(i+1, pos+1);
    }
}

int main() {
    int num;
    int c=0;

    while(cin >> num && num) {
        V.clear();
        memset(&res, 0, sizeof(res));
        
        for(int i=0; i<num; i++) {
            int n;
            cin >> n;
            V.push_back(n);
        }
        if (c > 0)
            cout << endl;

        result(0,0);
        c++;
    }

    return 0;
}