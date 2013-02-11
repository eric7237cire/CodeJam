package codejam.y2009.round_2.digging_problem;

import java.util.Arrays;
import java.util.Scanner;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Solution extends InputFilesHandler implements TestCaseHandler<SolutionInputData>, TestCaseInputScanner<SolutionInputData>

{

    public Solution() {
        super("B", 1, 1);
    }

    @Override
    public SolutionInputData readInput(Scanner scanner, int testCase)
    {
        SolutionInputData in = new SolutionInputData(testCase);

        in.h = scanner.nextInt();
        in.w = scanner.nextInt();
        in.f = scanner.nextInt();

        //int h = in.h;
        int w = in.w;
      //  int f = in.f;

        in.a = new char[in.h + 1][w + 2];

        char[][] a = in.a;

        for (int i = 0; i < in.h; i++)
        {
            a[i][0] = '#';
            a[i][w + 1] = '#';
            String s = scanner.next();
            for (int j = 0; j < w; j++)
            {
                a[i][j + 1] = s.charAt(j);
            }
        }

        return in;
    }

    private static final int INF = 1000000000;

    @Override
    public String handleCase(SolutionInputData in)
    {
        
        char[][] a = in.a;

        Arrays.fill(a[in.h], '#');
        int j = 1;
        while (a[0][j + 1] == '.' && a[1][j + 1] == '#')
            j++;

        int[][][] z = new int[52][52][52];
        for (int[][] ints : z)
        {
            for (int[] anInt : ints)
            {
                Arrays.fill(anInt, -1);
            }
        }

        int res = count(0, 1, j, in, z);

        return "Case #" + in.testCase + ": " + (res == INF ? "No" : "Yes " + res);
    }

    /**
     * 
     * l1 to r1 is walkable range
     */
    private static int count(int y, int l1, int r1, SolutionInputData in, int[][][] z)
    {
        int h = in.h;
      //  int w = in.w;
        int f = in.f;
        char[][] a = in.a;

        if (y == h - 1)
            return 0;
        if (z[y][l1][r1] == -1)
        {
            int res = INF;

            boolean bl = false;
            boolean br = false;
            int l = l1;
            int r = r1;

            if (a[y + 1][l] == '.')
            {
                l = l + 1;
                bl = true;
            }
            if (a[y + 1][r] == '.')
            {
                r = r - 1;
                br = true;
            }

            //Jump left
            if ((a[y][l - 1] == '.' || bl) && a[y + 1][l - 1] == '.')
            {
                int yy = y;
                while (a[yy + 1][l - 1] == '.')
                    yy++;
                if (yy - y <= f)
                {
                    int ll = l - 1;
                    while (a[yy][ll - 1] == '.' && a[yy + 1][ll - 1] == '#')
                        ll--;
                    int rr = l - 1;
                    while (a[yy][rr + 1] == '.' && a[yy + 1][rr + 1] == '#')
                        rr++;
                    res = Math.min(res, count(yy, ll, rr, in, z));
                }
            }

            //Jump right
            if ((a[y][r + 1] == '.' || br) && a[y + 1][r + 1] == '.')
            {
                int yy = y;
                while (a[yy + 1][r + 1] == '.')
                    yy++;
                if (yy - y <= f)
                {
                    int ll = r + 1;
                    while (a[yy][ll - 1] == '.' && a[yy + 1][ll - 1] == '#')
                        ll--;
                    int rr = r + 1;
                    while (a[yy][rr + 1] == '.' && a[yy + 1][rr + 1] == '#')
                        rr++;
                    res = Math.min(res, count(yy, ll, rr, in, z));
                }
            }

            //Digging
            if (r > l)
            {
                //Jump more than one cell
                for (int x = l; x <= r; x++)
                {
                    if (a[y + 2][x] == '.')
                    {
                        int yy = y + 2;
                        while (a[yy + 1][x] == '.')
                            yy++;
                        if (yy - y <= f)
                        {
                            int ll = x;
                            while (a[yy][ll - 1] == '.' && a[yy + 1][ll - 1] == '#')
                                ll--;
                            int rr = x;
                            while (a[yy][rr + 1] == '.' && a[yy + 1][rr + 1] == '#')
                                rr++;
                            res = Math.min(res, 1 + count(yy, ll, rr, in, z));
                        }
                    }
                }

                // Make hall left to right
                for (int rr = l; rr < r; rr++)
                    if (a[y + 2][rr] == '#')
                    {
                        int j = rr;
                        while (j > l && a[y + 2][j] == '#')
                            j--;
                        for (int ll = j; ll <= rr; ll++)
                        {
                            int lll = ll;
                            if (ll == l && a[y + 2][ll] == '#')
                            {
                                while (a[y + 1][lll - 1] == '.' && a[y + 2][lll - 1] == '#')
                                    lll--;
                            }
                            res = Math.min(res, (rr - ll + 1) + count(y + 1, lll, rr, in, z));
                        }
                    }

                // Make hall right to left
                for (int ll = l + 1; ll <= r; ll++)
                    if (a[y + 2][ll] == '#')
                    {
                        int j = ll;
                        while (j < r && a[y + 2][j] == '#')
                            j++;
                        for (int rr = ll; rr <= j; rr++)
                        {
                            int rrr = rr;
                            if (rr == r && a[y + 2][rr] == '#')
                            {
                                while (a[y + 1][rrr + 1] == '.' && a[y + 2][rrr + 1] == '#')
                                    rrr++;
                            }
                            res = Math.min(res, (rr - ll + 1) + count(y + 1, ll, rrr, in, z));
                        }
                    }
            }
            z[y][l1][r1] = res;
        }
        return z[y][l1][r1];
    }

}
