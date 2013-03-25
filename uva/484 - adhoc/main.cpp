#include <iostream>
#include <algorithm>
#include <cstdio>
#include <cstdlib>
#include <map>
#include <vector>

using namespace std;

typedef long long LL;

int main() {
    LL num;

    vector<LL> list_num;
    map<LL, LL> M;

    while(cin >> num) {
        if (M[num] == '\0') {
            M[num] = 1;
            list_num.push_back(num);
        } else {
            M[num]++;
        }
    }

    for(int i=0; i<list_num.size(); i++) {
        cout << list_num[i] << " " << M[list_num[i]] << endl;
    }

    return 0;
}