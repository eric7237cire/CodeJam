
#include<stdio.h>
#include<math.h>

#define EPS 1e-6

double lineardistance(double x1, double y1, double x2, double y2){
        double delx=x1-x2, dely=y1-y2;
        
        return sqrt(delx*delx+dely*dely);
}

double _abs(double x){
        if(x>-EPS && x<EPS) return 0;
        if(x<0) return -x;
        return x;
}

void solve(double a, double b, double c, double a1, double b1, double c1, double *x, double *y){
double det = a*b1-b*a1;
        if(_abs(det)==0) return;
        *x=(b*c1-c*b1)/det, *y=(c*a1-a*c1)/det;
}

int main(){
        int test;
        double x1, y1, x2, y2, r;
        double a, b, c, a1, b1, c1, xtmp, ytmp;
        double distance, delx, dely;
        double angle;
        double dis1, dis2;
        
        scanf("%d", &test);
        while(test--){
                scanf("%lf%lf%lf%lf%lf", &x1, &y1, &x2, &y2, &r);
                delx=x1-x2, dely=y1-y2;
                distance=sqrt(delx*delx+dely*dely);
                a=y1-y2, b=x2-x1, c=x1*y2-y1*x2; /*this are for ax+by+c=0 eqn*/
                b1=-a, a1=b, c1=0;
                solve(a, b, c, a1, b1, c1, &xtmp, &ytmp);
                dis1=lineardistance(xtmp, ytmp, x1, y1);
                dis2=lineardistance(xtmp, ytmp, x2, y2);
                if(_abs(c)<r*sqrt(a*a+b*b) && _abs(dis1+dis2-distance)==0){
                        a=distance;
                        b=x1*x1+y1*y1, c=x2*x2+y2*y2;
                        distance=sqrt(b-r*r)+sqrt(c-r*r);
                        b=sqrt(b), c=sqrt(c); /*a, b, c are the sides of the triangle formed by (0,0), (x1, y1), (x2, y2)*/
                        angle=acos((b*b+c*c-a*a)/(2*b*c))-acos(r/b)-acos(r/c);
                        distance+=angle*r;
                }
                
                printf("%.3lf\n", distance);
        }
        
        return 0;
}