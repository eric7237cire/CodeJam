#include "stdio.h"
#include <string>
#include <iostream>
#include <algorithm>
#include <cassert>
using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
 
string encode(string text)
{
 
    int order[] = {8,35,17,34,7,33,
        16,6,26,5,32,25,
        4,15,31,24,14,3,
    23,30,2,13,29,22,
    1,12,21,0,20,28,
    11,19,10,27,9,18 };
 
  while (text.length() % 36 != 0)
  {
    text=text+'#';
  }
 
  string result;
  for (int i=text.length()-1;i>-1;i-=36)
  {
    string block=text.substr(i-35,36);
    for (int j=0;j<36;j++)
    {
      result += block.substr(order[j],1);
    }
  }
  
  return result;
}   

string decode(string text)
{
    
    int order[] ={
        1,3,5,10,14,19,
        22,29,33,8,11,15,
        18,23,26,28,31,35,
        2,6,13,16,21,25,
        30,32,34,0,4,7,
    9,12,17,20,24,27};
    
  string result;
 
  assert(text.length() % 36 == 0);
 
  for (int i=0;i<text.length();i+=36)
  {
    string block=text.substr(i,36);
    for (int j=0;j<36;j++)
    {
      result += block.substr(order[j],1);
    }
  }
 
  while (result.substr(0,1) == "#")
  {
    result = result.substr(1);
  }
 
  reverse( result.begin(), result.end() );
  /*
  var splitText = result.split('');
  var reverseText = splitText.reverse();
  result = reverseText.join("");
 */
  return result;

}


int main()
{
	
	//string s = "Real Programmers use Fortran. Quiche Eaters use Pascal.";
	//string en = encode(s);
	
	//cout << en << endl;
	
	string s;
	while( getline(cin, s) )
	{
	     cout << decode(s) << endl;   
	}

	return 0;
}