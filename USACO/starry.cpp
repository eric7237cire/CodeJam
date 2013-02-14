/*
ID: eric7231
PROG: starry
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

struct ComplexPoint : public complex<int>;

template<typename Point> 
set<Point> flood_fill( const vvi& grid, const Point& start, int width, int height )
{
    vector<Point> dirs;
    typedef vector<Point> vp;
    for(int x = -1; x <= 1; ++x)
    {
        for(int y = -1; y <= 1; ++y)
        {
            if (x == 0 && y == 0)
                continue;
            
            dirs.pb(Point(x, y));
        }
    }
    
    queue<Point> toVisit;
    set<Point> visited;
    
    while(!toVisit.empty())
    {
        Point p = toVisit.front();
        
        toVisit.pop();
        
        if (contains(visited, p)) {
            continue;
        }
        
        visited.insert(p);
        
        for(vp::iterator it = dirs.begin();
            it != dirs.end(); ++it)
        {
            Point np = p + *it;
            if (np.x() >= 0 && np.x() < width &&
                np.y() >= 0 && np.y() < height)
            {
                toVisit.pb(np);
            }
        }
        
    }
}

/**
Find min x and min y and make minX, minY the origin.

This means when translation is done all points are >= 0
*/
template<typename Point, typename T> 
set<Point> normalize( const set<Point>& points)
{
    typedef set<Point> sp;
    T minX = numeric_limits<T>::max();
    T minY = numeric_limits<T>::max();
    
    for(sp::iterator it = points.begin(); it != points.end(); ++it)
    {
        minX = min(minX, it->x());
        minY = min(minY, it->y());
    }
    
    Point newOrigin(minX, minY);
    
    set<Point> ret;
    
    for(sp::iterator it = points.begin(); it != points.end(); ++it)
    {
        ret.insert( *it - newOrigin);
    }
    
    return ret;
}

/*
Assume set is already normalized, though not sure if that even matters...
*/
template<typename Point, typename T> 
set<Point> rotateSet90( const set<Point>& points)
{
    
    set<Point> ret;
    
    for(sp::iterator it = points.begin(); it != points.end(); ++it)
    {
        ret.insert( rotate90(*it) );
    }
    
    return normalize(ret);        
}

/**
flips about x axis
*/
template<typename Point, typename T> 
set<Point> flipSet( const set<Point>& points)
{
    
    set<Point> ret;
    
    for(sp::iterator it = points.begin(); it != points.end(); ++it)
    {
        ret.insert( flip(*it) );
    }
    
    return normalize(ret);        
}

complex<int> flip( const complex<int>& pt )
{
    return complex<int>( pt.real(), -pt.imag() );    
}


int main() {
    
	ofstream fout ("starry.out");
    ifstream fin ("starry.in");

    int width, height;
    fin >> width >> height;

    vvi grid;
    
    FOR(x, 0, width)
    {
        FOR(y, 0, height)
        {
            char ch;
            fin >> ch;
            grid[x][y] = ch;
        }
    }
     
    //Once grid has been read in, scan again, looking for unassigned stars
    
    return 0;
}
