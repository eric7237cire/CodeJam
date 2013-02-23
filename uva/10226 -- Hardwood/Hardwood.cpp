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

#define FORE(k,a,b) for(int k=(a); k <= (int)(b); ++k)
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
typedef map<string, int> msi;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 

int main() 
{
	//freopen ("input.txt","r",stdin);

	int T;
	const int bufSize = 31;
	char buf[bufSize];

	char dum;
	scanf("%d%c%c", &T, &dum, &dum);
	
	FORE(t, 1, T)
	{
		msi count;
		int total = 0;

		while(true)
		{
			char* cstr = fgets(buf, bufSize, stdin);

			if (cstr == NULL)
				break;

			string str(cstr);
			str.erase(str.end() - 1 );

			if (str.length() == 0)
				break;

			//printf("[%s] %d\n", str.c_str(), str.length());
			count[str] ++;
			total++;
		}

		for(msi::const_iterator it = count.begin(); it != count.end(); ++it)
		{
			printf("%s %.4f\n", it->first.c_str(), 100.0 * it->second / total);
		}

		if (t < T)
			cout << endl;
	}
}