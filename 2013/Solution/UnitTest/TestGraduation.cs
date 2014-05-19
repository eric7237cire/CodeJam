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
        public void TestNegMod()
        {
            Assert.AreEqual(-3, -103 % 20);
        }



        [Test]
        public void TestCarBestSegment()
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
