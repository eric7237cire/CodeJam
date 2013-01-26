/*
ID: eric7231
PROG: fence4
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

void findVisible(PointD observer, const PointD& target, const vector<SegmentD>& fence, vector<bool>& visible) {
  
  const SegmentD ray(observer, target);

  int closestFenceSegIdx = -1;
  
  double minDist = numeric_limits<double>::max();

  if (debug)
  cout << "Ray  Coords " << 
    ray.first.x << " " << 
    ray.first.y << " " << 
    ray.second.x << " " << 
    ray.second.y << endl;
  
  for(int i=0; i<fence.size(); i++) {
	  SegmentD segFence = fence[i];

	  if(isParallel(ray, segFence))
		continue;

	  PointD inter;

	  bool c = getIntersection(ray, segFence, inter);

	  if (debug)
	  cout << "Checking fence segment " << i
	  << " coords " <<
    segFence.first.x << " " << 
    segFence.first.y << " " << 
    segFence.second.x << " " << 
    segFence.second.y << "  Intersection " << inter << endl;
  
	  assert(c);

	  //First see if it is on the segment
	  double d1 = dist(segFence.first, inter);
	  double d2 = dist(segFence.second, inter);
	  double dSeg = dist(segFence.first, segFence.second);

	  double diff = d1+d2 - dSeg;

	  //Not on segment
	  if ( abs(diff) > tolerance )
	  {
	      
         if (debug)
	      cout << "Index " << i << " not on segment.  d1= " << d1 << " d2 " << d2 << " dSeg " << dSeg << " inter " << inter  << endl;
	      
	
		  continue;
	  }
	  
	  //Intersecting corners
	  if ( abs(d1) < tolerance )
      {
          int prevFenceIdx = i == 0 ? fence.size() - 1 : i -1;
	      SegmentD prevFence = fence[prevFenceIdx];
	      assert(prevFence.second == fence[i].first);
	      
	      //If the prev post's 1st point and the fence[i]'s second point are on the same side
	      //of the ray, it is just a corner sticking out
	      
	      if (getSide( ray.first, ray.second, prevFence.first ) ==
        getSide( ray.first, ray.second, fence[i].second ) )
          {
            if (debug)
	      cout << "Index " << i << " breezed corner first " << endl;
	      continue;    
          } else {
            cout << "Index " << i << " hit the corner.  d1= " << d1 << " d2 " << d2 << " dSeg " << dSeg << " inter " << inter  << endl;  
          }
          
	  }
	  
	  if ( abs(d2) < tolerance )
	  {
	      int nextFenceIdx = i == fence.size() - 1 ? 0 : i+1;
	      SegmentD nextFence = fence[nextFenceIdx];
	      
	      assert( fence[i].second == nextFence.first );
	      
	      if (getSide( ray.first, ray.second, nextFence.second ) ==
	          getSide( ray.first, ray.second, fence[i].first ) ) 
	      {
	          if (debug)
	              cout << "Index " << i << " breezed by corner second " << endl;
	          
	          continue;    
          } else {
               cout << "Index " << i << " hit the corner second.  d1= " << d1 << " d2 " << d2 << " dSeg " << dSeg << " inter " << inter  << endl;
          }
	      
	  }

	  if (!isBetween(inter.x, ray.first.x, ray.second.x))
	  {
	      if (debug)
	      cout << "Index " << i << " not between ray " << inter.x << " ray " << ray.first.x << " " << ray.second.x << endl;
	      continue;
	  }
	  
	  double dToObs = dist(observer, inter);
    
	  if (dToObs < minDist) {
		minDist = dToObs;
		closestFenceSegIdx=i;
	  }
  }

  if(closestFenceSegIdx < 0)
	  return;

  if (debug)
  cout << "SegIdx = " << closestFenceSegIdx << " Coords " << 
    fence[closestFenceSegIdx].first.x << " " << 
    fence[closestFenceSegIdx].first.y << " " << 
    fence[closestFenceSegIdx].second.x << " " << 
    fence[closestFenceSegIdx].second.y << endl;
  visible[closestFenceSegIdx] = true;
}

int main() {
    
	ofstream fout ("fence4.out");
    ifstream fin ("fence4.in");
	
    
    PointD ab(5, 5);
    PointD cd(-1.5, 2);
    
    PointD ef(0, 3);
    PointD gh(-3, 1);
    
    SegmentD seg1(ab, cd);
    SegmentD seg2(ef, gh);
    
    PointD inter;
    getIntersection( seg1, seg2, inter);
    cout << "intersection " <<  inter << endl;
    
    
    uint N;
    fin>>N;
    
    PointD observer;
    fin >> observer.x >> observer.y;
    
    vector<PointD> fence(N);
    FOR(i, 0, N)
    {
        fin>>fence[i].x >> fence[i].y;
    }
    
	vector<SegmentD> fenceSeg;
    
  // check if the fence is  valid 
    FOR(i, 0, N) 
    {
        int i2 = i==N-1 ? 0 : i+1;

		SegmentD seg1( fence[i], fence[i2] );
		fenceSeg.pb( seg1 );

        FOR(k, 0, N)
        {
            //Next point
            int k2 = (k+1)%N;

            if(i==k || i==k2 || i2==k)
                continue;
            
            SegmentD seg2( fence[k], fence[k2] );
            
            if (intersects(seg1, seg2))
            {
                fout << "NOFENCE" << endl;
                return 0;
            }
        }
    }

    
	vector<bool> visible(fenceSeg.size(), false);
  
	FOR(i, 0, N)
	{
		//cout << "i is " << i << " " << fenceSeg[i].first.x << " " << fenceSeg[i].first.y << " " << fenceSeg[i].second.x << " " << fenceSeg[i].second.y << endl;

		PointD nextFence = i == N-1 ? fence[0] : fence[i+1];
		PointD prevFence = i == 0 ? fence[N-1] : fence[i-1];
		
		//findVisible(observer, fence[i], fenceSeg, visible);
		
		findVisible(observer, fence[i]+ .001*(prevFence-fence[i]), fenceSeg, visible);
		findVisible(observer, fence[i]+ .001*(nextFence-fence[i]), fenceSeg, visible);
		
		
		findVisible(observer, fence[i]+ .499*(nextFence-fence[i]), fenceSeg, visible);
		//findVisible(observer, (fenceSeg[i].first + fenceSeg[i].second) / (double) 2, fenceSeg, visible);
	}
	
	swap(fenceSeg[fenceSeg.size() - 1], fenceSeg[fenceSeg.size() - 2]);
    swap(fenceSeg[fenceSeg.size() - 2].first, fenceSeg[fenceSeg.size() - 2].second);
    
	bool tmp =  visible[visible.size() - 1];
	visible[visible.size() - 1] =  visible[visible.size() - 2];
	visible[visible.size() - 2] = tmp;

	uint visCount = 0;
	FOR(i, 0, N)
	{
		if(!visible[i])
			continue;
		++visCount;
	}

	fout << visCount << endl;
	FOR(i, 0, N)
	{
		if(!visible[i])
			continue;

		fout << fenceSeg[i].first.x << " " << fenceSeg[i].first.y << " " << fenceSeg[i].second.x << " " << fenceSeg[i].second.y << endl;
	}
	
    return 0;
}
