#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif

using System;
using NUnit;
using NUnit.Framework;

using RoundFinal_2013.Problem3;
using Utils.geom;
using Utils.math;
using System.Collections.Generic;
using Utils;

using Logger = Utils.LoggerFile;

namespace UnitTest
{
	using Point = Utils.geom.Point<int>;
	using IP_Pair = Tuple<int, Utils.geom.Point<int>>;
	
	using Frac = Utils.math.Fraction;
	
    [TestFixture]
    public class TestXSpot
    {
    	[Test]
    	public void TestSmallAngle()
    	{
    		
    		BigFraction bf = BigFraction.createFromDouble(-0.499999999999875, 9);
    		Assert.IsTrue( bf < 0, bf.ToString());
    		
    		List<Point> points = new List<Point>();
    		points.Add(new Point(1, 1000000));
    		points.Add(new Point(1, 1000000-1));
    		points.Add(new Point(0, -1000000));
    		points.Add(new Point(0, -999999));
    		
    		LineSegment<double> splitLine1;
    		LineSegment<double> splitLine2;
    		
    		double angle = Math.Atan2( 2000000-1, 1 );
    		
    		int checkX = XSpot.FindX(points, angle, out splitLine1, out splitLine2);
    		
    		Assert.AreEqual(1, checkX);
    		
    		Logger.LogTrace("Lines {} {}", splitLine1.line, splitLine2.line);
    		Logger.LogTrace("Line slopes {} {}", -splitLine1.line.A / splitLine1.line.B, 
    			-splitLine2.line.A / splitLine2.line.B);
    		
    		

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
