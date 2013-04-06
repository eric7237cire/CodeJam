#include <cmath>
#include <cstdio>
#include <cstring>
#include <iostream>
#include <algorithm>

using namespace std;

const int N=110;
const double eps=1e-9;
struct point{
	double x,y;
	point(double x=0,double y=0):x(x),y(y){}
	point operator-(const point &a){return point(x-a.x,y-a.y);}
	int read(){return scanf("%lf%lf",&x,&y);}
	void print(){printf("%f %f\n",x,y);}
};

double a[N][N],g[N];

void solve(){
	int i,j,k;
	for (i=0;i<12;i++){
		for (j=i;j<12;j++)
			if (fabs(a[j][i])>eps) break;
if (j==12) puts("!!!");
		for (k=0;k<=12;k++) swap(a[j][k],a[i][k]);
		for (j=i+1;j<12;j++){
			double d=a[j][i]/a[i][i];
			for (k=0;k<=12;k++) a[j][k]-=a[i][k]*d;
		}
	}
	for (i=11;i>=0;i--){
		g[i]=a[i][12]/a[i][i];
		for (j=i-1;j>=0;j--)
			a[j][12]-=g[i]*a[j][i];
	}
}

int main(){
	point P,Q,R,v1,v2,v3;
	double m1,m2,m3,m4,m5,m6;
	int tt,cas;
	scanf("%d",&tt);
	for (cas=1;cas<=tt;cas++){
		P.read();
		Q.read();
		R.read();
		v1=Q-P;
		v2=R-Q;
		v3=P-R;
		scanf("%lf%lf%lf%lf%lf%lf",&m1,&m2,&m3,&m4,&m5,&m6);
		memset(a,0,sizeof(a));
		a[0][2]=v1.y, a[0][3]=-v1.x, a[0][12]=v1.y*P.x-v1.x*P.y;
		a[1][6]=v1.y, a[1][7]=-v1.x, a[1][12]=v1.y*P.x-v1.x*P.y;
		a[2][4]=v2.y, a[2][5]=-v2.x, a[2][12]=v2.y*Q.x-v2.x*Q.y;
		a[3][8]=v2.y, a[3][9]=-v2.x, a[3][12]=v2.y*Q.x-v2.x*Q.y;
		a[4][0]=v3.y, a[4][1]=-v3.x, a[4][12]=v3.y*R.x-v3.x*R.y;
		a[5][10]=v3.y, a[5][11]=-v3.x, a[5][12]=v3.y*R.x-v3.x*R.y;
		a[6][6]=-(m3+m4), a[6][0]=m3, a[6][4]=m4;
		a[7][7]=-(m3+m4), a[7][1]=m3, a[7][5]=m4;
		a[8][8]=-(m5+m6), a[8][2]=m5, a[8][0]=m6;
		a[9][9]=-(m5+m6), a[9][3]=m5, a[9][1]=m6;
		a[10][10]=-(m1+m2), a[10][4]=m1, a[10][2]=m2;
		a[11][11]=-(m1+m2), a[11][5]=m1, a[11][3]=m2;
		solve();
		for (int i=0;i<6;i++){
			printf("%8f",g[i]);
			if (i<5) putchar(' ');
			else putchar('\n');
		}
	}
	return 0;
}