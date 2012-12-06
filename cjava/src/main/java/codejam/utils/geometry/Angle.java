package codejam.utils.geometry;

public class Angle {
    /*
     * Math.atan2
     * 
     * goes from 0 to PI and 0 to -PI for 3rd / 4th quadrants
     */
    public static double makeAnglePositive(double angle) {
        double posAngle = angle;
        while(posAngle < 0) {
            posAngle += 2d * Math.PI;
        }
        while(posAngle > 2*Math.PI) {
            posAngle -= 2d * Math.PI;
        }
        return posAngle;
    }
    //Going counter clockwise
    public static double angBetween(double fromPolar, double toPolar) {
        fromPolar = makeAnglePositive(fromPolar);
        toPolar = makeAnglePositive(toPolar);
        
        double diffFromTo = toPolar - fromPolar;
        if (diffFromTo < 0) {
            return Math.PI * 2 - fromPolar + toPolar;
        }
        
        return diffFromTo;
    }
    public static int comparePolar(double minPolar, double maxPolar, double angle1, double angle2) {
        //as angle goes counter clockwise from minPolar to maxPolar, which angle gets hit first?
        
        minPolar = makeAnglePositive(minPolar);
        maxPolar = makeAnglePositive(maxPolar);
        angle1 = makeAnglePositive(angle1);
        angle2 = makeAnglePositive(angle2);
        
            double diffMinMax = maxPolar - minPolar;
            double diffMinAng1 = angle1 - minPolar;
            double diffMinAng2 = angle2 - minPolar;
            
            if (diffMinMax < 0) 
                diffMinMax = Math.PI * 2 - minPolar + maxPolar;
            
            if (diffMinAng1 < 0)
                diffMinAng1 = Math.PI * 2 - minPolar + angle1;
            
            if (diffMinAng2 < 0)
                diffMinAng2 = Math.PI * 2 - minPolar + angle2;
                
            
            //looking for least positive
            if (diffMinMax < diffMinAng1 || diffMinMax < diffMinAng2) {
                throw new IllegalArgumentException("angles not between min and max");
            }

        
        
        return Double.compare(diffMinAng1, diffMinAng2);
    }
}
