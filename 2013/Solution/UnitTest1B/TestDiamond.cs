using CodeJamUtils;
using Diamonds;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UnitTest1B
{
    [TestClass]
    public class TestDiamond
    {
        const double tolerance = 1e-7;

        private void runTest(String data, double expectedResult)
        {
            StringReader sr = new StringReader(data);
            Input input = Input.createInput(new Scanner(sr));

            Diamond d = new Diamond();
            double result = d.processInput(input);

            Assert.AreEqual(expectedResult, result, tolerance);
        }

        [TestMethod]
        public void TestSample1()
        {
            //runTest("1 0 0", 1);
           // runTest("1 0 2", 0);
           // runTest("3 0 0", 1);

            runTest("3 2 0", .75);
            runTest("3 1 1", .25);
            runTest("4 1 1", .5);
            runTest("4 0 2", 0);
            
        }
        [TestMethod]
        public void TestDiamondInfo()
        {
            DiamondInfo df = DiamondInfo.getDiamondInfo(1);
            Assert.AreEqual(1, df.baseDiamondSize);
            Assert.AreEqual(1, df.N);
            Assert.AreEqual(2, df.sideLength);
            Assert.AreEqual(2, df.outerLayerDistance);

            df = DiamondInfo.getDiamondInfo(2);
            Assert.AreEqual(1, df.baseDiamondSize);
            Assert.AreEqual(2, df.N);
            Assert.AreEqual(2, df.sideLength);

            df = DiamondInfo.getDiamondInfo(8);
            Assert.AreEqual(6, df.baseDiamondSize);
            Assert.AreEqual(8, df.N);
            Assert.AreEqual(4, df.sideLength);
            Assert.AreEqual(4, df.outerLayerDistance);

            df = DiamondInfo.getDiamondInfo(18);
            Assert.AreEqual(15, df.baseDiamondSize);
            Assert.AreEqual(18, df.N);
            Assert.AreEqual(6, df.sideLength);
            Assert.AreEqual(6, df.outerLayerDistance);

            df = DiamondInfo.getDiamondInfo(1000000);
            Assert.AreEqual(1000000, df.N);
        }

        private IDictionary<Node, double> getNodeProb(int n)
        {
            DiamondInfo di = DiamondInfo.getDiamondInfo(n);
            IDictionary<Node, double> nodeProb;

            Diamond d = new Diamond();
            d.processProb(di, out nodeProb);

            return nodeProb;
        }

        [TestMethod]
        public void TestDiamondProb2()
        {

            IDictionary<Node, double> nodeProb = getNodeProb(2);
            Assert.AreEqual(2, nodeProb.Count);

            testNode(1, 0, .5, nodeProb);
            testNode(0, 1, .5, nodeProb);

        }

        [TestMethod]
        public void TestDiamondProb3()
        {
            IDictionary<Node, double> nodeProb = getNodeProb(3);

            Assert.AreEqual(3, nodeProb.Count);

            testNode(2, 0, .25, nodeProb);
            testNode(1, 1, .5, nodeProb);
            testNode(0, 2, .25, nodeProb);

        }

        [TestMethod]
        public void TestDiamondProb4()
        {
            IDictionary<Node, double> nodeProb = getNodeProb(4);
            
            Assert.AreEqual(2, nodeProb.Count);

            testNode(2, 1, .5, nodeProb);
            testNode(1, 2, .5, nodeProb);

        }


        [TestMethod]
        public void TestDiamondProb5()
        {
            IDictionary<Node, double> nodeProb = getNodeProb(5);

            Assert.AreEqual(1, nodeProb.Count);

            testNode(2, 2, 1, nodeProb);

        }

        private void testNode(int left, int right, double expectedProb, IDictionary<Node, double> nodeProb)
        {
            Node n1 = new Node(left, right);
            Assert.IsTrue(nodeProb.ContainsKey(n1));

            double prob = nodeProb[n1];
            

            LoggerReal.Log("p {0} {1} {2}", expectedProb, prob, tolerance);
            Assert.AreEqual(expectedProb, prob, tolerance, "Tolerance: " + tolerance);
            
        }
    }
}
