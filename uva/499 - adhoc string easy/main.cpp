#include <iostream>
#include <algorithm>
#include <vector>
#include <fstream>
#include <cstdio>
#include <cstring>
#include <cstdlib>

using namespace std;

vector<pair<char, int> > V;

void init() {
    V.resize(52);
    for(int i=0; i<V.size(); i++) {
        if (i >= 0 && i < 26)
            V[i].first = i + 'A';
        else
            V[i].first = i - 26 + 'a';
        V[i].second = 0;
    }
}

bool comp(pair<char, int> A, pair<char, int> B) {
    if (A.second == B.second)
        return (A.first < B.first);
    else
        return (A.second > B.second);
}

int main() {
    string line;

    while(getline(cin, line)) {
        init();

        for(int i=0; i<line.length(); i++) {
            if (line[i] >= 'A' && line[i] <= 'Z')
                V[line[i] - 'A'].second++;
            else if (line[i] >= 'a' && line[i] <= 'z')
                V[line[i] - 'a' + 26].second++;
        }

        sort(V.begin(), V.end(), comp);
        int it=0;

        while(V[it].second == V[0].second) {
            cout << V[it].first; it++;
        }
        cout << " " << V[0].second << endl;
    }   

    return 0;
}
