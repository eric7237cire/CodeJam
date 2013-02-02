package codejam.y2010.round_final.ninjutsu;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Point;
import codejam.utils.geometry.PointInt;
import codejam.y2010.round_final.ninjutsu.PointData.PointDataIndex;
import codejam.y2010.round_final.ninjutsu.RopePathData.RopeLengthDataIndex;

import com.google.common.base.Preconditions;
import com.google.common.math.DoubleMath;

public class Dynamic
{
    final static Logger log = LoggerFactory.getLogger(Dynamic.class);

    public String handleCase(InputData in) {
        
        //First add a point -R, 0
        in.points.add(new PointInt(-in.R, 0));
        
        int dirIndex = in.points.size() - 1;
        

        Map<PointDataIndex, PointData[]> pointDataMemo = new HashMap<>();
        Map<RopeLengthDataIndex, RopePathData> rlDataMemo = new HashMap<>();
        
        int maxBends = maxBends(0, dirIndex, pointDataMemo, rlDataMemo, in, 0);
        

        return String.format("Case #%d: %d", in.testCase, maxBends);
    }
    
    
    
    /**
     * For a given pivot and initial direction
     * 
     *  Calculate a list of points with
     * 1. Relative polar angle starting from direction
     * 2. Distance to currentPoint
     * 
     * List is sorted by increasing polar angle starting from initial direction,
     * ie counter clockwise
     * 
     * Size of list is # of total points - 1 (the pivot)
     * 
     * direction is a direction vector, not a point
     * 
     */
    public PointData[] processOtherPoints(PointDataIndex pdIndex, InputData in,
            Map<PointDataIndex, PointData[]> pointDataMemo  )
    {
        PointData[] pd = pointDataMemo.get(pdIndex);
        if (pd != null) {
            return pd;
        }
        
        
        final PointInt currentPoint = in.points.get(pdIndex.pointIndex);
        Point currentPointDouble = currentPoint.toPoint();
    
        final PointInt dirPoint = in.points.get(pdIndex.dirPointIndex);
                
        PointInt direction = pdIndex.directionTowardsDirPointIndex ?
                dirPoint.translate(currentPoint) : //pointing towards dirPoint
                    currentPoint.translate(dirPoint)  //180 degrees opposite direction; away from dirPoint
                    ;
        
        PointData[] points =  new PointData[in.points.size()-1];
        
        double currentPolarAngle = direction.toPoint().polarAngle();
        
        int arrayIndex = -1;
        //For every other point, calculate it's polar angle with respect to the current point
        for(int pIdx = 0; pIdx < in.points.size(); ++pIdx) {
            if (pIdx == pdIndex.pointIndex)
                continue;
            ++arrayIndex;
            
            PointInt point = in.points.get(pIdx);
            Point pointDouble = point.toPoint();
            Point vec = pointDouble.translate(currentPointDouble);
            
            double polarAngle = vec.polarAngle() - currentPolarAngle;
            
            if (polarAngle < 0) {
                polarAngle += 2 * Math.PI;
            }
            
            double distance = currentPointDouble.distance(pointDouble);
            
            points[arrayIndex] = new PointData(pIdx, point, polarAngle, distance);
        }
        
        Arrays.sort(points, new Comparator<PointData>() {

            @Override
            public int compare(PointData o1, PointData o2)
            {
                //If lines are parallel, put the one with the greater distance first
                int cp = PointInt.crossProduct(
                        o1.point.translate(currentPoint),
                        o2.point.translate(currentPoint)
                        );
                if (cp == 0) {
                    return Double.compare(o2.distance, o1.distance);
                }
                return Double.compare(o1.polarAngle, o2.polarAngle);
            }
            
        });
        
        return points;
    }
    
    private static PointData findFirstPointWithinDistance(PointData[] pointData, double distance) {
        //TODO
        return null;
    }
    
    /**
     * Given a point as a pivot, a cut point;
     * determine the sequence of points that follow if we never cut the rope
     */
    public RopePathData getRopePath(RopeLengthDataIndex ropeLenDataIndex, 
            Map<RopeLengthDataIndex, RopePathData> memo,
            Map<PointDataIndex, PointData[]> pointDataMemo,
            InputData in) {
         
        if (memo.containsKey(ropeLenDataIndex))
            return memo.get(ropeLenDataIndex);
        
        //Get pivot and cut point
        PointInt pivot = in.points.get(ropeLenDataIndex.pivotIdx);
        
        PointInt cutPoint = in.points.get(ropeLenDataIndex.cutPointIdx);
        
        
        //Build return object
        RopePathData ret = new RopePathData();
        memo.put(ropeLenDataIndex, ret);
        
        //The starting length is where we just cut the rope
        double ropeLengthRemaining = pivot.distance(cutPoint);
                
        //Add starting pivot
        ret.ropeLengthRemaining.add(ropeLengthRemaining);
        ret.pointIndexList.add(ropeLenDataIndex.pivotIdx);
        
        int curPivIdx = ropeLenDataIndex.pivotIdx;
        int curDirIdx = ropeLenDataIndex.cutPointIdx;
        
        PointDataIndex pdi = new PointDataIndex(curPivIdx, curDirIdx, true);        
        PointData[] pointData = processOtherPoints(pdi, in, pointDataMemo);        
        PointData nextPoint = findFirstPointWithinDistance(pointData, ropeLengthRemaining);

        //No point within reach
        if (nextPoint == null) {        
            return ret;
        }
        
        //Add first bending point
        ropeLengthRemaining -= nextPoint.distance;
        ret.ropeLengthRemaining.add(ropeLengthRemaining);
        ret.pointIndexList.add(nextPoint.pointIndex);
        
        //Now our direction will be opposite of the old pivot
        curDirIdx = curPivIdx;
        //And pivot is the point encountered
        curPivIdx = nextPoint.pointIndex;
        
        
        while(true) {
            /**
             * Now, as we are bending, the direction is 180 degrees from the previous pivot point
             */
            pdi = new PointDataIndex(curPivIdx, curDirIdx, false);
            pointData = processOtherPoints(pdi, in, pointDataMemo);
            nextPoint = findFirstPointWithinDistance(pointData, ropeLengthRemaining);
            
            if (nextPoint == null) {
                //we are done
                return ret;
            }
            
            ropeLengthRemaining -= nextPoint.distance;
            
            if (ret.pointIndexList.contains(nextPoint.pointIndex)) {
                //we have reached our original point and made a loop
                
                int originalPointIdx = ret.pointIndexList.indexOf(nextPoint.pointIndex);
                
                double distanceLoop = ret.ropeLengthRemaining.get(originalPointIdx) - ropeLengthRemaining;
                int loopBends = ret.pointIndexList.size() - originalPointIdx;
                
                int loopsPossible = DoubleMath.roundToInt(ropeLengthRemaining / distanceLoop,RoundingMode.DOWN);
                
                ropeLengthRemaining -= distanceLoop * loopsPossible;
                
                //Should only be possible to process 1 loop
                Preconditions.checkState(ret.loopStart == null);
                ret.loopsBendCount = loopBends * loopsPossible;
                ret.loopStart = ret.pointIndexList.size();
                
            }
                            
            
            ret.pointIndexList.add(nextPoint.pointIndex);
            ret.ropeLengthRemaining.add(ropeLengthRemaining);
            
            
            //Direction is 180 degrees of vector pivot-->old_pivot
            curDirIdx = curPivIdx;
            curPivIdx = nextPoint.pointIndex;
            
            
        }
        
        //return ret;
    }
    
    
    
    public int maxBends( 
            int currentPointIdx, /*int directionPointIdx, boolean towardsDir,*/ 
            int cutPointIndex,
            Map<PointDataIndex, PointData[]> pointDataMemo,
            Map<RopeLengthDataIndex, RopePathData> rlDataMemo,
            InputData in, int recLevel)
    {
        String recLevelSpace = StringUtils.repeat(' ', 4 * recLevel);
        
        
        PointDataIndex pdi = new PointDataIndex(currentPointIdx, cutPointIndex, true);
        
        RopeLengthDataIndex rldIndex = new RopeLengthDataIndex(currentPointIdx,cutPointIndex);
        
        RopePathData ropePathData = getRopePath(rldIndex,rlDataMemo, pointDataMemo,in);
        
        
        
        int curMaxBends = 0;
        
        for(int pathPointIdx = 1; pathPointIdx < ropePathData.pointIndexList.size(); ++pathPointIdx) {
            
            int bends = pathPointIdx;
            if (ropePathData.loopStart != null && pathPointIdx >= ropePathData.loopStart) {
                bends += ropePathData.loopsBendCount;
            }
            
            //See if cutting is better
            int bendsIfCut = bends + maxBends(
                    ropePathData.pointIndexList.get(pathPointIdx-1), //Last pivot
                    ropePathData.pointIndexList.get(pathPointIdx), //cut point
                    pointDataMemo,
                    rlDataMemo,
                    in, recLevel);
        
            curMaxBends = Math.max(curMaxBends, bendsIfCut);
        }
        
                
        return curMaxBends;
        
    }

}
