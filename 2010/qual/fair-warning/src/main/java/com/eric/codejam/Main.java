package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

        log.info("Starting calculating case {}", caseNumber);
        
        BigInteger maxGcd = BigInteger.ONE;
        
        List<BigInteger> diffs = new ArrayList<>();
        
        for (int i = 0; i < input.events.length; ++i) {
            for(int j = i + 1; j < input.events.length; ++j) {
                diffs.add(input.events[i].subtract(input.events[j]).abs());
            }
        }
        
        BigInteger gcdCommon = diffs.get(0);
        
        for(int i = 1; i < diffs.size(); ++i) {
            gcdCommon = gcdCommon.gcd(diffs.get(i));
        }
        
        log.debug("GCD common {}", gcdCommon);
        
        BigInteger firstCandidate = null;
        //find the first T
        for (int i = 0; i < input.events.length; ++i) {
            BigInteger[] next = input.events[i].divideAndRemainder(gcdCommon);
            BigInteger remainer = next[1];
            if (!remainer.equals(BigInteger.ZERO)) {
                remainer = gcdCommon.subtract(remainer);
            }
           // log.debug("Remainder {}", remainer);
            firstCandidate = firstCandidate == null ? remainer : remainer.max(firstCandidate);
        }
        
        log.debug("First t possible {}", firstCandidate);
        
        //boolean[] ok = new boolean[input.events.length];
        
        for (int i = 0; i < input.events.length; ++i) {
            input.events[i] = input.events[i].add(firstCandidate) ;
        }
        
        for(int t = 0; t < 6; ++t) {
            log.debug("T is {}", t);
            boolean ok = true;
            for (int i = 0; i < input.events.length; ++i) {
                if (!input.events[i].divideAndRemainder(gcdCommon)[1].equals(BigInteger.ZERO)) {
                    ok = false;
                    continue;
                }
            }
            
            if (ok) {
                return ("Case #" + caseNumber + ": " + firstCandidate.add(BigInteger.valueOf(t).multiply(gcdCommon)));
            }
            
            for (int i = 0; i < input.events.length; ++i) {
                input.events[i] = input.events[i].add(gcdCommon) ;
            }
            
          
        }
        
        log.info("Done calculating answer case {}", caseNumber);
        
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + caseNumber + ": " );
    }
    
    
    @Override
    public InputData readInput(BufferedReader br, int testCase) throws IOException {
        
    
        String[] line = br.readLine().split(" ");
        
        int n = Integer.parseInt(line[0]);
        
        log.info("Reading data...Test case # {} ", testCase);
        
        log.info("Done Reading data...Test case # {} ", testCase);
        
        InputData  ip = new InputData(testCase);
        ip.events = new BigInteger[n];
        for (int i = 0; i <n; ++i) {
            ip.events[i] = new BigInteger(line[i+1]);
        }
        return ip;
        
    }

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
           args = new String[] { "sample.txt" };
           //args = new String[] { "smallInput.txt" };
           //args = new String[] { "largeInput.txt" };
        }
        log.info("Input file {}", args[0]);

        Main m = new Main();
     //   Runner.go(args[0], m, m, new InputData(-1));
        Runner.goSingleThread(args[0], m, m);
        
       
    }

    
}