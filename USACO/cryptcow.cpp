/*
ID: eric7231
PROG: cryptcow
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

template <class K, class V> 
V getMapValue( const map<K,V>& aMap, const K& key, const V& defaultValue )
{
    typename map<K, V>::const_iterator it = aMap.find(key);
    if ( it == aMap.end() )
        return defaultValue;
    return it->second;
}


const int notConnected = numeric_limits<int>::max();

int op_decrease (int i) { return --i; }

template<typename T>
ostream& operator<<( ostream& os, const vector<T>& vec )
{
    FOR(i, 0, vec.size())
    {
        os << setw(5) << vec[i];
    }
    return os;
}

bool check(const string& cur, const string& finalMsg)
{
    /*
    FOR(cIt, 0, cur.size())
        {
            switch(cur[cIt])
            {
                case 'C': C.pb(cur[cIt]); break;
                case 'O': O.pb(cur[cIt]); break;
                case 'W': W.pb(cur[cIt]); break;
            }
        }
      */  
      
  if (cur.size() == finalMsg.size())
      return true;
  
  FOR(c, 0, cur.size())
  {
      if (cur[c] == 'C')
          break;
      
      if (cur[c] == 'O' || cur[c] == 'W')
          return false;
  }
  
  for(int c = cur.size() - 1; c >= 0; --c)
  {
      if (cur[c] == 'W')
          break;
      
      if (cur[c] == 'O' || cur[c] == 'C')
          return false;
  }
  /*
    int cIndex = cur.find_first_of('C');
    int oIndex = cur.find_first_of('O');
    int wIndex = cur.find_first_of('W');
    
    int wLastIndex = cur.find_last_of('W');
    int oLastIndex = cur.find_last_of('O');
	int cLastIndex = cur.find_last_of('C');
	
    if (cIndex == string::npos || oIndex == string::npos
        || wIndex == string::npos)
        return false;
        
		
    if (cIndex>oIndex || cIndex>wIndex
        || wLastIndex < cLastIndex || 
        wLastIndex < oLastIndex) 
            return false;
    */
    return true;
}

int main() {
    
    string finalMsg = "Begin the Escape execution at the Break of Dawn";
    
	ofstream fout ("cryptcow.out");
    ifstream fin ("cryptcow.in");
	
    string msg;
    getline(fin, msg);
    
    if (finalMsg == msg) {
        fout << "1 0" << endl;
        return 0;
    }
     
    int ss = msg.size(), ts = finalMsg.size();
    if (ts>ss || (ss-ts)%3!=0) {
            fout << 0 << " " << 0 << endl;
			return 0;
    }
    int depth = (ss-ts)/3;
	vi s_alphabet(128, 0);
	vi f_alphabet(128, 0);
    
    FOR(f, 0, finalMsg.size()) f_alphabet[finalMsg[f]]++;
    FOR(s, 0, msg.size()) s_alphabet[msg[s]]++;

    FOR(i, 0, 128) {
		if (s_alphabet[i]!=f_alphabet[i] && !(i=='C'||i=='O'||i=='W'))  {
			fout << 0 << " " << 0 << endl;
			return 0;
		}
	}
    int ci = s_alphabet['C'];
	int oi = s_alphabet['O'];
	int wi = s_alphabet['W'];
    //cout << ci << " " << oi << " " << wi << endl;
    if (ci!=depth || oi!=depth || wi!=depth) {
            fout << 0 << " " << 0 << endl;
			return 0;
    }

    
    queue<string> toVisit;
    set<string> visited;
    
    toVisit.push(msg);
    
    while(!toVisit.empty()) {
        string cur = toVisit.front();
        toVisit.pop();
        
       // cout << "Visiting " << cur << endl;
        if (contains(visited, cur))
            continue;
        
        visited.insert(cur);
        
        if (cur.size() == finalMsg.size() && cur == finalMsg)
        {
            int steps = (msg.length() - finalMsg.length()) / 3;
            fout << "1 " << steps << endl;
            return 0;
        }

		if (cur.size() == finalMsg.size())
			continue;
        
        vi C,O,W;
        
        int cIndex = -1;
        
        while( (cIndex = cur.find_first_of('C', cIndex+1)) != string::npos)
        {
            int oIndex = cIndex;
            assert(cIndex >= 0 && cIndex < (int)cur.length());
            
            while( (oIndex = cur.find_first_of('O', oIndex+1)) != string::npos)
            {
                assert(oIndex > cIndex);
                int wIndex = oIndex;
                
                while( (wIndex = cur.find_first_of('W', wIndex+1)) != string::npos)
                {
                    assert(cIndex < oIndex);
                    assert(oIndex < wIndex);
                    
                    //Before C
                    string copy(cur.begin(), cur.begin() + cIndex);
                    
                    //Between O and W
                    copy.append(cur.begin()+oIndex+1, cur.begin()+wIndex);
                    
                    //Between C and O
                    copy.append(cur.begin() + cIndex+1,
                        cur.begin()+oIndex);
                    
                    //After W
                    copy.append(cur.begin() + wIndex+1,
                        cur.end());
                    
                    //copy.erase(wIndex, 1);
                    //copy.erase(oIndex, 1);
                    //copy.erase(cIndex, 1);
                    
                    if (check(copy, finalMsg))
                        toVisit.push(copy);
                }
            }
            
            //break;
        }
            
    }
    
    fout << 0 << " " << 0 << endl;
	
    return 0;
}
