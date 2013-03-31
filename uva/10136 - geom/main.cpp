//STARTCOMMON
#include <iostream>
#include <map>
#include <list>
#include <vector>
#include <algorithm>
#include <cassert>
#include <iomanip>
#include <limits>
#include <numeric>
#include <cmath>
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

/*
Util
*/

const double tolerance = 0.000002;

template<typename T> T gcd(T a, T b)
{
	assert(numeric_limits<T>::is_exact);
		
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

/*
int gcd(int a, int b)
{
    int c;
    while (b != 0)
    {
        c = a % b;
        a = b;
        b = c;
    }
    return a;
}*/
/*
template<typename T> T gcd(T a, T b)
{
    while(b) b ^= a ^= b ^= a %= b;
    return a;
}*/

template<> double gcd<double>(double a, double b)
{
	return min(a,b);
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


template<typename T> 
int cmp(T a, T b, T epsilon = tolerance)
{
	T dif = a - b;
	if (abs(dif) <= epsilon)
	{
		return 0;
	}
	
	if (dif > 0)
	{
		return 1; //a > b
	}
	
	return -1;
}  


template<typename T>
bool isBetween(T x, T a, T b)
{
	return (a <= x && x <= b) ||
		(b <= x && x <= a);
}


template<typename T>
ostream& operator<<( ostream& os, const vector<T>& vec )
{
    FOR(i, 0, vec.size())
    {
        os <<  vec[i] << endl;
    }
    return os;
}

/*
POINT
*/

template< typename T >
struct Point
{
    T x;
    T y;
    
    Point(T xx, T yy) : x(xx), y(yy) {}
	Point() : x(), y() {}

	template < typename U >
    operator Point<U> (  ) const
    {
    	Point<U> result( (U) x, (U) y );
    	return result;
    }
};


typedef Point<double> PointD;
typedef Point<int> PointI;
typedef Point<int> pi;
typedef pair<PointD, PointD> SegmentD;
typedef vector<PointD> vp;
typedef vector<PointI> vpi;
typedef vector<PointI> vecP ;

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

/*
template<typename T> 
Point<T> operator*( int lhs, const Point<T>& rhs) 
{
	return rhs * lhs;
}*/

template<typename T> 
ostream& operator<<( ostream& os, const Point<T>& rhs) 
{
	os << "(" << rhs.x << ", " << rhs.y << ")" ;
	return os;
}

template<typename T>
istream& operator>>( istream& in, Point<T>& rhs)
{
	in >> rhs.x >> rhs.y;
	return in;
}


template<typename T>
int cmpYX(const Point<T>& a, const Point<T>& b)
{
    if (a.y != b.y)
    {
        return a.y < b.y;
    }
    return a.x < b.x;
}

template<typename T> 
bool operator<( const Point<T>& lhs, const Point<T>& rhs) 
{
	return cmpYX(lhs, rhs);
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

bool cmp(const PointI& i, const PointI& j) {
	return (i.x != j.x)  ?
		i.x < j.x :
		i.y < j.y;
}

template<typename T>
T dot( const Point<T>& A, const Point<T>& B )
{
    return A.x*B.x + A.y * B.y;       
}

template<typename T>
T mag( const Point<T>& v )
{
    return sqrt(v.x * v.x + v.y * v.y);
}

template<typename T>
T cross( const Point<T>& A, const Point<T>& B )
{
    return A.x*B.y - A.y*B.x;
}



//Rotate about origin counter clockwise
template<typename T>
Point<T> rotate90(const Point<T> & p)
{
    return Point<T>(-p.y, p.x);
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
		if (p1.y < p2.y)
			return 1;
			
        if (p1.y == p2.y && p1.x < p2.x)
			return 1;
			
		return 0;
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
			
		if (cmp == 0 && p1 != p2)
		{
			double d1 = dist(p1, refPoint);
			double d2 = dist(p2, refPoint);
			if (::cmp(d1, d2, 1e-5) < 0)
				return true;
		}

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

/*
LINE
*/

template<class T>
class Line
{
	public:
	//ax + by + c = 0
	T A;
	T B;
	T C;

	Line(const Point<T>& p1, const Point<T>& p2, bool normalize = false)
	{
			
	    assert(p1 != p2);
		//cout << "Building line" << endl;
		A = p1.y - p2.y;
		B = p2.x - p1.x;
		C = p1.x*p2.y - p2.x * p1.y;

		if (normalize && numeric_limits<T>::is_exact)
		{
			//make A positive
			if (A < 0 || (A==0 && B < 0) )
			{
				A *= -1;
				B *= -1;
				C *= -1;
			}

			T gcdAB = gcd( abs(A), abs(B) );
			T gcdABC = gcd( gcdAB, abs(C) );

			A /= gcdABC;
			B /= gcdABC;
			C /= gcdABC;
		}
		//assert(A * p1.x + B * p1.y + C == 0);
		//assert(A * p2.x + B * p2.y + C == 0);
	}
	
	Line(T _A, T _B, T _C) : A(_A), B(_B), C(_C) {}
	
	/*
	| a11 a12 |-1             |  a22 -a12 |
| a21 a22 |    =  1/DET * | -a21  a11 |

with DET  =  a11a22-a12a21
*/
	bool intersection( const Line<T>& line2, Point<T>& pInt )
	{
		T a11 = A;
		T a12 = B;
		T a21 = line2.A;
		T a22 = line2.B;
		
		T det = a11*a22 - a12*a21;
		
		if (det == 0)
			return false;
			
		/*
		-C because we move it to rhs
		*/
			
		T x = a22/det * -C + -a12/det * -line2.C;
		T y = -a21/det * -C + a11/det * -line2.C;
		
		pInt.x = x;
		pInt.y = y;
		
		return true;
	}
	
	bool onLine( const Point<T>& pt)
	{
		T res = A * pt.x + B * pt.y + C;
		
		if (numeric_limits<T>::is_exact)
			return res == 0;
		else 
			return abs(res) < tolerance;
	}
};

template<class T>
class Segment
{
	public:
	//ax + by + c = 0
	Point<T> p1;
	Point<T> p2;

	Segment(const Point<T>& pp1 = Point<T>(0,0), const Point<T>& pp2 = Point<T>(0,0))
		: p1(pp1), p2(pp2)
	{
			
	    
	}
};

template<typename T> 
ostream& operator<<( ostream& os, const Segment<T>& rhs) 
{
	os << " " << rhs.p1 << " -- " << rhs.p2 << " " ;
	return os;
}

template<typename T> 
ostream& operator<<( ostream& os, const Line<T>& rhs) 
{
	os << " " << rhs.A << "x + " << rhs.B << " y + " << rhs.C << " = 0" ;
	return os;
}

template<typename T> 
istream& operator>>( istream& is, Segment<T>& rhs) 
{
	is >> rhs.p1 >> rhs.p2 ;
	return is;
}

template<typename T> 
bool operator<( const Line<T>& lhs, const Line<T>& rhs) 
{
	if (lhs.A != rhs.A)
		return lhs.A < rhs.A;

	if (lhs.B != rhs.B)
		return lhs.B < rhs.B;

	return lhs.C < rhs.C;
}

template<typename T> 
bool operator==( const Line<T>& lhs, const Line<T>& rhs) 
{
	return (lhs.A == rhs.A) &&
		
	 (lhs.B == rhs.B) &&
		 lhs.C == rhs.C;
}


template<typename T>
bool isParallel( const Point<T>& a1, const Point<T>& a2, 
				const Point<T>& b1, const Point<T>& b2)
{
	T z = cross(a2 - a1, b2 - b1 );

	if (numeric_limits<T>::is_exact && z == 0)
		return true;

	if (!numeric_limits<T>::is_exact && abs(z) < tolerance)
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
			//cout << "Points " << A << " " << B << " " << P << " 1 " << endl;
#endif
			return 1;
		}
		if (z < 0) {
#ifndef ONLINE_JUDGE
		//	cout << "Points " << A << " " << B << " " << P << " -1 " << endl;
#endif
			return -1;
		}
	}
	else {
		if (z - tolerance > 0)
			return 1; 
		if (z + tolerance < 0)
			return -1; 
	}

#ifndef ONLINE_JUDGE
	//cout << "Points " << A << " " << B << " " << P << " 0 " << endl;
#endif
    return 0; 
}

template<typename T>
bool isColinear(  const Point<T>& p1, 
    const Point<T>& p2, const Point<T>& p3)
{
	if (p1 == p2 || p2 == p3)
		return true;

    return 0 == getSide(p1, p2, p3);
}

/*
Do 2 line segments intersect?
*/
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
template<typename T>
bool getIntersection(const Point<T>& a1,const Point<T>& a2,
				const Point<T>& b1,const Point<T>& b2, PointD& inter)
{
    Point<double> p = (PointD) a1;
    Point<double> r = a2 - a1;
    
    PointD q = b1;
    PointD s = b2 - b1;
    /*
	The two lines intersect if we can find t and u such that:

p + t r = q + u s

Cross both sides with s, getting

(p + t r) X s = (q + u s) X s

And since s X s = 0, this means

t(r X s) = (q - p) X s

And therefore, solving for t:

t = (q - p) X s / (r X s)

In the same way, we can solve for u:

u = (q - p) X r / (r X s)
*/

    //P + t*r intersects q + u*s
    
    double rCrossS = cross(r, s);
    
    if ( abs( rCrossS ) < tolerance )
        return false;
    
    double t = cross( q-p,  s / rCrossS);
    
    inter = p + t*r;
    
	if (t+tolerance < 0 || t-tolerance > 1)
		return false;
   
	double u = cross(q-p, r / rCrossS);
    
    if (u+tolerance < 0 || u-tolerance > 1)
		return false;

    return true;
}

template<typename T>
double dist( const Point<T>& p1, const Point<T>& p2 )
{
	//return hypot( p1.x-p2.x, p1.y - p2.y );
	return sqrt( (p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y) );
}

template<typename T>
T sqr(T x) 
{ 
	return x * x; 
}

template<typename T>
T length_squared(const Point<T>& v, const Point<T>& w) 
{ 
	return sqr(v.x - w.x) + sqr(v.y - w.y); 
}


double length_squared(const SegmentD& seg)
{
    PointD pt = seg.second - seg.first;
    return pt.x * pt.x + pt.y * pt.y;
}




/**
 distance of point to segment
 
 http://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment
*/
double minimum_distance( const PointD& v, const PointD& w, const PointD& p, PointD& closest) {
  // Return minimum distance between line segment vw and point p
  const double l2 = length_squared(v, w);  // i.e. |w-v|^2 -  avoid a sqrt
  if (l2 == 0.0) return dist(p, v);   // v == w case
  // Consider the line extending the segment, parameterized as v + t (w - v).
  // We find projection of point p onto the line. 
  // It falls where t = [(p-v) . (w-v)] / |w-v|^2
  const double t = dot(p - v, w - v) / l2;
  if (t < 0.0) {
	  closest = v;
	  return dist(p, v);       // Beyond the 'v' end of the segment
  }
  else if (t > 1.0) {
		closest = w;  
	  return dist(p, w);  // Beyond the 'w' end of the segment
  }
  const PointD projection = v + t * (w - v);  // Projection falls on the segment
  closest = projection;
  return dist(p, projection);
}

/*
Circle
*/


const double PI = acos(-1); 

template<typename T>
class Circle
{
public:
	T x;
	T y;
	T r;

	Circle(T _x, T _y, T _r) : x(_x), y(_y), r(_r)
	{

	}

	int sgn(double x) {
        if (x < 0)
            return -1;
        return 1;
    }

	bool getPointsIntersectingLine(const Point<T>& p1,
			const Point<T>& p2, Point<double>& i1,  Point<double>& i2 )
	{
        //Move circle to origin
        T x2 = p2.x - x;
        T y2 = p2.y - y;
        T x1 =  p1.x - x;
        T y1 =  p2.y - y;
        T dx = x2-x1;
        T dy = y2-y1;
        double dr = sqrt(dx*dx+dy*dy);
        T D = x1*y2-x2*y1;

        double disc =r*r*dr*dr-D*D;
        if (disc < 0)
            return false;
        
        double discSqRt = sqrt(disc);
        i1.x = x+ (D*dy+sgn(dy)*dx*discSqRt) / (dr*dr);
        i1.y = y+ (-D*dx+abs(dy) * discSqRt) / (dr*dr);

        i2.x = x+ (D*dy-sgn(dy)*dx*discSqRt) / (dr*dr);
        i2.y = y+ (-D*dx-abs(dy) * discSqRt) / (dr*dr);

        return true;
    }
	
	//http://en.wikipedia.org/wiki/Circular_segment
    /**
     * Will find the area that is <= half or the area of the circle
     * @param segmentLength
     * @return
     */
    double findSegmentArea(double segmentLength) {

        double arg = (2 * r * r - segmentLength * segmentLength)
                / (2 * r * r);
        
        //Correct if arg = -1.0000000002 for ex
        if (arg < -1) {
            assert( arg >= -1.000001);
            arg = -1;
        }
        
        double ang = acos(arg);

        double area = (r * r / 2.0) * (ang - sin(ang));

        return area;
    }
};


/*
returns 0 inside, 1 border, 2 outside
*/
template<typename T>
int inCircle( const Point<T>& p, const Point<T>& c, T r) { 
  T dx = p.x - c.x;
  T dy = p.y - c.y;
  T Euc = dx * dx + dy * dy;
  T rSq = r * r; 

  if (numeric_limits<T>::is_exact) 
  {
	  return Euc < rSq ? 0 : Euc == rSq ? 1 : 2;  // inside/border/outside
  } else {
	  if ( abs(Euc - rSq) < tolerance )
		  return 1;

	  return Euc < rSq ? 0 : 2; 
  }
}

//Finds centers of circle given 2 pts and a radius
template<typename T>
bool circle2PtsRad(const Point<T>& p1, const Point<T>& p2, 
				   double r,  
				   Point<T>& c1,
				   Point<T>& c2) 
{
  T d2 = length_squared(p1, p2);

  /**
  if m is midway between p1 and p2

  ratio mc_1 / mp_1

  pythag says r^2 = mp_1^2 + mc_1^2

  so mc_1 = sqrt(r^2 - mp_1^2)

  h = sqrt(r^2 - mp_1^2) / mp_1

  h = sqrt(r^2 - mp_1^2) / sqrt(mp_1^2)
  h = sqrt( r^2 / mp_1^2 - 1 )

  4 * mp_1^2 = d2

  ratio mc_1 / mp_1 = 2 * sqrt( r^2 / d2 - 1/4 )
  */

  double det = r * r / d2 - 0.25;
  if (det < 0.0) return false;
  double h = sqrt(det);

  //point m is average p1 and p2  ; scaling / rotation combined (swap x,y neg x)
  c1.x = (p1.x + p2.x) * 0.5 + (p1.y - p2.y) * h;
  c1.y = (p1.y + p2.y) * 0.5 + (p2.x - p1.x) * h;

  c2.x = (p1.x + p2.x) * 0.5 + (p2.y - p1.y) * h;
  c2.y = (p1.y + p2.y) * 0.5 + (p1.x - p2.x) * h;
  return true; 
}

//given 3 points, find circle
void findCircle( PointD p1, PointD p2, PointD p3, double& radius, PointD& center)
{
	PointD midAB = (p1 + p2) / 2.0;
	PointD midBC = (p2 + p3) / 2.0;

	Line<double> lineAB(p1, p2);
	Line<double> lineBC(p2, p3);
		
	assert(lineAB.onLine(midAB));
	assert(lineBC.onLine(midBC));
		
	//Take the equation of line perpendicular to ax+by+c=0 as bx-ay+k=0 where k is a constant.
		
	double k1 = -(lineAB.B * midAB.x - lineAB.A * midAB.y);
	double k2 = -(lineBC.B * midBC.x - lineBC.A * midBC.y);
		
	//Create lines going from midpoints perpendicular to original lines
	Line<double> lineMidABCenter(lineAB.B, -lineAB.A, k1);
	Line<double> lineMidBCCenter(lineBC.B, -lineBC.A, k2);
		
	
	bool ok = lineMidABCenter.intersection(lineMidBCCenter, center);
		
	assert( lineMidABCenter.onLine(midAB) );
	assert( lineMidBCCenter.onLine(midBC) );
	assert( lineMidABCenter.onLine(center) );
	assert( lineMidBCCenter.onLine(center) );
		
	radius = dist(center, p1);
				
}

/*
Rectangle
*/

/*
Is (x, y) in rectangle ?  true if inside (not just touching edge)
*/
template<typename T>
bool inRectangle( T x, T y, T x1, T x2, T y1, T y2, T epsilon )
{	
	assert( x1 <= x2 );
	assert( y1 <= y2 );
	
	if (cmp(x, x1, epsilon) > 0 &&
		cmp(x, x2, epsilon) < 0 &&
		cmp(y, y1, epsilon) > 0 &&
		cmp(y, y2, epsilon) < 0 )
		return true;
	
	return false;
}

/*
Triangle
*/



/*
Inside triangle, not on the lines
*/
template<typename T>
bool inTriangle( T x, T y, T x1, T y1, T x2, T y2, T x3, T y3, T epsilon )
{	
	
	//x, y must be on the same side as all 3 edges
	Point<T> A(x1, y1);
	Point<T> B(x2, y2);
	Point<T> C(x3, y3);
	
	Point<T> P(x, y);

	//assumes triangle is not degenterate
	if ( getSide( A, B, P) == getSide( A, B, C) &&
		getSide( A, C, P) == getSide( A, C, B) &&
		getSide( B, C, P) == getSide( B, C, A) )
		return true;
	
	return false;
}



bool inTriangle2( const PointD& p, const PointD& tri1,
    const PointD& tri2, const PointD& tri3)
{
	PointD closest;
	//not sure if this is necesary for not degenerate triangles
	if ( abs(minimum_distance(tri1, tri2, p, closest)) < tolerance)
		return false;
	if ( abs(minimum_distance(tri1, tri3, p, closest)) < tolerance)
		return false;
	if ( abs(minimum_distance(tri2, tri3, p, closest)) < tolerance)
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



/*
Polygon
*/


template<typename T>
double polyArea ( const vector<Point<T> >& points) 
{
        double sum = 0;
        for(int i = 0; i < points.size() - 1; ++i) {            
            sum += points[i].x * points[i+1].y;  
        }
        
        sum += points[ points.size()-1].x * points[0].y;
        
        for(int i = 0; i < points.size() - 1; ++i) {            
            sum -= points[i+1].x * points[i].y;  
        }
        
        sum -= points[0].x * points[points.size()-1].y;
        
        return abs(sum) / 2;
}



template<typename T>
void grahamScan(const vector<Point<T> >& pointsIn, vector<Point<T> >& hullList)
{
    typedef vector<Point<T> > vpt;
    
   vector<Point<T> > points(pointsIn);
   
   sort(all(points), LowestYSortOrder<T>());
   
   for(typename vpt::iterator ito = points.begin();
	ito != points.end();
	++ito)
	{
		//cout << "1 Sorted input " << *ito << endl;
	}
	
   //Sort relative to polar angle with first point (lowest y point)
   sort(points.begin() + 1, points.end(), PolarOrder<T>(points[0]));
   
   for(typename vpt::iterator ito = points.begin();
	ito != points.end();
	++ito)
	{
		//cout << "Sorted input " << *ito << endl;
	}
   
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
	   
	   //cout << " t1 " << t1 << " t2 " << t2 << " point " << t3 << endl;
       
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
//STOPCOMMON

#include "stdio.h"

char buf[200];
PointD points[200];

double x, y;
int main() 
{

	int T;
	//trailing whitespace
	scanf("%d ", &T);

	FOR(t, 0, T)
	{
	    int pt = 0;
	    if (t > 0)
           puts("");
	    while(1)
	    {
	        if (!fgets(buf, 200, stdin) ||  2 != sscanf(buf, "%lf %lf", &x, &y))
		        break;
		    
		    points[pt++] = PointD(x, y);
		    //printf("Read %lf %lf\n", x, y);
		}
		
		int best = -1;
		
		FOR(p1, 0, pt) 
		{
		    FOR(p2, p1+1, pt)
		    {
		         PointD center1, center2;
		         bool ok = circle2PtsRad( points[p1], points[p2],
		             2.5,
		             center1,
		             center2);
		         
		         if (!ok)
		         {
		             //cout << ok << " Points p1 " << points[p1] << ", p2 " << points[p2] << endl;
		             //cout << ok << " " << center1 << ", " << center2 << endl;
		             continue;
		         }
		         
		         int total1 = 0;
		         int total2 = 0;
		         FOR(ptIdx, 0, pt)
		         {
		             if (inCircle(points[ptIdx], center1, 2.5) <= 1)
		                 total1++;
		             
		             if (inCircle(points[ptIdx], center2, 2.5) <= 1)
		                 total2++;
		         }
		         
		         best = max(best, total1);
		         best = max(best, total2);
		    }
		}
		
		printf("%d\n", best);
		//scanf("%d", &nSeg);
		
	}
	return 0;
}

