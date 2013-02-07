/*
ID: eric7231
PROG: lgame
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

#define FORE(k,a,b) for(int k=(a); k <= (int)(b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (int)(b); ++k)

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

typedef set<uint> si;
typedef vector<set<uint> > vs;
typedef vector<vs> vvs;


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


int scores[] = {
    2,
    5,
    4,
    4, //d
    1,
    6,
    5, //g
    5, //h
    1, 
    7,
    6, //k
    3,
    5,
    2,
    3,
    5,
    7, //q
    2,
    1,
    2,
    4,
    6, //v
    6, //w
    7, 
    5,
    7
};
    

int main()

{


	ofstream fout ("lgame.out");
	ifstream fin ("lgame.in");
	
	ifstream dictIn ("lgame.dict2");

	string letters;
	
	fin >> letters;
	
	vi maxCounts(26,0);
	FOR(c, 0, letters.length()) {
	    maxCounts[letters[c] - 'a'] ++;
	}
	
	sort(all(letters));
	
	//3, 4,     5, 6, 7
	vector<string> longWords;
	int longWordMin = 0;
	
	map<int, vector<string> > bestShortWords;
	map<int, int> bestShortScore;
	
	
	string dictWord;
	
	while(true)
	{
	    dictIn >> dictWord;
	    if (dictWord == ".")
	        break;
	    
	    string sorted = dictWord;
	    
	    int lettersIdx = 0;
	    
	    int key = 0;
	    bool valid = true;
	    int score = 0;
	    
	    sort(all(sorted));
	    
	    FOR(c, 0, sorted.length()) {
	        size_t idx = letters.find_first_of(sorted[c], lettersIdx);
	        if (idx == string::npos) {
	            valid = false;
	            cout << sorted[c] << " not found in letters " << letters
	            << "  idx " << lettersIdx << endl;
	            break;
	        }
	        lettersIdx = idx;
	        key |= (1 << idx);
	        
	        score += scores[dictWord[c] - 'a'];
	    }
	    
	    cout << "Word " << dictWord << " Score " << score << endl;
	    
	    if (!valid) {
	        continue;
	    }
	    
	    
	    
	}
	
	return 0;
}



