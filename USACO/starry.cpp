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

template<typename T>
Point<T> rotate90(const Point<T>& pt)
{
	return Point<T>( -pt.y, pt.x );
}

template<typename T>
class LowestYSortOrder
{
	public:
	LowestYSortOrder()
	{
	}

    int operator()(const Point<T>& p1, const Point<T>& p2) const
    {
        return p1.y < p2.y || (p1.y == p2.y && p1.x < p2.x);
    }
};


const double tolerance = 0.000002;

struct ComplexPoint : public complex<int>
{
	ComplexPoint(int xx, int yy) : complex<int>(xx,yy)
	{

	}
	int x() 
	{
		return real();
	}

	int y()
	{
		return imag();
	}
};

typedef Point<int> PPoint;

typedef set<Point<int>, LowestYSortOrder<int> > sp;

sp flood_fill( const vvi& grid, const PPoint& start, int width, int height )
{
    vector<PPoint> dirs;
    typedef vector<PPoint> vp;
    for(int x = -1; x <= 1; ++x)
    {
        for(int y = -1; y <= 1; ++y)
        {
            if (x == 0 && y == 0)
                continue;
            
            dirs.pb(PPoint(x, y));
        }
    }
    
    queue<PPoint> toVisit;
    sp visited;

	toVisit.push(start);
    
    while(!toVisit.empty())
    {
        PPoint p = toVisit.front();
        
        toVisit.pop();
        
        if (contains(visited, p)) {
            continue;
        }
        
        visited.insert(p);
        
        for(vp::iterator it = dirs.begin();
            it != dirs.end(); ++it)
        {
            PPoint np = p + *it;
            if (np.x >= 0 && np.x < width &&
                np.y >= 0 && np.y < height)
            {
				char ch = grid[np.x][np.y];
				assert(ch == '0' || ch == '1');
				if (ch == '1')
					toVisit.push(np);
            }
        }
        
    }

	return visited;
}



class OrderSetPoints
{
	public:
	OrderSetPoints()
	{
	}

	LowestYSortOrder<int> cmp;

    int operator()(const sp& s1, const sp& s2) const
    {
        return lexicographical_compare( all(s1), all(s2), 
			cmp );
    }
};




typedef map<sp, char, OrderSetPoints> mapSpChar;

sp flipSet( const sp& points);
sp normalize( const sp& points);
sp rotateSet90( const sp& points);

char findLetter ( mapSpChar& clusterLetters, const sp& points, int nLetters )
{
	//Try and find it
	sp norm = normalize(points);

	mapSpChar::iterator it = clusterLetters.find(norm); 

	if (it != clusterLetters.end()) {
		return it->second;
	}

	int nextLetter = 'a' + nLetters;

	sp flipped = flipSet(norm);

	clusterLetters.insert(mp(norm, nextLetter));
	clusterLetters.insert(mp(flipped, nextLetter));

	for(int i = 0; i < 3; ++i)
	{
		norm = rotateSet90(norm);
		flipped = rotateSet90(flipped);

		clusterLetters.insert(mp(norm, nextLetter));
		clusterLetters.insert(mp(flipped, nextLetter));

	}

	return nextLetter;
}

/**
Find min x and min y and make minX, minY the origin.

This means when translation is done all points are >= 0
*/
sp normalize( const sp& points)
{
    
    int minX = numeric_limits<int>::max();
    int minY = numeric_limits<int>::max();
    
    for(sp::iterator it = points.begin(); it != points.end(); ++it)
    {
        minX = min(minX, it->x);
        minY = min(minY, it->y);
    }
    
    Point<int> newOrigin(minX, minY);
    
    sp ret;
    
    for(sp::iterator it = points.begin(); it != points.end(); ++it)
    {
        ret.insert( *it - newOrigin);
    }
    
    return ret;
}



/*
Assume set is already normalized, though not sure if that even matters...
*/
sp rotateSet90( const sp& points)
{
    
    sp ret;
    
    for(sp::iterator it = points.begin(); it != points.end(); ++it)
    {
        ret.insert( rotate90(*it) );
    }
    
    return normalize(ret);        
}

Point<int> flip( const Point<int>& pt );

/**
flips about x axis
*/
sp flipSet( const sp& points)
{
    
    sp ret;
    
    for(sp::iterator it = points.begin(); it != points.end(); ++it)
    {
        ret.insert( flip(*it) );
    }
    
    return normalize(ret);        
}

Point<int> flip( const Point<int>& pt )
{
	return Point<int>( pt.x, -pt.y);
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

    vvi grid(width, vi(height, 0));
    
	for(int y = height - 1; y >= 0; --y)
    {
		FOR(x, 0, width)    
        {
            char ch;
            fin >> ch;
            grid[x][y] = ch;
        }
    }
     
    //Once grid has been read in, scan again, looking for unassigned stars

	mapSpChar clusterLetters;
	int nLetters = 0;
	char maxLetter = -1;

	for(int y = height - 1; y >= 0; --y)
    {
		FOR(x, 0, width)
        {
			if (grid[x][y] != '1')
				continue;

			sp cluster = flood_fill(grid, Point<int>(x,y), width, height);

			cout << "Cluster at " << Point<int>(x, y) << " size " << cluster.size() << endl;

			char letter = findLetter(clusterLetters, cluster, nLetters);

			if (letter > maxLetter) {
				++nLetters;
				maxLetter = letter;
			}

			cout << "Assigning " << letter << endl;

			for(sp::iterator it = cluster.begin(); it != cluster.end(); ++it)
			{
				grid[it->x][it->y] = letter;
			}

			/*
			for(int yy = height - 1; yy >= 0; --yy)
			{

				FOR(xx, 0, width)
				{
					cout << (char) grid[xx][yy];
				}
				cout << endl;
			}	
			cout << endl << endl;
			*/

		}
	}
    
	
    
    for(int y = height - 1; y >= 0; --y)
    {
		FOR(x, 0, width)
		{
			fout << (char) grid[x][y];
		}
		fout << endl;
	}

    return 0;
}
