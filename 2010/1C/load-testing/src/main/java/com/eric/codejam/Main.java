package com.eric.codejam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.InputData;
import com.eric.codejam.Main;
import com.eric.codejam.geometry.PointInt;
import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

        log.info("Starting calculating case {}", caseNumber);
        
        int numSteps = 0;
        long step = (long)input.L * input.C;
        while(step < (long)input.P) {
            step *= input.C;
            ++numSteps;
            //log.debug("num steps {} case {} step {}", numSteps, caseNumber, step);
        }
        
        int testsNeeded = 0;
        
        if (numSteps > 0) {
            testsNeeded = 1;
            while(IntMath.checkedPow(2, testsNeeded) - 1 < numSteps) {
                ++testsNeeded;
                log.debug("Tests needed {} for case {}", testsNeeded, caseNumber);
            }
        }

        log.info("Done calculating answer case {}", caseNumber);
        
        
        return ("Case #" + caseNumber + ": " + testsNeeded);
    }
    
    
    @Override
    public InputData readInput(BufferedReader br, int testCase) throws IOException {
        
    
        String[] line = br.readLine().split(" ");
        
        InputData  input = new InputData(testCase);
        
        input.L = Integer.parseInt(line[0]);
        input.P = Integer.parseInt(line[1]);
        input.C = Integer.parseInt(line[2]);
        
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