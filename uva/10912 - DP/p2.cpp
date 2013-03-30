//      ID : pallab81
//      PROG : 10912
//      LANG : C++
//      2011-08-21-04.22.45 Sunday
//
//      "I have not failed, I have just found 10000 ways that won't work.
//


#include <iostream>
#include <algorithm>
#include <vector>
#include <sstream>
#include <fstream>
#include <string>
#include <list>
#include <map>
#include <set>
#include <queue>
#include <stack>
#include <functional>
#include <bitset>

#include <ctime>
#include <cassert>
#include <cstdio>
#include <cmath>
#include <cstring>
#include <climits>
#include <cstring>

using namespace std;

#define CI( x ) scanf("%d",&x)
#define CL( x ) scanf("%lld",&x)
#define CD( x ) scanf("%lf",&x )
#define fo(i,ed) for(int i = 0 ; i < ed ; ++i )
#define foit(i, x) for (typeof((x).begin()) i = (x).begin(); i != (x).end(); i++)
#define bip system("pause")
#define Int long long
#define pb push_back
#define SZ(X) (int)(X).size()
#define LN(X) (int)(X).length()
#define mk make_pair
#define f first
#define s second
#define SET( ARRAY , VALUE ) memset( ARRAY , VALUE , sizeof(ARRAY) )

inline void wait( double seconds ){
    double endtime = clock() + ( seconds+ CLOCKS_PER_SEC );
    while( clock() < endtime ){;}
}
int L,S;
int cases;
int memo[ 27 ][ 27 ][ 352 ];

inline int go(int nwLen, int nwSum, int nw){
    if( nwLen==0 ){
        return ( nwSum == 0 ? 1 : 0 );
    }
    if(nw>26){
        return 0;
    }

    int &rem = memo[ nw ][ nwLen ][ nwSum ];
    if( rem!=-1 ){
        return rem;
    }
    rem=0;
    for(int i=nw; i<=26;++i){
        if(nwSum-i<0)continue;

        rem+= go(nwLen-1, nwSum-i,i+1);
    }
return rem;
}

inline void Proc(){
    int var;
    if( L>26 || S>351 ){
        var=0;
    }
    else{
        SET( memo,-1);
        var = go(L,S,1);
    }
    printf("Case %d: %d\n",cases,var);
}

int main(){
    
    cases=1;
    while(scanf("%d %d",&L,&S)==2){
        if( L==0 && S==0 )break;
        Proc();
        ++cases;
    }
return 0;
}