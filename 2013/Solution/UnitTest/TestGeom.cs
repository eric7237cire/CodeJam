#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE

using System;


using Utils.geom;
using System.Collections.Generic;
using NUnit.Framework;
using Utils;
using CodeJamUtils;
using Round3;
using System.IO;

using Logger = Utils.LoggerFile;

namespace UnitTest
{
    [TestFixture]
    public class TestGeom
    {
        [Test]
        public void TestSameSide()
        {

            LineSegment<int> line = LineExt.createSegmentFromCoords(1, 1, 3, 2);

            Assert.AreEqual(true, line.sameSide(new Point<int>(1, 1), new Point<int>(2, -1)));

            Assert.AreEqual(true, line.sameSide(new Point<int>(1, 2), new Point<int>(2, 6)));

        }
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
        public void TestPolarOrder()
        {
            Assert.AreEqual(-1, PointExt.polarOrder(new Point<int>(1, 1), new Point<int>(4, 2), new Point<int>(3, 4)));

            Assert.AreEqual(-1, PointExt.polarOrder(new Point<int>(0, 0), new Point<int>(4, 2), new Point<int>(3, 4)));

            Point<int>  A = new Point<int>(2, 0);
            Point<int>  B = new Point<int>(1, 1);
            Point<int>  C = new Point<int>(1, 2);
            Assert.AreEqual(-1, PointExt.polarOrder(new Point<int>(0, 0), A,B));
            Assert.AreEqual(-1, PointExt.polarOrder(new Point<int>(0, 0), B, C));
            Assert.AreEqual(-1, PointExt.polarOrder(new Point<int>(0, 0), A, C));

            Assert.AreEqual(1, PointExt.polarOrder(new Point<int>(0, 0), B, A));
            Assert.AreEqual(1, PointExt.polarOrder(new Point<int>(0, 0), C, B));
            Assert.AreEqual(1, PointExt.polarOrder(new Point<int>(0, 0), C, A));
            
        }

        #if !mono
        [Test]
        public void TestSmall8()
        {
            testInput(Properties.Resources.TestRuralSmall8, "0 4 7 5 1 6 2 3");
        }

        private void testInput(string inputTxt, string expectedAns)
        {
            Scanner scanner = new Scanner(new StringReader(inputTxt));

            Rural pong = new Rural();

            RuralInput input = pong.createInput(scanner);

            string ans = pong.processInput(input);

            Assert.AreEqual(expectedAns, ans);

        }
        #endif

        [Test]
        [Category("current")]
        public void TestConvexHull3()
        {
            List<Point<int>> list = new List<Point<int>>();
            list.Add(new Point<int>(3, 3));
            list.Add(new Point<int>(1, 1));
            list.Add(new Point<int>(3, 1));
            list.Add(new Point<int>(3, 2));
            list.Add(new Point<int>(2, 1));
            list.Add(new Point<int>(1, 2));
            list.Add(new Point<int>(2, 2));
            list.Add(new Point<int>(1, 3));
            list.Add(new Point<int>(2, 3));
            List<Point<int>> a;
            
            a = list.ConvexHull(true);
            testConvexHull(true, list, a);
           // Assert.AreEqual(8, a.Count);

            a = list.ConvexHull(false);

            //Assert.AreEqual(4, a.Count);
            testConvexHull(false, list, a);
        }

        [Test]
        [Category("current")]
        public void TestConvexHull2()
        {
            List<Point<int>> list = new List<Point<int>>();
            list.Add(new Point<int>(1, 1));
            list.Add(new Point<int>(6, 2));
            list.Add(new Point<int>(6, 4));
            list.Add(new Point<int>(3, 3));
            list.Add(new Point<int>(2, 4));
            List<Point<int>> a = list.ConvexHull();

            
            testConvexHull(true, list, a);
            //Assert.AreEqual(4, a.Count);
        }

        [Test]
        [Category("current")]
        public void TestConvexHull()
        {
            List<Point<int>> list = new List<Point<int>>();
            list.Add(new Point<int>(1, 2));
            list.Add(new Point<int>(2, 0));
            list.Add(new Point<int>(0, 0));
            list.Add(new Point<int>(1, 1));
            List<Point<int>> a = list.ConvexHull();

            Assert.AreEqual(3, a.Count);
            
            testConvexHull(true, list, a);
        }
        
        private void testConvexHull(bool shouldIncludeLinearPoints, IList<Point<int>> pointsOrig,
        	IList<Point<int>> hull)
        {
        	Logger.LogTrace("\nTesting hull. Hull:\n{}\nPoints:\n{}",
        		hull.ToCommaString(), pointsOrig.ToCommaString());
        	
        	//First check convexness, hull should be in counter-clockwise order
        	for(int i = 0; i < hull.Count; ++i)
        	{
        		int j = (i + 1) % hull.Count;
        		int k = (i + 2) % hull.Count;
        		
        		if (shouldIncludeLinearPoints) 
        			Assert.IsTrue( PointExt.ccw( hull[i], hull[j], hull[k] ) >= 0 );
        		if (!shouldIncludeLinearPoints)
        			Assert.IsTrue( PointExt.ccw( hull[i], hull[j], hull[k] ) > 0 );
        	}
        	
        	IList<Point<int>> interiorPoints = new List<Point<int>>();
        	foreach (var p in pointsOrig)
        	{
        		if (!hull.Contains(p))
        			interiorPoints.Add(p);
        	}
        	
        	//Go through each side of the hull, checking that all interior points are on the same side
        	for(int i = 0; i < hull.Count; ++i)
        	{
        		int j = (i + 1) % hull.Count;
        		
        		//All interior points should make a counter - clockwise turn if side ij
        		for(int k = 0; k < interiorPoints.Count; ++k)
        		{
        			//OK to be co-linear since 'interior points' can be on hull
        			if (!shouldIncludeLinearPoints)
        				Assert.IsTrue( PointExt.ccw( hull[i], hull[j], interiorPoints[k]) <= 1,
        				"{0} {1} {2} not counter clockwise".FormatThis( hull[i], hull[j], interiorPoints[k] )
        				);
        				
					if (shouldIncludeLinearPoints)
						Assert.IsTrue( PointExt.ccw( hull[i], hull[j], interiorPoints[k]) == 1,
        				"{0} {1} {2} not counter clockwise".FormatThis( hull[i], hull[j], interiorPoints[k] )
        				);
        		}
        		
        	}
        	
        }
        

        [Test]        
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

            List<Point<double>> hull = points.ConvexHull();

            Assert.AreEqual(8, hull.Count, hull.ToCommaString());
        }

    }
}
