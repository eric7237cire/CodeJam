#include<cstdio>
#include<sstream>
#include<cstdlib>
#include<cctype>
#include<cmath>
#include<algorithm>
#include<set>
#include<queue>
#include<stack>
#include<list>
#include<iostream>
#include<string>
#include<vector>
#include<cstring>
#include<map>
#include<cassert>
#include<climits>
using namespace std;

#define REP(i,n) for(int i=0, _e(n); i<_e; i++)
#define FOR(i,a,b) for(int i(a), _e(b); i<=_e; i++)
#define FORD(i,a,b) for(int i(a), _e(b); i>=_e; i--) 
#define FORIT(i, m) for (__typeof((m).begin()) i=(m).begin(); i!=(m).end(); ++i)
#define SET(t,v) memset((t), (v), sizeof(t))
#define ALL(x) x.begin(), x.end()
#define UNIQUE(c) (c).resize( unique( ALL(c) ) - (c).begin() )

#define sz size()
#define pb push_back
#define VI vector<int>
#define VS vector<string>

typedef long long LL;
typedef long double LD;
typedef pair<int,int> pii;

#define D(x) if(1) cout << __LINE__ <<" "<< #x " = " << (x) << endl;
#define D2(x,y) if(1) cout << __LINE__ <<" "<< #x " = " << (x) \
     <<", " << #y " = " << (y) << endl;

int main() {
        int T; cin >> T;
        REP(t, T) {
                string cards[52];
                REP(i, 52) {
                        string card;
                        cin >> card;
                        cards[i] = card;
                }
                int Y=0;
                int ptr = 51-25;

                REP(i, 3) {
                        int X;
                        if (cards[ptr][0] >= '2' && cards[ptr][0] <= '9') X = cards[ptr][0] - '0';
                        else X = 10;
                        Y+=X;

                        cards[ptr] = "X"; ptr--;

                        REP(j, 10-X) {
                                cards[ptr] = "X";
                                ptr--;
                        }
                }
                ptr=0;
                REP(i, 52) {
                        if (cards[i] != "X") ptr++;
                        if (ptr == Y) {ptr = i; break;}
                }
                cout << "Case " << t+1 << ": " << cards[ptr] << endl;
        }
        return 0;
}