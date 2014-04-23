using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Round1C;
using System.Collections.Generic;

using BTi = Round1C.BinaryTree<int>;

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
       public void testBinaryTree()
        {
            BinaryTree<int> t1 = BinaryTree<int>.create(6);

            int valToSet = 0;
            BTi.ProcessDelegate setValFunc = (ref int data, int nodeIndex) => {
                data = valToSet;
            };

            valToSet = 3;
            t1.traverse(2, 4, setValFunc);

            for(int i = 0; i < 15; ++i)
            {
                int val = t1.getDataAtNodeIndex(i);

                switch (i)
                {
                    case 4: case 11:
                        Assert.AreEqual(3, val, "I is " + i);
                        break;
                    default:
                        Assert.AreEqual(0, val);
                        break;
                }
            }

            valToSet = 4;
            t1.traverse(3, 4, setValFunc);

            for (int i = 0; i < 15; ++i)
            {
                int val = t1.getDataAtNodeIndex(i);

                switch (i)
                {
                    case 10:
                    case 11:
                        Assert.AreEqual(4, val, "I is " + i);
                        break;
                    case 4:
                        Assert.AreEqual(3, val);
                        break;
                    default:
                        Assert.AreEqual(0, val);
                        break;
                }
            }

            valToSet = 5;
            t1.traverse(0, 7, setValFunc);

            for (int i = 0; i < 15; ++i)
            {
                int val = t1.getDataAtNodeIndex(i);

                switch (i)
                {
                    case 0:
                        Assert.AreEqual(5, val, "I is " + i);
                        break;
                    case 10:
                    case 11:
                        Assert.AreEqual(4, val, "I is " + i);
                        break;
                    case 4:
                        Assert.AreEqual(3, val);
                        break;
                    default:
                        Assert.AreEqual(0, val);
                        break;
                }
            }
            
        }

        

       

        [TestMethod]
        public void TestHeight()
        {
            testHeight(0, 0);
            testHeight(1 , 1);
            testHeight(2 , 2);
            testHeight(2 , 3);
            testHeight(3 , 4);
            testHeight(3 , 5);
            testHeight(3 , 6);
            testHeight(3, 7);
            testHeight(4 , 8);

        }

       

        private void testHeight(int expectedHeight, int listSize)
        {
            
            BinaryTree<int> t1 = BinaryTree<int>.create(listSize);

            Assert.AreEqual(expectedHeight, t1.Height);

        }
    }
}
