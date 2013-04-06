/* 

Inscribed Circles and Isosceles Triangles

Jose Ricardo Bustos Molina

*/

#include <iostream>
#include <cmath>

#define _USE_MATH_DEFINES

using namespace std;

double calc(double B, double H);

int main(){
  int cases;
  cin >> cases;
  cout.precision(6);
  cout.setf(ios::fixed,ios::floatfield);
  for(int k=0; k<cases; ++k){
    double B, H;
    cin >> B >> H;
    if(k!=0) cout << endl;
    cout.width(13);
    cout.fill(' ');
    cout << calc(B/2,H) << endl;
  }
}

double calc(double Bm, double H){
  double alpha = atan(H/Bm);
  double r = Bm * tan(alpha/2);
  if(r > 0.000001){
    double p = 2 * M_PI * r;
    double nH = H - 2*r;
    double nBm = nH / tan(alpha);
    p += calc(nBm, nH);  
    return p;
  }
  return 0;
}