package codejam.y2010.chicks;

import java.util.ArrayList;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Rational;

import com.google.common.base.Preconditions;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String handleCase(InputData input) {

        int caseNumber = input.testCase;
        log.info("Starting calculating case {}", caseNumber);
        
        
        boolean[] canMakeIt = new boolean[input.numberChicks];
        
        for(int chick = 0; chick < input.numberChicks; ++chick) {
            int pos = input.chickLocations.get(chick) + input.minimumTime * input.chickSpeeds.get(chick);
            if (pos >= input.barnLocation) {
                canMakeIt[chick] = true;
            } else {
                canMakeIt[chick] = false;
            }
        }
        
        int madeIt = 0;
        int swapsNeeded = 0;
        int numberToSkip = 0;
        for(int chick = input.numberChicks -1; chick >= 0; --chick) {
            
            if (madeIt >= input.chicksToPass) {
                break;
            }
            if (!canMakeIt[chick]) {
                numberToSkip ++;
            } else {
                madeIt ++;
                swapsNeeded += numberToSkip;
            }
        }
        

        log.info("Done calculating answer case {}", caseNumber);
        
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + caseNumber + ": " + (madeIt >= input.chicksToPass ? swapsNeeded : "IMPOSSIBLE" ));
    }
    
    
    
    Rational[] getTimePosIntersection( int p1, int v1, int p2, int v2) {
        return getTimePosIntersection(Rational.fromInt(p1), Rational.fromInt(v1), Rational.fromInt(p2), Rational.fromInt(v2));
    }
    
    double getTimePosIntersection( double p1, double v1, double p2, double v2) {
    return 3.0;    
    }
    
    
    Rational[] getTimePosIntersection( Rational p1, Rational v1, Rational p2, Rational v2) {
        /*
         * p1b = p1 + t * v1
         * p2b = p2 + t * v2;
         * 
         * p1 + t * v1 = p2 + t * v2
         * p1 - p2 = t *v2 - t * v1
         * p1 - p2 = t (v2 - v1)
         * (p1 - p2) / (v2 - v1) = t
         */
        
        Rational t = p1.minus(p2).divide(v2.minus(v1));
        Rational newP1 = t.multiply(v1);
        Rational newP2 = t.multiply(v2);
        
        Preconditions.checkState(newP1.equals(newP2));
        
        return new Rational[] { t, newP1 };
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)  {
        
        
        InputData  input = new InputData(testCase);
        input.numberChicks = scanner.nextInt();
        input.chicksToPass = scanner.nextInt();
        input.barnLocation = scanner.nextInt();
        input.minimumTime = scanner.nextInt();
        
        input.chickLocations = new ArrayList<>();
        input.chickSpeeds = new ArrayList<>();
        
        for(int i = 0; i < input.numberChicks; ++i) {
            input.chickLocations.add(scanner.nextInt());
        }
        
        for(int i = 0; i < input.numberChicks; ++i) {
            input.chickSpeeds.add(scanner.nextInt());
        }
        
        return input;
        
    }

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            args = new String[] { "sample.txt" };
           // args = new String[] { "B-small-practice.in" };
//            args = new String[] { "B-large-practice.in" };
         }
         log.info("Input file {}", args[0]);

         Main m = new Main();
         Runner.goSingleThread(args[0], m, m);
         //Runner.go(args[0], m, m, new InputData(-1));

        
       
    }

    
}