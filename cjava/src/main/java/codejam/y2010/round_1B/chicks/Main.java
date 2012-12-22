package codejam.y2010.round_1B.chicks;

import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.math3.fraction.Fraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    @Override
    public String[] getDefaultInputFiles() {
       //return new String[] {"sample.in"};
        return new String[] {"B-small-practice.in", "B-large-practice.in"};
    }
    
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
    
    
    
    Fraction[] getTimePosIntersection( int p1, int v1, int p2, int v2) {
        return getTimePosIntersection(new Fraction(p1), new Fraction(v1), 
                new Fraction(p2), new Fraction(v2));
    }
    
   
    
    
    Fraction[] getTimePosIntersection( Fraction p1, Fraction v1, Fraction p2, Fraction v2) {
        /*
         * p1b = p1 + t * v1
         * p2b = p2 + t * v2;
         * 
         * p1 + t * v1 = p2 + t * v2
         * p1 - p2 = t *v2 - t * v1
         * p1 - p2 = t (v2 - v1)
         * (p1 - p2) / (v2 - v1) = t
         */
        
        Fraction t = p1.subtract(p2).divide(v2.subtract(v1));
        Fraction newP1 = t.multiply(v1);
        Fraction newP2 = t.multiply(v2);
        
        Preconditions.checkState(newP1.equals(newP2));
        
        return new Fraction[] { t, newP1 };
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

    
    

    
}