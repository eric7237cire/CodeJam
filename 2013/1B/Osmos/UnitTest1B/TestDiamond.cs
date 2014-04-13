using Diamonds;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UnitTest1B
{
    [TestClass]
    public class TestDiamond
    {
        [TestMethod]
        public void TestDiamondInfo()
        {
            DiamondInfo df = DiamondInfo.getDiamondInfo(1);
            Assert.AreEqual(1, df.baseDiamondSize);
            Assert.AreEqual(1, df.N);
            Assert.AreEqual(0, df.sideLength);

            df = DiamondInfo.getDiamondInfo(2);
            Assert.AreEqual(1, df.baseDiamondSize);
            Assert.AreEqual(2, df.N);
            Assert.AreEqual(0, df.sideLength);

            df = DiamondInfo.getDiamondInfo(8);
            Assert.AreEqual(6, df.baseDiamondSize);
            Assert.AreEqual(8, df.N);
            Assert.AreEqual(2, df.sideLength);

            df = DiamondInfo.getDiamondInfo(18);
            Assert.AreEqual(15, df.baseDiamondSize);
            Assert.AreEqual(18, df.N);
            Assert.AreEqual(4, df.sideLength);
        }
    }
}
