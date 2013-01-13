/*
ID: eric7231
PROG: zerosum
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
#include <sstream>
#include <bitset>
#include <cctype>
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
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef pair<int,int> ii;
typedef pair<uint,uint> uu;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 


int concatNums(int left, int right)
{
    int isNeg = left * right < 0 ? -1 : 1;
    left = abs(left);
    right = abs(right);
   // cout << "Concat " << left << " + "	<< right;
	//123 ; 456
	uint rightDigits = right;

	if (right == 0)
	    return 10 * left;
	
	while(rightDigits > 0)
	{
		rightDigits /= 10;
		left *= 10;
	}

	// cout << " = " << left+right << endl;
	
	return isNeg * (left+right);
}

int main() {
    
	ofstream fout ("zerosum.out");
    ifstream fin ("zerosum.in");
	
    uint N;
    
    fin >> N;
    
    uvi betNums(N-1, 0);
    vector<string> ans;
    
    bool done = false;
    while(!done)
    {
        done = true;
    
        //Try all permutations.  This loop finds the first position that can be incremented
        FOR(pos, 0, betNums.size()) 
        {
            betNums[pos]++;
            
            if (betNums[pos] < 3) {
                done = false;
                break;
            }
            
            //This position is now zero, look to increment combin
            betNums[pos] = 0;
        }
        ostringstream os;
        
        //Build the expression
        int val = 0;
        int term = 1;
        os << 1;
        int lastOp = 1; //+ , -1 for -1
        FOR(opIdx, 0, betNums.size())
        {
            int op = betNums[opIdx];
            int digit = 2 + opIdx;
            
            
            if (op == 0) {
                term = concatNums(term, digit);
                os << ' ' << digit;
                continue;
            } 
            
            //Term is done, add it
            val += lastOp * term;
            
            //cout << "Term " << term << " Val " << val
           // << " op " << op << " Digit " << digit << " Exp " << os.str() << endl;
            
            
            term = digit;
            lastOp = op == 1 ? 1 : -1;
            
            if (lastOp == 1) 
                os << "+" << digit;
            else
                os << "-" << digit;
        }
        
        val += lastOp * term;

        if (val == 0) {
            ans.pb(os.str());            
        }
    }
    
    sort( all(ans) );
    FOR(ansIdx, 0, ans.size())
	{
	    fout << ans[ansIdx] << endl;
	}
    
	return 0;
}
