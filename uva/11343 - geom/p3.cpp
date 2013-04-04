#include <iostream>
#include <complex>
#include <cstdlib>
#include <cstdio>
#include <algorithm>
#include <vector>
#include <iomanip>
 
using namespace std;
typedef complex <double> point;
#define X real();
#define Y imag();
#define polar(r,t) ((r)*exp(point(0,(t))))
#define length(a) hypot((a).X,(a).Y)
#define angle(a) atan2((a).Y,(a).X)
#define vec(a,b) ((b)-(a))
#define dot(a,b) ((conj(a)*(b)).real())
#define cross(a,b) ((conj(a)*(b)).imag())
#define lengthSqr(a) dot(a,a)
#define rotate(v,t) ((v)*exp(point(0,t)))
#define rotateAbout(v,t,a) (rotate(vex(a,v),t)+(a))
#define reflect(V,M) (conj((V)/(M))*(M))
#define EPS 1.0e-5
bool pointOnRay(const point &a, const point b, const point &p)
{
    return fabs(cross(vec(a,b),vec(a,p))) < EPS && dot(vec(a,b),vec(a,p)) > -EPS;
}
bool pointOnSegment(const point &a, const point b, const point &p)
{
    if(lengthSqr(vec(a,b)) < EPS) return lengthSqr(vec(a,p)) < EPS;
    return pointOnRay(a,b,p) && pointOnRay(b,a,p);
}
bool intersectSeg(const point &a, const point &b, const point &p, const point &q, point & ret)
{
    double d1 = cross(p - a, b - a);
    double d2 = cross(q - a, b - a);
    ret = (d1 * q - d2 * p) / (d1 - d2);
    if(pointOnSegment(a,b,p) || pointOnSegment(a,b,q)
                       || pointOnSegment(p,q,a) || pointOnSegment(p,q,b))
                       return true;
    return fabs(d1-d2) > EPS && pointOnSegment(a,b,ret) && pointOnSegment(p,q,ret);
}
 
int main()
{
    int tc,n,counter;
    cin >> tc;
    for(int i = 0 ; i < tc ; i++)
    {
        cin >> n;
        int cord[n][4];
        counter = 0;
        for(int j = 0 ; j < n ; j++)
        {
            for(int k = 0 ; k < 4 ; k++)
            {
                cin >> cord[j][k];
            }
        }
        for(int j = 0 ; j < n ; j++)
        {
            point firstS(cord[j][0],cord[j][1]);
            point firstE(cord[j][2],cord[j][3]);
            bool inters = false;
            for(int k = 0 ; k < n ; k++)
            {
                if(j != k)
                {
                    point secondS(cord[k][0],cord[k][1]);
                    point secondE(cord[k][2],cord[k][3]);
                    point ret;
                    if(intersectSeg(firstS,firstE,secondS,secondE,ret))
                    {
                        inters = true;
                        break;
                    }
                }
            }
            if(!inters)
                counter++;
        }
        cout << counter << endl;
    }
    return 0;
}