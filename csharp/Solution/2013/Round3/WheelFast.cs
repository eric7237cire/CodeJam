#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
//#define LOGGING_TRACE
#endif 

#define USING_BIGINT

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Utils.math;
using Logger = Utils.LoggerFile;

/*
 *  Unfortunately needs double long to be efficient,
 *  NW(i,j) goes above 1e308...
 */
#if USING_BIGINT
using NumWaysType = System.Numerics.BigInteger;
#else
//OK for small
using NumWaysType = System.Double;
#endif
namespace Round3
{



    public class WheelFast
    {
        const int MAXN = 200;
        
        /// <summary>
        /// NW(i,j) is how many ways to fill the
        /// interval [i,j] such that nothing spills over.
        /// 
        /// In getE_wheelFast, the brute force calculation, it is
        /// the denominator
        /// </summary>
        NumWaysType[][] numWays_IJ;
        int[][] emptyGondolasCount;
        NumWaysType[][] combin;

        //true if taken, 0 if not
        bool[] hasGondola;

        /// <summary>
        /// E(i,j) means the expected value of filling in [i,j] provided that nothing spills over,
        /// ie anything after j that is empty remains empty.
        /// </summary>
        double[][] expectedValue;

        int N;

        public WheelFast(int N)
        {
            int DIM = N * 2 + 1;

            numWays_IJ = new NumWaysType[DIM][];
            combin = new NumWaysType[DIM][];
            expectedValue = new double[DIM][];
            emptyGondolasCount = new int[DIM][];
            hasGondola = new bool[DIM];
            for (int i = 0; i < numWays_IJ.Length; ++i)
            {
                numWays_IJ[i] = new NumWaysType[DIM];
                combin[i] = new NumWaysType[DIM];
                expectedValue[i] = new double[DIM];
                emptyGondolasCount[i] = new int[DIM];
            }
        }

        double f(int n)
        {
            return N - (n - 1) * 0.5;
        }

        /// <summary>
        /// How many ways to fill in interval [i,j]
        /// such that nothing spills over on the 2 sides
        /// [i,k) and right side (k, j]
        /// and k is filled in last (or first)
        /// </summary>
        /// <param name="i"></param>
        /// <param name="j"></param>
        /// <param name="k"></param>
        /// <returns></returns>
        NumWaysType numWays_ijk(int i, int j, int k)
        {
            //How many ways to fill in left side
            NumWaysType numWaysLeft = (k >= i + 1 ? numWays_IJ[i][k - 1] : 1);

            //Right side
            NumWaysType numWaysRight = (k <= j - 1 ? numWays_IJ[k + 1][j] : 1);

            //Choose the order which people go left (1 person to fill each open space)
            int chooseN = (k >= i + 1 ? emptyGondolasCount[i][k - 1] : 0) + (k <= j - 1 ? emptyGondolasCount[k + 1][j] : 0);
            int chooseK = (k >= i + 1 ? emptyGondolasCount[i][k - 1] : 0);
            NumWaysType choose = combin[chooseN][chooseK];

            //number of ways to fill in the last guy
            int nWaysFillPosK = (k - i + 1);

           // Logger.LogTrace("S({},{},{}) = {} * {}  * Choose {} {} = {} * {}", i, j, k, pLeft, pRight, chooseN, chooseK, choose, (k - i + 1));
            return numWaysLeft * numWaysRight * choose * nWaysFillPosK;
        }

        public double solve(string s)
        {
            int n = s.Length;
            N = n;
            int i, j;
            int qq = 0;
            for (i = 0; i < n; i++)
            {
                hasGondola[i] = s[i] == 'X' ;
                //Doubling the array to avoid cycling issues
                hasGondola[i + n] = hasGondola[i];

                //Counting holes
                qq += (1 - (hasGondola[i] ? 1 : 0));
            }

            Preconditions.checkState(qq >= 0);

            for (i = 0; i <= 2 * n; i++)
            {
                numWays_IJ[i][i] = 1;
                emptyGondolasCount[i][i] = hasGondola[i] ? 0 : 1;
            }

            

            //u[i][j] is the number of free gondolas between i and j
            for (i = 0; i < 2 * n; i++)
                for (j = i + 1; j < 2 * n; j++)
                    emptyGondolasCount[i][j] = emptyGondolasCount[i][j - 1] + (hasGondola[j] ? 0 : 1);

           
            //Combination c[n][k] 
            combin[0][0] = 1;
            for (i = 1; i <= n; i++)
            {
                combin[i][0] = 1;
                for (j = 1; j <= i; j++)
                    combin[i][j] = combin[i - 1][j] + combin[i - 1][j - 1];
            }

            int l;

            //Length 2 to n 
            for (l = 2; l <= n; l++)
            {
                for (i = 0; i < 2 * n && i + l <= 2 * n; i++)
                {
                    int k;
                    NumWaysType w = 0;
                    int ok = 0;
                    j = i + l - 1;
                    //[i, j] 
                    for (k = i; k < i + l; k++) if (!hasGondola[k] )
                        {
                            ok = 1;
                            w += numWays_ijk(i, j, k);
                            //assert (S >= 0);
                        }
                    if (ok == 0)
                    {
                        w = 1;
                    }
                    Utils.Preconditions.checkState(w > 0);
                    //Preconditions.checkState(!Double.IsInfinity(w));
                    Logger.LogTrace("v[{}][{}] = {}", i, j, w);
                    numWays_IJ[i][j] = w;
                }
            }

            for (l = 1; l <= n; l++)
            {
                for (i = 0; i < 2 * n && i + l <= 2 * n; i++)
                {
                    j = i + l - 1;
                    int k;
                    double w = 0;
                    for (k = i; k <= j; k++) if (!hasGondola[k])
                        {
                        
#if USING_BIGINT
                        double evLeftRightCenter = (k >= i + 1 ? expectedValue[i][k - 1] : 0) +
                            (k <= j - 1 ? expectedValue[k + 1][j] : 0) +
                            f(k - i + 1);

                        NumWaysType ijk = numWays_ijk(i, j, k);

                        double weightedPercentage = (double)new BigFraction(ijk, numWays_IJ[i][j]);

                        w += evLeftRightCenter * weightedPercentage;
#else
                        w += numWays_ijk(i, j, k) / numWays_IJ[i][j] * ((k >= i + 1 ? expectedValue[i][k - 1] : 0) + (k <= j - 1 ? expectedValue[k + 1][j] : 0) + f(k - i + 1));
#endif
                        Preconditions.checkState(!Double.IsNaN(w));
                        }

                    expectedValue[i][j] = w;
                    Logger.LogTrace("r[{}][{}] = {}  ", i, j, w);

                   // Fraction check = WheelBruteForce.getE_wheelFast(gondolas, i, j, freeCount[i][j], N);
                   // Logger.LogTrace("r[{}][{}] = {}  check {}  Wheel {}", i, j, w, (double) check, s);

                   
                }
            }

            double rr = 0;
            for (i = 0; i < n; i++) if (!hasGondola[i + n - 1])
                {
                    j = i + n - 1;
                    int k = j;
                    //assert (S <= v[i][j]);
                    int cc;
#if USING_BIGINT
                    BigFraction frac = numWays_ijk(i, j, k);
                    for (cc = 0; cc < qq; cc++)
                    {
                        frac /= n;
                    }
                    double eee = (double) frac * ((j >= i + 1 ? expectedValue[i][j - 1] : 0) + f(n));
                    Preconditions.checkState(!Double.IsNaN(eee));
#else
                    double eee = numWays_ijk(i, j, k) * ((j >= i + 1 ? expectedValue[i][j - 1] : 0) + f(n));
                    for (cc = 0; cc < qq; cc++)
                    {
                        eee /= n;
                    }
#endif

                    rr += eee;
                }
            return (double)rr;

        }
    }

    /*
        
  
  


}*/
}
