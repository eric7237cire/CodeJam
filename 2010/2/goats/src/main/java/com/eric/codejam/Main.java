package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.geometry.Circle;
import com.eric.codejam.geometry.Point;
import com.eric.codejam.geometry.PointInt;
import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.math.DoubleMath;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
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
        int closestCircle;
        public Arc(double fromAngle, double toAngle, int closestCircle) {
            super();
            this.fromAngle = fromAngle;
            this.toAngle = toAngle;
            this.closestCircle = closestCircle;
        }
        @Override
        public String toString() {
            return "Arc [fromAngle=" + fromAngle + ", toAngle=" + toAngle
                    + ", closestCircle=" + closestCircle + "]";
        }
        
    }
    @Override
    public String handleCase(int caseNumber, InputData input) {

        log.info("Starting calculating case {}", caseNumber);
        
        StringBuffer sb = new StringBuffer();
        
        DecimalFormat decim = new DecimalFormat("0.00000000");
        decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        DecimalFormat shortDecim = new DecimalFormat("0.00");
        shortDecim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        
        //double ans = DivideConq.findMinPerimTriangle(input.points);
        for(PointInt bucketPos : input.bucketPositions) {
            Circle[] circles = new Circle[input.goatPolePositions.length];
            for(int gp = 0; gp < input.N; ++gp) {
                PointInt goatPos = input.goatPolePositions[gp];
                circles[gp] = new Circle(goatPos.getX(), goatPos.getY(), goatPos.distance(bucketPos));
            }
            
            Point[] intPoints = circles[0].getIntersection(circles[1]);
            
            
            Preconditions.checkState(
                    (DoubleMath.roundToInt(intPoints[0].getX(), RoundingMode.HALF_UP ) == bucketPos.getX() &&
                    DoubleMath.roundToInt(intPoints[0].getY(), RoundingMode.HALF_UP ) == bucketPos.getY() )  ||
                    (DoubleMath.roundToInt(intPoints[1].getX(), RoundingMode.HALF_UP ) == bucketPos.getX() &&
                    DoubleMath.roundToInt(intPoints[1].getY(), RoundingMode.HALF_UP ) == bucketPos.getY() ) );
            
           
            
            CircleWithAngle[] circlesWithpolarAngles = new CircleWithAngle[input.N];

            double minPolAngle = -Math.PI;
            double maxPolAngle = 2d * Math.PI;
            
            for(int c = 0; c < circles.length; ++c) {
                double x = circles[c].getX() - bucketPos.getX();
                double y = circles[c].getY() - bucketPos.getY();
                
                circlesWithpolarAngles[c] = new CircleWithAngle( circles[c],  Math.atan2(y,x) );
                
                if (circlesWithpolarAngles[c].polarAngle < -Math.PI / 2) {
                    circlesWithpolarAngles[c].polarAngle += 2d * Math.PI;
                }
                
                double min = circlesWithpolarAngles[c].polarAngle - Math.PI / 2;
                double max = circlesWithpolarAngles[c].polarAngle + Math.PI / 2;
                
                
                
                minPolAngle = Math.max(min, minPolAngle);
                maxPolAngle = Math.min(max, maxPolAngle);
                
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
            }
            
            Arrays.sort(circlesWithpolarAngles, new Comparator<CircleWithAngle>() {

                @Override
                public int compare(CircleWithAngle o1, CircleWithAngle o2) {
                    return ComparisonChain.start().compare(o1.polarAngle, o2.polarAngle).result();
                }
                
            });
            
            Stack<Arc> arcs = new Stack<>();

            for (int circNum = 0; circNum < circlesWithpolarAngles.length; ++circNum) {

                if (arcs.isEmpty()) {
                    arcs.add(new Arc(minPolAngle, maxPolAngle, circNum));
                    continue;
                }
                double startingAngle = arcs.peek().fromAngle;

                while (true) {
                    Circle circ = circlesWithpolarAngles[circNum].c;
                    // get intersection of circNum and circle on stack
                    int circStack = arcs.peek().closestCircle;
                    Circle stackCircl = circlesWithpolarAngles[circStack].c;

                    Point[] intersection = stackCircl.getIntersection(circ);

                    Point intPoint = null;
                    if (intersection[0].equalsPointInt(bucketPos)) {
                        intPoint = intersection[1];
                    } else {
                        Preconditions.checkState(intersection[1]
                                .equalsPointInt(bucketPos));
                        intPoint = intersection[0];
                    }

                    // Now get polar angle of intersection
                    double x = intPoint.getX() - bucketPos.getX();
                    double y = intPoint.getY() - bucketPos.getY();

                    double angle = Math.atan2(y, x);
                    if (angle < 0) {
                        angle += 2d * Math.PI;
                        // Preconditions.checkState(angle <= maxPolAngle &&
                        // angle >= minPolAngle);
                    }

                    double stackAngleMin = arcs.peek().fromAngle;
                    double stackAngleMax = arcs.peek().toAngle;

                    if (angle < stackAngleMin) {
                        // The intersection comes before the range, meaning as
                        // we increase
                        // the polar angle, the distance to the added circle
                        // will increase.
                        // the circle on stack is closest on the range
                        break;
                    } else if (angle >= stackAngleMin && angle <= stackAngleMax) {
                        // anything after angle, the added circle will be
                        // closest from stackAngleMin to angle
                        // the circle on stack closest from angle to
                        // stackAngleMax
                        Arc arcToDelete = arcs.pop();
                        Arc arc1 = new Arc(angle, stackAngleMax,
                                arcToDelete.closestCircle);
                        Arc arc2 = new Arc(startingAngle, angle, circNum);
                        arcs.push(arc1);
                        arcs.push(arc2);
                        break;
                    } else {
                        arcs.pop();
                    }

                }
            }
            
            //Loop through arcs, skipping first one and last one
            Arc[] arcsArray = new Arc[arcs.size()];
            arcs.toArray(arcsArray);
            
            double area = 0;
            
            if (arcsArray.length > 2) {
            Circle c1 = circlesWithpolarAngles[arcsArray[0].closestCircle].c;
            Circle c2 = circlesWithpolarAngles[arcsArray[1].closestCircle].c;
            Circle c3 = circlesWithpolarAngles[arcsArray[2].closestCircle].c;
            double aaa = c2.getArea() - c1.findAreaIntersection(c2);
            double bbb = c2.getArea() - c2.findAreaIntersection(c3);
            area = c2.getArea() - aaa - bbb;
            } else if (arcsArray.length == 2){
                Circle c1 = circlesWithpolarAngles[arcsArray[0].closestCircle].c;
                Circle c2 = circlesWithpolarAngles[arcsArray[1].closestCircle].c;
                area = c1.findAreaIntersection(c2);
            }

            sb.append(decim.format(area));
            sb.append(' ');
        }

        log.info("Done calculating answer case {}", caseNumber);
        
        
        return ("Case #" + caseNumber + ": " + sb.toString().trim());
    }
    
    
   
    
    @Override
    public InputData readInput(BufferedReader br, int testCase) throws IOException {
        
    
        String[] line = br.readLine().split(" ");
        
        InputData  input = new InputData(testCase);
        
        input.N = Integer.parseInt(line[0]);
        input.M = Integer.parseInt(line[1]);
        
        input.goatPolePositions = new PointInt[input.N];
        input.bucketPositions = new PointInt[input.M];
        
        for(int n = 0; n < input.N; ++n) {
            line = br.readLine().split(" ");
            input.goatPolePositions[n] = new PointInt(Integer.parseInt(line[0]),Integer.parseInt(line[1]));
        }
        
        for(int m = 0; m < input.M; ++m) {
            line = br.readLine().split(" ");
            input.bucketPositions[m] = new PointInt(Integer.parseInt(line[0]),Integer.parseInt(line[1]));
        }
        
        return input;
        
    }

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
           args = new String[] { "sample.txt" };
            //args = new String[] { "D-small-practice.in" };
//            args = new String[] { "B-large-practice.in" };
         }
         log.info("Input file {}", args[0]);

         Main m = new Main();
         Runner.goSingleThread(args[0], m, m);
         //Runner.go(args[0], m, m, new InputData(-1));

        
       
    }

    
}