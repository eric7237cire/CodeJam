#include <iostream>
#include <algorithm>
#include <vector>
#include <fstream>
#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <queue>

using namespace std;

typedef unsigned long long ULL;
typedef long long LL;

int BFS(string str) {
    queue<string> Q;
    int ctr=0;

    for(char i='a'; i<='z'; i++) {
        string temp; temp.push_back(i);
        Q.push(temp);
    }

    while(!Q.empty()) {
        string strtemp = Q.front(); Q.pop();

        ctr++;
        if (strtemp == str) break;

        for(char i=strtemp[strtemp.length()-1] + 1; i<='z'; i++) {
            string cand = strtemp + i;
            Q.push(cand);
        }
    }

    return ctr;
}

bool valid(string str) {
    for(int i=1; i<str.length(); i++) {
        if (str[i] <= str[i-1])
            return false;
    }
    return true;
}

int main() {
    string str;
    while(cin >> str) {
        if (!valid(str)) cout << '0' << endl;
        else cout << BFS(str) << endl;
    }

    return 0;
}