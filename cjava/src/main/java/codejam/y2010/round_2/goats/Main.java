package codejam.y2010.round_2.goats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import codejam.utils.geometry.Angle;
import codejam.utils.geometry.Circle;
import codejam.utils.geometry.Point;
import codejam.utils.geometry.PointInt;
import codejam.utils.geometry.Polygon;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    
    public Main() {
        super("D", 1, 1, 0);
    }
    
        
    private static class CircleWithAngle {
        Circle c;
        double polarAngle;

        public CircleWithAngle(Circle c, double polarAngle) {
            super();
            this.c = c;
            this.polarAngle = polarAngle;
        }

    }
    
    private static class Arc {
        double fromAngle;
        double toAngle;
        
        Point intPointTo;
        int closestCircle;
        public Arc(double fromAngle, double toAngle, int closestCircle, Point intPointTo) {
            super();
            this.fromAngle = fromAngle;
            this.toAngle = toAngle;
            this.closestCircle = closestCircle;
            this.intPointTo = intPointTo;
        }
        @Override
        public String toString() {
            return "Arc [fromAngle=" + fromAngle + ", toAngle=" + toAngle
                    + ", intPointTo=" + intPointTo + ", closestCircle="
                    + closestCircle + "]";
        }
       
        
    }
    
   
    
    @Override
    public String handleCase(InputData input) {
        int caseNumber = input.testCase;
        log.info("Starting calculating case {}", caseNumber);
        
        StringBuffer sb = new StringBuffer();
                


        //double ans = DivideConq.findMinPerimTriangle(input.points);
        for(PointInt bucketPos : input.bucketPositions) {
            
            /**
             * Circles with centers where the goats are and the radius necesary to reach the buckets
             */
            Circle[] circles = new Circle[input.goatPolePositions.length];
            for(int gp = 0; gp < input.N; ++gp) {
                PointInt goatPos = input.goatPolePositions[gp];
                circles[gp] = new Circle(goatPos.x(), goatPos.y(), goatPos.distance(bucketPos));
            }

            CircleWithAngle[] circlesWithpolarAngles = new CircleWithAngle[input.N];

            
            /**
             * For each circle, calculate angle from bucket (where all circles intersect)
             * to circle
             */
            for (int c = 0; c < circles.length; ++c) {
                double ang = circles[c].getCenter().translate(bucketPos.toPoint()).polarAngle(); //.Math.atan2(y, x);
                circlesWithpolarAngles[c] = new CircleWithAngle(circles[c],
                        ang);
     
                /*
                log.debug("Angle for circle {} : {} \n is rad {} deg {}." +
                		"\n  Range for cirl {}/{} to {}/{}. " +
                		"Range angles {}/{} - {}/{}", c, circles[c], 
                		shortDecim.format(circlesWithpolarAngles[c].polarAngle), 
                		shortDecim.format(circlesWithpolarAngles[c].polarAngle * 180d / Math.PI), 
                		shortDecim.format(min),
                		shortDecim.format(min * 180d / Math.PI),
                		shortDecim.format(max),
                		shortDecim.format(max* 180d / Math.PI),
                		shortDecim.format(minPolAngle),
                		shortDecim.format(minPolAngle* 180d / Math.PI),
                		shortDecim.format(maxPolAngle),
                		shortDecim.format(maxPolAngle* 180d / Math.PI)
                		);
                */
               
            }

            Arrays.sort(circlesWithpolarAngles,
                    new Comparator<CircleWithAngle>() {

                        @Override
                        public int compare(CircleWithAngle o1,
                                CircleWithAngle o2) {
                            return ComparisonChain.start()
                                    .compare(o1.polarAngle, o2.polarAngle)
                                    .result();
                        }

                    });
           
            double minPolAngleToCircleCenter = circlesWithpolarAngles[0].polarAngle;
            double maxPolAngleToCircleCenter = circlesWithpolarAngles[circlesWithpolarAngles.length-1].polarAngle;
            
                        
            boolean allCentersWithinPiPolarAngle = false;
            if (maxPolAngleToCircleCenter  - minPolAngleToCircleCenter <= Math.PI) {
                //All polar angles of circles intersections between minPolAngle and maxPolAngle.  This
                //covers the case where min is in 1st quadrant / max 2nd
                //min 3rd max 4th and min 4th max 1st
                
                //In the first the angle goes 0 to PI / 2
                //Second PI / 2 to PI
                //Third -PI to -PI / 2
                //Fourth -PI / 2 to 0
                
                minPolAngleToCircleCenter = circlesWithpolarAngles[0].polarAngle;
                maxPolAngleToCircleCenter = circlesWithpolarAngles[circlesWithpolarAngles.length-1].polarAngle;
                
                allCentersWithinPiPolarAngle = true;
            } else {
                
                //We need to check one more case.  If the min is in the 2nd quadrant
                //and max is in the 3rd. then the ordering will be off.  We look for a
                //jump of pi.  The jump goes past the 4th and 1st quadrants.
                
                //We do not need additional checks because if there is a jump more than pie, all the points
                //must be within a range of length PI.
                for (int i = 0; i < circlesWithpolarAngles.length - 1; i++)
                {
                    if (circlesWithpolarAngles[i + 1].polarAngle - circlesWithpolarAngles[i].polarAngle > Math.PI + 1e-9)
                    {
                        CircleWithAngle[] newArray = new CircleWithAngle[circlesWithpolarAngles.length];
                        
                        //i + 1 to len goes to front of array
                        System.arraycopy(circlesWithpolarAngles, i + 1, newArray, 0, circlesWithpolarAngles.length - (i + 1));
                        
                        //Then follows 0 to i to back of array
                        System.arraycopy(circlesWithpolarAngles, 0, newArray, circlesWithpolarAngles.length - i - 1, i + 1);
                        
                        minPolAngleToCircleCenter = circlesWithpolarAngles[i + 1].polarAngle;
                        maxPolAngleToCircleCenter = circlesWithpolarAngles[i].polarAngle + 2 * Math.PI;
                        
                        
                        circlesWithpolarAngles = newArray;
                        allCentersWithinPiPolarAngle = true;
                                                
                       
                        break;
                    }
                }
            }
            
            final double minPolAngleRange  = maxPolAngleToCircleCenter - Math.PI / 2;
            final double maxPolAngleRange = minPolAngleToCircleCenter  + Math.PI / 2;
            
            
            Stack<Arc> arcs = new Stack<>();

            if (allCentersWithinPiPolarAngle) {
                for (int circNum = 0; circNum < circlesWithpolarAngles.length; ++circNum) {

                    if (arcs.isEmpty()) {
                        arcs.add(new Arc(minPolAngleRange, maxPolAngleRange,
                                circNum, new Point(bucketPos)));
                        continue;
                    }
                    double startingAngle = arcs.peek().fromAngle;

                    while (true) {
                        
                        /**
                         * This algorithm is similiar in spirit to grahams
                         * scan, using a stack / calculating a convex hull
                         */

                        Circle circ = circlesWithpolarAngles[circNum].c;
                        // get intersection of circNum and circle on stack
                        int circStack = arcs.peek().closestCircle;
                        Circle stackCircl = circlesWithpolarAngles[circStack].c;

                        Point intPoint = stackCircl.getOtherIntersection(circ,
                                new Point(bucketPos));

                        // Now get polar angle of intersection
                        double x = intPoint.x() - bucketPos.x();
                        double y = intPoint.y() - bucketPos.y();

                        double angle = Math.atan2(y, x);

                        double stackAngleMin = arcs.peek().fromAngle;
                        double stackAngleMax = arcs.peek().toAngle;

                        //Comparing polar angles is a pain.  Must use reference points
                        int compMin = Angle.comparePolar(minPolAngleToCircleCenter - Math.PI
                                / 2, maxPolAngleToCircleCenter + Math.PI / 2, angle,
                                stackAngleMin);
                        int compMax = Angle.comparePolar(minPolAngleToCircleCenter - Math.PI
                                / 2, maxPolAngleToCircleCenter + Math.PI / 2, angle,
                                stackAngleMax);
          
                        if (-1 == compMin && -1 == compMax) {
                            Preconditions.checkState(-1 == compMin
                                    && -1 == compMax);
                            // The intersection comes before the range, meaning
                            // as
                            // we increase
                            // the polar angle, the distance to the added circle
                            // will increase.
                            // the circle on stack is closest on the range
                            break;
                        } else if ((0 == compMin || 1 == compMin)
                                && (-1 == compMax || 0 == compMax)) {

                            // anything after angle, the added circle will be
                            // closest from stackAngleMin to angle
                            // the circle on stack closest from angle to
                            // stackAngleMax
                            Arc arcToDelete = arcs.pop();
                            Arc arc1 = new Arc(angle, stackAngleMax,
                                    arcToDelete.closestCircle,
                                    arcToDelete.intPointTo);
                            Arc arc2 = new Arc(startingAngle, angle, circNum,
                                    intPoint);
                            arcs.push(arc1);
                            arcs.push(arc2);
                            break;
                        } else {
                            Preconditions.checkState(1 == compMin
                                    && 1 == compMax);

                            arcs.pop();
                            if (arcs.isEmpty()) {
                                throw new IllegalStateException("Stack should never be empty");
                            }
                        }

                    }
                }
            }

            //Loop through arcs, skipping first one and last one
            Arc[] arcsArray = new Arc[arcs.size()];
            arcs.toArray(arcsArray);
            org.apache.commons.lang3.ArrayUtils.reverse(arcsArray);
            
            double area = getArea(arcsArray, circlesWithpolarAngles, bucketPos);
        
            sb.append(DoubleFormat.df7.format(area));
            sb.append(' ');
        }
        
        
        

        log.info("Done calculating answer case {}", caseNumber);
        
        
        return ("Case #" + caseNumber + ": " + sb.toString().trim());
    }
    
    private double getArea(Arc[] arcsArray, CircleWithAngle[] circlesWithpolarAngles, PointInt bucketPos) {
        if (arcsArray.length == 2){
            Circle c1 = circlesWithpolarAngles[arcsArray[0].closestCircle].c;
            Circle c2 = circlesWithpolarAngles[arcsArray[1].closestCircle].c;
            return c1.findAreaIntersection(c2);
        } else if (arcsArray.length > 2){
        
            
            List<Point> polygonPoints = new ArrayList<Point>();
            //array is in counter clockwise, same order as polar angles
            for(Arc arc : arcsArray) {
                polygonPoints.add(arc.intPointTo);
            }
            
            double area = Polygon.area(polygonPoints);
            
            Point fromPoint = new Point(bucketPos);
            
            //Circular segments
            for(Arc arc : arcsArray) {
                double segLength = fromPoint.distance(arc.intPointTo);
                if ( Angle.angBetween(arc.fromAngle, arc.toAngle) > Math.PI / 2) {
                    //Over half the circle is part of the intersection
                    area += circlesWithpolarAngles[arc.closestCircle].c.getArea()-circlesWithpolarAngles[arc.closestCircle].c.findSegmentArea(segLength);
                } else {
                    area += circlesWithpolarAngles[arc.closestCircle].c.findSegmentArea(segLength);
                }
                
                fromPoint = arc.intPointTo;
            }
            
            return area;
            
        }

        return 0;
    }
    
   
    @Override
    public InputData readInput(Scanner scanner, int testCase) {


        InputData input = new InputData(testCase);

        input.N = scanner.nextInt();
        input.M = scanner.nextInt();

        input.goatPolePositions = new PointInt[input.N];
        input.bucketPositions = new PointInt[input.M];

        for (int n = 0; n < input.N; ++n) {
            input.goatPolePositions[n] = new PointInt(scanner.nextInt(),scanner.nextInt());
        }

        for (int m = 0; m < input.M; ++m) {
            input.bucketPositions[m] = new PointInt(scanner.nextInt(),scanner.nextInt());
        }

        return input;

    }

    
    
 
    
}