package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;
import com.eric.codejam.utils.Prime;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    public boolean bruteForce(InputData input) {
        List<Integer> primes = Prime.generatePrimes(IntMath.pow(10,input.D));
        
        boolean  ambig = false; 
        int nextNumber = -1;
        int maxSeed = Collections.max(Arrays.asList(ArrayUtils.toObject(input.seedOutput)));
        
        outerLoop:
        for(int p : primes) {
            if (p <= maxSeed)
                continue;
            for(int a = 0; a < p; ++a) {
                
                for(int b = 0; b < p; ++b) {
                    boolean valid=true;
                    for(int s = 1; s < input.K; ++s) {
                        if ( (a * input.seedOutput[s-1] + b) % p != input.seedOutput[s])
                        {
                            valid = false;
                            break;
                        }
                    }
                    if (valid) {
                        int thisNextNum = (a * input.seedOutput[input.seedOutput.length-1] + b) % p; 
                        if (nextNumber == -1) {
                            nextNumber = thisNextNum;
                        } else if (nextNumber != thisNextNum){
                            ambig = true;
                            log.debug("Second Match a {} b {} p {}  numbers next {}  {}", a, b, p, nextNumber, thisNextNum);
                            break outerLoop;
                        }
                        
                        
                    }
                }
                
            }
        }
        
        return ambig;
    }
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

        log.info("Starting calculating case {}", caseNumber);
        
        List<Integer> primes = Prime.generatePrimes(IntMath.pow(10,input.D));
        
        boolean  ambig = false; 
        int nextNumber = -1;
        
        int maxSeed = Collections.max(Arrays.asList(ArrayUtils.toObject(input.seedOutput)));
        
        if (input.K == 1) {
            if (input.seedOutput[0] == 0) {
                //ambig = false;
                //nextNumber = 0;
                ambig=true;
            } else {
                ambig = true;
            }
        } else {
        
        outerLoop:
        for(int p : primes) {
            if (p <= maxSeed)
                continue;
            for(int a = 0; a < p; ++a) {
                
                //Calculate a(s1) mod p
                int a_s = a * input.seedOutput[0] % p;
                
                Preconditions.checkState(a_s >= 0 && a_s < p);
                // a(s1)%p + b%p <= 2P - 2
                // a(s1)%p + b%p = s2 or s2 + p
                int b1 = input.seedOutput[1] - a_s;
                int b2 = input.seedOutput[1]+p - a_s;
                int b;
                if (0 <= b1 && b1 < p) {
                    Preconditions.checkState(!(0 <= b2 && b2 < p));
                    b = b1; 
                } else if (0 <= b2 && b2 < p){
                    Preconditions.checkState(!(0 <= b1 && b1 < p));
                    b=b2;
                } else {
                    break;
                }
                Preconditions.checkArgument(b >= 0 && b < p);
                
                Preconditions.checkState( (a * input.seedOutput[0] + b) % p == input.seedOutput[1]);
                        
                //for(int b = 0; b < p; ++b) {
                    boolean valid=true;
                    for(int s = 1; s < input.K; ++s) {
                        if ( (a * input.seedOutput[s-1] + b) % p != input.seedOutput[s])
                        {
                            valid = false;
                            break;
                        }
                    }
                    if (valid) {
                        int thisNextNum = (a * input.seedOutput[input.seedOutput.length-1] + b) % p; 
                        if (nextNumber == -1) {
                            log.debug("First Match a {} b {} p {}  numbers next {}", a, b, p, thisNextNum);
                            nextNumber = thisNextNum;
                        } else if (nextNumber != thisNextNum){
                            ambig = true;
                            log.debug("Second Match a {} b {} p {}  numbers next {}", a, b, p, thisNextNum);
                            break outerLoop;
                        }
                        //log.debug("Match a {} b {} p {}", a, b, p);
                        
                    }
               // }
                
            }
        }
        
        }

        if (input.D <= 2) {
//        boolean check = bruteForce(input);
        
  //      Preconditions.checkState(check == ambig);
        }
         log.info("Done calculating answer case {}", caseNumber);
        
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        if (!ambig)
        return ("Case #" + caseNumber + ": " + nextNumber);
        else {
            return ("Case #" + caseNumber + ": I don't know");
        }
    }
    
    
    @Override
    public InputData readInput(BufferedReader br, int testCase) throws IOException {
        
        //Scanner sc = new Scanner(br);
        
        String[] line = br.readLine().split(" ");
        
        InputData  input = new InputData(testCase);
        
        input.D = Integer.parseInt(line[0]);
        input.K = Integer.parseInt(line[1]);
        
        input.seedOutput = new int[input.K];
        line = br.readLine().split(" ");
        for(int i = 0; i < input.K; ++i) {
            input.seedOutput[i] = Integer.parseInt(line[i]);
        }
        
        return input;
        
    }

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            args = new String[] { "sample.txt" };
            //args = new String[] { "A-small-practice.in" };
//            args = new String[] { "B-large-practice.in" };
         }
         log.info("Input file {}", args[0]);

         Main m = new Main();
         Runner.goSingleThread(args[0], m, m);
         //Runner.go(args[0], m, m, new InputData(-1));

        
       
    }

    
}