package codejam.y2010.round_final.ninjutsu;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Point;
import codejam.utils.geometry.PointInt;
import codejam.y2010.round_final.ninjutsu.PointData.PointDataIndex;

import com.google.common.math.DoubleMath;

public class Dynamic
{
    final static Logger log = LoggerFactory.getLogger(Dynamic.class);

    public String handleCase(InputData in) {
        
        int maxBends = maxBends(in.R, 0, new PointInt(-1, 0), in, 0);

        return String.format("Case #%d: %d", in.testCase, maxBends);
    }
    
    static class PointData {
        
        
        
    }
    
    /**
     * For all other points, calculate 
     * 1. Relative polar angle starting from direction
     * 2. Distance to currentPoint
     * 
     * Then sort by angle.
     * 
     * direction is a direction vector, not a point
     * 
     */
    public PointData[] processOtherPoints(PointDataIndex pdIndex, InputData in, Map<PointDataIndex, PointData[]> pointDataMemo  )
    {
        PointData[] pd = pointDataMemo.get(pdIndex);
        if (pd != null) {
            return pd;
        }
        
        
        final PointInt currentPoint = in.points.get(pdIndex.pointIndex);
        Point currentPointDouble = currentPoint.toPoint();
    
        final PointInt dirPoint = in.points.get(pdIndex.dirPointIndex);
        
        
        PointInt direction = pdIndex.directionTowardsDirPointIndex ?
                cutPoint.translate(currentPoint) :
                    ;
        
        PointData[] points =  new PointData[in.points.size()-1];
        
        double currentPolarAngle = direction.toPoint().polarAngle();
        
        int arrayIndex = -1;
        //For every other point, calculate it's polar angle with respect to the current point
        for(int pIdx = 0; pIdx < in.points.size(); ++pIdx) {
            if (pIdx == currentPointIdx)
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
    
    
    /**
     * Given a point, a direction, and number of bends, determine the remaining rope length
     */
    public double getRopeLen(RopeLengthData ropeLenData, 
            Map<RopeLengthData, Double> memo,
            PointData[][][] pointDataMemo,
            InputData in) {
         
        if (memo.containsKey(ropeLenData))
            return memo.get(ropeLenData);
        
        PointInt pivot = in.points.get(ropeLenData.pivotIdx);
        
        PointInt cutPoint = in.points.get(ropeLenData.cutPointIdx);
        
        PointInt direction = cutPoint.translate(pivot);
        
        double distanceBeforeBends = pivot.distance(cutPoint);
        
        int bends = 0;
        
        PointInt curPiv = pivot;
        PointInt curDir = direction;
        int curPivIdx = ropeLenData.pivotIdx;
        int curDirIdx = ropeLenData.cutPointIdx;
        
        while(true) {
            PointData[] pointData = processOtherPoints(curPivIdx,curDirIdx,in, pointDataMemo);
            
            if (pointData.length == 0)
                break;
            
            ++bends;
            
            curDirIdx = curPivIdx;
            curPivIdx = pointData[0].pointIndex;
            
            if (pointData[0].pointIndex == ropeLenData.pivotIdx) {
                //we have reached our original point and made a loop
                break;
            }
        }
        
        
        /**
         * Just find first bend
         */
        for(int paIdx = 0; paIdx < pointDataList.size(); ++paIdx) {
            
            PointData pointData = pointDataList.get(paIdx);
            
            /*
            log.debug("{}P {}  R remaining {} Potential Q {}",
                    recLevelSpace,
                    currentPoint, DoubleFormat.df3.format(currentRopeLength), pointData);
            */
            
            //Rope not long enough
            if (DoubleMath.fuzzyCompare(distanceBeforeBends, pointData.distance, 0.00001) <= 0) {
                continue;
            }
            
            //We hit the first bend
            RopeLengthData rld = new RopeLengthData(ropeLenData.pivotIdx,ropeLenData.cutPointIdx, 1);
            double distToFirstBend = pointData.distance;
            
            memo.put(rld, distToFirstBend);
        }
        
        /**
         * Then use data to get subsequent
         */
        
    }
    
    
    
    public int maxBends(double remainingRopeLength, 
            int currentPointIdx, PointInt direction, InputData in, int recLevel)
    {
        String recLevelSpace = StringUtils.repeat(' ', 4 * recLevel);
        
        PointInt currentPoint = in.points.get(currentPointIdx);
        Point currentPointDouble = in.points.get(currentPointIdx).toPoint();
        
        List<PointData> points = processOtherPoints(remainingRopeLength,currentPointIdx,direction,in);
        
        double currentRopeLength = remainingRopeLength;
        
        log.debug("{}Point {}  rope remaining {}", recLevelSpace,currentPoint, currentRopeLength);
        
        int curMaxBends = 0;
        
        for(int paIdx = 0; paIdx < points.size(); ++paIdx) {
            
            //Point Q in solution text
            PointData pointData = points.get(paIdx);
            
            /*
            log.debug("{}P {}  R remaining {} Potential Q {}",
                    recLevelSpace,
                    currentPoint, DoubleFormat.df3.format(currentRopeLength), pointData);
            */
            
            //Rope not long enough
            if (DoubleMath.fuzzyCompare(currentRopeLength, pointData.distance, 0.00001) <= 0) {
                continue;
            }
            
            //Calculate length if we do not cut the rope
            int bends = 1 + maxBends(currentRopeLength - pointData.distance, //minus rope  used to get to point 
                    pointData.pointIndex, //the index 
                    pointData.point.translate(currentPoint), //initial direction vector PQ  
                    in, 1 + recLevel);
            
            curMaxBends = Math.max(bends, curMaxBends);
            
            //In order to continue, the rope must be shorter than distance to Q
            currentRopeLength = pointData.distance;
            
        }
        
        return curMaxBends;
        
    }

}
