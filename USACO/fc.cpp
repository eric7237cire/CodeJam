/*
ID: eric7231
PROG: fc
LANG: C++
*/
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
#include <bitset>
#include <iomanip>
#include <cctype>
#include <limits>
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(uint k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (int) (b); ++k)

#define pb push_back 
#define mp make_pair 

typedef vector<int> vi; 
typedef vector<double> vd;
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef vector<vd> vvd;
typedef pair<int,int> ii;
typedef pair<uint,uint> uu;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 

bool debug = false;

template <class K, class V> 
V getMapValue( const map<K,V>& aMap, const K& key, const K& defaultValue )
{
    typename map<K, V>::const_iterator it = aMap.find(key);
    if ( it == aMap.end() )
        return defaultValue;
    return it->second;
}

uint getIndex(int row, int col, int cols)
{
    return row*cols + col;
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
typedef pair<PointD, PointD> SegmentD;

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
    
    double u = cross(q-p, r / rCrossS);
    
    inter = p + t*r;
    return true;
}

double dist( const PointD& p1, const PointD& p2 )
{
	return sqrt( (p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y) );
}

bool isBetween(double n, double a, double b)
{
    double maxDiff = max(a,b) - n;
    double minDiff = n - min(a,b);
    
    if (abs(maxDiff) < tolerance || abs(minDiff) < tolerance)
        return true;
    
    if (n >= min(a,b) && n <= max(a,b))
        return true;
    
    return false;
}

double sqr(double x) { return x * x; }

double length_squared(const PointD& v, const PointD& w) { return sqr(v.x - w.x) + sqr(v.y - w.y); }

double dot(PointD u, PointD v)
{
    return u.x*v.x + u.y*v.y;
}

double minimum_distance( const PointD& v, const PointD w, const PointD& p) {
  // Return minimum distance between line segment vw and point p
  const double l2 = length_squared(v, w);  // i.e. |w-v|^2 -  avoid a sqrt
  if (l2 == 0.0) return dist(p, v);   // v == w case
  // Consider the line extending the segment, parameterized as v + t (w - v).
  // We find projection of point p onto the line. 
  // It falls where t = [(p-v) . (w-v)] / |w-v|^2
  const double t = dot(p - v, w - v) / l2;
  if (t < 0.0) return dist(p, v);       // Beyond the 'v' end of the segment
  else if (t > 1.0) return dist(p, w);  // Beyond the 'w' end of the segment
  const PointD projection = v + t * (w - v);  // Projection falls on the segment
  return dist(p, projection);
}

bool inTriangle( const PointD& p, const PointD& tri1,
    const PointD& tri2, const PointD& tri3)
{
	if ( abs(minimum_distance(tri1, tri2, p)) < tolerance)
		return false;
	if ( abs(minimum_distance(tri1, tri3, p)) < tolerance)
		return false;
	if ( abs(minimum_distance(tri2, tri3, p)) < tolerance)
		return false;


    if ( getSide(p, tri1, tri2) != 
        getSide(tri3, tri1, tri2) )
        return false;
        
    if ( getSide(p, tri1, tri3) != 
        getSide(tri2, tri1, tri3) )
        return false;
        
    
    if ( getSide(p, tri2, tri3) != 
        getSide(tri1, tri2, tri3) )
        return false;
        
    //cout << p.x << ", " << p.y << " in triangle " << endl;
    return true;
    
}

//Sort lowest y first, then lowest x
template<typename T>
class LowestYSortOrder
{
	public:
	LowestYSortOrder()
	{
	}

    int operator()(const Point<T>& p1, const Point<T>& p2)
    {
        return p1.y < p2.y || (p1.y == p2.y && p1.x < p2.x);
    }
};

template<typename T>
class PolarOrder
{
    Point<T> refPoint;
    
public:
    PolarOrder(const Point<T>& refPt) : refPoint(refPt) 
    {
    }
    
    
	//Returns true if p1 goes before p2
	int operator()(const Point<T>& p1, const Point<T>& p2)
	{
		int cmp = compare(p1, p2);

		if (cmp == -1)
			return true;

		return false;
	}

    int compare(const Point<T>& p1, const Point<T>& p2)
    {
        Point<T> v1 = p1 - refPoint;
        Point<T> v2 = p2 - refPoint;
        
        //Lowest angle is 0, then goes counter clockwise.  This
        //will hit the positive angle first, so it is "less than" the other
        if (v1.y >= 0 && v2.y < 0)
            return -1;
        
        //Similar logic
        if (v1.y < 0 && v2.y >= 0)
            return +1;
        
        if (v1.y == 0 && v2.y == 0) 
        {
            //v1 has an angle of 0 and v2 -PI
            if (v1.x >= 0 && v2.x < 0)
                return -1;  
            
            //v2 has an angle of 0 and v1 -PI
            if (v2.x >= 0 && v1.x < 0)
                return +1;
            
            //both the same
            return 0;
        }
        
        //At this point both are above or below the y axis ; take the cross product
        
        int ccwRes = ccw(refPoint, p1, p2);
                
        //v2 is counter clockwise to v1, so v1 goes before
        if (ccwRes > 0)
            return -1;
        
        //v2 is clockwise of v1, so v2 goes before
        if (ccwRes < 0)
            return +1;
        
        //Vectors v1 and v2 are colinear
        return 0;
    }
};

template<typename T>
void grahamScan(const vector<Point<T> >& pointsIn, vector<Point<T> >& hullList)
{
    typedef vector<Point<T> > vpt;
    
   vector<Point<T> > points(pointsIn);
   
   sort(all(points), LowestYSortOrder<T>());
   
   //Sort relative to polar angle with first point (lowest y point)
   sort(points.begin() + 1, points.end(), PolarOrder<T>(points[0]));
   
   list<Point<T> > hull;
   
   hull.push_front(points[0]);
   
   typename vpt::iterator it;
   
   for(it = points.begin()+1; it != points.end(); ++it)
   {
       if (*it != points[0]) 
		   break;
   }
   
   if (it == points.end()) {
       //all points equal to first
       return;
   }
   
   typename vpt::iterator k1 = it;
   ++it;
   //Find first non collinear point 
   while( it != points.end() )
   {
       if (!isColinear(points[0], *k1, *it))
           break;
       
       ++it;
   }
   
   //The point right before the first non
   //linear point is added
   hull.push_front(*(it - 1));
   
   for( ; it != points.end(); ++it)
   {
       Point<T> t2 = hull.front();
       hull.pop_front();
       
       Point<T> t1 = hull.front();
       
       Point<T> t3 = *it;
       
       //Continue poping until we find a left turn
       while(ccw(t1, t2, t3) <= 0) 
       {
		   assert(hull.front() == t1);
           t2 = hull.front();
           hull.pop_front();
       
           t1 = hull.front();
           
           
       }
       
       hull.push_front(t2);
       hull.push_front(t3);
       
   }
   
   
   hullList.insert(hullList.end(), hull.rbegin(), hull.rend());
}

int main() {
    
	ofstream fout ("fc.out");
    ifstream fin ("fc.in");

    int N;

    fin >> N ;

    double x, y;

    vector<PointD> points;
    vector<PointD> hull;
    FOR(i, 0, N)
    {
        fin >> x;
        fin >> y;
        points.pb( PointD(x,y) );
    }
    
    
    
    //return 0;

  	grahamScan(points, hull);
    
	double d = 0;

    FOR(i, 0, hull.size())
    {
		int j = (i+1) % hull.size();
        cout << hull[i] << endl ;

		d += dist(hull[i], hull[j]);
    }
    
    fout << setprecision(2) << fixed << d << endl;
	
    return 0;
}
