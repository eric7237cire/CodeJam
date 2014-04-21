using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PascalsTriangle;

namespace UnitTest1B
{
    [TestClass]
    public class TestPascals
    {
        [TestMethod]
        public void testBasic()
        {
            PascalsTriangle<int> basic = PascalTriangleCreator.createNormal(10);

            Assert.AreEqual(1, basic[0, 0]);
            
            Assert.AreEqual(1, basic[1, 0]);
            Assert.AreEqual(1, basic[1, 1]);

            Assert.AreEqual(1, basic[2, 0]);
            Assert.AreEqual(2, basic[2, 1]);
            Assert.AreEqual(1, basic[2, 2]);

            Assert.AreEqual(1, basic[3, 0]);
            Assert.AreEqual(3, basic[3, 1]);
            Assert.AreEqual(3, basic[3, 2]);
            Assert.AreEqual(1, basic[3, 3]);

            Assert.AreEqual(1, basic[4, 0]);
            Assert.AreEqual(4, basic[4, 1]);
            Assert.AreEqual(6, basic[4, 2]);
            Assert.AreEqual(4, basic[4, 3]);
            Assert.AreEqual(1, basic[4, 4]);
        }

        const double tolerance = 1e-7;


        [TestMethod]
        public void testProb()
        {
            PascalsTriangle<double> prob = PascalTriangleCreator.createProb(10);

            Assert.AreEqual(1, prob[0, 0], tolerance);

            Assert.AreEqual(.5, prob[1, 0], tolerance);
            Assert.AreEqual(.5, prob[1, 1], tolerance);

            Assert.AreEqual(.25, prob[2, 0], tolerance);
            Assert.AreEqual(.5, prob[2, 1], tolerance);
            Assert.AreEqual(.25, prob[2, 2], tolerance);

            Assert.AreEqual(1d / 8, prob[3, 0], tolerance);
            Assert.AreEqual(3d / 8, prob[3, 1], tolerance);
            Assert.AreEqual(3d / 8, prob[3, 2], tolerance);
            Assert.AreEqual(1d / 8, prob[3, 3], tolerance);

            Assert.AreEqual(1d / 16, prob[4, 0], tolerance);
            Assert.AreEqual(4d / 16, prob[4, 1], tolerance);
            Assert.AreEqual(6d / 16, prob[4, 2], tolerance);
            Assert.AreEqual(4d / 16, prob[4, 3], tolerance);
            Assert.AreEqual(1d / 16, prob[4, 4], tolerance);
        }
    }
}
