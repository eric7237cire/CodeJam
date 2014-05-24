#define PERF
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Logger = Utils.LoggerFile;

[assembly: System.Runtime.CompilerServices.InternalsVisibleTo("UnitTest")]

namespace DataStructures
{
	public interface BinaryTreeUpdater<DataType>
	{
		//Once update is done
		void ApplyLeftRightData(DataType leftData, DataType rightData, ref DataType curNodeData);
		
		//Called for each matching interval
		void Update(ref DataType data, int nodeIndex);
		
	}
	
	public interface BinaryTreeQuery<DataType>
	{
		void Query(DataType data, int nodeIndex);
		
		void PassingParent(DataType data, int nodeIndex, ref bool stop);
		
	}
    /**
     * 
     * Implements a segment tree
     * Example Tree of height 3, 15 nodes, 8 data points
     * NodeIndex 0 ( 0 <= x <= 7 )
     * Nodeindex 1 ( 0 <= x <= 3 ) NodeIndex 2 ( 4 <= x <= 7)
     * NodeIndex 3 ( 0 <= x <= 1) ... NodeIndex 6 ( 6 <= x <= 7 )
     * NodeIndex 7 ( x == 0 )  ... NodeIndex 14 ( x == 7)
     * 
     */
    public class BinaryTree<DataType>
    {
        public int Count;
        public int Height { get; private set;}

        private DataType[] data;

        public DataType getDataAtNodeIndex(int nodeIndex)
        {
            return data[nodeIndex];
        }

        

        public static BinaryTree<DataType> create(int numPoints)
        {
            BinaryTree<DataType> ret = new BinaryTree<DataType>();

            ret.Height = getHeight(numPoints);

            ret.data = new DataType[(1 << ret.Height+1) - 1];
           
            return ret;
        }

        

        public static int getParentNodeIndex(int nodeIndex)
        {
            return (nodeIndex - 1) / 2;
        }

        public void traverse(int targetStartEndpointIndex, int targetStopEndPointIndex, 
        	BinaryTreeUpdater<DataType> updater 
        	)
        {
            if (targetStartEndpointIndex > targetStopEndPointIndex)
            {
                return;
            }

            

            traverseHelper(targetStartEndpointIndex, targetStopEndPointIndex, 0, 0, null, updater);
        }
        
        public void traverse(int targetStartEndpointIndex, int targetStopEndPointIndex, 
        	BinaryTreeQuery<DataType> query = null,
        	BinaryTreeUpdater<DataType> updater = null
        	)
        {
            if (targetStartEndpointIndex > targetStopEndPointIndex)
            {
                return;
            }

            

            traverseHelper(targetStartEndpointIndex, targetStopEndPointIndex, 0, 0, query, updater);
        }

       
        void traverseHelper(int targetStart, int targetStop, int nodeIndex, int curHeight,
        	BinaryTreeQuery<DataType> query,
        	BinaryTreeUpdater<DataType> updater)
        {
            int intervalLength = 1 << Height - curHeight;
            int firstNodeIndexOnLevel = (1 << curHeight) - 1; 
            int nodePosition = nodeIndex - firstNodeIndexOnLevel;
            int intervalStart = nodePosition * intervalLength;
            int intervalStop = intervalStart + intervalLength - 1;
            
            //Check if current node is completely encompassed 
            if (intervalStart >= targetStart && intervalStop <= targetStop)
            {
                Logger.LogTrace("Found interval fully contained in target interval.  idx {0}:{1} target idx {2}:{3}",
                    intervalStart, intervalStop, targetStart, targetStop);
                if (query != null)
                    query.Query(data[nodeIndex], nodeIndex);

                if (updater != null)
                	updater.Update(ref data[nodeIndex], nodeIndex);
                
                return;
            }
            //leaf node
            if (curHeight == Height)
            {
                return;
            }

            Logger.LogTrace("traversing parent node idx {0}:{1} target idx {2}:{3}",
                    intervalStart, intervalStop, targetStart, targetStop);
            
            bool stopTraverse = false;
            
            if (query != null)
                    query.PassingParent(data[nodeIndex], nodeIndex, ref stopTraverse);
            
            if (stopTraverse)
            {
                return;
            }

            //Is target start in left child node ?
            int targetStartHalf =  (targetStart -intervalStart) / (intervalLength >> 1);
            int targetStopHalf = (targetStop -intervalStart) / (intervalLength >> 1);
            if ( targetStartHalf <= 0 || targetStopHalf <= 0)
            {
                int leftNodeIndex = nodeIndex * 2 + 1;
                //if (ApplyParentDataFunc != null) ApplyParentDataFunc(data[nodeIndex], ref data[leftNodeIndex]);
                traverseHelper(targetStart, targetStop, leftNodeIndex, curHeight+1, query, updater);
            }


            if (targetStartHalf >= 1 || targetStopHalf >= 1)
            {
                int rightNodeIndex = nodeIndex * 2 + 2;

                //if (ApplyParentDataFunc != null) ApplyParentDataFunc(data[nodeIndex], ref data[rightNodeIndex]);
                traverseHelper(targetStart, targetStop, rightNodeIndex, curHeight+1, query, updater);
            }
            
            //Now combine results
            if (updater != null)
            {
                updater.ApplyLeftRightData(data[nodeIndex * 2 + 1], data[nodeIndex * 2 + 2], ref data[nodeIndex]);
            }
        }

        

        private static int getHeight(int size)
        {
            int h = 0;

            while(size > 0)
            {
                size >>= 1;
                h++;
            }

            return h;
        }
    }
}
