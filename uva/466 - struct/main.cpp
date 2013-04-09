#include <stdio.h>

#define S "Pattern %d was "
#define U "preserved"
#define R "rotated "
#define V "reflected vertically"
#define X "90"D
#define Y "180"D
#define Z "270"D
#define D " degrees"
#define C V" and rotated "
#define I S"improperly transformed"E
#define E ".\n"
#define P(x,y,l,s)            \
  for (i = n; i--;)           \
    for (j = n; j--;)         \
      if (o[i][j] != t[x][y]) \
        goto l;               \
  printf(S s E, ++c);         \
  continue;                   \
l:

int main(void)
{
  int c = 0, i, j, n;
  char o[10][11], t[10][11];

  while (scanf("%d", &n) == 1) {
    for (i = 0; i < n; ++i)
      scanf("%s %s\n", o[i], t[i]);
    P(i, j, l1, U);
    P(j, n - i - 1, l2, R X);
    P(n - i - 1, n - j - 1, l3, R Y);
    P(n - j - 1, i, l4, R Z);
    P(n - i - 1, j, l5, V);
    P(j, i, l6, C X);
    P(i, n - j - 1, l7, C Y);
    P(n - j - 1, n - i - 1, l8, C Z);
    printf(I, ++c);
  }

  return 0;
}