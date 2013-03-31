#include<iostream>
#include<cstdio>
using namespace std;

int H,U,D,F;
int day;

int snail()
{
	double h=0;
	double u=U;
	double d=D;
	day=0;
	double f=F/100.0*U;
	while(1){
//		printf("%d %lf %lf %lf %lf\n",day,h,u,d,f);
		day++;
		h+=u;
		if(h>H)return 1;
		h-=d;
		if(h<0)return 0;
		u-=f;
		if(u<0)u=0;
	}
}

int main()
{
	while(cin>>H>>U>>D>>F){
		if(H==0)break;
		if(snail())cout<<"success on day "<<day<<endl;
		else cout<<"failure on day "<<day<<endl;
	}
	return 0;
}