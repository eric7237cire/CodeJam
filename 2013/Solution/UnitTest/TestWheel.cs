#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif 

using CombPerm;
using NUnit.Framework;
using Round3;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
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

            //Affects something beyond index 3
            Assert.AreEqual(-1, WheelBruteForce.simulateForWheelFast(gondolas, new int[] { 0, 2, 2 }));
            Assert.AreNotEqual(0, WheelBruteForce.simulateForWheelFast(gondolas, new int[] { 1, 0, 0 }));

            Assert.AreNotEqual(0, WheelBruteForce.simulateForWheelFast(gondolas, new int[] { 1, 0, 3 }, 10));
            //Perm 1, 0, 3 for ij False, False, True, False. 
        }

        [Test]
        public void TestP()
        {
            bool[] gondolas = new bool[] { false, false, true, false, true };

            WheelLargeSlow dp = new WheelLargeSlow(gondolas);
            Assert.AreEqual((double)new BigFraction(12, 64), (double)dp.P(0, 3));

            gondolas = new bool[] { false, true, false, true, false };
            dp = new WheelLargeSlow(gondolas);

            Assert.AreEqual((double)new BigFraction(12, 64), (double)dp.P(4, 2));
        }

        //Comares P(i,j) of slow large solution and brute force
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

                WheelLargeSlow dp = new WheelLargeSlow(gondolas);
                Logger.LogTrace("Gondolas {} start {} stop {}", gondolas.ToCommaString(), start, stop);
                Assert.AreEqual((double)WheelBruteForce.P_bruteForce(gondolas, start, stop), 
                	(double)dp.P(start, stop), 1e-9, gondolas.ToCommaString());
            }
        }

        [Test]
        public void TestRBruteForce()
        {
            bool[] a = new bool[] {false,true,false};
            Assert.AreEqual(new Fraction(7,1), WheelBruteForce.getE_wheelFast(a, 0, 1, 1, 7));

            Assert.AreEqual(new Fraction(13,2), WheelBruteForce.getE_wheelFast(a, 1, 2, 1, 7));
        }

        [Test]
        public void TestPij_bruteforce()
        {
            bool[] gondolas = new bool[] { false, false, true, false, true };

            Assert.AreEqual(new BigFraction(12, 64), (BigFraction)WheelBruteForce.P_bruteForce(gondolas, 0, 3));

            Assert.AreEqual(new BigFraction(12, 64), (BigFraction)WheelBruteForce.P_bruteForce(gondolas, 0, 3));

            //Same thing but shifted +4
            gondolas = new bool[] { false, true, false, true, false };
            Assert.AreEqual(new BigFraction(12, 64), (BigFraction)WheelBruteForce.P_bruteForce(gondolas, 4, 2));

            //Test base case
            Assert.AreEqual(new BigFraction(1, 1), (BigFraction)WheelBruteForce.P_bruteForce(new bool[] { true, true, false, true },
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
                sum += (BigFraction)WheelBruteForce.P_bruteForce(gondolas, 0, 3, k);
            }

            Assert.AreEqual(new BigFraction(12, 64), (BigFraction)sum);

        }

        [Test]
        public void TestPijk_bruteForce2()
        {
            bool[] gondolas = new bool[] { false, true, false, false, true, false, false };
            //False, True, False, False, True, False, False [6 to 2] k=0

            Assert.AreEqual(new BigFraction(4, 4 * 4 * 4), (BigFraction)WheelBruteForce.P_bruteForce(gondolas, 6, 2, 0));
        }


     
        
    }
}
