#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif

using System;
using NUnit;
using NUnit.Framework;

using RoundFinal;
using Utils.geom;
using System.Collections.Generic;
using Utils;

using Logger = Utils.LoggerFile;

namespace UnitTest
{
	using Point = Utils.geom.Point<int>;
	using IP_Pair = Tuple<int, Utils.geom.Point<int>>;
	
    [TestFixture]
    public class TestXSpot
    {
    	[Test]
    	public void TestGenTestSet()
    	{
    		List<Point> points = XSpot.generateTestSet(50, 0, 20);
    		
    		Logger.LogTrace("Test set:\n{}", points.ToCommaString());
    		
    		Point testLine = new Point(10, 9);
    		
    		List<IP_Pair> withCP = new List<IP_Pair>();
    		
    		foreach(Point pt in points)
    		{
    			int cp = pt.CrossProduct(testLine);
    			
    			//object cp = PointExt.CrossProduct(pt, testLine);
    			
    			//int cp = PointExt.CrossProduct2(pt.X, pt.Y, testLine.X, testLine.Y);
    			
    			Logger.LogTrace("Pt {}.  cp {} type {}", pt, cp, cp.GetType());
    			withCP.Add( new IP_Pair( cp, pt ) );	
    		}
    		
    		withCP.Sort( (lhs, rhs) => lhs.Item1.CompareTo(rhs.Item1));
    		
    		Logger.LogTrace("Sorted {}",  withCP.ToCommaString());
    		
    		/*
    		points = XSpot.generateTestSet(50, 10, 10);
    		
    		Logger.LogTrace("Test set:\n{}", points.ToCommaString());
    		
    		points = XSpot.generateTestSet(50, 0, 4);
    		
    		Logger.LogTrace("Test set:\n{}", points.ToCommaString());
    		*/
    	}
    	
        [Test]
        public void TestColinear()
        {
         	List<Point> points = new List<Point>();
         	points.Add(new Point(-17, 5));
         	points.Add(new Point(-17, 0));
         	points.Add(new Point(3 - 100*17, -93833 + 987*17));
         	
         	//3 , 4 , 5
         	points.Add(new Point(-17, -138383385));
         	points.Add(new Point(383838374, 0));         	
         	points.Add(new Point(3, -93833));
         	
         	//6 , 7 , 8
         	points.Add(new Point(-17, 0));
         	points.Add(new Point(383838373, 0));
         	points.Add(new Point(3 - 100*-45, -93833 + 987*-45));
         	
         	List<int> colinearIndexes;
         	List<int> dups;
         	
         	XSpot.FindColinear(points, out colinearIndexes, out dups);
         	
         	Logger.LogTrace("Point indexs {}", colinearIndexes.ToCommaString());
         	
         	int idx = 0;
         	Assert.AreEqual(0, colinearIndexes[idx++]);
         	Assert.AreEqual(1, colinearIndexes[idx++]);
         	Assert.AreEqual(3, colinearIndexes[idx++]);
         	
         	Assert.AreEqual(1, colinearIndexes[idx++]);
         	Assert.AreEqual(4, colinearIndexes[idx++]);
         	Assert.AreEqual(7, colinearIndexes[idx++]);
         	
         	Assert.AreEqual(2, colinearIndexes[idx++]);
         	Assert.AreEqual(5, colinearIndexes[idx++]);
         	Assert.AreEqual(8, colinearIndexes[idx++]);
         	
         	Assert.AreEqual(2, dups.Count);
         	Assert.AreEqual(1, dups[0]);
         	Assert.AreEqual(6, dups[1]);
         	
        }
        
    }
}
