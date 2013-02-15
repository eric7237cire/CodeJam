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
typedef pair<uint,uint> uu;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 

int main() 
{
    int n;
    cin >> n;
    
    typedef map<string, int> CountryMap;
    map<string, int> countryCounts;
    
    string line;
	//newline after N
    getline(cin, line);

    FOR(i, 0, n)
    {
        getline(cin, line);
        int sp = line.find_first_of(' ', 0);

        string country(line.begin(), line.begin()+sp);
        
		//When entry does not exist, default constructor of int is called
        countryCounts[country]++; 
    }
    
    for(CountryMap::iterator it = countryCounts.begin(); it != countryCounts.end(); ++it)
    {
        cout << it->first << " " << it->second << endl;
    }
     return 0;   
}
