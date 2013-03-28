#include<iostream>
using namespace std;

int Q;
int N;
int a[10];

int factors()
{
	for(int i=2;i<=9;i++)a[i]=0;
	for(int i=9;i>=2;i--){
		while(N%i==0){
			N/=i;
			a[i]++;
			//cout<<i;
		}
	}
	if(N!=1)return 0;
	Q=0;
	for(int i=2;i<=9;i++){
		for(int j=0;j<a[i];j++){
			Q*=10;
			Q+=i;
		}
	}
	return 1;
}

int main()
{
	int tc;
	cin>>tc;
	for(int i=0;i<tc;i++){
		cin>>N;
		if(N==1)
			cout<<1<<endl;
		else if(factors())
			cout<<Q<<endl;
		else
			cout<<-1<<endl;
	}
	return 0;
}