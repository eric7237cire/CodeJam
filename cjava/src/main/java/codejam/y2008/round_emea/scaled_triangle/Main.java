package codejam.y2008.round_emea.scaled_triangle;

import java.math.RoundingMode;
import java.util.Scanner;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Line;
import codejam.utils.geometry.Point;
import codejam.utils.geometry.PointInt;
import codejam.utils.geometry.TriangleInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

import com.google.common.base.Preconditions;
import com.google.common.math.DoubleMath;

public class Main implements TestCaseHandler<InputData>,
        TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
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
   
  /**
   * See solution explanation
   * 
   * Will calculate the transform matrix directly
   */
    public String solveUsingTranslationMatrix(InputData input) {
        RealMatrix fm = new Array2DRowRealMatrix(3, 3);
        
        fm.setEntry(0,0, input.smallTriangle.p1.getX());
        fm.setEntry(1,0, input.smallTriangle.p1.getY());
        fm.setEntry(2,0, 1);
        
        fm.setEntry(0,1, input.smallTriangle.p2.getX());
        fm.setEntry(1,1, input.smallTriangle.p2.getY());
        fm.setEntry(2,1, 1);
        
        fm.setEntry(0,2, input.smallTriangle.p3.getX());
        fm.setEntry(1,2, input.smallTriangle.p3.getY());
        fm.setEntry(2,2, 1);
        
        RealMatrix fmLarge = new Array2DRowRealMatrix(3, 3);
        
        fmLarge.setEntry(0,0, input.largeTriangle.p1.getX());
        fmLarge.setEntry(1,0, input.largeTriangle.p1.getY());
        fmLarge.setEntry(2,0, 1);
        
        fmLarge.setEntry(0,1, input.largeTriangle.p2.getX());
        fmLarge.setEntry(1,1, input.largeTriangle.p2.getY());
        fmLarge.setEntry(2,1, 1);
        
        fmLarge.setEntry(0,2, input.largeTriangle.p3.getX());
        fmLarge.setEntry(1,2, input.largeTriangle.p3.getY());
        fmLarge.setEntry(2,2, 1);
        
        LUDecomposition fd = new LUDecomposition(fm);
        
       // FieldLUDecomposition<Fraction> fd = new FieldLUDecomposition<>(fm);
        
        //Translation matrix
        RealMatrix m = fmLarge.multiply( fd.getSolver().getInverse());
        
        
        
        RealMatrix identity = new Array2DRowRealMatrix( 3, 3);
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                if (i == j) {
                    identity.setEntry(i, j, 1);
                } else {
                    identity.setEntry(i, j, 0);
                }
            }
        }
        
        
      //  
        
        /**
         * To solve, I just take a 
         * [ a b c       [ x
         *  d e f          y
         *  g h i ]  *     1 ]  
         *  
         *  so ax + by + c = 0 ; dx + ey + f = 0 gx + hy + i = 0;
         *  take 1st 2 and solve
         */
        RealMatrix sub = new Array2DRowRealMatrix(2, 2);
        RealMatrix sub2 = new Array2DRowRealMatrix(2, 1);
        
        for(int i = 0; i < 2; ++i) {
            for(int j = 0; j < 2; ++j) {
        sub.setEntry(i, j, m.subtract(identity).getEntry(i,j));
            }
        }
        
        sub2.setEntry(0,0, m.subtract(identity).getEntry(0,2)*-1);
        sub2.setEntry(1,0, m.subtract(identity).getEntry(1,2)*-1);
        
        RealMatrix  ans = 
                new LUDecomposition(sub).
                getSolver().getInverse().multiply(sub2);
        
        
        /*
         * Checks point should translate to itself
         */
        FieldMatrix<Fraction> pointM = new Array2DRowFieldMatrix<>(FractionField.getInstance(), 3, 1);        
        pointM.setEntry(0,0, fromDouble(ans.getEntry(0,0)));
        pointM.setEntry(1,0, fromDouble(ans.getEntry(1,0)));
        pointM.setEntry(2,0, new Fraction(1));
        
        //FieldMatrix<Fraction> trans = m.multiply(pointM);
        
        //And M - I * point = 0
        //FieldMatrix<Fraction> what2 = m.subtract(identity).multiply(pointM);
        
       // log.debug("Translated {}", trans);
        return String.format("Case #%d: %s %s", input.testCase, DoubleFormat.df6.format(ans.getEntry(0,0)), DoubleFormat.df6.format(ans.getEntry(1,0)));
    }
    
    static Fraction fromDouble(double d) {
        int intPart = DoubleMath.roundToInt(d, RoundingMode.DOWN);
        
        return new Fraction(d - intPart, 1e-5, 10000).add(intPart);
    }
    
    
    
    
    @Override
    public String handleCase(InputData input) {
        
      //  if (1==1)
        //    return solveUsingTranslationMatrix(input);
        final PointInt origin = new PointInt(0,0);
        
        /**
         * I think in the problem, the p1's always correspond.
         * 
         * Make p1 the origin.
         */
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
        
        return String.format("Case #%d: %s %s", input.testCase, DoubleFormat.df6.format(p.getX()), DoubleFormat.df6.format(p.getY()));
    }

}
