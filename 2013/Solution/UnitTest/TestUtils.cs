#define LOGGING_TRACE
#define LOGGING
using NUnit.Framework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CodeJamUtils;
using Utils;


using Logger = Utils.LoggerFile;
using Round2;
using Round2.ErdosNS;
using Utils.math;

namespace UnitTest
{
   

    [TestFixture]
    public class TestUtils
    {
     //   [ClassCleanup]
        public static void cleanup()
        {
            Logger.CurrentDomain_ProcessExit(null, null);
        }

        [Test]
        public void testToStringFrac()
        {
            Assert.AreEqual("0", ((BigFraction)0).ToString());
        }


        [Test]
        public void testDIS()
        {

            CollectionAssert.AreEqual(
                new int[] { 9, 6, 3 },
                Erdos.getLDS(new int[] { 
                7, 8, 9, //0 - 2
                4, 5, 6, // 3 - 5
                1, 2, 3 })); // 6 - 8

            CollectionAssert.AreEqual(
                new int[] { 9, 8, 7 },
                Erdos.getLDS(new int[] { 
                3, 2, 1, //0 - 2
                6, 5, 4, // 3 - 5
                9, 8, 7 })); // 6 - 8

            CollectionAssert.AreEqual(
                new int[] { 7, 4 },
                Erdos.getLDS(new int[] { 
                4, 5, 6, //0 - 2
                1, 7, 2, // 3 - 5
                3, 7, 4 })); // 6 - 8

            CollectionAssert.AreEqual(
                new int[] { 7, 4 },
                Erdos.getLDS(new int[] { 
                4, 5, 6, //0 - 2
                1, 7, 1, 2, // 3 - 5
                3, 7, 4, 4, 8  })); // 6 - 8

            //Non decreasing sequence
            CollectionAssert.AreEqual(
               new int[] { 1, 1, 2, 3, 4, 4, 8 },
               Erdos.getLongestSequence(new int[] { 
                4, 5, 6, //0 - 2
                1, 7, 1, 2, // 3 - 5
                3, 7, 4, 4, 8  }, (lhs, rhs) => { if (lhs <= rhs) return -1; return 1; })); // 6 - 8
        }

        [Test]
        public void testLIS()
        {
            
            CollectionAssert.AreEqual(
                new int[]{1, 2, 3},
                Erdos.getLIS(new int[] { 
                7, 8, 9, //0 - 2
                4, 5, 6, // 3 - 5
                1, 2, 3 })); // 6 - 8

            CollectionAssert.AreEqual(
                new int[] { 1, 4, 7 },
                Erdos.getLIS(new List<int>(new int[] { 
                3, 2, 1, //0 - 2
                6, 5, 4, // 3 - 5
                9, 8, 7 }))); // 6 - 8

            CollectionAssert.AreEqual(
                new int[] { 1, 2, 3, 4 },
                Erdos.getLIS(new List<int>(new int[] { 
                4, 5, 6, //0 - 2
                1, 7, 2, // 3 - 5
                3, 7, 4 }))); // 6 - 8

            CollectionAssert.AreEqual(
                new int[] { 1, 2, 3, 4, 8 },
                Erdos.getLIS(new List<int>(new int[] { 
                4, 5, 6, //0 - 2
                1, 7, 1, 2, // 3 - 5
                3, 7, 4, 4, 8  }))); // 6 - 8
        }

        [Test]
        public void testLowerBound()
        {
            List<int> list = new List<int>(new int[] { 
                4, 7, 8, //0 - 2
                18, 19, 19, // 3 - 5
                19, 23, 44 }); // 6 - 8

            Assert.AreEqual(0, list.lowerBound(0, list.Count, 3));
            Assert.AreEqual(1, list.lowerBound(1, list.Count, 3));

            Assert.AreEqual(3, list.lowerBound(0, list.Count, 9));
            Assert.AreEqual(3, list.lowerBound(0, list.Count, 18));
            Assert.AreEqual(4, list.lowerBound(0, list.Count, 19));
            Assert.AreEqual(7, list.lowerBound(0, list.Count, 20));
            Assert.AreEqual(7, list.lowerBound(0, list.Count, 23));
            Assert.AreEqual(8, list.lowerBound(0, list.Count, 24));
            Assert.AreEqual(8, list.lowerBound(0, list.Count, 44));
            Assert.AreEqual(9, list.lowerBound(0, list.Count, 45));

            list = new List<int>(new int[] { 
                3, 5});

            Assert.AreEqual(2, list.lowerBound(7));
        }

        [Test]
        public void testBinarySearch()
        {
            List<int> list = new List<int>(new int[] { 4, 7, 8, 18, 19, 23, 44 });

            Tuple<int, int> a1 = list.binarySearch(5);

            Assert.AreEqual(4, a1.Item1);
            Assert.AreEqual(7, a1.Item2);

            a1 = list.binarySearch(18);
            Assert.AreEqual(18, a1.Item1);
            Assert.AreEqual(19, a1.Item2);

            a1 = list.binarySearch(19);
            Assert.AreEqual(19, a1.Item1);
            Assert.AreEqual(23, a1.Item2);

            a1 = list.binarySearch(43);
            Assert.AreEqual(23, a1.Item1);
            Assert.AreEqual(44, a1.Item2);
        }

        [Test]
        public void testModdedLong()
        {
            int mod = 7;
            for(int i = 2; i <= 800; ++i)
            {
                for(int j = 2; j < 3; ++j)
                {
                    if (i % j != 0)
                        continue;

                    int real = i / j;

                    ModdedLong ml = new ModdedLong(i);
                    ml /= j;
                    Assert.AreEqual(real % mod, ml.Value, "{0} / {1} % {2}".FormatThis(i,j,mod));
                }
            }
        }


        [Test]
        public void testModdedLong2()
        {
            
            int mod = 1000002013;

            int an = ModdedLong.modInverse(2, mod);

            long start = 35184372088832;
            long end = start + 1000;
            for (long v = start; v <= end; ++v)
            {
                for (long j = 2; j < 3; ++j)
                {
                    if (v % j != 0)
                        continue;

                    long real = v / j;

                    ModdedLong ml = new ModdedLong(v);
                    ml /= (int)j;
                    Assert.AreEqual(real % mod, ml.Value, "{0} / {1} % {2}".FormatThis(v, j, mod));
                }
            }
        }

        public static long IntPower(long x, int pow)
        {
            long ret = 1;
            while (pow != 0)
            {
                if ((pow & 1) == 1)
                    ret *= x;
                x *= x;
               
                pow >>= 1;
            }
            return ret;
        }

        public static long IntPower2(int x, short power)
        {
            if (power == 0) return 1;
            if (power == 1) return x;
            // ----------------------
            int n = 15;
            while ((power >>= 1) >= 0) n--;

            long tmp = x;
            while (--n > 0)
                tmp = tmp * tmp *
                     (((power <<= 1) < 0) ? x : 1);
            return tmp;
        }

        [Test]
        public void testPow()
        {
            int mod = 1000002013;
            for(int i = 1; i <= 10; ++i)
            {
                for(short j = 1; j <= 10; ++j)
                {
                    long expected = IntPower(i, j) % mod;
                    long actual = ModdedLong.pow(i, j, mod);
                    Assert.AreEqual(expected, actual);
                }
            }
        }

        [Test]
        public void testPow2()
        {
            int mod = 17;
            for (int i = 1; i <= 10; ++i)
            {
                for (short j = 1; j <= 10; ++j)
                {
                    long expected = IntPower(i, j) % mod;
                    long actual = ModdedLong.pow(i, j, mod);
                    Assert.AreEqual(expected, actual);
                }
            }
        }

    }
}
