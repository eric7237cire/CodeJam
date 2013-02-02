package codejam.y2010.round_final.ninjutsu;

import codejam.utils.geometry.PointInt;
import codejam.utils.utils.DoubleFormat;

public class PointData
{

    static class PointDataIndex
    {
        int pointIndex;
        int dirPointIndex;
        
        /**
         * Either the initial direction is from point to dirPoint ;
         * or it is in the opposite direction.
         * 
         * If we are cutting, then the direction is going towards the point where we cut.
         * If we are bending, then the direction will be opposite the old pivot point.
         */
        boolean directionTowardsDirPointIndex;

        /**
         * 
         */
        public PointDataIndex(int pointIndex, int dirPointIndex, boolean directionTowardsDirPointIndex) {
            super();
            this.pointIndex = pointIndex;
            this.dirPointIndex = dirPointIndex;
            this.directionTowardsDirPointIndex = directionTowardsDirPointIndex;
        }
        
        
    }

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


