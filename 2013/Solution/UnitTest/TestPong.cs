#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE

using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using CodeJamUtils;
using System.IO;
using Round2.Pong;
using Utils;

using Logger = Utils.LoggerFile;
using System.Collections.Generic;

namespace UnitTest
{
    [TestClass]
    public class TestPong
    {

        [TestMethod]
        public void TestAdd2()
        {
            for (int target = 1; target <= 5; ++target )
                test(49, 99, 100, target);


           // test(16, 85, 100, 150);
            test(99, 99, 100, target:98);

            test(47, 99, 100, 30);

            Random r = new Random(3);

            for(int i = 0; i < 5; ++i)
            {
                test(r.Next(0,301), r.Next(1, 1000), 100, r.Next(1, 100)); 
            }

           // test(0, 135, 100, 50);
          //  test(0, 65, 100, 50);

            //test(0, 26, 100, 20);
            //test(0, 26, 22, 20);
           // test(0, 26+44, 22, 20);

            //test(0, 135, 100, 10);
        }

        //Manually step through the points, making sure that the result of calcToTarget is the first point
        //with a difference > target.
        private void test(int offset, int toAdd, int height, long target)
        {
            PongMain.calcStats(offset, toAdd, height);
            long targetPoint = PongMain.calcToTarget(offset, toAdd, height, target);
            Logger.LogDebug("nPoints {}", targetPoint);
            List<int> posList = new List<int>();
            posList.Add(offset);

            bool found = false;

            for(int i = 1; i <= targetPoint; ++i)
            {
                int ans = (int)PongMain.calcAdd(offset, toAdd, i, height);

                Logger.LogInfo("{} + n {} * {} = {} : {} [diff {}] with respect to height {}",
                    offset,
                    i, toAdd, offset + i * toAdd, ans, (ans - posList.GetLastValue()), height);

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

        [TestMethod]
        public void TestAdd()
        {
            int height = 100;

            int toAddStart = 25;
            int toAddEnd = toAddStart + 15;

            int offset = 0;

            for(int toAdd = toAddStart; toAdd <= toAddEnd; ++toAdd)
            {
                PongMain.calcStats(offset, toAdd, height);
                List<int> posList = new List<int>();
                posList.Add(offset);

                for(int pNum = 0; pNum <= 30; ++pNum)
                {
                    int ans = (int) PongMain.calcAdd(0, toAdd, pNum, height);
                    
                    Logger.LogInfo("n {} * {} = {} : {} [diff {}] with respect to height {}", 
                        pNum, toAdd, pNum*toAdd, ans, (ans - posList.GetLastValue()), height);

                    posList.Add(ans);
                }
            }
        }
        
        [TestMethod]
        public void TestSmall()
        {
            string inputTxt = UnitTest.Properties.Resources.TestPong1;

            Scanner scanner = new Scanner(new StringReader(inputTxt));

            PongMain  pong = new PongMain();

            PongInput input = pong.createInput(scanner);

            string ans = pong.processInput(input);

            Assert.AreEqual("RIGHT 19393", ans);

        }
    }
}
