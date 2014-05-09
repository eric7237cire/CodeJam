#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE

using System;
using NUnit.Framework;
using CodeJamUtils;
using System.IO;
using Round2.Pong;
using Utils;

using Logger = Utils.LoggerFile;
using System.Collections.Generic;
using Utils.math;

/*
 *  2 questions, does the cycle hit the max diff without fail
 *  
 * 2: for extremely short cycles
 * 
 * delta-diff = 2 * (height - maxdiff) ?
 * 
 * 99, h: 100 -- delta diff = 2
 * 
 */

namespace UnitTest
{
    [TestFixture]
    public class TestModMath
    {
        [Test]
        public void TestGCD()
        {
            
            int a = 240;
            int b = 46;
            int[] ans = ModdedLong.extendedEuclid(a, b);

            Assert.AreEqual(ans[1] * a + ans[2] * b, ans[0]);

        }

        [Test]
        public void TestModInverse()
        {
            int mod = 17;

            for(int a = 1; a < 1000; ++a)
            {
                if (ModdedLong.gcd_recursive(a, mod) != 1)
                    continue;

                int modInv = ModdedLong.modInverse(a, mod);
                long modInv2 = ModdedLong.mod_inverse_recursive(a, mod);
                Logger.LogTrace("a: {} modInv: {}  recursively {}", a, modInv, modInv2);
                Assert.AreEqual(1, (a * modInv) % mod);
            }

        }

       
    }
    [TestFixture] 
    public class TestPong
    {
        [Test]
        [Category("current")]
        public void testFirstHit()
        { 
             
             
            int modulus = 20;
            int deltaP = 17;
            int a = 6;
            int b = 10;
            long firstHit = PongMain.ericFirstHit(deltaP,modulus,a,b);
             
            testFirstHitHelper(modulus,deltaP, a,b,(int)firstHit);

            //exact hit on first cycle
            a = 5; b = 5;

            firstHit = PongMain.ericFirstHit(deltaP, modulus, a, b);
            testFirstHitHelper(modulus, deltaP, a, b, (int)firstHit);

            a = 15; b = 16;

            firstHit = PongMain.ericFirstHit(deltaP, modulus, a, b);
            testFirstHitHelper(modulus, deltaP, a, b, (int)firstHit);

            /*first hit deltaP 10 modulus 374  a 239 b 244
                Δp = 10
                bestIndex 24 best 240
                Returning 24*/
            modulus = 374;
            a = 239;
            b = 244;
            deltaP = 10;
            firstHit = PongMain.ericFirstHit(deltaP, modulus, a, b);
            testFirstHitHelper(modulus, deltaP, a, b, (int)firstHit);

            //deltaP 374 modulus 758  a 504 b 509
            modulus = 758;
            a = 504;
            b = 509;
            deltaP = 374;
            firstHit = PongMain.ericFirstHit(deltaP, modulus, a, b);
            testFirstHitHelper(modulus, deltaP, a, b, (int)firstHit);

            //384 modulus 758  a 249 b 254

            modulus = 758;
            a = 249;
            b = 254;
            deltaP = 384;
            firstHit = PongMain.ericFirstHit(deltaP, modulus, a, b);
            testFirstHitHelper(modulus, deltaP, a, b, (int)firstHit, false);
            

            //first hit deltaP 758 modulus 1890  a 1007 b 1012
            modulus = 1890;
            a = 1007;
            b = 1012;
            deltaP = 758;
            firstHit = PongMain.ericFirstHit(deltaP, modulus, a, b);
            testFirstHitHelper(modulus, deltaP, a, b, (int)firstHit, false);

            //First hit deltaP 1890 modulus 40448  a 2768 b 2773
            modulus = 40448;
            a = 2768;
            b = 2773;
            deltaP = 1890;
            firstHit = PongMain.ericFirstHit(deltaP, modulus, a, b);
            testFirstHitHelper(modulus, deltaP, a, b, (int)firstHit, false);

            modulus = 2 * 142513;
            deltaP = 244578;
            a = 142513 - 16511;
            b = 142513 + 5 - 16511;

            firstHit = PongMain.ericFirstHit(deltaP, modulus, a, b);
            testFirstHitHelper(modulus, deltaP, a, b, (int)firstHit);

        }

        private void testFirstHitHelper(int modulus, long deltaP,
             int lower, int upper, int firstHitExpected, bool logEverything = false)
        {
            for (long pIdx = 0; pIdx < firstHitExpected; ++pIdx)
            {
                long p = (deltaP * pIdx) % modulus;

                if (logEverything)
                {
                    Logger.LogTrace("testFirstHitHelper pIdx: [{}] p: [{}] deltaP: [{}] lower: {} upper: {}",
                        pIdx, p, deltaP, lower, upper);
                }
                if (!(p < lower || p > upper))
                {
                    Logger.LogTrace("testFirstHitHelper failure! pIdx: [{}] p: [{}] deltaP: [{}] lower: {} upper: {}",
                        pIdx, p, deltaP, lower, upper);
                }
                Assert.IsTrue(p < lower || p > upper);                

            }

            long finalP = (deltaP * firstHitExpected) % modulus;
            Logger.LogTrace("Final point is {} = {}", firstHitExpected, finalP);

            if (!(finalP >= lower && finalP <= upper))
            {
                for (long pIdx = firstHitExpected; pIdx < firstHitExpected+10000; ++pIdx)
                {
                    long p = (deltaP * pIdx) % modulus;

                    if (logEverything)
                    {
                        Logger.LogTrace("testFirstHitHelper pIdx: [{}] p: [{}] deltaP: [{}] lower: {} upper: {}",
                            pIdx, p, deltaP, lower, upper);
                    }

                    if (!(p < lower || p > upper))
                    {
                        Logger.LogTrace("testFirstHitHelper found real pIdx: [{}] p: [{}] deltaP: [{}] lower: {} upper: {}",
                            pIdx, p, deltaP, lower, upper);
                        break;
                    }                    

                }
            }
            Assert.IsTrue(finalP >= lower && finalP <= upper);
        }

    	[Test]
    	
    	public void testForbiddenInterval()
    	{
    		int height = 12;
    		
    		int diffStop = 3 * height;
    		
    		for(int diff = 1; diff <= diffStop; ++diff)
    		{
    			for(int targetDiff = 1; targetDiff <= height; ++targetDiff)
    			{
    				BigFraction lower, upper;
    				bool found = PongMain.calculateForbiddenInterval(targetDiff,
    					diff, height, out lower, out upper);
    				
    				Logger.LogTrace("testForbiddenInterval deltaP {} target {} lower {} upper {} found {}", 
    					diff, targetDiff, lower, upper, found);
    		
    				testForbiddenIntervalHelper(height, diff, targetDiff,  lower,
    					 upper, found);
    			}
    		}
    		
    	}
    	
    	/*
    	Tests manually that should their be a prohibited interval, that
    	the difference between 2 values in the sequence is > a target difference.
    	*/
    	private void testForbiddenIntervalHelper(int height, int deltaP, 
    		int target, BigFraction lower, BigFraction upper, bool found)
    	{
    		for(int p0 = 0; p0 <= 3 * height; ++p0)
    		{
    			int p0_adj = (int) PongMain.calcAdd(p0, deltaP, 0, height);
    			int p1 = (int) PongMain.calcAdd(p0, deltaP, 1, height);
    			
    			int diff = Math.Abs(p0_adj-p1);
    			
    			Logger.LogTrace("testForbiddenIntervalHelper p0 [{}] p1 [{}] diff [{}] target {} lower {} upper {}",
    				p0, p1, diff, target, lower, upper);
    			
    			int p0_mh = p0 % height;
    			
    			if (!found)
    			{
    				Assert.IsTrue(diff <= target);
    				
    			} else if (p0_mh < lower) 
    			{
    				Assert.IsTrue(diff > target);	
    			} else if (p0_mh > upper) {
    				Assert.IsTrue(diff > target);	
    			} else {
    				Assert.IsTrue(diff <= target);	
    			}
    			
    		}
    		
    	}
    	
        [Test]
        public void testFrac()
        {
            BigFraction deltaP = 0;
            BigFraction height = 100;

            long rem = (long)(deltaP / (2 * height));

            Assert.IsTrue(new BigFraction(19, 1) >= new BigFraction(19, 1));
        }
        [Test]
        public void TestCalcToTargetUsingRand()
        {
            for (int target = 1; target <= 5; ++target )
                testCalcToTarget(49, 99, 100, target);


           // test(16, 85, 100, 150);
            testCalcToTarget(99, 99, 100, target:98);

            testCalcToTarget(47, 99, 100, 30);

            Random r = new Random(3);

            for(int i = 0; i < 500; ++i)
            {
                testCalcToTarget(r.Next(0,301), r.Next(1, 1000), 100, r.Next(1, 100)); 
            }

           // test(0, 135, 100, 50);
          //  test(0, 65, 100, 50);

            //test(0, 26, 100, 20);
            //test(0, 26, 22, 20);
           // test(0, 26+44, 22, 20);

            //test(0, 135, 100, 10);
        }

        /*
        [Test]
        public void TestCalcToTargetUsingOffset()
        {
            int[] toAddToTest = new int[] {97};
            Random r = new Random(3);

            for(int offset = 0; offset <= 400; ++offset)
            {
                foreach(int toAdd in toAddToTest)
                {
                    testCalcToTargetUsingOffset(offset, toAdd, 100, r.Next(0, toAdd + 1));
                }
            }
            
        }*/
        

        //Manually step through the points, making sure that the result of calcToTarget is the first point
        //with a difference > target.
        private void testCalcToTarget(int p0, int toAdd, int height, long target)
        {
            
            long targetPoint = PongMain.calcToTarget(p0, toAdd, height, target);
            Logger.LogDebug("Testing.  Should get > {} in nPoints {}", target, targetPoint);

            if (targetPoint < 0)
                return;

            List<int> posList = new List<int>();
            posList.Add( (int) PongMain.calcAdd(p0, toAdd, 0, height));

            bool found = false;

            for(int i = 1; i <= targetPoint; ++i)
            {
                int ans = (int)PongMain.calcAdd(p0, toAdd, i, height);

                Logger.LogInfo("{} + n {} * {} = {} : {} [diff {}] with respect to height {}",
                    p0,
                    i, toAdd, p0 + i * toAdd, ans, (ans - posList.GetLastValue()), height);

                int diff = Math.Abs(ans - posList.GetLastValue());
                if (diff > target)
                {
                    Assert.AreEqual(targetPoint, i);
                    found = true;
                }
                posList.Add(ans);
            }

            Assert.AreEqual(true, found);
        }
/*
        private void testCalcToTargetUsingOffset(int p0, int toAdd, int height, long target)
        {
            long targetPoint = (long) PongMain.calcToTargetUsingOffset(p0, toAdd, height, target);
            Logger.LogDebug("Testing.  Should get > {} in nPoints {}", target, targetPoint);

            if (targetPoint < 0)
                return;

            List<int> posList = new List<int>();
            posList.Add((int)PongMain.calcAdd(p0, toAdd, 0, height));

            bool found = false;

            for (int i = 1; i <= targetPoint; ++i)
            {
                int ans = (int)PongMain.calcAdd(p0, toAdd, i, height);

                Logger.LogInfo("{} + n {} * {} = {} : {} [diff {}] with respect to height {}",
                    p0,
                    i, toAdd, p0 + i * toAdd, ans, (ans - posList.GetLastValue()), height);

                int diff = Math.Abs(ans - posList.GetLastValue());
                if (diff > target)
                {
                    Assert.AreEqual(targetPoint, i);
                    found = true;
                }
                posList.Add(ans);
            }

            Assert.AreEqual(true, found);
        }*/

        //Just to see output
       // [Test]
        public void ShowOutput()
        {
            int height = 100;

            int toAddStart = 97;
            int toAddEnd = toAddStart + 0;

            int offset = 19;

            for(int toAdd = toAddStart; toAdd <= toAddEnd; ++toAdd)
            {
                
                List<int> posList = new List<int>();
                posList.Add(offset);

                for(int pNum = 0; pNum <= 30; ++pNum)
                {
                    int ans = (int) PongMain.calcAdd(offset, toAdd, pNum, height);
                    
                    Logger.LogInfo("{} + n {} * {} = {} : {} [diff {}] with respect to height {}", 
                        offset, pNum, toAdd, offset+pNum*toAdd, ans, (ans - posList.GetLastValue()), height);

                    posList.Add(ans);
                }
            }
        }

        //[Test]
        public void ShowChangeOffset()
        {
            
            int height = 100;

            int toAdd = 97;

            int offsetStart = 0;
            int offsetEnd = 299;
            int target = toAdd - 1;
            

           // 301537 + Δp 92205906 * N  height: 142513  target: 142506 

            /*
            int height = 142513;

            int toAdd = 142508;

            int offsetStart = height - 20;
            int offsetEnd = height + 20;
            int target = 142506;*/

            List<int> posList = new List<int>();
            posList.Add(0);
            for(int i = 0; i < 210; ++i)
            {
                int ans = (int)PongMain.calcAdd(0, toAdd, i, height);
                    Logger.LogInfo("{} + n {} * {} = {} : {} [diff {}] with respect to height {}",
                        0, i, toAdd, i * toAdd, ans, (ans - posList.GetLastValue()), height);

                    posList.Add(ans);
            }

            for (int offset = offsetStart; offset <= offsetEnd; ++offset)
            {
                Logger.LogInfo("\n\nOffset {}", offset);
                int pointsToDiff = (int) PongMain.calcToTarget(offset, toAdd, height, target);
                    
                Logger.LogInfo("Offset {}.  Points to target diff {}\n", offset, pointsToDiff);

            }
        }
        private void testInput(string inputTxt, string expectedAns)
        {
            Scanner scanner = new Scanner(new StringReader(inputTxt));

            PongMain pong = new PongMain();

            PongInput input = pong.createInput(scanner);

            string ans = pong.processInput(input);

            Assert.AreEqual(expectedAns, ans);

        }

        
        [Test]
        public void TestSample()
        {
        	#if !mono
            testInput(Properties.Resources.TestPongSample1, "LEFT 2");

            testInput(Properties.Resources.TestPongSample4, "RIGHT 11");
            #endif
        }

        [Test]
        public void TestCalcToTarget()
        {
            //calcToTarget p0 40901485 + Δp 92205906 * N  height: 142513  target: 142506 
            long man = PongMain.calcToTargetManual(40901485, 92205906, 142513, 142506);

            long man2 = PongMain.calcToTarget(40901485, 92205906, 142513, 142506);

            Assert.AreEqual(man, man2);
        }

        [Test]
        public void TestCalcToTarget2()
        {
            //24627962071 + Δp 47571681280 * N  height: 654329  target: 654320
            long test = PongMain.calcToTarget(24627962071, 47571681280, 654329, 654320);

            Assert.AreEqual(46739, test);
        }

        [Test]
        public void TestCalcToTarget3()
        {
            long test = PongMain.calcToTarget(78721099883, 122325778320, 654327, 654320);
           // long exp = PongMain.calcToTargetManual(78721099883, 122325778320, 654327, 654320);

            Assert.AreEqual(109056, test);
        }

        [Test]
        public void TestCalcToTarget4()
        {
            Assert.AreEqual(6, PongMain.calcToTargetManual(153, 97, 100, 22));

            Assert.AreEqual(7, PongMain.calcToTarget(56, 97, 100, 22));
            
            Assert.AreEqual(6, PongMain.calcToTarget(153, 97, 100, 22));
            Assert.AreEqual(5, PongMain.calcToTarget(50, 97, 100, 22));
            Assert.AreEqual(4, PongMain.calcToTarget(147, 97, 100, 22));
        }

        #if !mono
        [Test]
        public void TestSmall4()
        {
            testInput(Properties.Resources.TestPong4small, "RIGHT 165026");
        }

        [Test]
        public void TestSmall89()
        {
            testInput(Properties.Resources.TestPong89Small, "RIGHT 249996");
        }
        
        
        [Test]
        public void TestSmall()
        {
            string inputTxt = UnitTest.Properties.Resources.TestPong1;

            Scanner scanner = new Scanner(new StringReader(inputTxt));

            PongMain  pong = new PongMain();

            PongInput input = pong.createInput(scanner);

            string ans = pong.processInput(input);

            Assert.AreEqual("RIGHT 19393", ans);

        }
        #endif
    }
}
