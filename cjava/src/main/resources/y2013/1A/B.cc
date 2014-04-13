// another fine solution by misof
// #includes {{{
#include <algorithm>
#include <numeric>

#include <iostream>
#include <sstream>
#include <string>
#include <vector>
#include <queue>
#include <set>
#include <map>
#include <stack>

#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <cctype>
#include <cassert>

#include <cmath>
#include <complex>
using namespace std;
// }}}

/////////////////// PRE-WRITTEN CODE FOLLOWS, LOOK DOWN FOR THE SOLUTION ////////////////////////////////

// pre-written code {{{
// BEGIN CUT HERE
#define DEBUG(var) { cout << #var << ": " << (var) << endl; }
template <class T> ostream& operator << (ostream &os, const vector<T> &temp) { os << "[ "; for (unsigned int i=0; i<temp.size(); ++i) os << (i?", ":"") << temp[i]; os << " ]"; return os; } // DEBUG
template <class T> ostream& operator << (ostream &os, const set<T> &temp) { os << "{ "; for(__typeof((temp).begin()) it=(temp).begin();it!=(temp).end();++it) os << ((it==(temp).begin())?"":", ") << (*it); os << " }"; return os; } // DEBUG
template <class T> ostream& operator << (ostream &os, const multiset<T> &temp) { os << "{ "; for(__typeof((temp).begin()) it=(temp).begin();it!=(temp).end();++it) os << ((it==(temp).begin())?"":", ") << (*it); os << " }"; return os; } // DEBUG
template <class S, class T> ostream& operator << (ostream &os, const pair<S,T> &a) { os << "(" << a.first << "," << a.second << ")"; return os; } // DEBUG
template <class S, class T> ostream& operator << (ostream &os, const map<S,T> &temp) { os << "{ "; for(__typeof((temp).begin()) it=(temp).begin();it!=(temp).end();++it) os << ((it==(temp).begin())?"":", ") << (it->first) << "->" << it->second; os << " }"; return os; } // DEBUG
// END CUT HERE
// }}}

/////////////////// CODE WRITTEN DURING THE COMPETITION FOLLOWS ////////////////////////////////

template <class T> ostream& operator << (ostream &os, const deque<T> &temp) { os << "[ "; for (int i=0; i<temp.size(); ++i) os << (i?", ":"") << temp[i]; os << " ]"; return os; } // DEBUG

long long solve(int t) {
    long long E, R;
    int N;
    cin >> E >> R >> N;
    if (R > E) R = E;
    vector<long long> V(N);
    for (auto &v : V) cin >> v;

    long long answer = 0;
    deque<long long> amount, gain;
    amount.push_back( E );
    gain.push_back( V[0] );

    for (int n=1; n<N; ++n) {
        // zober energiu ktoru uz nebudu moct pouzit
        long long take = R;
        while (take > 0) {
            if (amount.empty()) break;
            if (amount.front() > take) {
                answer += gain.front() * take;
                amount.front() -= take;
                take = 0;
            } else {
                answer += gain.front() * amount.front();
                take -= amount.front();
                amount.pop_front();
                gain.pop_front();
            }
        }
        // pridaj novu energiu
        long long add = R;
        while (!gain.empty() && gain.back() <= V[n]) {
            add += amount.back();
            amount.pop_back();
            gain.pop_back();
        }
        amount.push_back(add);
        gain.push_back(V[n]);
    }
    while (!gain.empty()) {
        answer += amount.front() * gain.front();
        amount.pop_front();
        gain.pop_front();
    }
    return answer;
}

int main() {
    int T; cin >> T;
    for (int t=1; t<=T; ++t) {
        cout << "Case #" << t << ": " << solve(t) << endl;
    }
}
// vim: fdm=marker:commentstring=\ \"\ %s:nowrap:autoread
