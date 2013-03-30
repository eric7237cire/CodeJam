/* 

Mapping the Swaps

Jose Ricardo Bustos Molina

*/

#include <iostream>
#include <stack>
#include <algorithm>

using namespace std;

int countswaps(stack<int*> p, int n, int k);
bool isSort(int* v, int n);

int main(){
  int t = 1;
  int n;
  cin >> n;
  while(n!=0){
    int* v = new int[n];
    for(int i=0; i<n; ++i) cin >> v[i];
    stack<int*> p;
    p.push(v);
    cout << "There are " << (isSort(v, n)?0:countswaps(p,n,0)) << " swap maps for input data set " << t++ << "." << endl;
    cin >> n;
  }
}

int countswaps(stack<int*> p, int n, int k){
  stack<int*> np;
  int c=0;
  while(!p.empty()){
    int* v = p.top();
    p.pop(); 
    if(isSort(v, n)) c++;
    if(c==0){
      for(int i=1; i<n; ++i){
        int* nv = new int[n];
        copy(v, v+n, nv);
        swap(nv[i-1],nv[i]);
        np.push(nv);
      }
    }
  }
  if(c>0) return c;
  return countswaps(np, n, k+1);
}

bool isSort(int* v, int n){
  for(int i=1; i<n; ++i) if(v[i-1]>v[i]) return false;
  return true;
}