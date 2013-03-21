//STARTGEOM
//STOPGEO

#include "stdio.h"

double a;


#include <math.h>

int main()
{
    double x,y,z,l,areaOfRect;
    double pi=acos(-1);
    while(scanf("%lf",&x)==1){
        areaOfRect=x*x;
        y = areaOfRect * (1 - sqrt(3) + pi / 3);
        z = areaOfRect * (2 * sqrt(3) - 4 + pi / 3);
        l = areaOfRect * (4 - sqrt(3) - 2 * pi/ 3);
        printf("%.3f %.3f %.3f\n", y,z,l);
    }
    return 0;
}

/*
Your question "How to solve this problem without calculus?" causes me to believe that you are asking a classic question.

I am visualizing a unit square with a circle centered at each corner. The circles all have radius 1. A quarter of each circle falls inside the square. Each circular arc connects two, diagonally opposite corners.

The square is divided by the inscribed quarter circles into 9 areas.
-- The four slender areas along the sides. I will call these "widow's peaks," because they somewhat resemble the hairline of somebody who has a widow's peak.
-- The four bullet shapes with their points at the four corners of the square. I will call these "bullets."
-- The sort of bulgey square in the center, tilted up on one corner. I will call this "the bulgey square."

In the classic problem, you are asked to find the area of the bulgey square without using calculus. That is the problem I will address here.

Start with a simplified diagram. Draw the square and just part of the two quarter circles centered on the bottom two corners. Draw just enough of each arc to reach from the upper two corners of the square to the point where the two arcs intersect. You should now have a square with just a single widow's peak along the top side, and nothing else drawn inside the square. Your objective, for the moment, is to find the area of the widow's peak.

The secret to success is to add two lines to the diagram. Connect the intersection of the two circular arcs to the two lower corners of the square. Doing so constructs an equilateral triangle with side 1.

Your simplified diagram has now divided the square into four areas:
-- The widow's peak.
-- The triangle.
-- Two "pizza slices."

You should be able to find the area of the triangle.

You should be able to determine the central angle of the pizza slices, and from that derive the area of each pizza slice.

The area of the widow's peak is the area of the square less the area of the equilateral triangle and the areas of the two pizza slices.

Now return to the original diagram.

Your next objective is to find the area of one bullet. This is easy. You know the area of one quarter circle. Subtract it from the area of the square. From what remains, subtract the area of two widow's peaks. What still remains is the area of one bullet.

The only area you have not yet found is the one you are looking for: the area of the bulgey square. It's area is the area of the square less the areas of the four widow's peaks and the areas of the four bullets.

If you have followed this far, and done the calculations, then you have answered the question I suspect you were asked.

For your reference, this is the answer: 1 - √3 + π/3.
*/