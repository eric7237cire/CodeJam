#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <stack>
#include <set>
#include <list>
#include <vector>
#include <algorithm>
#include <cassert>
#include <iterator>
#include <sstream>
#include <complex>
#include <bitset>
#include <iomanip>
#include <cctype>
#include <limits>
#include <numeric>
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(int k=(a); k <= (int)(b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (int) (b); ++k)

#define pb push_back 
#define mp make_pair 

typedef vector<int> vi; 
typedef vector<bool> vb;
typedef vector<vb> vvb;
typedef vector<double> vd;
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef vector<vd> vvd;
typedef pair<int,int> ii;
typedef vector<pair<int,int> > vii;
typedef vector<vii> vvii;
typedef pair<uint,uint> uu;
typedef map<string, int> msi;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 



template< typename T >
struct Point
{
    T x;
    T y;
    
    Point(T xx, T yy) : x(xx), y(yy) {}
	Point() : x(), y() {}
};

template<typename T> 
Point<T> operator-( const Point<T>& lhs, const Point<T>& rhs) 
{
	Point<T> ret( lhs.x - rhs.x, lhs.y - rhs.y);
	return ret;
}

template<typename T> 
Point<T> operator+( const Point<T>& lhs, const Point<T>& rhs) 
{
	Point<T> ret( lhs.x + rhs.x, lhs.y + rhs.y);
	return ret;
}

template<typename T> 
bool operator==( const Point<T>& lhs, const Point<T>& rhs) 
{
	return lhs.x == rhs.x && lhs.y == rhs.y;
}

template<typename T> 
bool operator!=( const Point<T>& lhs, const Point<T>& rhs) 
{
	return lhs.x != rhs.x || lhs.y != rhs.y;
}

template<typename T> 
Point<T> operator/( const Point<T>& lhs, const T&  rhs) 
{
	Point<T> ret( lhs.x / rhs, lhs.y / rhs);
	return ret;
}

template<typename T> 
Point<T> operator*( const Point<T>& lhs, const T&  rhs) 
{
	Point<T> ret( lhs.x * rhs, lhs.y * rhs);
	return ret;
}

template<typename T> 
Point<T> operator*( const T&  lhs, const Point<T>& rhs) 
{
	return rhs * lhs;
}

template<typename T> 
ostream& operator<<( ostream& os, const Point<T>& rhs) 
{
	os << "(" << rhs.x << ", " << rhs.y << ")" ;
	return os;
}

typedef Point<double> PointD;
typedef pair<PointD, PointD> SegmentD;
typedef vector<PointD> vp;

double cross( const PointD& A, const PointD& B )
{
    return A.x*B.y - A.y*B.x;
}

const double tolerance = 0.000002;

template<typename T>
bool isColinear(  const Point<T>& p1, 
    const Point<T>& p2, const Point<T>& p3)
{
    Point<T> v1 = p2 - p1;
    Point<T> v2 = p3 - p1;
    
    T crossProduct = cross(v1, v2); 
    
    if (crossProduct == 0) {
        return true;
    }
    
    return false;
}

template<> 
bool isColinear<double>(  const Point<double>& p1, 
    const Point<double>& p2, const Point<double>& p3)
{
    Point<double> v1 = p2 - p1;
    Point<double> v2 = p3 - p1;
    
    double crossProduct = cross(v1, v2); 
    
    if (abs(crossProduct) < tolerance) {
        return true;
    }
    
    return false;
}

/*
vector p1p2 ; p1p3
returns 
-1 if p1p3 is clockwise of p1p2 ( "right turn" )
+1 if p1p3 is counter-clockwise of p1p2 ( "left turn " )
0 if parallel
*/
template<typename T>
int  ccw(  const Point<T>& p1, 
    const Point<T>& p2, const Point<T>& p3)
{
    Point<T> v1 = p2 - p1;
    Point<T> v2 = p3 - p1;
    
    T cp = cross(v1, v2);
    
    //v2 is counter clockwise to v1, so we turned left
    if (cp > 0)
        return +1;
    
    //v2 is clockwise of v1, 
    if (cp < 0)
        return -1;
    
    //Vectors v1 and v2 are colinear
    return 0;
}

template<> 
int ccw<double>(  const Point<double>& p1, 
    const Point<double>& p2, const Point<double>& p3)
{
    Point<double> v1 = p2 - p1;
    Point<double> v2 = p3 - p1;
    
    double cp = cross(v1, v2);
    
    //Vectors v1 and v2 are colinear
    if (abs(cp) < tolerance) {
        return 0;
    }
    
    //v2 is counter clockwise to v1, so we turned left
    if (cp > 0)
        return +1;
    
    //v2 is clockwise of v1, 
    if (cp < 0)
        return -1;
    
	int up = 3;
    throw up;
}

bool isParallel( const SegmentD& seg1, const SegmentD& seg2)
{
	double z = cross(seg1.second - seg1.first, seg2.second - seg2.first );

	if (abs(z) < tolerance)
		return true;

	return false;
}

int getSide( const PointD& A, const PointD& B, const PointD& P)
{
    double z = cross(B-A, P-A);
    
    if (z > tolerance)
        return 1; 
    if (z < tolerance)
        return -1; 

    return 0; 
}

bool intersects(const SegmentD& seg1, const SegmentD& seg2)
{
    if (getSide( seg1.first, seg1.second, seg2.first ) ==
        getSide( seg1.first, seg1.second, seg2.second ) )
        return false;
        
    if (getSide( seg2.first, seg2.second, seg1.first ) ==
        getSide( seg2.first, seg2.second, seg1.second ) )
        return false;
        
    return true;
}

//http://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
bool getIntersection( const SegmentD& seg1, const SegmentD& seg2, PointD& inter)
{
    PointD p = seg1.first;
    PointD r = seg1.second - seg1.first;
    
    PointD q = seg2.first;
    PointD s = seg2.second - seg2.first;
    
    //P + t*r intersects q + u*s
    
    double rCrossS = cross(r, s);
    
    if ( abs( rCrossS ) < tolerance )
        return false;
    
    double t = cross(q-p, s / rCrossS);
    
	if (t+tolerance < 0 || t-tolerance > 1)
		return false;
   // double u = cross(q-p, r / rCrossS);
    
    inter = p + t*r;
    return true;
}

double dist( const PointD& p1, const PointD& p2 )
{
	return sqrt( (p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y) );
}


bool cmp(const PointD& i, const PointD& j) {
	return i.x < j.x;
}

int main() {
#ifndef ONLINE_JUDGE
	freopen ("input.txt","r",stdin);
#endif

	int T;
	scanf("%d", &T);
	
	FOR(t, 0, T)
	{
		int N;
		scanf("%d", &N);
		vp points(N);
		FOR(i, 0, N)
			scanf("%lf %lf", &points[i].x, &points[i].y);

		sort( all(points), cmp);
		// solve
		
		double totalDist = 0;
		PointD lastHiPoint(40000,0);

		for (int i = N - 2; i >= 0; --i) {
			const PointD& pt = points[i];
			
			if (pt.y < lastHiPoint.y)
				continue;

			const PointD& lowPt = points[i+1];

			//Intersect a horizontal segment from last point to current segment
			PointD inter;
			bool check = getIntersection( mp(pt, lowPt), mp(lastHiPoint, PointD(0, lastHiPoint.y)), inter);
			assert(check);
				
			totalDist += dist( inter, pt );
			lastHiPoint = pt;

			
		}
		

		printf("%.2f\n", totalDist);
	}
	return 0;
}
