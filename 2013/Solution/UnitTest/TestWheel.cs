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
    public class TestWheel
    {
        [Test]
        public void TestDiffs()
        {
            Assert.AreEqual(3, ModdedLong.diff(5, 0, 8));

            Assert.AreEqual(0, ModdedLong.diff(5, 5, 8));

            Assert.AreEqual(2, ModdedLong.diff(1, 3, 8));
        }
    }
}
