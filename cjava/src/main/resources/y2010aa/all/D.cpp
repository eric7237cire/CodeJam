#include <cstring>
#include <string.h>
#include <map>
#include <deque>
#include <queue>
#include <stack>
#include <sstream>
#include <iostream>
#include <iomanip>
#include <cstdio>
#include <cmath>
#include <cstdlib>
#include <ctime>
#include <algorithm>
#include <vector>
#include <set>
#include <complex>
#include <list>

using namespace std;

int n,N;

//#define SMALL
//#define LARGE
int main() {
	//freopen("a.txt", "rt", stdin);
#ifdef SMALL
	freopen("D-small-attempt0.in","rt",stdin);
	freopen("D-small.out","wt",stdout);
#endif
#ifdef LARGE
	freopen("D-large.in","rt",stdin);
	freopen("D-large.out","wt",stdout);
#endif

	int c;
	cin >> N;
	for(int nn = 1 ; nn <= N ; nn++ ) {
		int m;
		cin>>n>>m;
		//for(int i = 0 ; i < n; i++)
		//	cin>>arr[i];
		vector <pair<int,int> > T,L;
		vector <pair<int,pair<int,int> > > S,D;
		for (int i = 0; i < m; ++i) {
			int ii,jj,kk;
			char tt;
			cin >> ii >> tt;
			ii--;
			if( tt == 'T' ){
				cin >> jj;
				jj--;
				T.push_back(make_pair(ii,jj));
			}else if( tt == 'L' ){
				cin >> jj;
				jj--;
				L.push_back(make_pair(ii,jj));
			}else if( tt == 'S'){
				cin >> jj >> kk;
				jj--;
				kk--;
				S.push_back(make_pair(ii,make_pair(jj,kk)));
			}else{
				cin >> jj >> kk;
				jj--;
								kk--;
				D.push_back(make_pair(ii,make_pair(jj,kk)));
			}
		}
		printf("Case #%d: ", nn);
		vector <int> all(n,0);
		for (int i = 0; i < (1<<n); ++i) {
			bool val = 1;
			for (int ii = 0; ii < T.size(); ++ii) {
				if( (i & (1 << T[ii].first)) != 0 ){
					if( (i & (1 << T[ii].second)) == 0 )
						val = 0;
				}else{
					if( (i & (1 << T[ii].second)) != 0 )
						val = 0;
				}
			}
			for (int ii = 0; ii < L.size(); ++ii) {
				if( (i & (1 << L[ii].first)) != 0 ){
					if( (i & (1 << L[ii].second)) != 0 )
						val = 0;
				}else{
//					cerr << L[i].second << " " << (1 << L[i].second) << " " << (i & (1 << L[i].second)) << " " << ((i & (1 << L[i].second)) == 0) << endl;
					if( (i & (1 << L[ii].second)) == 0 )
						val = 0;
				}
			}
			for (int ii = 0; ii < S.size(); ++ii) {
				if( i & (1 << S[ii].first) ){
					if( ((i & (1 <<S[ii].second.first))==0) != ((i & (1 <<S[ii].second.second)) ==0))
						val = 0;
				}else{
					if( ((i & (1 <<S[ii].second.first))==0) == ((i & (1 <<S[ii].second.second)) ==0))
						val = 0;
				}
			}
			for (int ii = 0; ii < D.size(); ++ii) {
				if( i & (1 << D[ii].first) ){
					if( ((i & (1 <<D[ii].second.first))==0) == ((i & (1 <<D[ii].second.second)) ==0))
						val = 0;
				}else{
					if( ((i & (1 <<D[ii].second.first))==0) != ((i & (1 <<D[ii].second.second)) ==0))
						val = 0;
				}
			}
			if( val ){
				for (int l = 0; l < n; ++l) {
					all[l] |= ((i>>l)&1)+1;
				}
			}
		}
		for (int i = 0; i < n; ++i) {
			if( all[i] == 1 )
				printf(" L");
			else if( all[i] == 2 )
				printf(" T");
			else
				printf(" -");
		}
		printf("\n");
	}
	return 0;
}
