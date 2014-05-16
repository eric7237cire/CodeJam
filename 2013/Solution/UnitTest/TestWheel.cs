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
        public void TestPijk()
        {
            bool[] gond = new bool[20];

            BigFraction ans = Wheel.P(gond, 1, 4, 1);
            Assert.AreEqual(new BigFraction(1, 6), ans);
        }

        [Test]
        public void TestCombinationIterator()
        {
            List<long> l = new List<long>(Combinations.iterateCombin(4, 1));
            Assert.AreEqual(4, l.Count);
            Assert.AreEqual(1, l[0]);
            Assert.AreEqual(2, l[1]);
            Assert.AreEqual(4, l[2]);
            Assert.AreEqual(8, l[3]);
        }

        [Test]
        public void TestPermutationWithRep()
        {
            int i = 0;
            foreach(List<int> list in Combinations.nextPermutationWithRepetition(3, 4))
            {
                if (i==0) CollectionAssert.AreEqual(new int[] { 0, 0, 0 }, list, list.ToCommaString());
                if (i == 1) CollectionAssert.AreEqual(new int[] { 1, 0, 0 }, list, list.ToCommaString());
                if (i == 2) CollectionAssert.AreEqual(new int[] { 2, 0, 0 }, list, list.ToCommaString());
                if (i == 3) CollectionAssert.AreEqual(new int[] { 3, 0, 0 }, list, list.ToCommaString());
                if (i == 4) CollectionAssert.AreEqual(new int[] { 0, 1, 0 }, list, list.ToCommaString());

                ++i;
            }
            

            Assert.AreEqual(64, i);
        }
    }
}
