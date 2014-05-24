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
    public class TestMath
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
                new BigFraction(BigInteger.Pow(10, 40), 1), 1e25);

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
        public void TestModdedLongArithmetic()
        {
            ModdedLong m = new ModdedLong(2, 3);
            long test = m + 2 - 1;
            Assert.AreEqual(0, test);

            //Was an interesting bug where m was being implicity converted to an int first
            test = m + 2L - 1L;
            Assert.AreEqual(0, test);
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
    }
}
