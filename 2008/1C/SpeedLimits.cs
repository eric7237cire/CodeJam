using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Diagnostics;

namespace LogicProblems
{
    class SpeedLimits : TestCaseHandler
    {
        #region TestCaseHandler Membres

        const int totalLimit = 1000000007;

        public string doTestCase() {
            /*
             * The first line of input gives the number of cases, N. N test cases follow. 
             * The first line of each case contains n, m, X, Y and Z each separated by a space. 
             * n will be the length of the sequence of speed limits. m will be the length of the generating array A.
             * The next m lines will contain the m elements of A, one integer per line (from A[0] to A[m-1]).
             * Using A, X, Y and Z, the following pseudocode will print the speed limit sequence in order. mod indicates the remainder operation. 
             */

            string[] input = Console.ReadLine().Split(new char[] { ' ' });
            
            int n = int.Parse(input[0]);
            int m = int.Parse(input[1]);
            long X = int.Parse(input[2]);
            int Y = int.Parse(input[3]);
            int Z = int.Parse(input[4]);

            int[] A = new int[m];

            for (int i = 0; i < m; ++i) {
                A[i] = int.Parse(Console.ReadLine());
            }

            int[] speedLimits = new int[n];

            for (uint i = 0; i < n; ++i) {
                speedLimits[i] = A[i % m];                
                A[i % m] = (int) ((X * A[i % m] + Y * (i + 1)) % Z) ;
                //Trace.WriteLine(speedLimits[i]);
            }

            int[] sortedSpeedLimits = new int[n];
            Array.Copy(speedLimits, sortedSpeedLimits, n);

            Array.Sort(sortedSpeedLimits);

            for (uint i = 0; i < n; ++i) {
                speedLimits[i] = Array.BinarySearch(sortedSpeedLimits, speedLimits[i]);
            }

            BinaryIntervalTree rootBucket = new ArrayBIT(n, totalLimit);
            
            for (uint i = 0; i < n; ++i) {
                int speedLimit = speedLimits[i];
                //Console.WriteLine("Speed limit " + speedLimit);

                //We have a new subsequence for every existing sequence that ends in a value < speedLimit
                int newSubsequences = speedLimit == 0 ? 1 : 1 + rootBucket.valuesInRange(speedLimit - 1);

                //Debug.WriteLine("Adding " + newSubsequences);
                rootBucket.Add(speedLimit, newSubsequences);                
            }

            
            return rootBucket.totalValue().ToString();  //total.ToString();

        }

        #endregion
    }
}
