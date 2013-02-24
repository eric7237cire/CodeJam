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
typedef Point<int> PointI;
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
		if (A < 0)
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


int main() {
#ifndef ONLINE_JUDGE
	freopen ("input.txt","r",stdin);
#endif

	while(true)
	{
		vector<PointI> points;
		int x, y;
		int r;
		while(r = scanf("%d%d", &x, &y) == 2)
		{
			//printf("Read point %d, %d\n", x, y);
			if (x == 0 && y == 0)
				break;

			points.pb(PointI(x,y));

			
		}
		if (r == 0)
			break;

		if (points.empty())
			break;

		//sort( all(points), cmp);
		typedef map<Line<int>, set<int> > LinePointsMap;

		LinePointsMap linePoints;

		for(int i = 0; i < points.size(); ++i)
		{
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
			}
		}
		
		vector<vector<PointI>> lines;

		for( LinePointsMap::iterator it = linePoints.begin();
			it != linePoints.end();
			++it)
		{
			set<int> pointIndexes = it->second;

			if (pointIndexes.size() < 3)
				continue;

			vector<PointI> v;

			for(set<int>::iterator sIt = pointIndexes.begin();
				sIt != pointIndexes.end();
				++sIt)
			{
				v.pb(points[*sIt]);
			}
			
			sort(all(v), cmp);

			lines.pb(v);
			
		}
		
		sort(all(lines), cmpLine);

		if (lines.empty())
		{
			printf("No lines were found\n");
		} 
		else 
		{
			printf("The following lines were found:\n");
			FOR( linIdx, 0, lines.size())
			{
				FOR(i, 0, lines[linIdx].size())
				{
					printf("(%4d,%4d)", lines[linIdx][i].x, lines[linIdx][i].y);
				}
				printf("\n");
			}
		}
	}
	return 0;
}
