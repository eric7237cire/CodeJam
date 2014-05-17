#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE

using CombPerm;
using NUnit.Framework;
using Round3;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Utils.math;

using Logger = Utils.LoggerFile;

namespace UnitTest
{
    [TestFixture]
    public class TestWheel
    {
        [Test]
        public void TestDiffs()
        {
            Assert.AreEqual(3, ModdedLong.diff(5, 0, 8));

            Assert.AreEqual(0, ModdedLong.diff(5, 5, 8));

            Assert.AreEqual(2, ModdedLong.diff(1, 3, 8));
        }

        [Test]
        public void TestInc()
        {
            ModdedLong l = new ModdedLong(1, 3);
            Assert.AreEqual(1, l);
            Assert.AreEqual(2, ++l);
            Assert.AreEqual(2, l);
            Assert.AreEqual(2, l++);
            Assert.AreEqual(0, l);
        }

        [Test]
        public void TestCopyArray()
        {
            int[] array = new int[] { 3, 8, 1, -3, 45, 19, -5 };

            int[] copy1 = Wheel.copyArray(array, 6, 0);
            CollectionAssert.AreEqual(new int[] { -5, 3 }, copy1, copy1.ToCommaString());

            int[] copy2 = Wheel.copyArray(array, 1, 3);
            CollectionAssert.AreEqual(new int[] { 8, 1, -3 }, copy2);
        }

        [Test]
        public void TestSimulatePermutation()
        {
            bool[] gondolas = new bool[] {false, false, true, false};

            Assert.AreEqual(0, Wheel.simulatePermutation(gondolas, new int[] {0, 2, 0}));
            Assert.AreNotEqual(0, Wheel.simulatePermutation(gondolas, new int[] {1, 0, 0}));

            Assert.AreNotEqual(0, Wheel.simulatePermutation(gondolas, new int[]{1,0,3}, 0));
            //Perm 1, 0, 3 for ij False, False, True, False. 
        }

        [Test]
        public void TestP()
        {
            bool[] gondolas = new bool[] { false, false, true, false, true };

            DynamicProgrammingLarge dp = new DynamicProgrammingLarge(gondolas);
            Assert.AreEqual(new BigFraction(12, 64), dp.P( 0, 3));

            gondolas = new bool[] { false, true, false, true, false };
            Assert.AreEqual(new BigFraction(12, 64), dp.P( 4, 2));
        }

        [Test]
        public void TestCompare()
        {
            int trials = 100;
            Random r = new Random(3);

            for(int t = 0; t < trials; ++t)
            {
                bool[] gondolas = new bool[7];
 
                for(int i = 0; i < gondolas.Length; ++i)
                {
                    gondolas[i] = r.Next(2) == 1 ? true : false;
                }
                int start = r.Next(0, gondolas.Length);
                int stop = r.Next(0, gondolas.Length);

                gondolas[start] = false;
                gondolas[stop] = false;

                DynamicProgrammingLarge dp = new DynamicProgrammingLarge(gondolas);
                Logger.LogTrace("Gondolas {} start {} stop {}", gondolas.ToCommaString(), start, stop);
                Assert.AreEqual(Wheel.P_bruteForce(gondolas, start, stop), dp.P(start, stop), gondolas.ToCommaString());
            }
        }

        [Test]
        public void TestPij_bruteforce()
        {
            bool[] gondolas = new bool[] { false, false, true, false, true };
            
            Assert.AreEqual(new BigFraction(12, 64), Wheel.P_bruteForce(gondolas, 0, 3));

            Assert.AreEqual(new BigFraction(12, 64), Wheel.P_bruteForce(gondolas, 0, 3));

            //Same thing but shifted +4
            gondolas = new bool[] { false, true, false, true, false };
            Assert.AreEqual(new BigFraction(12, 64), Wheel.P_bruteForce(gondolas, 4, 2));

            //Test base case
            Assert.AreEqual(new BigFraction(1, 1), Wheel.P_bruteForce(new bool[] { true, true, false, true },
                3, 2));
        }

        [Test]
        public void TestPijk_bruteForce()
        {
            bool[] gondolas = new bool[] { false, false, true, false, true };
            

            BigFraction sum = 0;
            //Test that Pijk sums up to Pij
            for(int k = 0; k < 3; ++k)
            {
                sum += Wheel.P_bruteForce(gondolas, 0,3,k);
            }

            Assert.AreEqual(new BigFraction(12, 64), sum);

        }

        [Test]
        public void TestPijk_bruteForce2()
        {
            bool[] gondolas = new bool[] { false, true, false, false, true, false, false };
            //False, True, False, False, True, False, False [6 to 2] k=0

            Assert.AreEqual(new BigFraction(4, 4*4*4), Wheel.P_bruteForce(gondolas, 6, 2, 0));
        }

        [Test]
        public void TestEI_bruteForce()
        {
            bool[] gondolas = new bool[] { false, true, false, false, true, false, false };
            //False, True, False, False, True, False, False [6 to 2] k=0

            Assert.AreEqual(new BigFraction(18+19+20+21, 4 * 4 * 4), Wheel.E_bruteForce(gondolas, 6, 2, 0,gondolas.Length));
        }

        
    }
}
