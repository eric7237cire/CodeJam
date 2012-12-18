package codejam.y2008.round_emea.scaled_triangle;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;

import codejam.utils.geometry.Line;
import codejam.utils.geometry.Point;
import codejam.utils.geometry.PointInt;
import codejam.utils.geometry.TriangleInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;

public class Main implements TestCaseHandler<InputData>,
        TestCaseInputScanner<InputData>, DefaultInputFiles {

    @Override
    public String[] getDefaultInputFiles() {
    //    return new String[] {"sample.in"};
        return new String[] {"A-small-practice.in", "A-large-practice.in"};
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData i = new InputData(testCase);
        i.largeTriangle = new TriangleInt(scanner.nextInt(),scanner.nextInt(),scanner.nextInt(),scanner.nextInt(),scanner.nextInt(),scanner.nextInt());
        i.smallTriangle = new TriangleInt(scanner.nextInt(),scanner.nextInt(),scanner.nextInt(),scanner.nextInt(),scanner.nextInt(),scanner.nextInt());
        return i;
    }
   
  
    @Override
    public String handleCase(InputData input) {
        final PointInt origin = new PointInt(0,0);
        
        PointInt p2LargeTranslated = input.largeTriangle.p2.translate(input.largeTriangle.p1);
        PointInt p2SmallTranslated = input.smallTriangle.p2.translate(input.smallTriangle.p1);
        
        double angP2 = Math.atan2(p2LargeTranslated.getY(), p2LargeTranslated.getX()) ;
        double angSmP2 = Math.atan2(p2SmallTranslated.getY(), p2SmallTranslated.getX()) ;
        
        /**
         * The angle of rotation between the small triangle and the large
         */
        double rotAngStoL = angP2 - angSmP2;
        double rotAngLtoS = 2*Math.PI - rotAngStoL;
        
        /**
         * Calculate the scaling factors
         */
        double scaleStoL = p2LargeTranslated.distance(origin) / p2SmallTranslated.distance(origin);
        double scaleLtoS = 1D / scaleStoL;
        
        /**
         * The formulas derive from (was in degrees)
         * = {{input point }}.translate(input.largeTriangle.p1).toPoint().rotate((360-rotAng) * Math.PI / 180D).scale(1 / scale).translate(input.smallTriangle.p1.toPoint().scale(-1));
         * 
         * Basically we want the point that translates to itself
         * 
         */
       
        
        /**
         * Now we break the x, y formulas into lines for x and y.
         * 
         * Follows is 
         *  f(yLarge) to x
         *  
         *    Derived by setting xSmall = xLarge in the derivation.
         *    
         *    What yLarge point gets translated to x such that x = xLarge?
         */
        double xIntercept = (  
                - input.largeTriangle.p1.getX() * Math.cos(rotAngLtoS) * scaleLtoS
                + input.largeTriangle.p1.getY() * Math.sin(rotAngLtoS) * scaleLtoS
                + input.smallTriangle.p1.getX() ) / (1-Math.cos(rotAngLtoS)*scaleLtoS);
        
        double xSlope = -Math.sin(rotAngLtoS) * scaleLtoS / (1-Math.cos(rotAngLtoS)*scaleLtoS);
        
        /**
         * Similarly
         * 
         * f(ySmall) to x
         */        
        double xIntercept2 = (  
                - input.smallTriangle.p1.getX() * Math.cos(rotAngStoL) * scaleStoL
                + input.smallTriangle.p1.getY() * Math.sin(rotAngStoL) * scaleStoL
                + input.largeTriangle.p1.getX() ) / (1-Math.cos(rotAngStoL)*scaleStoL);
        
        double xSlope2 = -Math.sin(rotAngStoL) * scaleStoL / (1-Math.cos(rotAngStoL)*scaleStoL);
        
        /**
         * f(xLarge) to y
         */
        double slope = Math.sin(rotAngLtoS) * scaleLtoS / (1-Math.cos(rotAngLtoS)*scaleLtoS);
        
        double yIntercept = (  
                - input.largeTriangle.p1.getY() * Math.cos(rotAngLtoS) * scaleLtoS
                - input.largeTriangle.p1.getX() * Math.sin(rotAngLtoS) * scaleLtoS
                + input.smallTriangle.p1.getY() ) / (1-Math.cos(rotAngLtoS)*scaleLtoS);
        
        /**
         * f(xSmall) to y
         */
//        double slope2 = Math.sin(rotAngLtoS) * scaleStoL / (1-Math.cos(rotAngLtoS)*scaleStoL);
//        
//        double yIntercept2 = (  
//                - input.smallTriangle.p1.getY() * Math.cos(rotAngStoL) * scaleStoL
//                - input.smallTriangle.p1.getX() * Math.sin(rotAngStoL) * scaleStoL
//                + input.largeTriangle.p1.getY() ) / (1-Math.cos(rotAngStoL)*scaleStoL); 
        
        
        //Convert to y = mx + b form
        Line l1 = Line.dc.compare(xSlope,0d) == 0 ? new Line(new Point(xIntercept, 0), new Point(xIntercept, 2)) : new Line(1 / xSlope, -xIntercept/xSlope);
        Line l2 = Line.dc.compare(xSlope2, 0d) == 0 ? new Line(new Point(xIntercept2, 0), new Point(xIntercept2, 2)) : new Line(1 / xSlope2, -xIntercept2/xSlope2);
        
        Line fxLargeToEqualY = new Line(slope, yIntercept);
        
        Point p = null;
        
        if (l1.equals(l2) && !l1.equals(fxLargeToEqualY)) {
            p = l1.getIntersection(fxLargeToEqualY);
            
            Preconditions.checkState(p!= null);
        }  else {
            Preconditions.checkState(Line.dc.compare(xSlope,xSlope2) != 0);
            p = l1.getIntersection(l2);
        }
        
        DecimalFormat df = new DecimalFormat("0.######");
        
        
        df.setRoundingMode(RoundingMode.HALF_UP);
        return String.format("Case #%d: %s %s", input.testCase, df.format(p.getX()), df.format(p.getY()));
    }

}
