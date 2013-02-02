package codejam.y2010.round_final.ninjutsu;

import java.util.List;

import com.google.common.collect.Lists;


public class RopePathData
{

    /**
     * Both of these lists represent the path
     * 
     * At a pointIndexList[i], there is ropeLengthRemaining[i] left of rope
     */
    List<Double> ropeLengthRemaining;
    
    List<Integer> pointIndexList;
    
    /**
     * Null means no loop
     */
   // Double loopRopeLength;
    //Total bends added from loop
    Integer loopsBendCount;
    Integer loopStart;
    
    //List<Integer> afterLoopPointIndexList;
    //Integer addedBendCount;
    
    
    RopePathData() {
        ropeLengthRemaining = Lists.newArrayList();
    }
    
    static class RopeLengthDataIndex {
        final int pivotIdx;
        final int cutPointIdx;
        //final int bends;
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
          //  result = prime * result + bends;
            result = prime * result + cutPointIdx;
            result = prime * result + pivotIdx;
            return result;
        }
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            RopeLengthDataIndex other = (RopeLengthDataIndex) obj;
            
            if (cutPointIdx != other.cutPointIdx)
                return false;
            if (pivotIdx != other.pivotIdx)
                return false;
            return true;
        }
        public RopeLengthDataIndex(int pivotIdx, int cutPointIdx) {
            super();
            this.pivotIdx = pivotIdx;
            this.cutPointIdx = cutPointIdx;
            //this.bends = bends;
        }

    }
}
