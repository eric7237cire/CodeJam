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
           // test(16, 85, 100, 150);
            test(99, 99, 100, target:98);
            

           // test(0, 135, 100, 50);
          //  test(0, 65, 100, 50);

            //test(0, 26, 100, 20);
            //test(0, 26, 22, 20);
           // test(0, 26+44, 22, 20);

            //test(0, 135, 100, 10);
        }

        private void test(int offset, int toAdd, int height, long target)
        {
            PongMain.calcStats(offset, toAdd, height);
            long nPoints = PongMain.calcToTarget(offset, toAdd, height, target);
            List<int> posList = new List<int>();
            posList.Add(offset);

            bool found = false;

            for(int i = 0; i < nPoints; ++i)
            {
                int ans = (int)PongMain.calcAdd(offset, toAdd, i, height);

                Logger.LogInfo("{} + n {} * {} = {} : {} [diff {}] with respect to height {}",
                    offset,
                    i, toAdd, offset + i * toAdd, ans, (ans - posList.GetLastValue()), height);

                int diff = ans - posList.GetLastValue();
                if (diff > target)
                {
                    Assert.AreEqual(nPoints - 1, i);
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
