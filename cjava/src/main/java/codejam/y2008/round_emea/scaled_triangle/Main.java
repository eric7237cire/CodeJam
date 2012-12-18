package codejam.y2008.round_emea.scaled_triangle;

import java.util.Scanner;

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
        return new String[] {"sample.in"};
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
        double cons = (  
                - input.largeTriangle.p1.getX() * Math.cos(rotAngLtoS) * scaleLtoS
                + input.largeTriangle.p1.getY() * Math.sin(rotAngLtoS) * scaleLtoS
                + input.smallTriangle.p1.getX() ) / (1-Math.cos(rotAngLtoS)*scaleLtoS);
        
        double coef = -Math.sin(rotAngLtoS) * scaleLtoS / (1-Math.cos(rotAngLtoS)*scaleLtoS);
        
        double tes = coef * 1.538462 + cons;
        
        final Point cp =  new Point( 2.692308, 1.538462);
        Point cpLtoS = cp.translate(input.largeTriangle.p1.toPoint()).rotate((360-rotAng) * Math.PI / 180D).scale(1 / scale).translate(input.smallTriangle.p1.toPoint().scale(-1));
        
        Point p2StoL = input.smallTriangle.p2.translate(input.smallTriangle.p1).toPoint().rotate(rotAng * Math.PI / 180D).scale(scale).translate(input.largeTriangle.p1.toPoint().scale(-1));
        Point p3StoL = input.smallTriangle.p3.translate(input.smallTriangle.p1).toPoint().rotate(rotAng * Math.PI / 180D).scale(scale).translate(input.largeTriangle.p1.toPoint().scale(-1));
        Point cpStoL = cp.translate(input.smallTriangle.p1.toPoint()).rotate(rotAng * Math.PI / 180D).scale(scale).translate(input.largeTriangle.p1.toPoint().scale(-1));
        return "bo";
    }

}
