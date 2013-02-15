#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <stack>
#include <set>
#include <list>
#include <vector>
#include <algorithm>
#include <cassert>
#include <iterator>
#include <sstream>
#include <complex>
#include <bitset>
#include <iomanip>
#include <cctype>
#include <limits>
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(uint k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (int) (b); ++k)

#define pb push_back 
#define mp make_pair 

typedef vector<int> vi; 
typedef vector<double> vd;
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef vector<vd> vvd;
typedef pair<int,int> ii;
typedef vector<pair<int,int> > vii;
typedef vector<vii> vvii;
typedef pair<uint,uint> uu;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 

int main() 
{
	//freopen ("C:\\codejam\\CodeJam\\uva\\11559 - Event Planning\\input.txt","r",stdin);
    //Number of participants: N
    //Budget: B
    //Number of hotels: H
    //Weekends : W
    int N, B, H, W;
    while(scanf("%d%d%d%d", &N, &B, &H, &W) == 4)
    {
        
        vi hotelPrices(H);
        vvi hotelBedsAvail(H, vi(W));
        
        vvii bedPrices(W);
        
        int price;
        int bedAvail;
        FOR(h, 0, H)
        {
            cin >> price;
            
            FOR(w, 0, W)
            {
                cin >> bedAvail;
                bedPrices[w].pb( mp(price, bedAvail) );
            }
        }
        
        int currentMinCost = numeric_limits<int>::max();
        
        FOR(w, 0, W)
        {
            sort( all( bedPrices[w] ));
            int nRest = N;
         
            int cost = 0;
            
            for(int bpIdx = 0; bpIdx < bedPrices[w].size() && nRest > 0; ++bpIdx)
            {
                int take = min(nRest, bedPrices[w][bpIdx].second);
    
                //oops the constraint says all must be in same hotel
                if (take < N)
                    continue;
    
                cost += take * bedPrices[w][bpIdx].first;
                
                nRest -= take;
            }
            
            if (nRest > 0)
                continue;
            
            currentMinCost = min(currentMinCost, cost);
        }
        
        if (currentMinCost <= B)
            cout << currentMinCost << endl;
        else 
            cout << "stay home" << endl;
    }    
     return 0;   
}
