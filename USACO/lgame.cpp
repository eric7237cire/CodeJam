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
        os <<  vec[i] << endl;
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
	
	ifstream dictIn ("lgame.dict");

	string letters;
	
	fin >> letters;
	
	vi maxCounts(26,0);
	FOR(c, 0, letters.length()) {
	    maxCounts[letters[c] - 'a'] ++;
	}
	
	sort(all(letters));
	
	//Store best single word	 
	vector<string> longWords;
	int longWordMin = 0;
	
	//Store best short words, key is a binary mask of which letters were used
	map<int, vector<string> > bestShortWords;
	map<int, int> bestShortScore;
	
	
	string dictWord;
	
	while(true)
	{
	    dictIn >> dictWord;
	    if (dictWord == ".")
	        break;
	    
	    string sorted = dictWord;
	    sort(all(sorted));
	    
	    int lettersIdx = 0;
	    
	    int key = 0;
	    bool valid = true;
	    int score = 0;
	    
	    
	    //Build the 'key' of which letters were used
	    FOR(c, 0, sorted.length()) {
	        size_t idx = letters.find_first_of(sorted[c], lettersIdx);
	        if (idx == string::npos) {
	            valid = false;
	            break;
	        }
	        lettersIdx = idx+1;
	        key |= (1 << idx);
	        
	        score += scores[dictWord[c] - 'a'];
	    }
	    
	    if (!valid) {
	        continue;
	    }
	    
	    //No need to store words that are no longer the best
	   	if (score > longWordMin) {
				longWordMin = score;
				longWords.clear();
        }

        if (score >= longWordMin) {
            longWords.pb(dictWord);
        }
    
		if (dictWord.length() >= 5)
		    continue;
	    
		//Deal with short words
		int currentMin = 0;
		if (contains(bestShortScore, key)) {
			currentMin = bestShortScore[key];
		}

		//Clear lower words
		if (score > currentMin)
		{
			bestShortWords[key].clear();
			bestShortScore[key] = score;
		}

		if (score >= currentMin) 
		{
			bestShortWords[key].pb(dictWord);
		}
	}

	//cout << "Long words " << longWords << endl;
	
	//For short words, we at least have to do better than with a long word
	int ansScore = longWordMin;
	vector<string> ans(longWords);

	FOR(k1, 0, (1 << 7) - 1)
	{
		if (!contains(bestShortScore, k1))
			continue;

		FOR(k2, k1, (1 << 7) - 1)
		{
			if (!contains(bestShortScore, k2))
				continue;

			//Add up k1 and k2
			bool valid = true;

			//Do another letter count (cant do a binary or because if they both used the one of 2 letters, the index would be the same
			vi counts(26, 0);
			FOR(i, 0, letters.length())
			{
				if (  ( (1 << i) & k1 ) == 0 )
					continue;

				int letIdx = letters[i] - 'a';
				counts[ letIdx ] ++;
				if (counts[ letIdx ] > maxCounts[letIdx]) {
					//cout << "k1 " << k1 << " not valid too many " << letters[i] << " " << i << " " << counts[ letIdx ] << endl;
					valid = false;
					break;
				}
			}

			FOR(i, 0, letters.length())
			{
				if (  ( (1 << i) & k2 ) == 0 )
					continue;

				int letIdx = letters[i] - 'a';
				counts[ letIdx ] ++;
				if (counts[ letIdx ] > maxCounts[letIdx]) {
					//cout << "k2 " << k2 << " not valid too many " << letters[i] << " " << i << " " << counts[ letIdx ] << endl;
					valid = false;
					break;
				}
			}

			if (!valid)
				continue;

			//cout << "Valid keys " << k1 << ", " << k2 << endl;

			int shortScore = bestShortScore[k1] + bestShortScore[k2];

			if (shortScore < ansScore)
				continue;

			if (shortScore > ansScore)
			{
			    //found a new best score, clear current best list
			    ansScore = shortScore;
				ans.clear();
			}

			const vector<string>& k1Words = bestShortWords[k1];

			const vector<string>& k2Words = bestShortWords[k2];

			assert(!k1Words.empty());
			assert(!k2Words.empty());

			//Add all combinations of short words
			FOR(k1w, 0, k1Words.size()) 
			{
				FOR(k2w, 0, k2Words.size())
				{
					string k1wStr = k1Words[k1w];
					string k2wStr = k2Words[k2w];

					if (k1wStr < k2wStr) {
						string a = k1wStr + " " + k2wStr;
						ans.pb(a);
					} else {
						string a = k2wStr + " " + k1wStr;
						ans.pb(a);
					}
				}
			}
		}
	}

	fout << ansScore << endl;

	sort(all(ans));

	fout << ans ;
	return 0;
}



