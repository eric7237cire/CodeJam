#include <stdio.h>

int main(void)
{
  int n, f, i, j, t[1000];
  double s[100000][4], r1, r2, d, t1, t2, t3, t4, t5, t6, *c, *v;

  while (scanf("%d", &n) == 1 && n) {
    for (i = 0; i < n; ++i)
      scanf("%lf %lf %lf %lf", &s[i][0], &s[i][1], &s[i][2], &s[i][3]);
    f = 0;
    for (i = n; i--; ) {
      c = s[i];
      t1 = c[2] - c[0];
      t2 = c[3] - c[1];
      for (j = n; --j > i; ) {
        v = s[j];
        t3 = v[2] - v[0];
        t4 = v[3] - v[1];
        t5 = c[0] - v[0];
        t6 = c[1] - v[1];
        r1 = t3 * t6 - t4 * t5;
        r2 = t1 * t6 - t2 * t5;
        d = t4 * t1 - t3 * t2;
        if (d < 0) {
          d = -d;
          r1 = -r1;
          r2 = -r2;
        }
        if (r1 >= 0 && r1 <= d && r2 >= 0 && r2 <= d)
          break;
      }
      if (j == i)
        t[f++] = i + 1;
    }
    printf("Top sticks:");
    for (i = f; i--; )
      printf(" %d%s", t[i], i == 0 ? "" : ",");
    printf(".\n");
  }

  return 0;
}