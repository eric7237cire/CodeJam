using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Round1C
{
    /**
     * Stores endpoints, leaf nodes at bottom level are the smallest intervals
     */
    public class BinaryTree<DataType>
    {
        public int Count;
        public int Height { get; private set;}

        private DataType[] data;

        //range of nodes
        private int[] start;
        private int[] stop;

        public static  BinaryTree<DataType> create2(int numElements) 
        {
            BinaryTree<DataType> ret = new BinaryTree<DataType>();

            ret.Height = getHeight(numElements);

            ret.data = new DataType[ (1 << ret.Height) - 1];
            ret.start = new int[ret.data.Length];
            ret.stop = new int[ret.data.Length];

            int rootNode = ret.data.Length >> 1;
            return ret;        
        }

        public static BinaryTree<DataType> create(int numEndpoints)
        {
            BinaryTree<DataType> ret = new BinaryTree<DataType>();

            ret.Height = 1 + getHeight(numEndpoints);

            ret.data = new DataType[(1 << ret.Height) - 1];
            ret.start = new int[ret.data.Length];
            ret.stop = new int[ret.data.Length];

            
            return ret;
        }

        class TraverseData
        {
            //Position in the data array, children given by 2*ni + 1 and 2*ni + 2
            internal int nodeIndex;

            //how much to adjust endPointIndex when traversing right or left child nodes
            internal int step;

            //Which endpointIndex the node represents
            internal int endpointIndex;

            //The range encompassed by the node
            internal int startEndpointIndex;
            internal int stopEndpointIndex;
        }

        public void traverse(int targetStartEndpointIndex, int targetStopEndPointIndex, 
            ProcessDelegate process)
        {
            if (targetStartEndpointIndex >= targetStopEndPointIndex)
            {
                return;
            }

            TraverseData td = new TraverseData();
            td.startEndpointIndex = 0;
            td.stopEndpointIndex = (1 << Height - 1) - 1;

            td.endpointIndex = data.Length >> 2;
            td.nodeIndex = 0;
            td.step = 1 << Height - 3;

            bool found = false;
            traverseHelper(targetStartEndpointIndex, targetStopEndPointIndex, process, td, ref found);
        }

        public delegate void ApplyParentDataDelegate(DataType parentData, ref DataType data);

        public ApplyParentDataDelegate ApplyParentDataFunc { get; set; }

        public delegate void ProcessDelegate(int startEndpointIndex, int stopEndpointIndex, ref DataType data);

        void traverseHelper(int targetStartEndpointIndex, int targetStopEndPointIndex, 
            ProcessDelegate process, TraverseData td, ref bool found)
        {
            if (found)
            {
                return;
            }
            
            //Check if current node is completely encompassed 
            if (td.startEndpointIndex >= targetStartEndpointIndex && td.stopEndpointIndex <= targetStopEndPointIndex)
            {
                if (process != null)
                    process(td.startEndpointIndex, td.stopEndpointIndex, ref data[td.nodeIndex]);

                
                return;
            }
            //leaf node
            if (td.endpointIndex == -1)
            {
                return;
            }
            if (targetStartEndpointIndex < td.endpointIndex)
            {
                TraverseData leftTd = new TraverseData();
                leftTd.startEndpointIndex = td.startEndpointIndex;
                leftTd.stopEndpointIndex = td.endpointIndex;

                leftTd.endpointIndex = td.step == 0 ? -1 : td.endpointIndex - td.step;
                leftTd.nodeIndex = td.nodeIndex * 2 + 1;
                leftTd.step = td.step >> 1;

                if (ApplyParentDataFunc != null) ApplyParentDataFunc(data[td.nodeIndex], ref data[leftTd.nodeIndex]);
                traverseHelper(targetStartEndpointIndex, targetStopEndPointIndex, process, leftTd, ref found);
            }


            if (targetStopEndPointIndex > td.endpointIndex)
            {
                TraverseData rightTd = new TraverseData();
                rightTd.startEndpointIndex = td.endpointIndex;
                rightTd.stopEndpointIndex = td.stopEndpointIndex;

                rightTd.endpointIndex = td.step == 0 ? -1 : td.endpointIndex + td.step;
                rightTd.nodeIndex = td.nodeIndex * 2 + 2;
                rightTd.step = td.step >> 1;

                if (ApplyParentDataFunc != null) ApplyParentDataFunc(data[td.nodeIndex], ref data[rightTd.nodeIndex]);
                traverseHelper(targetStartEndpointIndex, targetStopEndPointIndex, process, rightTd, ref found);
            }
        }

        public int getNodeIndexForEndPointIndex(int targetEndPointIndex)
        {
            int endpointIndex = data.Length >> 2;
            int nodeIndex = 0;
            int curHeight = Height;
            int curStep = 1 << (curHeight - 3);

            while(endpointIndex != targetEndPointIndex)
            {
                if (targetEndPointIndex > endpointIndex)
                {
                    endpointIndex += curStep;
                    nodeIndex = 2 * nodeIndex + 2;
                }
                else
                {
                    endpointIndex -= curStep;
                    nodeIndex = 2 * nodeIndex + 1;
                }

                curStep >>= 1;
            }

            return nodeIndex;
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
