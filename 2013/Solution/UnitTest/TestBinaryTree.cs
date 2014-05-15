using System;
using NUnit.Framework;
//using Round1C;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using DataStructures;

using BTi = DataStructures.BinaryTree<int>;
using Logger = Utils.LoggerFile;

namespace UnitTest
{

    public class Updater : BinaryTreeUpdater<int>
	{
		int valueToUpdate;
		
		public Updater(int val)
		{
			valueToUpdate = val;
		}
		
		//Once update is done
		public void ApplyLeftRightData(int leftData, int rightData, ref int curNodeData)
		{
			
		}
		
		//Called for each matching interval
		public void Update(ref int data, int nodeIndex)
		{
			data = valueToUpdate;	
		}
		
	}
	

    [TestFixture]
    public class TestBinaryTree
    {
       

        public delegate void TestDel<in UserParamType>(ref string data, int nodeIndex, UserParamType obj);

        [Test]
       public void testBinaryTree()
        {
            BinaryTree<int> t1 = BinaryTree<int>.create(6);

            //TestDel<int> t = (ref string data, int n, int obj) => { };

            t1.traverse(2, 4, new Updater(3));

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

            t1.traverse(3, 4, new Updater(4));

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

            t1.traverse(0, 7, new Updater(5));

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

        

       

        [Test]
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
