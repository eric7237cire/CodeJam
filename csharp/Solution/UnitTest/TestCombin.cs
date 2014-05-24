using CombPerm;
using NUnit.Framework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;

namespace UnitTest
{
    [TestFixture]
    public class TestCombinationClasses
    {
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
            foreach (List<int> list in Combinations.nextPermutationWithRepetition(3, 4))
            {
                if (i == 0) CollectionAssert.AreEqual(new int[] { 0, 0, 0 }, list, list.ToCommaString());
                if (i == 1) CollectionAssert.AreEqual(new int[] { 1, 0, 0 }, list, list.ToCommaString());
                if (i == 2) CollectionAssert.AreEqual(new int[] { 2, 0, 0 }, list, list.ToCommaString());
                if (i == 3) CollectionAssert.AreEqual(new int[] { 3, 0, 0 }, list, list.ToCommaString());
                if (i == 4) CollectionAssert.AreEqual(new int[] { 0, 1, 0 }, list, list.ToCommaString());

                ++i;
            }


            Assert.AreEqual(64, i);
        }

        [Test]
        public void TestCombin()
        {
            Assert.AreEqual(1, Combinations.factorial(0));
            //Assert.AreEqual(2598960, Combinations.combin(52, 5));
            Assert.AreEqual(1, Combinations.combin(4, 0));

        }
    }
}
