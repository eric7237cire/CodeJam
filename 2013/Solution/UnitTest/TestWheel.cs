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
        //TODO put this with a test fraction class
        [Test]
        public void TestRef()
        {
            BigFraction[] testArray = new BigFraction[3];
            BigFraction init = new BigFraction(-1, 1);

            testArray[0] = init;
            testArray[1] = init;

            Assert.IsFalse(ReferenceEquals(testArray[0], testArray[1]));
        }

        [Test]
        public void TestLargeFractionToDouble()
        {
            
            BigInteger i = 10;
            i = BigInteger.Pow(i, 304);
            BigFraction f = i;

            Assert.AreEqual(1e304, (double)f);

            Assert.AreEqual(1e40, (double)
                new BigFraction(BigInteger.Pow(10, 40),1), 1e25);

            BigFraction f2 = new BigFraction(BigInteger.Pow(10, 344), BigInteger.Pow(10, 304), false);

            double d2 = (double)f2;

            Assert.AreEqual(1e40, d2, 1e25);

            BigInteger bi1 = BigInteger.Parse("2302073370472459132074404233276820422639103254133904159801405804887478238801437662513031371866270764870331175412196858812068470158590547389959356493994877674610281421320525996469152129835615048054927204470044026763562485477222367114215047175691226850045987011995428311814345330046987446489172747558771614920732");
            BigInteger bi2 = BigInteger.Parse("17365835781473320889421419340788343549711913213696800725957886198888610396662017817843102760805884240481836360411987064468925009319623959418836710583418588884564675920797412194729605191255404903006715915289897693739972657264720773841442988736074423009480659734946421941204433341642671302399787605534020996634264");

            double expected = .132563350214817635401;
            double f3 = (double)new BigFraction(bi1, bi2);
            Assert.IsFalse(Double.IsNaN(f3));
            Assert.AreEqual(expected, f3, 1e-9);
        }


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

            Assert.AreEqual(0, WheelBruteForce.simulateForWheelFast(gondolas, new int[] { 0, 2, 0 }));
            Assert.AreNotEqual(0, WheelBruteForce.simulateForWheelFast(gondolas, new int[] { 1, 0, 0 }));

            Assert.AreNotEqual(0, WheelBruteForce.simulateForWheelFast(gondolas, new int[] { 1, 0, 3 }, 0));
            //Perm 1, 0, 3 for ij False, False, True, False. 
        }

        [Test]
        public void TestP()
        {
            bool[] gondolas = new bool[] { false, false, true, false, true };

            WheelLargeSlow dp = new WheelLargeSlow(gondolas);
            Assert.AreEqual(new BigFraction(12, 64), (BigFraction)dp.P(0, 3));

            gondolas = new bool[] { false, true, false, true, false };
            dp = new WheelLargeSlow(gondolas);

            Assert.AreEqual(new BigFraction(12, 64), (BigFraction)dp.P(4, 2));
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
                Assert.AreEqual(WheelBruteForce.P_bruteForce(gondolas, start, stop), dp.P(start, stop), gondolas.ToCommaString());
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
