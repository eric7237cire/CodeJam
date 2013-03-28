/* 

The Coco-Cola Store

Jose Ricardo Bustos Molina

*/

#include <iostream>

using namespace std;

int f(int N);

int main(){
  int N;
  cin >> N;
  while(N!=0){
    cout << f(N) << endl;
    cin >> N;
  }
}

int f(int N){
  if(N<2) return 0;
  if(N==2) return 1;
  return N/3 + f(N%3+N/3);
}