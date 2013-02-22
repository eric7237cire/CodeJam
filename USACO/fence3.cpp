/*
ID: eric7231
PROG: fence3
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
#include <complex>
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

double dot(PointD u, PointD v)
{
    return u.x*v.x + u.y*v.y;
}


double sqr(double x) { return x * x; }

double length_squared(const PointD& v, const PointD& w) { return sqr(v.x - w.x) + sqr(v.y - w.y); }


double length_squared(const SegmentD& seg)
{
    PointD pt = seg.second - seg.first;
    return pt.x * pt.x + pt.y * pt.y;
}

double dist(const PointD pt1, const PointD pt2)
{
    PointD pt = pt2 - pt1;
    return sqrt(pt.x * pt.x + pt.y * pt.y);
}

/**
 distance of point to segment
 
 http://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment
*/
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


double getTotalDistance(const vector<SegmentD>& seg, PointD pt)
{
    double t = 0;
    FOR(i, 0, seg.size())
    {
		double dis = minimum_distance( seg[i].first, seg[i].second, pt);
        t += dis;
    }
    
    return t;
}

void ternarySearch(const vector<SegmentD>& seg, PointD& pt, bool doX)
{
    
    PointD leftPt = PointD(pt);
    PointD rightPt = PointD(pt);
    
    PointD leftThirdPt = PointD(pt);
    PointD rightThirdPt = PointD(pt);
    
    double& left = doX ? leftPt.x : leftPt.y;
    double& right = doX ? rightPt.x : rightPt.y;
    
    left = 0;
    right = 200;
     
    double& leftThird = doX ? leftThirdPt.x : leftThirdPt.y;
    double& rightThird = doX ? rightThirdPt.x : rightThirdPt.y;
    
    
//  invariant list[loIdx] <= target ; list[hiIdx] > target
        while (true) {
            cout << fixed << setprecision(5) ;
			cout << "Left " << left << " right " << right << endl;

			cout << "Points l " << leftPt << " r " << rightPt << " l thi " << leftThirdPt << " r thi " << rightThirdPt << endl;

			assert(right >= left);

			double f1 = getTotalDistance(seg, leftPt);
			double f2 = getTotalDistance(seg, rightPt);
			
			cout << " left = " << f1 << " right = " << f2 << endl;
			
			if ( abs(f2-f1) < 0.001 )
			{
			    leftThird = (left + right) / 2;
			    pt = leftThirdPt;
                return;
            }
            /*
            if ( (right - left) < 0.01)
            {
                pt = leftThirdPt;
                return;
            }*/
             
            leftThird = (2*left + right) / 3;
            rightThird = (left + 2*right) / 3;
                
            double t1 = getTotalDistance(seg, leftThirdPt);
            double t2 = getTotalDistance(seg, rightThirdPt);

			cout << "Left third " << leftThird << " = " << t1 << " Right third " << rightThird << " =  " << t2 << endl;
            
            if (t1 > t2)
            {
                //Can't be between left and left third
                left = leftThird;
            } else {
                right = rightThird;
            }

			cout << endl;
        }
        
        
        return;
}

int main() {
    
	ofstream fout ("fence3.out");
    ifstream fin ("fence3.in");

    int N;  
    fin >> N;
    
    vector<SegmentD> seg;
  
    FOR(i, 0, N)
    {
        int a,b,c,d;
        fin >> a >> b >> c >> d;
        
        seg.pb( mp( PointD(a,b), PointD(c,d) ) );
    }
    
    PointD pt(220,220);
    
	
	ternarySearch(seg, pt, true);
    ternarySearch(seg, pt, false);    
    ternarySearch(seg, pt, true);    
    ternarySearch(seg, pt, false);
    ternarySearch(seg, pt, true);    
    ternarySearch(seg, pt, false);
    
    //fout << pt << endl;
    
    double d = getTotalDistance(seg, pt);
    
    fout << fixed << setprecision(1) << pt.x << " " << pt.y << " " << d << endl;
    
	cout << "Total " << getTotalDistance(seg, PointD(15.9, 15.9)) << endl;

    return 0;  
}  
