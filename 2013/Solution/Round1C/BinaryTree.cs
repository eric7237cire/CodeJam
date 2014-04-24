#define PERF
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

[assembly: System.Runtime.CompilerServices.InternalsVisibleTo("UnitTest")]

namespace Round1C
{
#if (PERF)
   
    using Logger = CodeJamUtils.LoggerEmpty;
#else
    using Logger = CodeJamUtils.LoggerReal;
#endif

    /**
     * Tree of height 3, 15 nodes, 8 data points
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

        internal DataType getDataAtNodeIndex(int nodeIndex)
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

        public void traverse(int targetStartEndpointIndex, int targetStopEndPointIndex, ProcessDelegate procFu)
        {
            if (targetStartEndpointIndex > targetStopEndPointIndex)
            {
                return;
            }

            stopTraverse = false;
            this.ProcessFunc = procFu;

            traverseHelper(targetStartEndpointIndex, targetStopEndPointIndex, 0, 0);
        }

        public delegate void ApplyParentDataDelegate(DataType parentData, ref DataType data);
        public delegate void ApplyLeftRightDataDelegate(DataType leftData, DataType rightData, ref DataType curNodeData);

        public ApplyParentDataDelegate ApplyParentDataFunc { get; set; }
        public ApplyLeftRightDataDelegate ApplyLeftRightDataFunc { get; set; }

        //if !isParent, then interval associated with data is fully containted in the target interval,
        //otherwise, the target interval is the one that is fully contained
        public delegate void ProcessDelegate(ref DataType data, int nodeIndex);

        public delegate void ProcessParentDelegate(DataType data, int nodeIndex, ref bool stop);
        private bool stopTraverse;

        public ProcessDelegate ProcessFunc {get; set;}
        public ProcessParentDelegate ParentProcessFunc { get; set; }

        void traverseHelper(int targetStart, int targetStop, int nodeIndex, int height)
        {
            int intervalLength = (1 << Height - height);
            int firstNodeIndexOnLevel = (1 << height) - 1; 
            int nodePosition = nodeIndex - firstNodeIndexOnLevel;
            int intervalStart = nodePosition * intervalLength;
            int intervalStop = intervalStart + intervalLength - 1;
            
            //Check if current node is completely encompassed 
            if (intervalStart >= targetStart && intervalStop <= targetStop)
            {
                Logger.Log("Found interval fully contained in target interval.  idx {0}:{1} target idx {2}:{3}",
                    intervalStart, intervalStop, targetStart, targetStop);
                if (ProcessFunc != null)
                    ProcessFunc(ref data[nodeIndex], nodeIndex);

                
                return;
            }
            //leaf node
            if (height == Height)
            {
                return;
            }

            Logger.Log("traversing parent node idx {0}:{1} target idx {2}:{3}",
                    intervalStart, intervalStop, targetStart, targetStop);
            
            if (ParentProcessFunc != null)
                ParentProcessFunc(data[nodeIndex], nodeIndex, ref stopTraverse);
            //process(ref data[td.nodeIndex], true);

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
                if (ApplyParentDataFunc != null) ApplyParentDataFunc(data[nodeIndex], ref data[leftNodeIndex]);
                traverseHelper(targetStart, targetStop, leftNodeIndex, height+1);
            }


            if (targetStartHalf >= 1 || targetStopHalf >= 1)
            {
                int rightNodeIndex = nodeIndex * 2 + 2;

                if (ApplyParentDataFunc != null) ApplyParentDataFunc(data[nodeIndex], ref data[rightNodeIndex]);
                traverseHelper(targetStart, targetStop, rightNodeIndex, height+1);
            }
            
            //Now combine results
            if (ApplyLeftRightDataFunc != null)
            {
                ApplyLeftRightDataFunc(data[nodeIndex * 2 + 1], data[nodeIndex * 2 + 2], ref data[nodeIndex]);
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
