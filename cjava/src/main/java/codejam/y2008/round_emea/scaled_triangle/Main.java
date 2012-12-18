package codejam.y2008.round_emea.scaled_triangle;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;

import com.google.common.base.Preconditions;

import codejam.utils.geometry.Line;
import codejam.utils.geometry.Point;
import codejam.utils.geometry.PointInt;
import codejam.utils.geometry.TriangleInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseHandler<InputData>,
        TestCaseInputScanner<InputData>, DefaultInputFiles {

    @Override
    public String[] getDefaultInputFiles() {
    //    return new String[] {"sample.in"};
        return new String[] {"A-small-practice.in"};
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData i = new InputData(testCase);
        i.largeTriangle = new TriangleInt(scanner.nextInt(),scanner.nextInt(),scanner.nextInt(),scanner.nextInt(),scanner.nextInt(),scanner.nextInt());
        i.smallTriangle = new TriangleInt(scanner.nextInt(),scanner.nextInt(),scanner.nextInt(),scanner.nextInt(),scanner.nextInt(),scanner.nextInt());
        return i;
    }
    
    //x' = x cos(θ) - y sin(θ)

    //y' = x \sin \theta + y \cos \theta\,
    
    
    
    @Override
    public String handleCase(InputData input) {
        PointInt origin = new PointInt(0,0);
        
        PointInt p2LargeTranslated = input.largeTriangle.p2.translate(input.largeTriangle.p1);
        PointInt p3LargeTranslated = input.largeTriangle.p3.translate(input.largeTriangle.p1);
        
        double angP2 = Math.atan2(p2LargeTranslated.getY(), p2LargeTranslated.getX()) * 180D / Math.PI;
        double angP3 = Math.atan2(p3LargeTranslated.getY(), p3LargeTranslated.getX()) * 180D / Math.PI;
        
        PointInt p2SmallTranslated = input.smallTriangle.p2.translate(input.smallTriangle.p1);
        PointInt p3SmallTranslated = input.smallTriangle.p3.translate(input.smallTriangle.p1);

        double angSmP2 = Math.atan2(p2SmallTranslated.getY(), p2SmallTranslated.getX()) * 180D / Math.PI;
        double angSmP3 = Math.atan2(p3SmallTranslated.getY(), p3SmallTranslated.getX()) * 180D / Math.PI;
        
        double rotAng = angP2 - angSmP2;
        double rot2Ang = angP3 - angSmP3;
        
        double scale = p2LargeTranslated.distance(origin) / p2SmallTranslated.distance(origin);
        double scale2 = p3LargeTranslated.distance(origin) / p3SmallTranslated.distance(origin);
        
        double rotAngLtoS = (360 - rotAng) * Math.PI / 180;
        double rotAngStoL = rotAng * Math.PI / 180;
        
        double scaleLtoS = 1D / scale;
        Point p2LtoS = input.largeTriangle.p2.translate(input.largeTriangle.p1).toPoint().rotate((360-rotAng) * Math.PI / 180D).scale(1 / scale).translate(input.smallTriangle.p1.toPoint().scale(-1));
        Point p3LtoS = input.largeTriangle.p3.translate(input.largeTriangle.p1).toPoint().rotate((360-rotAng) * Math.PI / 180D).scale(1 / scale).translate(input.smallTriangle.p1.toPoint().scale(-1));
        
        double x = input.largeTriangle.p2.getX();
        double y = input.largeTriangle.p2.getY();
        double xSmall =  ( (x - input.largeTriangle.p1.getX()) *
                Math.cos(rotAngLtoS) -
                (y - input.largeTriangle.p1.getY()) * 
                Math.sin(rotAngLtoS) ) * scaleLtoS + input.smallTriangle.p1.getX();
        
        //Solving for x in terms of yL
        double xIntercept = (  
                - input.largeTriangle.p1.getX() * Math.cos(rotAngLtoS) * scaleLtoS
                + input.largeTriangle.p1.getY() * Math.sin(rotAngLtoS) * scaleLtoS
                + input.smallTriangle.p1.getX() ) / (1-Math.cos(rotAngLtoS)*scaleLtoS);
        
        double coef = -Math.sin(rotAngLtoS) * scaleLtoS / (1-Math.cos(rotAngLtoS)*scaleLtoS);
        
        double slope = Math.sin(rotAngLtoS) * scaleLtoS / (1-Math.cos(rotAngLtoS)*scaleLtoS);
        
        double yIntercept = (  
                - input.largeTriangle.p1.getY() * Math.cos(rotAngLtoS) * scaleLtoS
                - input.largeTriangle.p1.getX() * Math.sin(rotAngLtoS) * scaleLtoS
                + input.smallTriangle.p1.getY() ) / (1-Math.cos(rotAngLtoS)*scaleLtoS);
        //x = coef * yL + cons
        
        double xIntercept2 = (  
                - input.smallTriangle.p1.getX() * Math.cos(rotAngStoL) * scale
                + input.smallTriangle.p1.getY() * Math.sin(rotAngStoL) * scale
                + input.largeTriangle.p1.getX() ) / (1-Math.cos(rotAngStoL)*scale);
        
        double coef2 = -Math.sin(rotAngStoL) * scale / (1-Math.cos(rotAngStoL)*scale);
        
        double slope2 = Math.sin(rotAngLtoS) * scale / (1-Math.cos(rotAngLtoS)*scale);
        
        double yIntercept2 = (  
                - input.smallTriangle.p1.getY() * Math.cos(rotAngStoL) * scale
                - input.smallTriangle.p1.getX() * Math.sin(rotAngStoL) * scale
                + input.largeTriangle.p1.getY() ) / (1-Math.cos(rotAngStoL)*scale); 
        
        //x = coef2 * ys + cons2
        
        Line l1 = Line.dc.compare(coef,0d) == 0 ? new Line(new Point(xIntercept, 0), new Point(xIntercept, 2)) : new Line(1 / coef, -xIntercept/coef);
        Line l2 = Line.dc.compare(coef2, 0d) == 0 ? new Line(new Point(xIntercept2, 0), new Point(xIntercept2, 2)) : new Line(1 / coef2, -xIntercept2/coef2);
        
        Line fxLargeToEqualY = new Line(slope, yIntercept);
        
        Line fxSmallToEqualY = new Line(slope2, yIntercept2);
        
        Point p = null;
        
        if (l1.equals(l2) && !l1.equals(fxLargeToEqualY)) {
            p = l1.getIntersection(fxLargeToEqualY);
            
            //do intersection of line and inner triangle
            /*
            p  = l1.intersectsSegment(input.smallTriangle.p1.toPoint(),input.smallTriangle.p2.toPoint());
            if (p == null)  
            p  = l1.intersectsSegment(input.smallTriangle.p1.toPoint(),input.smallTriangle.p3.toPoint());
            if (p == null)  
                p  = l1.intersectsSegment(input.smallTriangle.p2.toPoint(),input.smallTriangle.p3.toPoint());
            */
            if (p== null) {
                return String.format("Case #%d: No Solution", input.testCase);
            }
        } else if (l1.equals(l2) && l1.equals(fxLargeToEqualY)) {
            return "Chaos";
        }
        
        DecimalFormat df = new DecimalFormat("0.######");
        if (p != null) {
            Point c1 = l1.getPointGivenX(p.getX());
            Point c2 = l2.getPointGivenX(p.getX());
            
            Point p1 = l1.getPointGivenX(input.largeTriangle.p1.getX());
            Point p2 = l1.getPointGivenX(input.largeTriangle.p2.getX());
            Point p3 = l1.getPointGivenX(input.largeTriangle.p3.getX());
            
            Point ps1 = l2.getPointGivenX(input.smallTriangle.p1.getX());
            Point ps2 = l2.getPointGivenX(input.smallTriangle.p2.getX());
            Point ps3 = l2.getPointGivenX(input.smallTriangle.p3.getX());
            return String.format("Case #%d: %s %s", input.testCase, df.format(p.getX()), df.format(p.getY()));
        }
        
        Preconditions.checkState(Line.dc.compare(coef,coef2) != 0);
        p = l1.getIntersection(l2);
        //Line 1 and 2 are for x in terms of y, so must reverse coords
        //p  = new Point(p.getY(), p.getX());
        
        if (!input.smallTriangle.pointInTriangle(p)) {
            return String.format("Case #%d: No Solution", input.testCase);            
        }
        
        double tes = coef * 1.538462 + xIntercept;
        double tes2 = coef2 * 1.538462 + xIntercept2;
        
        final Point cp =  new Point( 2.692308, 1.538462);
        Point cpLtoS = cp.translate(input.largeTriangle.p1.toPoint()).rotate((360-rotAng) * Math.PI / 180D).scale(1 / scale).translate(input.smallTriangle.p1.toPoint().scale(-1));
        
        Point p2StoL = input.smallTriangle.p2.translate(input.smallTriangle.p1).toPoint().rotate(rotAng * Math.PI / 180D).scale(scale).translate(input.largeTriangle.p1.toPoint().scale(-1));
        Point p3StoL = input.smallTriangle.p3.translate(input.smallTriangle.p1).toPoint().rotate(rotAng * Math.PI / 180D).scale(scale).translate(input.largeTriangle.p1.toPoint().scale(-1));
        Point cpStoL = cp.translate(input.smallTriangle.p1.toPoint()).rotate(rotAng * Math.PI / 180D).scale(scale).translate(input.largeTriangle.p1.toPoint().scale(-1));
        
        
        df.setRoundingMode(RoundingMode.HALF_UP);
        return String.format("Case #%d: %s %s", input.testCase, df.format(p.getX()), df.format(p.getY()));
    }

}
