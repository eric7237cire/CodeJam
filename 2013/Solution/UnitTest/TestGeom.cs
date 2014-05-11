using System;

using Utils.geom;
using System.Collections.Generic;
using NUnit.Framework;
using Utils;

namespace UnitTest
{
    [TestFixture]
    public class TestGeom
    {
        [Test]
        public void TestIntersection()
        {
            Line<double> line1 = LineExt.createFromPoints(new Point<double>(1d, 1d),
                 new Point<double>(3d,2d));
            Line<double> line2 = LineExt.createFromPoints(
                  new Point<double>(1d, 4d),
                   new Point<double>(2d, -1d));

            Point<double> pt = line1.intersection(line2);

            Assert.AreEqual( 17d / 11, pt.X, 1e-5);
            Assert.AreEqual(14d / 11, pt.Y, 1e-5);
        }

        [Test]
        [Category("current")]
        public void TestPolygon()
        {
            List<Point<double>> points = new List<Point<double>>();

            /*
             * <FreePoint Name="A" Style="1" X="16" Y="27" />
    <FreePoint Name="C" Style="1" X="36" Y="18" />
    <FreePoint Name="D" Style="1" X="39" Y="9" />
             * 
    <FreePoint Name="E" Style="1" X="29" Y="4" />
    <FreePoint Name="F" Style="1" X="17" Y="7" />
    <FreePoint Name="G" Style="1" X="11" Y="12" />
             * 
    <FreePoint Name="H" Style="1" X="7" Y="15" />
    <FreePoint Name="I" Style="1" X="2" Y="14" />
    <FreePoint Name="J" Style="1" X="2" Y="16" />
             * 
    <FreePoint Name="K" Style="1" X="4" Y="23" />
    <FreePoint Name="L" Style="1" X="26" Y="11" />
    <FreePoint Name="M" Style="1" X="31" Y="16" />
             */
            points.Add(new Point<double>(16, 27));
            points.Add(new Point<double>(36, 18));
            points.Add(new Point<double>(39, 9));

            points.Add(new Point<double>(29, 4));
            points.Add(new Point<double>(17, 7));
            points.Add(new Point<double>(11, 12));

            points.Add(new Point<double>(7, 15));
            points.Add(new Point<double>(2, 14));
            points.Add(new Point<double>(2, 16));

            points.Add(new Point<double>(4, 23));
            points.Add(new Point<double>(26, 11));
            points.Add(new Point<double>(31, 16));

            double area = points.PolygonArea();

            Assert.AreEqual(334, area, 0.000001d);

            Stack<Point<double>> hull = points.ConvexHull();

            Assert.AreEqual(3, hull.Count, hull.ToCommaString());
        }

    }
}
