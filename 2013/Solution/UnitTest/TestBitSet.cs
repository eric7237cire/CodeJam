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
    public class TestBitSet
    {

        [Test]
        public void TestToString()
        {
            Assert.AreEqual("0011", 3.ToBinaryString(4));
            Assert.AreEqual("1111", 15.ToBinaryString(4));
            Assert.AreEqual("1111", 31.ToBinaryString(4));
            Assert.AreEqual("01011", 11.ToBinaryString(5));
        }
        [Test]
        public void TestBasic()
        {
            BitSet b = new BitSet(0);

            for(int i = 0; i < 31; ++i)
            {
                Assert.AreEqual(false, b[i]);
            }

            b[3] = true;
            for (int i = 0; i < 31; ++i)
            {
                Assert.AreEqual(i == 3, b[i]);
            }

            b[30] = true;
            b[0] = true;

            for (int i = 0; i < 31; ++i)
            {
                Assert.AreEqual(i == 3 || i == 30 || i == 0, b[i]);
            }

            b[30] = false;

            for (int i = 0; i < 32; ++i)
            {
                Assert.AreEqual(i == 3 || i == 0, b[i]);
            }

            b[31] = true;

            for (int i = 0; i < 32; ++i)
            {
                Assert.AreEqual(i==31 || i == 3 || i == 0, b[i]);
            }

        }

        
    }
}
