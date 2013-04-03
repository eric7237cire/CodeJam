#include<iostream>
#include<cstdio>
using namespace std;

int N;
int a;
int M;

struct point{
	double x;
	double y;
};

bool near(struct point a, struct point b)
{
	return (a.x-b.x)*(a.x-b.x)+(a.y-b.y)*(a.y-b.y)<1;
}

bool isInArea(struct point t)
{
	struct point A = {0.0,0.0};
	struct point B = {1.0,0.0};
	struct point C = {0.0,1.0};
	struct point D = {1.0,1.0};
	struct point T = {t.x/a,t.y/a};
	return near(T,A)&&near(T,B)&&near(T,C)&&near(T,D);
}


int main()
{
	while(cin>>N>>a&&N){
		struct point t;
		M=0;
		for(int i=0;i<N;i++){
			cin>>t.x>>t.y;
			if(isInArea(t))M++;
		}
		double r = 1.0;
		r *=M;
		r /=N;
		r *=a*a;
		printf("%.5f\n",r);
	}
	return 0;
}