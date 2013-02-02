package codejam.y2010.round_final.ninjutsu;

import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.math.DoubleMath;


public class RopePathData
{

    static class PathNode
    {
        double ropeLengthRemaining;
        int pointIndex;
        int bends;
        public PathNode(double ropeLengthRemaining, int pointIndex, int bends) {
            super();
            this.ropeLengthRemaining = ropeLengthRemaining;
            this.pointIndex = pointIndex;
            this.bends = bends;
        }
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + bends;
            result = prime * result + pointIndex;
            long temp;
            temp = Double.doubleToLongBits(ropeLengthRemaining);
            result = prime * result + (int) (temp ^ (temp >>> 32));
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
            PathNode other = (PathNode) obj;
            if (bends != other.bends)
                return false;
            if (pointIndex != other.pointIndex)
                return false;
            if (Double.doubleToLongBits(ropeLengthRemaining) != Double.doubleToLongBits(other.ropeLengthRemaining))
                return false;
            return true;
        }
        
        
    }
    /**
     * Both of these lists represent the path
     * 
     * At a pointIndexList[i], there is ropeLengthRemaining[i] left of rope
     */
    List<PathNode> ropePath;
    
    /**
     * Null means no loop
     */
   // Double loopRopeLength;
    //Total bends added from loop
    Map<Integer,PathNode> lastPointIndex;
    
    //List<Integer> afterLoopPointIndexList;
    //Integer addedBendCount;
    
    void addInitialPathNode(double totalRopeLen, int pointIndex) {
        PathNode pathNode = new PathNode(totalRopeLen, pointIndex, 0);
        ropePath.add(pathNode);
        lastPointIndex.put(pointIndex, pathNode);
    }
    
    double addPathNode(double ropeUsed, int pointIndex) {
        PathNode lastPathNode = ropePath.get(ropePath.size()-1);
        
        double ropeLenRemaining = lastPathNode.ropeLengthRemaining - ropeUsed;
        int bends = lastPathNode.bends + 1;
        
        //Loop detection
        if (lastPointIndex.containsKey(pointIndex)) {
            PathNode loopStart = lastPointIndex.get(pointIndex);
            
            double distanceLoop = loopStart.ropeLengthRemaining - ropeLenRemaining;
            int loopBends = bends - loopStart.bends;
            
            //Leave a full loop 
            int loopsPossible = Math.max(0,DoubleMath.roundToInt(ropeLenRemaining / distanceLoop,RoundingMode.DOWN) - 1);
            
            ropeLenRemaining -= distanceLoop * loopsPossible;
            bends += loopBends * loopsPossible;
        }
        
        PathNode pathNode = new PathNode(ropeLenRemaining, pointIndex, bends);
        ropePath.add(pathNode);
        return pathNode.ropeLengthRemaining;
    }
    
    
    RopePathData() {
        ropePath = Lists.newArrayList();
        lastPointIndex = Maps.newHashMap();
    }
    
    static class PivotCutPointIndex {
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
            PivotCutPointIndex other = (PivotCutPointIndex) obj;
            
            if (cutPointIdx != other.cutPointIdx)
                return false;
            if (pivotIdx != other.pivotIdx)
                return false;
            return true;
        }
        public PivotCutPointIndex(int pivotIdx, int cutPointIdx) {
            super();
            this.pivotIdx = pivotIdx;
            this.cutPointIdx = cutPointIdx;
            //this.bends = bends;
        }

    }
}
