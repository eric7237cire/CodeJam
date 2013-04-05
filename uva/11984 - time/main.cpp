#include <iostream>

#include <cmath>

#include <iomanip>

using namespace std;

int main()

{

int C,d,T;

while(cin >> T){

for(int i=0;i<T;i++){

cin>>C>>d;

float f=(9.0/5.0)*C+32;

float r1= f+d;

float r2=(5.0/9.0)*(r1-32);

cout.setf(ios::fixed);

cout<< setprecision(2) << "Case " << i+1 << ": " << r2 << endl;

}

}

return 0;

}