/*
ID: eric7231
PROG: calfflac
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
#include <cctype>
using namespace std;

typedef unsigned int uint;

bool isNotAlpha(char c) {
    return !( ('a' <= c && 'z' >= c) || ('A' <= c && 'Z' >= c) ); 
}

char toLowerCase(char c)
{
    return tolower(c);
}

// trim from start
static inline std::string &ltrim(std::string &s) {
        s.erase(s.begin(), std::find_if(s.begin(), s.end(), std::ptr_fun<int, int>(std::isalpha)));
        return s;
}

// trim from end
static inline std::string &rtrim(std::string &s) {
     s.erase(std::find_if(s.rbegin(), s.rend(), std::ptr_fun<int, int>(std::isalpha)).base(), s.end());
     
       // s.erase(std::find_if(s.rbegin(), s.rend(), std::not1(std::ptr_fun<int, int>(std::isalpha))).base(), s.end());
        return s;
}

// trim from both ends
static inline std::string &trim(std::string &s) {
        return ltrim(rtrim(s));
}

template<typename T, typename InputIterator>  
void Print(std::ostream& ostr,   
           InputIterator itbegin,   
           InputIterator itend,   
           const std::string& delimiter)  
{  
    std::copy(itbegin,   
              itend,   
              std::ostream_iterator<T>(ostr, delimiter.c_str()));  
}  

int main() {
	ofstream fout ("calfflac.out");
    ifstream fin ("calfflac.in");
		
    string line;
	string all;
	
    while (std::getline(fin, line))
	{
	    copy(line.begin(), line.end(), back_inserter(all));
	    all.append(1, '\n');
	}
	
	int maxPalinLen = 0;
	string palin;
	
	vector<int> nextChange(all.length());
	vector<int> nextChangeRev;
	
	nextChangeRev.push_back(0);
	char lastChar = tolower(all[0]);
	//cout << all.length() << endl;
	for(int p = 1; p < all.length(); ++p) 
	{
	    //cout << p << ", " << all[p] << endl;
	    if (!isalpha(all[p]) || tolower(all[p]) == lastChar) {
	         nextChangeRev.push_back(nextChangeRev[p-1]);   
	    } else {
	        nextChangeRev.push_back(p);
	        lastChar = tolower(all[p]);
	    }
	}
	
	cout << "Next change rev ";
	Print<int, vector<int>::iterator>(cout, nextChangeRev.begin(), nextChangeRev.end(), ", ");
	cout << endl;
	
	lastChar = tolower(all[all.length() - 1]);
	nextChange[all.length() - 1] = all.length() - 1;
	
	for(int p = all.length() - 2; p >= 0; --p)
	{
	    if (!isalpha(all[p]) || tolower(all[p]) == lastChar) {
	         nextChange[p] = nextChange[p+1];   
	    } else {
	        nextChange[p] = p;
	        lastChar = tolower(all[p]);
	    }
	}
	cout << "Next change " ;
	Print<int, vector<int>::iterator>(cout, nextChange.begin(), nextChange.end(), ", ");
cout << endl;    
	
	for(int start = 0; start < all.length(); ++start)
	{
	    int left = start;
	     
	     for(int offset = 0; offset <= 1; ++offset)
	     {
	         int right = start + offset;
	     
             int palinLen = offset;
             
             while(true)
             {
                  if (left < 0 || right >= all.length()) {
                      break;
                  }
                  
                  if(!isalpha(all[left])) {
                      --left;
                      continue;
                  }
                  
                  if (!isalpha(all[right])) {
                      ++right;
                      continue;
                  }
                  
                  if (tolower(all[left]) == tolower(all[right])) {
                      int nextToRight = nextChange[right];
                      int nextToLeft = nextChangeRev[left];
                      
                      int blockRight = nextToRight - right;
                      int blockLeft = left - nextToLeft;
                      
                      int minBlock = min(blockRight, blockLeft);
                      
                      cout << "Min block " << minBlock << endl;
                      left -= minBlock;
                      left --;
                      ++right;
                      right += minBlock;
                      palinLen+=2+2*minBlock;
                      
                      string substr = std::string(all.begin() + left + 1, all.begin() + right);
             
                      if (palinLen > maxPalinLen)
                      {
                          maxPalinLen = palinLen;
                          palin = substr;
                      }
             //cout << "Substr " << substr << endl;
             
                      continue;
                  }
                  
                  break;
             }
             
             
         }
	}
	    
	fout << maxPalinLen << endl;
	fout << palin << endl;

    return 0;
}
