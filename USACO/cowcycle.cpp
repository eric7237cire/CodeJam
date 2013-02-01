/*
ID: eric7231
PROG: cowcycle
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
        os <<  vec[i] << endl;
    }
    return os;
}




#include <cstdlib>

#include <cstring>

#include <cstdio>

#define MAX 10







double minvar=4e+9;

void FDFS(int, int);

void RDFS(int, int);

int nowupper;



int nFront, nRear;

int F[2], R[2];



int front[MAX], rear[MAX];

int ansfront[MAX], ansrear[MAX];

int main()

{


	ofstream fout ("cowcycle.out");
	ifstream fin ("cowcycle.in");

	freopen("cowcycle.in", "r", stdin);

	freopen("cowcycle.out", "w", stdout);

	fin >> nFront >> nRear >> F[0] >> F[1] >> R[0] >> R[1];

	//load
	FDFS(F[0], 0);



	printf("%d", ansfront[0]);

	for(int i=1; i<nFront; i++)

		printf(" %d", ansfront[i]); 

	putchar('\n');

	printf("%d", ansrear[0]);

	for(int i=1; i<nRear; i++)

		printf(" %d", ansrear[i]);      

	putchar('\n');

	return 0;

}



void FDFS(int FLow, int FDepth)

{

	if(FDepth==nFront)
	{
		RDFS(R[0], 0);
		return ;
	}



	for(int i=FLow; i<=F[1]-(nFront-FDepth-1); i++)
	{

		front[FDepth]=i;

		FDFS(i+1, FDepth+1);
	}

	//select Front gear
}



void RDFS(int RLow, int RDepth)

{

	if(RDepth==nRear)
	{

		if(front[nFront-1]*rear[nRear-1]<3*front[0]*rear[0])
			return ;      

		
		double sum=0;

		double Nsigma=0;

		int N=0;

		double temp[5*MAX];

		double delta[5*MAX];



		for(int i=0; i<nFront; i++)
			for(int j=0; j<nRear; j++)
			{

				double res=(double)front[i]/(double)rear[j];

				temp[N]=res, N++;    

			}

			//calculate



			for(int i=1; i<N; i++)

			{

				int index=i;

				double tempv=temp[i];



				for(int j=0; j<i&&index==i; j++)

					if(temp[j]<=temp[i])

						index=j;



				for(int j=i; j>index; j--)

					temp[j]=temp[j-1];

				temp[index]=tempv;

				//insert

			}

			//insertion sort

			for(int i=0; i<N-1; i++)

				delta[i]=temp[i+1]-temp[i];

			//push



			for(int i=0; i<N-1; i++)

				Nsigma+=(delta[i]*delta[i]), sum+=delta[i];

			//calculate variance

			Nsigma-=(sum*sum)/(double)(N-1);



			if(Nsigma<minvar)

				memcpy(ansfront, front, sizeof(front)), memcpy(ansrear, rear, sizeof(rear)), minvar=Nsigma;

			//copy
			return ;

	}

	//end and calculate result



	for(int i=RLow; i<=R[1]-(nRear-RDepth-1); i++)
	{
		rear[RDepth]=i;
		RDFS(i+1, RDepth+1);
	}

	//select Rear gear

}




