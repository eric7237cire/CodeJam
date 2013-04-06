#include<iostream>
#include<iomanip>
#include<cmath>
#define PI 3.14159265
using namespace std;

int a,b,s,m,n;

int main()
{
	while(cin>>a>>b>>s>>m>>n){
		if(a==0&&b==0&&s==0&&m==0&n==0)break;
//		cout<<a<<b<<s<<m<<n<<endl;
		double A = atan2(n*b,m*a);
		double V = n*b/sin(A)/s;
		cout<<fixed << setprecision(2) << A*180/PI << " "<< V << endl;
	}
	return 0;
}