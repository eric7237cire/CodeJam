package codejam.y2010.round_qual.fair_warning;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String handleCase(InputData input) {
        int caseNumber = input.testCase;

        log.info("Starting calculating case {}", caseNumber);
        
        
        List<BigInteger> diffs = new ArrayList<>();
        
        //Can just do all events - first because
        //in gcd algorithm..see doc
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
    public InputData readInput(Scanner scanner, int testCase) {
        
    
        int n = scanner.nextInt();
        
        log.info("Reading data...Test case # {} ", testCase);
        
        log.info("Done Reading data...Test case # {} ", testCase);
        
        InputData  ip = new InputData(testCase);
        ip.events = new BigInteger[n];
        for (int i = 0; i <n; ++i) {
            ip.events[i] = scanner.nextBigInteger();
        }
        return ip;
        
    }

    


    public Main() {
        super();
    }
    
    
    
}