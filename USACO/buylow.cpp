/*
ID: eric7231
PROG: buylow
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
#include <iomanip>
#include <sstream>
#include <bitset>
#include <limits>
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
#define SZ(x) ((int) (x).size())

template <class K, class V> 
V getMapValue( const map<K,V>& aMap, const K& key, const V& defaultValue )
{
    typename map<K, V>::const_iterator it = aMap.find(key);
    if ( it == aMap.end() )
        return defaultValue;
    return it->second;
}


const int notConnected = numeric_limits<int>::max();


template<typename T>
ostream& operator<<( ostream& os, const vector<T>& vec )
{
    FOR(i, 0, vec.size())
    {
        os <<  setw(5) << vec[i]; // << endl;
    }
    return os;
}



struct BigNum{
		
        vector<long> s;
        BigNum(long n=0) : s(200, 0){
                s[0]=n;
                //cout << "Constructor " << s << endl;
        }
        void smooth(){
                for(long i=0;i<199;++i){
                        s[i+1]+=s[i]/1000000;
                        s[i]%=1000000;
                }
        }
        void operator-=(BigNum b){
                for(long i=0;i<200;++i)
                        s[i]-=b.s[i];
                for(long i=0;i<199;++i){
                        while(s[i]<0){
                                s[i]+=1000000;
                                --s[i+1];
                        }
                        s[i+1]+=s[i]/1000000;
                        s[i]%=1000000;
                }
        }
        void operator+=(BigNum b){
                for(long i=0;i<200;++i)
                        s[i]+=b.s[i];
                smooth();
        }
        static void output(long n, ostream& os){
                string str;
				ostringstream ostr;
				ostr << n;
                str = ostr.str();
               // cout << "Str " << str << endl;
				for(long l=str.length();l<6;++l)
                        os << ('0');
                os << str;
        }
        
};

ostream& operator<<(ostream& os, const BigNum& bignum){
                long i=199;
                for(;bignum.s[i]==0 && i > 0;--i);
                os << bignum.s[i];
                for(--i;i>=0;--i)
                        bignum.output(bignum.s[i], os);
                
                return os;
        }

int main()

{


	ofstream fout ("buylow.out");
	ifstream fin ("buylow.in");

	int N;
	fin >> N;
	
	vi num(N);
	FOR(n, 0, N)
	    fin >> num[n];
	
	//Add a common ending value
	num.pb(-1);
	vi q(N+1);
	vector<BigNum> seqCount(N+1, BigNum(0) );
	
	//cout << seqCount[0] << endl;
	//return 0;
	/*seqCount[0] = 99999;
	for(int i = 0; i < 1000; ++i) {
		seqCount[0]+=seqCount[0];
		cout << seqCount[0] << endl;
	}*/

    FORE(k, 0, N)
    {
        int max = 0;
        FOR(j, 0, k) {
            if (num[k] < num[j]) {
                if (q[j] > max) {
                    max = q[j];
                }
            }
        }
        q[k] = max + 1;        
    }
    
    vector<BigNum> sum(N+1);
    vi s( num );
    vi dp( N+1, 0);
    //s.pb(-1);
    //s[N]=-1;
        for(int i=0;i<=N;++i){
                dp[i]=1;
                for(int j=0;j<i;++j)
                        if(s[j]>s[i]&&dp[j]+1>dp[i])
                                dp[i]=dp[j]+1;
        }
        for(int i=0;i<=N;++i){
                if(dp[i]==1)sum[i]=1;
                int j;
                for(j=0;j<i;++j)
                        if(s[j]>s[i]&&dp[j]+1==dp[i])
                                sum[i]+=sum[j];
                for(j=0;j<i&&(s[j]!=s[i]||dp[j]!=dp[i]);++j);
                if(j!=i)sum[i]-=sum[j];
        }
    cout << s << endl;
    cout << dp << endl;
    cout << sum << endl;
    
    //Now count distince sequences
    FORE(i, 0, N)
    {
        if (q[i] == 1) {
            seqCount[i] = 1;
        }
        int j;
        for(j=0; j < i; ++j)
        {
            //Look for numbers that could be next in sequence
            if (num[j] > num[i] &&
                q[j]+1 == q[i])
            {
                seqCount[i] += seqCount[j];
            }
                
        }
        
        //Subtract for duplicates;  j will be same number with same seq
        for(j=0; j<i && (num[j]!=num[i] || q[j]!=q[i]);++j);
                
        if(j!=i)seqCount[i]-=seqCount[j];

		cout << "Sequence counts after " << i << endl;
		cout << seqCount << endl;
    }
    
    cout << num << endl;
	cout << q << endl;
	cout << seqCount << endl;
			
	fout << q[N] - 1 << " " << seqCount[N] << endl;

	
	return 0;
}



