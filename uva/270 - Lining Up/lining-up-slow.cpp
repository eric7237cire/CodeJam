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

template<typename T>
bool isBetween(T x, T a, T b)
{
	return (a <= x && x <= b) ||
		(b <= x && x <= a);
}

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
typedef Point<int> PointI;
typedef pair<PointD, PointD> SegmentD;
typedef vector<PointD> vp;

template<typename T>
T cross( const Point<T>& A, const Point<T>& B )
{
    return A.x*B.y - A.y*B.x;
}

const double tolerance = 0.000002;




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

template<typename T> T gcd(T a, T b)
{
    if (a == 0)
       return b;
    while (b != 0)
	{
        if (a > b)
           a = a - b;
        else
           b = b - a;
	}
    return a;
}

template<class T>
class Line
{
	public:
	//ax + by + c = 0
	T A;
	T B;
	T C;

	Line(const Point<T>& p1, const Point<T>& p2)
	{
			
		//cout << "Building line" << endl;
		A = p1.y - p2.y;
		B = p2.x - p1.x;
		C = p1.x*p2.y - p2.x * p1.y;

		//make A positive
		if (A < 0 || (A==0 && B < 0) )
		{
			A *= -1;
			B *= -1;
			C *= -1;
		}

		int gcdAB = gcd( abs(A), abs(B) );
		int gcdABC = gcd( gcdAB, abs(C) );

		A /= gcdABC;
		B /= gcdABC;
		C /= gcdABC;

		assert(A * p1.x + B * p1.y + C == 0);
		assert(A * p2.x + B * p2.y + C == 0);
	}
};

template<typename T> 
bool operator<( const Line<T>& lhs, const Line<T>& rhs) 
{
	if (lhs.A != rhs.A)
		return lhs.A < rhs.A;

	if (lhs.B != rhs.B)
		return lhs.B < rhs.B;

	return lhs.C < rhs.C;
}


bool isParallel( const SegmentD& seg1, const SegmentD& seg2)
{
	double z = cross(seg1.second - seg1.first, seg2.second - seg2.first );

	if (abs(z) < tolerance)
		return true;

	return false;
}

template<typename T>
int getSide( const Point<T>& A, const Point<T>& B, const Point<T>& P)
{
    T z = cross(B-A, P-A);
    
	if (numeric_limits<T>::is_exact) {
		if (z > 0) {
#ifndef ONLINE_JUDGE
			cout << "Points " << A << " " << B << " " << P << " 1 " << endl;
#endif
			return 1;
		}
		if (z < 0) {
#ifndef ONLINE_JUDGE
			cout << "Points " << A << " " << B << " " << P << " -1 " << endl;
#endif
			return -1;
		}
	}
	else {
		if (z > tolerance)
			return 1; 
		if (z < tolerance)
			return -1; 
	}

#ifndef ONLINE_JUDGE
	cout << "Points " << A << " " << B << " " << P << " 0 " << endl;
#endif
    return 0; 
}

template<typename T>
bool isColinear(  const Point<T>& p1, 
    const Point<T>& p2, const Point<T>& p3)
{
    return 0 == getSide(p1, p2, p3);
}

template<typename T>
bool intersects(const Point<T>& a1,const Point<T>& a2,
				const Point<T>& b1,const Point<T>& b2)
{
	if (a1==a2)
	{ 
		return isBetween(a1.x, b1.x, b2.x) &&
			isBetween(a1.y, b1.y, b2.y);
	}
	
	if ( getSide( a1, a2, b1 ) == getSide( a1, a2, b2 ) )
		return false; 

      
	if (b1==b2)
	{
		return isBetween(b1.x, a1.x, a2.x) &&
			isBetween(b1.y, a1.y, a2.y);
	}

    if ( getSide( b1, b2, a1) == getSide( b1, b2, a2 ) )
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


bool cmp(const PointI& i, const PointI& j) {
	return (i.x != j.x)  ?
		i.x < j.x :
		i.y < j.y;
}

typedef vector<PointI> vecP ;

bool cmpLine(const vecP& v1, const vecP& v2)
{
	int i = 0;
	while( i < v1.size() && i < v2.size() )
	{
		if (v1[i] != v2[i])
		{
			return cmp(v1[i], v2[i]);
		}
		++i;
	}

	return v1.size() < v2.size();
}

template<typename A, typename B>
void minPair( pair<A,B>& minPair, const pair<A,B>& pair)
{
	minPair.first = min(minPair.first, pair.first);
	minPair.second = min(minPair.second, pair.second);
}

template<typename A, typename B>
void maxPair( pair<A,B>& maxPair, const pair<A,B>& pair)
{
	maxPair.first = max(maxPair.first, pair.first);
	maxPair.second = max(maxPair.second, pair.second);
}

int cmpYX(const PointI& a, const PointI& b)
{
    if (a.y != b.y)
    {
        return a.y < b.y;
    }
    return a.x < b.x;
}

template<typename T>
struct PolarCmp
{
    Point<T> origin;
    
    PolarCmp( const Point<T> ori ) : origin(ori) 
    {
    }
    
    int operator()(const Point<T>& a, const Point<T>& b)
    {
       int isCCW = ccw(origin, a, b);
       
       if (isCCW == 1) 
           return true;
       
       return false;
    }
};


int main() {
#ifndef ONLINE_JUDGE
	freopen ("input.txt","r",stdin);
	//freopen ("in.txt","r",stdin);
#endif

	int T;
	scanf("%d", &T);
	
	string line;
	getline(cin,line);
	getline(cin,line);

	FOR(t, 0, T)
	{
	    vector<PointI> points;
	    
	    int x;
	    int y;
	    
	    while(getline(cin,line)){
            if(line=="") break;
            sscanf(line.c_str(),"%d %d",&x,&y);
            points.pb( PointI(x, y) );
	         assert(points.size() <= 700 );
            
        }
	    
	       
	    typedef map<Line<int>, set<int> > LinePointsMap;
	    
	    LinePointsMap linePoints;
	    int maxPtCount = 0;
	    
		for(int i = 0; i < points.size(); ++i)
		{
			//printf("%d ...\n", i);
			for(int j = i+1; j < points.size(); ++j)
			{
			    Line<int> line(points[i], points[j]);

				//LinePointsMap::iterator it = linePoints.find(line);
#ifndef ONLINE_JUDGE 
				/*
				printf("Line from (%d, %d) and (%d, %d) is %dx + %dy + %d = 0\n",
					points[i].x, points[i].y, points[j].x, points[j].y, 
					line.A, line.B, line.C
					);*/
#endif
					linePoints[line].insert(i);
					linePoints[line].insert(j);
					
					maxPtCount = max(maxPtCount,
					    (int) linePoints[line].size());
			}
		}
		
		cout << maxPtCount << endl;
		if (t < T - 1)
		    cout << endl;
	}
	return 0;
}
