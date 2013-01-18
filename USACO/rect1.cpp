/*
ID: eric7231
PROG: rect1
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
#include <iomanip>
#include <iterator>
#include <sstream>
#include <bitset>
#include <cctype>
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(uint k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(uint k=(a); k < (b); ++k)

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

struct Rect;

ostream& operator<<( ostream& os, const Rect& r);

struct Rect {
    uint x1, x2, y1, y2;
    uint color;
    
    Rect( uint llx, uint lly, uint urx, uint ury, uint _color) :
        x1(llx),
        y1(lly),
        x2(urx),
        y2(ury),
        color(_color)
    {
        assert(y2 > y1);
        assert(x2 > x1);
    }
    
    uint getArea() const
    {
        return (x2-x1) * (y2-y1);
    }
    
    bool intersects(const Rect& o) const
    {
        //Equals because we do not care about 0 width / 0 height rectangles
        if (x1 >= o.x2)
            return false;
        
        if (x2 <= o.x1)
            return false;
        
        if (y1 >= o.y2)
            return false;
        
        if (y2 <= o.y1)
            return false;
        
        return true;
    }
    
    //Top rectangle
    void addNewRects(const Rect& tr, vector<Rect>& newRects) const
    {
       // cout << "AddNewRects --\n" << *this << "\ntop rectangle:\n" << tr << endl;
        
        assert(intersects(tr));
        
        uint iX1 = max(tr.x1, x1);
        uint iX2 = min(tr.x2, x2);
        uint iY1 = max(tr.y1, y1);
        uint iY2 = min(tr.y2, y2);
        
       // cout << "Intersection X: " << iX1 << ", " << iX2 << endl;
        //cout << "Intersection Y: " << iY1 << ", " << iY2 << endl;
        //Create a rectangle immediately to the left 
        
        if (iX1 > x1)
        {
            
            newRects.pb( Rect( x1, iY1, iX1, iY2, color) );
            //cout << "Creating new rect left of intersection " << *newRects.rbegin() << endl;
        }
        
        if (iX2 < x2)
        {
            newRects.pb( Rect( iX2, iY1, x2, iY2, color) );
            //cout << "Creating new rect right of intersection " << *newRects.rbegin() << endl;
        }
        
        //The rectangles above are the same width as rectangle 
        if (iY1 > y1)
        {
            newRects.pb( Rect( x1, y1, x2, iY1, color) );
            //cout << "Creating new rect below of intersection " << *newRects.rbegin() << endl;
            
        }

        if (iY2 < y2)
        {
            newRects.pb( Rect( x1, iY2, x2, y2, color) );
           // cout << "Creating new rect below of intersection " << *newRects.rbegin() << endl;
            
        }
        
    }

};

ostream& operator<<( ostream& os, const Rect& r)
{
    os << "top left (" << r.x1 << ", " << r.y2 << ") "
    << " top right (" << r.x2 << ", " << r.y2 << ") "
    << " bot right (" << r.x2 << ", " << r.y1 << ") "
    << " bot left (" << r.x1 << ", " << r.y1 << ") ";

    return os;
}

int main() {
    
	ofstream fout ("rect1.out");
    ifstream fin ("rect1.in");
	
    uint W, H, N;
    
    fin >> W >> H >> N;
        
    vector<Rect> rects;
    
    rects.pb( Rect( 0, 0, W, H, 1) );
    
    vector<Rect> newRects;
    
    uint llx, lly, urx, ury, color;
    
    FOR(r, 0, N)
    {
        fin >> llx >> lly >> urx >> ury >> color;

        Rect topRect(llx, lly, urx, ury, color);
        
        newRects.clear();
        
        vector<Rect>::iterator it = rects.begin();
        while( it != rects.end() )
            
        {
            const Rect rect = *it;
            if (rect.intersects(topRect)) {
                rect.addNewRects(topRect, newRects);
                it = rects.erase(it);
            } else {
                ++it;
            }
        }
        rects.pb(topRect);
        rects.insert( rects.end(), all(newRects) );
    }
            
    uvi colorCount( 2500, 0 );
    
    for(vector<Rect>::iterator it = rects.begin();
        it != rects.end();
        ++it)
    {
        colorCount[ it->color - 1 ] += it->getArea();
    }
    
    FOR( color, 0, 2500 )
    {
        if (colorCount[color] == 0)
            continue;
        
        fout << color+1 << " " << colorCount[color] << endl;
    }
            
    return 0;
}
