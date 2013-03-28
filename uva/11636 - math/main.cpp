
#include <iostream>
#include <sstream>
#include <vector>
#include <string>
#include <cstdio>
#include <cstring>
#include <algorithm>
#include <queue>
#include <set>
#include <stack>
#include <map>
#include <list>
#include <cmath>

using namespace std;

typedef pair<int,int>   ii;
typedef vector<int>     vi;
typedef vector<ii>      vii;
typedef vector<vi>     vvi;
typedef vector<vii>    vvii;
typedef map<int,int>    mii;

#define EPS 1e-6

int main(){
    int n;
    int CC = 1;
    double lg2 = log(2);
    while(cin >> n, n > 0){
        printf("Case %d: %d\n",CC++, (int)floor(log(n)/lg2 - EPS) + 1);
    }

    return 0;
}