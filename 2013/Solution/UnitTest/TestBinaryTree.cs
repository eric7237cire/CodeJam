using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Round1C;
using System.Collections.Generic;

namespace UnitTest
{
#if (PERF)
   
    using Logger = CodeJamUtils.LoggerEmpty;
#else
    using Logger = CodeJamUtils.LoggerReal;
    using System.IO;
    using System.Reflection;
#endif
    [TestClass]
    public class TestBinaryTree
    {
        [ClassCleanup]
        public static void cleanup()
        {
            Logger.CurrentDomain_ProcessExit(null, null);
        }

        

       

        

       

        [TestMethod]
        public void TestHeight()
        {
            testHeight(0+1, 0);
            testHeight(1 + 1, 1);
            testHeight(2 + 1, 2);
            testHeight(2 + 1, 3);
            testHeight(3 + 1, 4);
            testHeight(3 + 1, 5);
            testHeight(3 + 1, 6);
            testHeight(3 + 1, 7);
            testHeight(4 + 1, 8);

        }

        [TestMethod]
        public void TestNodeIndexes()
        {
            BinaryTree<int> bt = BinaryTree<int>.create(7);

            Assert.AreEqual(0, bt.getNodeIndexForEndPointIndex(3));
            Assert.AreEqual(1, bt.getNodeIndexForEndPointIndex(1));
            Assert.AreEqual(2, bt.getNodeIndexForEndPointIndex(5));
            Assert.AreEqual(3, bt.getNodeIndexForEndPointIndex(0));
            Assert.AreEqual(4, bt.getNodeIndexForEndPointIndex(2));
            Assert.AreEqual(5, bt.getNodeIndexForEndPointIndex(4));
            Assert.AreEqual(6, bt.getNodeIndexForEndPointIndex(6));
        }

        private void testHeight(int expectedHeight, int listSize)
        {
            
            BinaryTree<int> t1 = BinaryTree<int>.create(listSize);

            Assert.AreEqual(expectedHeight, t1.Height);

        }
    }
}
