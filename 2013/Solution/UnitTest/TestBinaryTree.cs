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

        private static void setMinimum(int parentData, ref int childData)
        {
            childData = Math.Max(childData, parentData);
        }

        [TestMethod]
        public void setPercolateUp()
        {
            BinaryTree<int> bt = BinaryTree<int>.create(7);
            bt.ApplyParentDataFunc = TestBinaryTree.setMinimum;
            bt.ApplyLeftRightDataFunc = (int lhs, int rhs, ref int cur) => {
                cur = Math.Max(cur, Math.Max(lhs, rhs));
            };

            int minToSet = 0;
            BinaryTree<int>.ProcessDelegate setMinimum = (int startEndpointIndex, int stopEndPointIndex, ref int data) =>
            {
                Logger.Log("SetMinimum {0} to {1} data {2}.  minToSet {3}", startEndpointIndex, stopEndPointIndex, data, minToSet);
                data = Math.Max(minToSet, data);
            };



            Boolean anyLower = false;
            int toFind = 3;
            BinaryTree<int>.ProcessDelegate isAnyLower = (int startEndpointIndex, int stopEndPointIndex, ref int data) =>
            {
                Logger.Log("Find Lower {0} to {1} data {2}.  query {3}", startEndpointIndex, stopEndPointIndex, data, toFind);
                if (data < toFind)
                {
                    anyLower = true;
                }

            };

            anyLower = false; toFind = 0;

            
            minToSet = 3;
            bt.traverse(0, 3, setMinimum);
            bt.traverse(3, 7, setMinimum);

            anyLower = false; toFind = 3;
            bt.traverse(0, 7, isAnyLower);
            Assert.AreEqual(false, anyLower);

            //Logger.CurrentDomain_ProcessExit(null, null);
        }

        [TestMethod]
        public void setMinimums()
        {
            BinaryTree<int> bt = BinaryTree<int>.create(7);
            bt.ApplyParentDataFunc = TestBinaryTree.setMinimum;

            int minToSet = 0;
            BinaryTree<int>.ProcessDelegate setMinimum = (int startEndpointIndex, int stopEndPointIndex, ref int data) =>
                {
                    Logger.Log("SetMinimum {0} to {1} data {2}.  minToSet {3}", startEndpointIndex, stopEndPointIndex, data, minToSet);
                    data = Math.Max(minToSet, data);
                };

            

            Boolean anyLower = false;
            int toFind = 3;
            BinaryTree<int>.ProcessDelegate isAnyLower = (int startEndpointIndex, int stopEndPointIndex, ref int data) =>
            {
                Logger.Log("Find Lower {0} to {1} data {2}.  query {3}", startEndpointIndex, stopEndPointIndex, data, toFind);
                if (data < toFind)
                {
                    anyLower = true;
                }
                
            };

            anyLower = false; toFind = 0;

            bt.traverse(1, 2, isAnyLower);
            Assert.AreEqual(false, anyLower);

            anyLower = false; toFind = 1;
            bt.traverse(1, 2, isAnyLower);
            Assert.AreEqual(true, anyLower);

            minToSet = 1;
            bt.traverse(1, 4, setMinimum);

            anyLower = false; toFind = 1;
            bt.traverse(1, 2,  isAnyLower);
            Assert.AreEqual(false, anyLower);

            minToSet = 3;
            bt.traverse(2, 5, setMinimum);

            anyLower = false; toFind = 3;
            bt.traverse(4, 5, isAnyLower);
            Assert.AreEqual(false, anyLower);

            anyLower = false; toFind = 3;
            bt.traverse(5,6, isAnyLower);
            Assert.AreEqual(true, anyLower);
            //Logger.CurrentDomain_ProcessExit(null, null);
        }
        [TestMethod]
        public void Traverse()
        {
            Logger.Log("go");
            string s = Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location);
            BinaryTree<int> bt = BinaryTree<int>.create(7);

            bt.traverse(1, 4, process);
            
        }

        [TestMethod]
        public void Traverse2()
        {
            Logger.Log("go");
            string s = Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location);
            BinaryTree<int> bt = BinaryTree<int>.create(7);

            bt.traverse(0, 3, process);
        }

        

        public static void process(int startEndpointIndex, int stopEndPointIndex, ref int data)
        {
            Logger.Log("Process {0} to {1} data {2}", startEndpointIndex, stopEndPointIndex, data);
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
