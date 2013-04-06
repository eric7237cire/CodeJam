#include <stdio.h>
#include <math.h>
#include <assert.h>

#define EPS 1e-8
#define MAXN 50

typedef struct {
  long double x, y;
} Point;

int circle(Point p1, Point p2, Point p3, Point *center, long double *r)
{
  long double a,b,c,d,e,f,g;

  a = p2.x - p1.x;
  b = p2.y - p1.y;
  c = p3.x - p1.x;
  d = p3.y - p1.y;
  e = a*(p1.x + p2.x) + b*(p1.y + p2.y);
  f = c*(p1.x + p3.x) + d*(p1.y + p3.y);
  g = 2.0*(a*(p3.y - p2.y) - b*(p3.x - p2.x));
  if (fabs(g) < EPS) {
    return 0;
  }
  center->x = (d*e - b*f) / g;
  center->y = (a*f - c*e) / g;
  *r = sqrt((p1.x-center->x)*(p1.x-center->x) +
       (p1.y-center->y)*(p1.y-center->y));
  return 1;
}

int main () {
  Point g[MAXN], inter;
  int i, n, c = 0;
  long double theta, x, y, minx, miny, maxx, maxy, radius;;
  while (1) {
    scanf("%d",&n);
    if (!n) return 0;
    c++;
    for (i = 0; i < 3; i++) 
      if (scanf("%Lf%Lf", &g[i].x, &g[i].y) != 2)
	goto A;
    assert (circle(g[0],g[1],g[2],&inter,&radius) == 1);
    x = g[0].x-inter.x;
    y = g[0].y-inter.y;
    minx = maxx = g[0].x;
    miny = maxy = g[0].y;
    theta = 2*M_PI/n;
    for (i = 1; i < n; i++) {
      g[i].x = inter.x+x*cos(theta*i)-y*sin(theta*i);
      g[i].y = inter.y+x*sin(theta*i)+y*cos(theta*i);
      if (g[i].x < minx) minx = g[i].x;
      if (g[i].x > maxx) maxx = g[i].x;
      if (g[i].y < miny) miny = g[i].y;
      if (g[i].y > maxy) maxy = g[i].y;
    }
    printf("Polygon %d: %.3Lf\n", c, (maxx-minx)*(maxy-miny));
  }
 A: return 0;
}


