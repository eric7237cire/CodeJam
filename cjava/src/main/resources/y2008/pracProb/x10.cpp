#include <cstdio>
#include <cmath>
#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <float.h>
#include <map>
#include <limits>
#include <cstring>
#include <sstream>
using namespace std;
#define FOR(a,b,c) for(int a=(b);a<(c);a++)
#define SQR(a) ((a)*(a))
//dp state = (mask, which store at, holding perish)
double ans[1 << 16][55][2];
bool perish[16];
int price[55][16];
double X[55], Y[55];
map<string, int> itemNum;
map<int, string> itemName;
int numStores;
int numItems;
int gasPrice;

const int INT_MAX = numeric_limits<int>::max();

inline double dist(int A, int B){
	return sqrt(SQR(X[B]-X[A]) + SQR(Y[B]-Y[A]));
}

double solve(int mask, int store, int havePerish)
{
//	cout << "solve(" << mask << ", " << store << ", " << havePerish << ")" << endl;
	double &ret = ans[mask][store][havePerish];
	if(ret != -1.0) return ret;
	if(mask == (1 << numItems) - 1){
		return dist(store, 0) * gasPrice;
	}
	ret = DBL_MAX;
	FOR(i,0,numItems){
		if(!(mask & (1 << i)) && price[store][i] < INT_MAX)
			ret = min(ret, solve(mask | (1 << i), store, havePerish || perish[i])
				  + price[store][i]);
	}
	FOR(i,1,numStores+1){
		if(i == store) continue;
		double d;
		if(havePerish)
			d = dist(store, 0) + dist(0, i);
		else
			d = dist(store, i);
//		cout << "paying " << gasPrice * d << " to go to " << i << " from " << store << endl;
		ret = min(ret, gasPrice * d + solve(mask, i, false));
	}
//	cout << "solve(" << mask << ", " << store << ", " << havePerish << ") = " << ret << endl;
	return ret;
}

int main(){
	//istream fin(cin);
	//ostream cout(cout);
	int N; cin >> N;
	FOR(tcase,1,N+1){
		FOR(i,0,1<<16) FOR(j,0,55) FOR(k,0,2) ans[i][j][k] = -1.0;
		memset(perish,0,sizeof(perish));
		memset(X,0,sizeof(X));
		memset(Y,0,sizeof(Y));
		itemNum.clear();
		itemName.clear();
		FOR(i,0,55) FOR(j,0,16) price[i][j] = INT_MAX;
		cin >> numItems >> numStores >> gasPrice;
		FOR(i,0,numItems){
			string s; cin >> s;
			if(s[s.length()-1] == '!'){
				perish[i] = 1;
				s = s.substr(0, s.length()-1);
			}
			itemNum[s] = i;
			itemName[i] = s;
		}
		X[0] = Y[0] = 0.0;
		cin.get();
		FOR(i,1,numStores+1){
			char line[1000];
			cin.getline(line, 1000);
			stringstream ss; ss << line;
			ss >> X[i] >> Y[i];
			string tmp;
			while(ss >> tmp){
				int n = tmp.find(":"); tmp[n] = ' ';
				char name[500]; int p;
				sscanf(tmp.c_str(), "%s %d", name, &p);
				price[i][itemNum[name]] = p;
//				cout << name << " costs " << p << " at " << i << endl;
			}
		}
//		cout << "tcase = " << tcase << endl;
		double ret = solve(0, 0, 0); 
		char buf[60];
		memset(buf,0x00,sizeof(buf));
		sprintf(buf, "Case #%d: %.7lf", tcase, ret);
		cout << buf << endl;;
	}
	return 0;
}

