/*
ID: eric7231
PROG: shopping
LANG: C++
*/
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <set>
#include <vector>
#include <algorithm>
#include <cassert>
#include <iterator>
#include <sstream>
#include <bitset>
#include <cctype>
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(uint k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(uint k=(a); k < (b); ++k)

#define pb push_back 
#define mp make_pair 

typedef vector<int> vi; 
typedef vector<double> vd;
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef vector<vd> vvd;
typedef pair<int,int> ii;
typedef pair<uint,uint> uu;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 

class Offer;

istream& operator>>(istream& in, Offer& rhs);

class Offer
{
public:
    uint n;
    
    map<uint, uint> productAmount;
    
    uvi prodIdxAmount;
    
    uint p;
    
    friend istream& operator>> (istream &in, Offer &rhs);  
};

istream& operator>>(istream& in, Offer& rhs)
{
    in >> rhs.n;
    uint c, k;
    FOR(p, 0, rhs.n)
    {
        in >> c >> k;
        rhs.productAmount[c] = k;
    }
    
    in >> rhs.p;
    
    return in;
}

class Basket
{
public:
    uvi prodQuantity;
    uint price;
    
};

uint quantityToIndex(const uvi& quantity)
{
    uint ret = 0;
    uint pow6 = 1;
    FOR(i, 0, quantity.size())
    {
        ret += quantity[i] * pow6;
        pow6 *= 6;
    }
    return ret;
}

uvi indexToQuantity(uint index)
{
    uvi ret;
    
    while(index > 0)
    {
        ret.pb(index % 6);
        index /= 6;
    }
    
    return ret;
}
    

int main() {
    
	ofstream fout ("shopping.out");
    ifstream fin ("shopping.in");
	
    uint s;
    //Special Offers
    fin >> s;
    
    vector< Offer > offers(s);
    
    FOR(i, 0, s)
    {
        fin >> offers[i];
    }
    
    uint b;
    
    fin >> b;
    
    map<uint, uint> prodCodeToIdx;
    uvi prodIdxToCode;
    
    uvi prodRegPrice(b, 0);
    
    Basket target;
    
    uint c,k,p;
    
    FOR(pIdx, 0, b)
    {
        fin >> c >> k >> p;
        prodCodeToIdx[c] = pIdx;
        prodIdxToCode.pb( c );
        
        prodRegPrice[pIdx] = p;
        
        target.prodQuantity.resize(b);
        
        target.prodQuantity[pIdx] = k;
    }
    
    uint maxState = 1;
    
    FOR(pIdx, 0, b)
    {
        maxState *= 6;
    }
    
    uvi minPrice(maxState, 300000000);
    
    map<uvi, uint> stateToIndex;
    uvvi indexToState;
    
    //minPrice[0] = 0;
    
    //Initialize
    FOR(state, 0, maxState)
    {
        uvi stateQ = indexToQuantity(state);
        
        stateQ.resize(b, 0);
        
        stateToIndex.insert( mp(stateQ, state) );
        indexToState.pb( stateQ );
              
        /*
        uint qIdx = quantityToIndex(stateQ);
        printf(" state %d, idx %d : ", state, qIdx );
        
        FOR( q, 0, stateQ.size() )
        {
            printf(" %d ", stateQ[q] );
        }
        printf("\n");
        */
        uint regPrice = 0;
        
        FOR( q, 0, stateQ.size() ) 
        {
            regPrice += prodRegPrice[q] * stateQ[q];    
        }   
        
        minPrice[ state ] = regPrice;
    }
    
    FOR(offerIdx, 0, s)
    {
        FOR(state, 0, maxState)
        {
            uvi quantity = indexToState[state];
            
            uvi prevQuantity(quantity);
            
            FOR( pcIdx, 0, prodIdxToCode.size() )
            {
                uint code = prodIdxToCode[pcIdx];
                
                uint amt = offers[offerIdx].productAmount[code];
            }
            
        }
        
    }
    
    uint targetIdx = quantityToIndex( target.prodQuantity );
    //cout << targetIdx << endl;
    
    fout << minPrice[targetIdx] << endl;
	
    return 0;
}
