using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Utils.geom;


namespace UnitTest
{
    [TestClass]
    public class TestGeom
    {
        [TestMethod]
        public void TestIntersection()
        {
            Line line1 = Line.createFromPoints( new Point<double>(1d,1d),
                 new Point<double>(3d,2d));
            Line line2 = Line.createFromPoints(
                  new Point<double>(1d, 4d),
                   new Point<double>(2d, -1d));

            Point<double> pt = line1.intersection(line2);

            Assert.AreEqual( 17d / 11, pt.X, 1e-5);
            Assert.AreEqual(14d / 11, pt.Y, 1e-5);
        }
    }
}
