#include<iostream>
using namespace std;


int main()
{
	int e;
	int f;
	int c;
	int tc;
	cin>>tc;
	while(cin>>e>>f>>c){
		int t = e + f;
		int sum = 0;
		while(t>=c){
			sum += t/c;
			t = t%c+t/c;
		}
		cout<<sum<<endl;
	}
	return 0;
}
