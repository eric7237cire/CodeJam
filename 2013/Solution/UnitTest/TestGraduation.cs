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

using Logger = Utils.LoggerFile;

namespace UnitTest
{
    [TestFixture]
    public class TestGraduation
    {
        [Test]
        public void TestNegMod()
        {
            Assert.AreEqual(-3, -103 % 20);
        }
        
        /*
       Where !!3rd back intersection is genuinte
       1st back is directly on line
       2nd car is not there
       but 3rd works
        */
        [Test]
        [Category ("current")]
        public void Test3rdBackIntersection()
        {
        	GraduationInput input = new GraduationInput();
            input.start = new long[] { 2 };
            input.stop = new long[] { 0 };
            input.timeEntered = new long[] { 1 };
            input.nIntersections = 3;
            input.totalTime = 5;	
            
            Graduation grad = new Graduation();

            List<Point<long>> best;
            Car car = new Car(input, 0);
            
            grad.getLongestSegment(2, 4, out best, car);
            //Logger.LogDebug("TESTTEST");
            Assert.AreEqual(2, best.Count);
            Assert.AreEqual(new Point<long>(1, 2), best[0]);
            Assert.AreEqual(new Point<long>(1, 5), best[1]);
        	
        }
        
        /*
        
         Tests when car intersects the starting point
        but it has not entered yet
        */
        [Test]
        [Category ("current")]
        public void TestStartSamePoint()
        {
        	GraduationInput input = new GraduationInput();
            input.start = new long[] { 0 };
            input.stop = new long[] { 1 };
            input.timeEntered = new long[] { 2 };
            input.nIntersections = 5;
            input.totalTime = 3;	
            
            Graduation grad = new Graduation();

            List<Point<long>> best;
            Car car = new Car(input, 0);
            
            grad.getLongestSegment(4, 1, out best, car);
            
            Assert.AreEqual(2, best.Count);
            Assert.AreEqual(new Point<long>(0, 0), best[0]);
            Assert.AreEqual(new Point<long>(2, 3), best[1]);
        	
        }
        
        /*
        Test where car exits before forward intersection
        */
        [Test]
        [Category ("current")]
        public void TestForwardExitBefore()
        {
        	GraduationInput input = new GraduationInput();
            input.start = new long[] { 4 };
            input.stop = new long[] { 1 };
            input.timeEntered = new long[] { 0 };
            input.nIntersections = 5;
            input.totalTime = 3;	
            
            Graduation grad = new Graduation();

            List<Point<long>> best;
            Car car = new Car(input, 0);
            
            grad.getLongestSegment(4, 1, out best, car);
            
            Assert.AreEqual(2, best.Count);
            Assert.AreEqual(new Point<long>(4, 1), best[0]);
            Assert.AreEqual(new Point<long>(2, 3), best[1]);
        	
        }

        /*
        Test finding the best stopping point that does not intersect
        other cars
        */
        [Test]
        [Category ("current")]
        public void TestForward()
        {
        	GraduationInput input = new GraduationInput();
            input.start = new long[] { 2 };
            input.stop = new long[] { 6 };
            input.timeEntered = new long[] { 4 };
            input.nIntersections = 10;
            input.totalTime = 10;	
            
            
            Graduation grad = new Graduation();

            List<Point<long>> best;
            Car car = new Car(input, 0);
            
            grad.getLongestSegment(5, 1, out best, car);

            Assert.AreEqual(2, best.Count);
            Assert.AreEqual(new Point<long>(6, 0), best[0]);
            //Would intersect with beginning of car at 2, 4
            Assert.AreEqual(new Point<long>(3, 3), best[1]);
            
            
            grad.getLongestSegment(4, 1, out best, car);

            Assert.AreEqual(2, best.Count);
            //No intersections at all
            Assert.AreEqual(new Point<long>(5, 0), best[0]);            
            Assert.AreEqual(new Point<long>(5, 10), best[1]);
            
            
            grad.getLongestSegment(3, 1, out best, car);
            //No intersections
            Assert.AreEqual(2, best.Count);
            Assert.AreEqual(new Point<long>(4, 0), best[0]);            
            Assert.AreEqual(new Point<long>(7, 7), best[1]);
        	
        }
        
        /*
        Test finding the best starting point that does not intersect
        other cars.
        
        Uses 3 points only differening by position
        */
        
        [Test]
        public void TestBackward()
        {
        	GraduationInput input = new GraduationInput();
            input.start = new long[] { 2 };
            input.stop = new long[] { 6 };
            input.timeEntered = new long[] { 4 };
            input.nIntersections = 10;
            input.totalTime = 10;	
            
            
            Graduation grad = new Graduation();

            List<Point<long>> best;
            Car car = new Car(input, 0);
            
            grad.getLongestSegment(4, 9, out best, car);
            
            Assert.AreEqual(2, best.Count);
            Assert.AreEqual(new Point<long>(5, 8), best[0]);            
            Assert.AreEqual(new Point<long>(3, 10), best[1]);
            
            grad.getLongestSegment(5, 9, out best, car);
            
            Assert.AreEqual(2, best.Count);
            Assert.AreEqual(new Point<long>(5, 9), best[0]);            
            Assert.AreEqual(new Point<long>(4, 10), best[1]);
            
            grad.getLongestSegment(6, 9, out best, car);
            
            //No intersections
            Assert.AreEqual(2, best.Count);
            Assert.AreEqual(new Point<long>(5, 0), best[0]);            
            Assert.AreEqual(new Point<long>(5, 10), best[1]);
        }

        [Test]
        [Category("current")]
        /*we intersect with a car going both forward and backward from
         * the query point
         */
            
        public void TestBeforeAndAfterIntersection()
        {
            GraduationInput input = new GraduationInput();
            input.start = new long[] { 3 };
            input.stop = new long[] { 2 };
            input.timeEntered = new long[] { 0 };
            input.nIntersections = 5;
            input.totalTime = 100000;

            Graduation grad = new Graduation();

            List<Point<long>> best;
            Car car = new Car(input, 0);
            
            grad.getLongestSegment(2, 1, out best, car);

            Assert.AreEqual(2, best.Count);

            Assert.AreEqual(new Point<long>(2, 1), best[0]);
            Assert.AreEqual(new Point<long>(1, 2), best[1]);
        }
    }
}
