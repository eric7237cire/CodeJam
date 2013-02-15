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
typedef vector<pair<int,int> > vii;
typedef vector<vii> vvii;
typedef pair<uint,uint> uu;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 


/*
curOrder -- vector holding the current ordering.  Length stays constant

*/
void backtrack( string& curOrder, int usedLetters,
    int numSet,
    const vi& placedBefore,
    int existMask,
    vector<string>& ans)
{
    
    if (numSet*2-1 == curOrder.length())
    {
        ans.pb(curOrder);
        return;
    }
    
    FOR(let, 0, 26)
    {
		//Each letter that precedes this one must have been already placed
        if ( (usedLetters & placedBefore[let]) != placedBefore[let])
            continue;
        
		//Letter must exist 
        if ( ( 1<<let & existMask ) == 0) 
            continue;
        
        //Letter must not have already been used
        if ( ( 1<<let & usedLetters ) != 0) 
            continue;
        
        //Since there is a space between, multiply index by 2
        curOrder[numSet*2] = let + 'A';
        
        //Continue searching
        backtrack(curOrder, 
            usedLetters | 1 << let, //update usedLetters to avoid repeats
            numSet+1,
            placedBefore, existMask, ans);
            
        
    }
    
}


int main() 
{
	int T;
    cin >> T;
    
	//50 constraints at 4 characters each
	string line;
	
	//Chomp newline after test case number
	getline(cin, line);	
	
    FOR(t, 0, T)
    {
        //Chomp a newline that goes before each input
        getline(cin, line);
        getline(cin, line);
        
        int existMask = 0;
        int totalVar = 0;
        
        //Get all upper case letters and add to existMask
        FOR(c, 0, line.length())
        {
            int ch = line[c] - 'A';
            if (ch < 0 || ch >= 26)
                continue;
            
            existMask |= (1 << ch);
            ++totalVar;
        }
        
        //Ok if we can an eof flag, but not badbit or failbit
        bool ok = !getline(cin, line).fail();
        assert(ok && line.length() >= 3);
        
        //Build up bit masks of which letters must be precede others
        //placedBefore['E'] = 00010001101 where each set bit means 
        //that letter must go before 'E', ie A < E; C < E; D < E
        vi placedBefore(26, 0);
        
        for(int c = 0; c < line.length(); c+=4)
        {
            int ltLet = line[c] - 'A';
            assert(line[c+1] == '<');
            int gtLet = line[c+2] - 'A';
            
            //check bounds
            assert(ltLet >= 0 && ltLet < 26);
            assert(gtLet >= 0 && gtLet < 26);
            
            placedBefore[gtLet] |= 1 << ltLet;
        }
        
        FOR(i, 0, 26)
        {
            //placedBefore[i] &= existMask;
        }
    
        //int orderingLength = NumberOfSetBits(existMask);
        int orderingLength = totalVar;
        
        vector<string> ans;
    
        //Allocate space for the permutation plus enough for spaces
        string curOrder(orderingLength*2-1, ' ');
        
        backtrack(curOrder, 0, 0, placedBefore, existMask, ans);
        
        sort( all(ans) );
        if (t > 0)
            cout << "\n";
        
        if (ans.empty())
            ans.pb("NO");
        
        FOR(i, 0, ans.size())
        {
            cout << ans[i] << endl;
        }        
    }    
   // cout << endl;
     return 0;   
}
