/* 

SMS Typing

Jose Ricardo Bustos Molina

*/

#include <iostream>

using namespace std;

int v[] = {1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,4,1,2,3,1,2,3,4};

int main(){
  int t;
  cin >> t;
  cin.get();
  for(int i=1; i<=t; ++i){
    string s;
    getline(cin, s);
    int x = 0;
    for(string::iterator c=s.begin(); c!=s.end(); ++c){
      if(*c==' ') ++x;
      else x+=v[*c-'a'];
    }
    cout << "Case #" << i << ": " << x << endl;
  }
}