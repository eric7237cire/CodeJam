#include <stdio.h>
#include <complex>
#include <algorithm>
#include <cstdlib>
#include <iostream>
using namespace std;
 
typedef complex<double> point;
 
#define EPS 0.000000001
#define vec(a,b)  ((b)-(a))
#define cross(a,b) ((conj(a)*b).imag())
#define dot(a,b) ((conj(a)*b).real())
 
bool intersect (point &a,point &b,point &p,point &q,point &ret)
{
    double d1 = cross(vec(a,p),vec(a,b));
    double d2 = cross(vec(a,q),vec(a,b));
    ret = (d1*q-d2*p)/(d1-d2);
    return fabs(d1-d2) > EPS;
}
 
bool pointOnRay(point &a,point &b,point &p)
{
    return fabs(cross(vec(a,b),vec(a,p))) < EPS &&
            dot(vec(a,b),vec(a,p)) > -1*EPS; // leave some space before zerp
}
bool pointOnSegment(point &a,point &b,point &p)
{
 
    return pointOnRay(a,b,p)&&pointOnRay(b,a,p);
}
 
bool check(point &a,point &b,point &c,point &d)
{
    point p;
    if(pointOnSegment(a,b,c)||pointOnSegment(a,b,d)||pointOnSegment(c,d,a)||pointOnSegment(c,d,b))
        return false;
    else if(!intersect(a,b,c,d,p))
            return true;
    else {
        bool aa = pointOnSegment(a,b,p)&&pointOnSegment(c,d,p);
        return !aa;
}}
int main()
{
    int t,n,x1,x2,y1,y2;
    scanf("%d",&t);
    while(t-->0)
    {
        scanf("%d",&n);
        point arr[n][2];
        for(int i=0;i<n;i++)
        {
            scanf("%d %d %d %d",&x1,&y1,&x2,&y2);
            point p1(x1,y1);
            point p2(x2,y2);
            arr[i][0] = p1;
            arr[i][1] = p2;
        }
        int count=0;
        for (int i=0;i<n;i++){
        int c = 0;
        for (int j=0;j<n;j++) if(i!=j)
        {
 
            if(!check(arr[i][0],arr[i][1],arr[j][0],arr[j][1]))
                c++;
        }
            if(c==0){
                count++;
            }
 
        }
        printf("%d\n",count);
    }
}