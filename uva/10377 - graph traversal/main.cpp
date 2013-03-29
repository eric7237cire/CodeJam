#include <cstdio>
#define VALID(x, y) (x >= 0 && x < n && y >= 0 && y < m && (grid[x][y] != '*'))
#define REP(i, a, b) for (int i = a; i < b; ++i)
int m, n;
char grid[80][80];
 
int main () {
        int TC, x, y; char ch;
        scanf("%d\n", &TC);
        while (TC--) {
                scanf("%d %d\n", &n, &m);
                REP(i, 0, n) gets(grid[i]);
                scanf("%d %d", &x, &y);
                x--; y--;
                char o = 'N';
                while (scanf("%c", &ch), ch != 'Q') {
                        if (ch == 'R') {
                                if (o == 'N') o = 'E';
                                else if (o == 'E') o = 'S';
                                else if (o == 'S') o = 'W';
                                else if (o == 'W') o = 'N';
                        }
                        else if (ch == 'L') {
                                if (o == 'N') o = 'W';
                                else if (o == 'E') o = 'N';
                                else if (o == 'S') o = 'E';
                                else if (o == 'W') o = 'S';
                        }
                        else if (ch == 'F') {
                                if (o == 'N' && VALID(x-1,y)) x-=1;
                                else if (o == 'E' && VALID(x, y+1)) y+=1;
                                else if (o == 'S' && VALID(x+1, y)) x+=1;
                                else if (o == 'W' && VALID(x, y-1)) y-=1;
                        }
                }
                printf("%d %d %c\n", x+1, y+1, o);
                if (TC) printf("\n");
        }
        return 0;
}