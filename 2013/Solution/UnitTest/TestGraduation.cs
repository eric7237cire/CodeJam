using System;
using NUnit;
using NUnit.Framework;

using RoundFinal;
using Utils.geom;
using System.Collections.Generic;

namespace UnitTest
{
    [TestFixture]
    public class TestGraduation
    {
        
        [Test]
        public void TestMethod1()
        {
            GraduationInput input = new GraduationInput();
            input.start = new long[] { 3 };
            input.stop = new long[] { 2 };
            input.nIntersections = 5;

            Graduation grad = new Graduation();

            List<Point<int>> intersections;
           // grad.getLongestSegment(2, 1, out intersections, input);

            //Assert.AreEqual(2, intersections.Count);

            //Assert.AreEqual(new Point<int>(2, 1), intersections[0]);
           // Assert.AreEqual(new Point<int>(1, 2), intersections[1]);
        }
    }
}
