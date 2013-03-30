#include<iostream>
#include<cstdio>
using namespace std;

int a[18];
int n;
long long p[18][18];

long long product()
{
	for(int i=0;i<n;i++)
		for(int j=i;j<n;j++){
			p[i][j]=1;
			for(int k=i;k<=j;k++)
				p[i][j]*=a[k];
		}
	long long v = 0;
	for(int i=0;i<n;i++)
		for(int j=i;j<n;j++)
			v = max(p[i][j],v);
	return v;
}


int main()
{
	int tc = 0;
	while(cin>>n){
		for(int i=0;i<n;i++)cin>>a[i];
		long long maximun = product();
		printf("Case #%d: The maximum product is %lld.\n",++tc,maximun);
		printf("\n");
	}
	return 0;
}