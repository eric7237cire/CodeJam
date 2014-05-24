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
        
        [Test]
        public void testIntervalTree()
        {
        	int nToCreate = 10000;
        	int lowBound = 10;
        	int upperBound = 550;
        	int nQueries = 100;
        	
        	Random r = new Random();
        	
        	List<Interval<int,double>> ivals = new List<Interval<int,double>>();
        	
        	for(int i = 0; i < nToCreate; ++i)
        	{
        		int lb = r.Next(lowBound, upperBound);
        		int up = r.Next(lb, upperBound);
        		double data = r.NextDouble();
        		ivals.Add( new Interval<int,double> ( lb, up, data ) );
        		Logger.LogTrace("Adding interval {} - {} data {}", lb, up, data);
        	}
        	
        	IntervalTree <int,double> tree = IntervalTree <int,double>.CreateIntervalTree(ivals, lowBound, upperBound);
        	
        	//Do a bunch of queries
        	for(int q = 0; q < nQueries; ++q)
        	{
        		int lb = r.Next(lowBound, upperBound);
        		int up = r.Next(lb, upperBound);
        		
        		List<Interval<int,double>> contained = new List<Interval<int,double>>();
        		List<Interval<int,double>> overlapped = new List<Interval<int,double>>();
        		
        		tree.findOverlapping( lb, up, overlapped );
        		
        		tree.findContained( lb, up, contained );
        		
        		HashSet<Interval<int,double>> containedSet = new HashSet<Interval<int,double>>( contained );
        		HashSet<Interval<int,double>> overlappedSet = new HashSet<Interval<int,double>>( overlapped );
        		
        		foreach( Interval<int,double> inter in ivals )
        		{
        			bool isContained = (inter.start >= lb && inter.stop <= up );
        			bool isOverlap = ( inter.stop >= lb && inter.start <= up );
        			
        			Logger.LogTrace("Testing interval [{} - {}] with query [{} - {}], contained {} overlap {}",
        				inter.start, inter.stop, lb, up, isContained, isOverlap);
        			
        			Assert.IsTrue( !isContained || containedSet.Contains(inter) );
        			Assert.IsTrue( isContained || !containedSet.Contains(inter) );
        			
        			Assert.IsTrue( !isOverlap || overlappedSet.Contains(inter) );
        			Assert.IsTrue( isOverlap || !overlappedSet.Contains(inter) );
        			
        			containedSet.Remove(inter);
        			overlappedSet.Remove(inter);
        			
        		}
        		
        		
        	}
        	
        }
    }
}
