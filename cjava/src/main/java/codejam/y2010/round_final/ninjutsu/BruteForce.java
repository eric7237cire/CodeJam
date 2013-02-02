package codejam.y2010.round_final.ninjutsu;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Point;
import codejam.utils.geometry.PointInt;
import codejam.utils.utils.DoubleFormat;

import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;

public class BruteForce
{
    final static Logger log = LoggerFactory.getLogger(BruteForce.class);

    public String handleCase(InputData in) {
        
        int maxBends = maxBends(in.R, 0, new PointInt(-1, 0), in, 0);

        return String.format("Case #%d: %d", in.testCase, maxBends);
    }
    
    static class PointData {
        
        //Index of point in input array
        int pointIndex;        
        
        //A point, in solution, Q.  A point we come across while swinging around point P
        PointInt point;
        
        //The angle relative to the initial direction.  ie, how much we swung
        double polarAngle;
        
        //Distance of point to swinging point
        double distance;
        
        public PointData(int pointIndex, PointInt point, double polarAngle, double distance) {
            super();
            this.pointIndex = pointIndex;
            this.point = point;
            this.polarAngle = polarAngle;
            this.distance = distance;
        }

        @Override
        public String toString()
        {
            return "[" + point + ", ang=" + DoubleFormat.df3.format(polarAngle)
                    + ", d=" + DoubleFormat.df3.format(distance) + "]";
        }
        
        
    }
    
    /**
     * For all other points, calculate 
     * 1. Relative polar angle from direction
     * 2. Distance
     * 
     * Then sort by angle
     * 
     * @param remainingRopeLength
     * @param currentPointIdx
     * @param direction
     * @param in
     * @return
     */
    public List<PointData> processOtherPoints(double remainingRopeLength, int currentPointIdx, PointInt direction, InputData in)
    {
        final PointInt currentPoint = in.points.get(currentPointIdx);
        Point currentPointDouble = in.points.get(currentPointIdx).toPoint();
        
        List<PointData> points = Lists.newArrayList();
        
        double currentPolarAngle = direction.toPoint().polarAngle();
        
        //For every other point, calculate it's polar angle with respect to the current point
        for(int pIdx = 0; pIdx < in.points.size(); ++pIdx) {
            if (pIdx == currentPointIdx)
                continue;
            
            PointInt point = in.points.get(pIdx);
            Point pointDouble = point.toPoint();
            Point vec = pointDouble.translate(currentPointDouble);
            
            double polarAngle = vec.polarAngle() - currentPolarAngle;
            
            if (polarAngle < 0) {
                polarAngle += 2 * Math.PI;
            }
            
            double distance = currentPointDouble.distance(pointDouble);
            
            points.add(new PointData(pIdx, point, polarAngle, distance));
        }
        
        Collections.sort(points, new Comparator<PointData>() {

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
    
    public int maxBends(double remainingRopeLength, 
            int currentPointIdx, PointInt direction, InputData in, int recLevel)
    {
        String recLevelSpace = StringUtils.repeat(' ', 4 * recLevel);
        
        PointInt currentPoint = in.points.get(currentPointIdx);
       // Point currentPointDouble = in.points.get(currentPointIdx).toPoint();
        
        List<PointData> points = processOtherPoints(remainingRopeLength,currentPointIdx,direction,in);
        
        double currentRopeLength = remainingRopeLength;
        
        log.debug("{}Point {}  rope remaining {}", recLevelSpace,currentPoint, currentRopeLength);
        
        int curMaxBends = 0;
        
        for(int paIdx = 0; paIdx < points.size(); ++paIdx) {
            
            //Point Q in solution text
            PointData pointData = points.get(paIdx);
            
            
            log.debug("{}P {}  R remaining {} Potential Q {}",
                    recLevelSpace,
                    currentPoint, DoubleFormat.df3.format(currentRopeLength), pointData);
            
            
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
