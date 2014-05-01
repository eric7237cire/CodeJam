using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using CodeJamUtils;
using System.IO;
using Round2.Pong;

namespace UnitTest
{
    [TestClass]
    public class TestPong
    {
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
