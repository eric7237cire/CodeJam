#define LOGGING_TRACE
#define LOGGING
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CodeJamUtils;
using Utils;


using Logger = Utils.LoggerFile;

namespace UnitTest
{
   

    [TestClass]
    public class TestUtils
    {
        [ClassCleanup]
        public static void cleanup()
        {
            Logger.CurrentDomain_ProcessExit(null, null);
        }

        [TestMethod]
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

        [TestMethod]
        public void testModdedLong()
        {
            ModdedLong.mod = 7;
            for(int i = 2; i <= 800; ++i)
            {
                for(int j = 2; j < 3; ++j)
                {
                    if (i % j != 0)
                        continue;

                    int real = i / j;

                    ModdedLong ml = new ModdedLong(i);
                    ml /= j;
                    Assert.AreEqual(real % ModdedLong.mod, ml.Value, "{0} / {1} % {2}".FormatThis(i,j,ModdedLong.mod));
                }
            }
        }


        [TestMethod]
        public void testModdedLong2()
        {
            
            ModdedLong.mod = 1000002013;

            int an = ModdedLong.modInverse(2, (int)ModdedLong.mod);

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
                    Assert.AreEqual(real % ModdedLong.mod, ml.Value, "{0} / {1} % {2}".FormatThis(v, j, ModdedLong.mod));
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

        [TestMethod]
        public void testPow()
        {
            ModdedLong.mod = 1000002013;
            for(int i = 1; i <= 10; ++i)
            {
                for(short j = 1; j <= 10; ++j)
                {
                    long expected = IntPower(i, j) % ModdedLong.mod;
                    long actual = ModdedLong.pow(i, j);
                    Assert.AreEqual(expected, actual);
                }
            }
        }

        [TestMethod]
        public void testPow2()
        {
            ModdedLong.mod = 17;
            for (int i = 1; i <= 10; ++i)
            {
                for (short j = 1; j <= 10; ++j)
                {
                    long expected = IntPower(i, j) % ModdedLong.mod;
                    long actual = ModdedLong.pow(i, j);
                    Assert.AreEqual(expected, actual);
                }
            }
        }

    }
}
