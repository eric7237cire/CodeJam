/*
ID: eric7231
PROG: contact
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
#include <iomanip>
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

int main() {
    
	ofstream fout ("contact.out");
    ifstream fin ("contact.in");
	
    uint patMin, patMax, numToPrint;
    
    fin >> patMin >> patMax >> numToPrint;
    
    /**
len 1
0 
1
len 2
00
01
10
11
Len 3
000
001
etc
    */    
    uvvi lenToPatCounts;
    
    FORE(patLen, 1, patMax)
    {
        uvi patCount(1 << patLen, 0);
        lenToPatCounts.pb(patCount);
    }
    
    uint lenRead = 0;
    string line;
    //Keeps track of last 1, 2, 3, etc bits
    uvi lastNChar(patMax, 0);
    
    while( getline(fin, line) )
    {
        FOR(cIdx, 0, line.length())
        {
            char ch = line[cIdx];
            assert(ch == '0' || ch == '1');
            uint bit = ch == '1' ? 1 : 0;

            ++lenRead;
            
            FORE(patLen, 1, patMax)
            {
                // shift, add the bit, then mask to keep the length consistent
                lastNChar[patLen-1] = 
                    ( (lastNChar[patLen-1] << 1) | bit ) & (lenToPatCounts[patLen-1].size() - 1);
                    
                //if (patLen == 4)
                  //  fout << "cIdx " << cIdx << " 4-- " << lastNChar[patLen-1] << endl;
                
                if (lenRead >= patLen)
                {
                    //we have read enough, lets increment the count!
                    lenToPatCounts[patLen - 1][ lastNChar[patLen - 1] ]++;
                }                    
            }
            
            
        }
            
    }
    
    //Need frequency ==>  len, pattern
    typedef map<uint, set< uu >, greater<uint> > AnsMap;
    
    AnsMap ans;
    
    FORE(patLen, patMin, patMax)
    {
        const uvi& patCount = lenToPatCounts[patLen - 1];
        FOR(pat, 0, patCount.size())
        {
            uint count = patCount[pat];
            if (count == 0)
                continue;
            
            ans[count].insert(  mp( patLen, pat ) );
        }
    }
    
    uint freqIdx = 0;
    
    AnsMap::const_iterator aIt = ans.begin();
    
    while( aIt != ans.end() && freqIdx < numToPrint) 
    {
        ++freqIdx;
        
        uint patIdx = 0;
        uint freq = aIt->first;
        
        fout << freq << endl;
        
        set<uu>::const_iterator it = aIt->second.begin();

        while( it != aIt->second.end() ) 
        {
            ++patIdx;
            uint pat = it->second;
            uint len = it->first;
            
            string patStr = bitset<13>( pat ).to_string();
            
            patStr.erase( patStr.begin(), patStr.begin() + 13 - len);
            while( patStr.length() < len ) {
                patStr.insert(0, "0");
            }
            
			fout << patStr;
            
            
            ++it;
            
            if (it == aIt->second.end()) {
                fout << endl;
                break;
            } else if (patIdx % 6 == 0) {
                fout << endl;
            } else {
                fout << " ";
            }
        }
        
        ++aIt;
    }
    
    
            
    return 0;
}
