#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
//#define LOGGING_TRACE
#endif

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using CodeJamUtils;
using Utils;
using Utils.tree;
using Logger = Utils.LoggerFile;

using CodeJam.Utils.graph;
using Utils.geom;
using NUnit.Framework;

namespace Round1B_2014.Problem2
{
    public class LotteryInput
    {
        public int A;
        public int B;
        public int K;
    }

    public class Lottery : InputFileProducer<LotteryInput>, InputFileConsumer<LotteryInput, long>
    {
        public LotteryInput createInput(Scanner scanner)
        {
            LotteryInput input = new LotteryInput();
            input.A = scanner.nextInt();
            input.B = scanner.nextInt();
            input.K = scanner.nextInt();

            return input;
        }

        public long processInputSmall(LotteryInput input)
        {
            int a1 =  countBruteForce(input.A, input.B, input.K);
            

            return a1;
        }

        public long processInput(LotteryInput input)
        {
            long[][][][] dp;
            Ext.createArray(out dp, 32, 2, 2, 2, -1);

            long a2 = count(31, false, false, false, input.A, input.B, input.K, dp);
            return a2;
        }

        public static int countBruteForce(int A, int B, int K)
        {
            int ways = 0;
            int bsLen = 8;
            for (int a = 0; a < A; ++a)
            {
                for (int b = 0; b < B; ++b)
                {
                    if ((a & b) < K)
                    {
                        Logger.LogTrace("Counting a {}: {} b {}: {} for k {}: {}",
                            a, a.ToBinaryString(bsLen), b, b.ToBinaryString(bsLen),
                            (a & b), (a & b).ToBinaryString(bsLen),
                            K, K.ToBinaryString(bsLen));
                        ++ways;
                    }
                }
            }

            return ways;

        }

        public static int count(int i, bool isPrefixLessThanM, int M)
        {
            if (i == -1)  //# The base case.
                return isPrefixLessThanM ? 1 : 0; //  # only count if it is strictly less than M.

            bool isOneFeasible = isPrefixLessThanM || M.GetBit(i);

            /*
             * Set prefix's new LSB to 0.  New prefix will be less than
             * M if the ith bit of M is 1 -or- if prefix was already less than M.
             */
            int res = count(i - 1, isOneFeasible, M);

            //Set prefix's LSB to 1
            if (isOneFeasible)
            {
                res += count(i - 1, isPrefixLessThanM, M);
            }

            return res;
        }

        /*
         *  Given some prefix with bits in positions > i
         *  101110110[i]......
         *  count how many binary strings a and b can be generated such that a & b < K
         */
        public static long count(int i, bool lessA, bool lessB, bool lessK, int A, int B, int K, long[][][][] dp)
        {
            if (i == -1)
            {
                return (lessA && lessB && lessK) ? 1 : 0;
            }

            if (dp[i][lessA ? 1 : 0][lessB ? 1 : 0][lessK ? 1 : 0] >= 0)
            {
                return dp[i][lessA ? 1 : 0][lessB ? 1 : 0][lessK ? 1 : 0];
            }

            long res = 0;

            bool isOneFeasibleB = lessB || B.GetBit(i);
            bool isOneFeasibleA = lessA || A.GetBit(i);
            //Using K to see if we can add a 1 to the prefix of A&B, A&B's prefix must
            //already be less than K or K's i'th bit is set
            bool isOneFeasibleAandB = lessK || K.GetBit(i);

            //8 cases in theory, adding 0 or 1 to the prefixes of A, B, and K

            //0 to A, 0 to B, 0 to K
            res += count(i - 1, isOneFeasibleA, isOneFeasibleB, isOneFeasibleAandB, A, B, K, dp);

            //0 to A, 1 to B, 0 to A&B            
            if (isOneFeasibleB)
            {
                res += count(i - 1, isOneFeasibleA, lessB, isOneFeasibleAandB, A, B, K, dp);
            }
                        
            //1 to A, 0 to B, 0 to A&B
            if (isOneFeasibleA)
            {
                res += count(i - 1, lessA, isOneFeasibleB, isOneFeasibleAandB, A, B, K, dp);
            }
                        
            // 1 to A, 1 to B, 1 to A&B
            if (isOneFeasibleAandB && isOneFeasibleA && isOneFeasibleB)
            {
                res += count(i - 1, lessA, lessB, lessK, A, B, K, dp);
            }

            return dp[i][lessA ? 1 : 0][lessB ? 1 : 0][lessK ? 1 : 0] = res;
        }

        /// <summary>
        /// Returns 0000[1]11111
        /// </summary>
        /// <param name="i">Location of brackets</param>
        /// <returns></returns>
        public static int genMask(int i)
        {
            return (1 << i + 1) - 1;
        }

    }

    [TestFixture]
    public class TestLottery
    {
        [Test]
        public void TestSimple()
        {

            TestPrefix(
                sPrefixA: "0000 ",
                sPrefixB: "0000 ",
                sPrefixAandB: "0000 ",
                i: 0, A: 1, B: 1, K: 1);

            TestPrefix(
               sPrefixA: "0000 ",
               sPrefixB: "0000 ",
               sPrefixAandB: "0000 ",
               i: 0, A: 1, B: 3, K: 2);

            //TestPrefix(prefix: 0, i: 1, A: 1, B: 3, K: 2);
        }

        public int PrefixToBinary(string str)
        {
            int ans = 0;
            for (int i = 0; i < str.Length; ++i)
            {
                int bitPos = str.Length - 1 - i;
                if (str[i] == '1')
                {
                    ans += 1 << bitPos;
                }
            }
            return ans;
        }

        [Test]
        public void TestPrefixToBinary()
        {
            Assert.AreEqual(20, PrefixToBinary("101  "));
        }

        public void TestPrefix(string sPrefixA, string sPrefixB, string sPrefixAandB, int i, int A, int B, int K)
        {
            int mask = Lottery.genMask(i);

            int prefixA = PrefixToBinary(sPrefixA);
            int prefixB = PrefixToBinary(sPrefixB);
            int prefixAandB = PrefixToBinary(sPrefixAandB);

            //To be a valid test, otherwise prefix is not feasible
            Assert.IsTrue(prefixA <= A);
            Assert.IsTrue(prefixB <= B);
            Assert.IsTrue(prefixAandB <= K);

            Logger.LogTrace("Testing prefix\n{}\n{}\n{}", sPrefixA, sPrefixB, sPrefixAandB);
            int check = 0;
            int bsLen = 8;
            for (int a = 0; a < A; ++a)
            {
                for (int b = 0; b < B; ++b)
                {
                    if ((a & b) < K
                        && (a >> (i + 1)) == prefixA >> (i + 1)
                        && (b >> (i + 1) ) == prefixB >> (i + 1)
                        && ((a & b) >> (i + 1) ) == prefixAandB >> (i + 1))
                    {
                        Logger.LogTrace("Counting a {}: {} b {}: {} for k {}: {}",
                            a, a.ToBinaryString(bsLen), b, b.ToBinaryString(bsLen),
                            (a & b), (a & b).ToBinaryString(bsLen),
                            K, K.ToBinaryString(bsLen));
                        ++check;
                    }
                }
            }

            //Is the same prefix in A less than the given prefix?
            //Prefix is in bits > i 
            //mask is 000000[1]11111
            //mask will be 11111[0]00000
            bool lessA = prefixA < (A & ~mask);
            bool lessB = prefixB < (B & ~mask);
            bool lessK = prefixAandB < (K & ~mask);

            long[][][][] dp;           
            Ext.createArray(out dp, 4, 2, 2, 2, -1);
            long ans = Lottery.count(i, lessA, lessB, lessK, A, B, K,dp);

            Assert.AreEqual(check, ans);
        }

        [Test]
        public void TestCount()
        {
            int testInt = 123;
            Assert.AreEqual(testInt, Lottery.count(31, false, testInt));
        }

        [Test]
        public void TestGenMask()
        {
            Assert.AreEqual(7, Lottery.genMask(2));
            Assert.AreEqual(3, Lottery.genMask(1));
            Assert.AreEqual(1, Lottery.genMask(0));
            //Assert.AreEqual(0, Lottery.genMask(0));
        }
    }
}