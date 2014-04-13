using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;

using Osmos;
using System.IO;
using CodeJamUtils;

namespace UnitTest1B
{
    [TestClass]
    public class TestOsmos
    {
        [TestMethod]
        public void TestMethod1()
        {
            runTest("3 1\n2", 0);
        }

        [TestMethod]
        public void TestMethod2()
        {
            /*
             


*/
            runTest("2 2\n2 1", 0);
        }

        [TestMethod]
        public void TestMethod3()
        {
            runTest("2 4\n2 1 1 6", 1);
        }

        [TestMethod]
        public void TestMethod4()
        {
            runTest("10 4\n25 20 9 100", 2);
        }

        [TestMethod]
        public void TestMethod5()
        {
            runTest("1 4\n1 1 1 1", 4);
        }

        private void runTest(String data, int expectedResult)
        {
            StringReader sr = new StringReader(data);
            Input input = new Input(new Scanner(sr));

            Osmos.Osmos osmos = new Osmos.Osmos();
            int result = osmos.processInput(input);

            Assert.AreEqual<int>(expectedResult, result);
        }
    }
}
