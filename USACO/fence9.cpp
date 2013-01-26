/*
ID: eric7231
PROG: fence9
LANG: C++
*/
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <set>
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


int main() {
    
	ofstream fout ("fence9.out");
    ifstream fin ("fence9.in");

    int m, n, p;

    fin >> m >> n >> p;

    //To m,n the to p,0 to 0,0

    PointD t1( PointD(0,0) );
    PointD t2( PointD(m,n) );
    PointD t3( PointD(p,0) );  	
    
    int deltaRight = (m-p) / n;
    
    if (deltaRight <= 0)
        deltaRight = 1;
    int left = 0;
    int right = p;
    
    uint total = 0;
    
    cout << "Tri test " << inTriangle( PointD(4, 1), t1, t2, t3 ) << endl;
    cout << "Tri test " << inTriangle( PointD(3, 1), t1, t2, t3 ) << endl;
    
    cout << "Delta right " << deltaRight << endl;
    
    for(uint y = 1; y < n; ++y)
    {
        //multiply by 2 just to be sure we are outside the triangle
        right += 2 * deltaRight;
        
        while(!inTriangle( PointD(right, y), t1, t2, t3 ) && right > left) {
            right --;
            //cout << "Moving right " << right << endl;
        }
        
        right ++;
        
        while(!inTriangle( PointD(left, y), t1, t2, t3 ) && left < right) { 
            ++left;
        }
        
        --left;
        
        
        int count = right - left - 1;
        
        if (count < 0)
            break;
        
      //  cout << "y " << y << " has " << count << " total " << total << endl;
      //  cout << "left " << left << " right " << right << endl;
        total += count;
    }
    
    fout << total << endl;
	
    return 0;
}
