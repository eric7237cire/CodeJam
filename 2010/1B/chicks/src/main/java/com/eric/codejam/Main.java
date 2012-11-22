package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;
import com.eric.codejam.utils.Rational;
import com.google.common.base.Preconditions;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

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
    public InputData readInput(BufferedReader br, int testCase) throws IOException {
        
    
        String[] line = br.readLine().split(" ");
        
        InputData  input = new InputData(testCase);
        input.numberChicks = Integer.parseInt(line[0]);
        input.chicksToPass = Integer.parseInt(line[1]);
        input.barnLocation = Integer.parseInt(line[2]);
        input.minimumTime = Integer.parseInt(line[3]);
        
        input.chickLocations = new ArrayList<>();
        input.chickSpeeds = new ArrayList<>();
        
        line = br.readLine().split(" ");
        for(int i = 0; i < input.numberChicks; ++i) {
            input.chickLocations.add(Integer.parseInt(line[i]));
        }
        
        line = br.readLine().split(" ");
        for(int i = 0; i < input.numberChicks; ++i) {
            input.chickSpeeds.add(Integer.parseInt(line[i]));
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