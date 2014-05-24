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
using Utils.math;
using System.Collections.Generic;
using Utils;

using Logger = Utils.LoggerFile;

namespace UnitTest
{

    [TestFixture]
    public class TestCantStop
    {
        [Test]
        public void TestBestInterval()
        {
            CantStopInput input = new CantStopInput();
            input.rolls = new List<int[]>();

            input.rolls.Add(new int[] { 4, 5 });
            input.rolls.Add(new int[] { 1, 6 });
            input.rolls.Add(new int[] { 3, 8 });

            input.rolls.Add(new int[] { 2, 19 });
            input.rolls.Add(new int[] { 0, 14 });
            input.rolls.Add(new int[] { 7, 7 });

            //6 7 8
            input.rolls.Add(new int[] { 8, 14 });
            input.rolls.Add(new int[] { 3, 19 });
            input.rolls.Add(new int[] { 18, 17 });


            input.N = input.rolls.Count;
            input.k = 1;
            input.D = 2;

            int bestStart = 0, bestLength = 0;

            Logger.LogDebug("k=1");

            CantStop.bestInterval(out bestStart, out bestLength, 0, input.N - 1, input);

            Assert.AreEqual(0, bestStart);
            Assert.AreEqual(1, bestLength);

            Logger.LogDebug("k=2");

            input.k = 2;
            CantStop.bestInterval(out bestStart, out bestLength, 0, input.N - 1, input);

            Assert.AreEqual(4, bestStart);
            Assert.AreEqual(3, bestLength);
        }

        //[Test]
        public void TestExpand()
        {
            CantStopInput input = new CantStopInput();
            input.rolls = new List<int[]>();

            input.rolls.Add(new int[] { 4, 5 });            
            input.rolls.Add(new int[] { 1, 6 });
            input.rolls.Add(new int[] { 3, 8 });
            
            input.rolls.Add(new int[] { 2, 19 });
            input.rolls.Add(new int[] { 0, 14 });
            input.rolls.Add(new int[] { 7, 7 });

            //6 7 8
            input.rolls.Add(new int[] { 8, 14 });            
            input.rolls.Add(new int[] { 3, 19 });
            input.rolls.Add(new int[] { 18, 17 });

            input.N = input.rolls.Count;
            input.k = 1;
            input.D = 2;

            int bestStart=0, bestLength=0;

            Logger.LogDebug("k=1");
            CantStop.expand(ref bestStart, ref bestLength, 2, 2, new List<int>(), input);
            Assert.AreEqual(1, bestLength);
            Assert.AreEqual(2, bestStart);

            input.k = 2;
            Logger.LogDebug("k=2");
            CantStop.expand(ref bestStart, ref bestLength, 2, 2, new List<int>(), input);
            Assert.AreEqual(2, bestLength);
            Assert.AreEqual(1, bestStart);

            input.k = 3;
            Logger.LogDebug("k=3");
            CantStop.expand(ref bestStart, ref bestLength, 2, 2, new List<int>(), input);
            Assert.AreEqual(3, bestLength);
            Assert.AreEqual(0, bestStart);

            Logger.LogDebug("k=3");
            CantStop.expand(ref bestStart, ref bestLength, 5, 5, new List<int>(), input);
            Assert.AreEqual(5, bestLength);
            Assert.AreEqual(3, bestStart);

        }
    }
}