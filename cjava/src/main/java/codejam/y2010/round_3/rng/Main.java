package codejam.y2010.round_3.rng;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Prime;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

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
    
    public long determinant(int S_1, int S_2, int S_3, int S_4) {
        //Three points (S_1, S_2) (S2, S3) and (S3,S4)
        
        //Normalize points, make p1 origin
        int p1_x = S_1;
        int p1_y = S_2;
        
        int p2_x = S_2 - p1_x;
        int p2_y = S_3 - p1_y;
        
        int p3_x = S_3 - p1_x;
        int p3_y = S_4 - p1_y;
        
        //Det ab cd
        long a = p2_x;
        long b = p2_y;
        long c = p3_x;
        long d = p3_y;
        return Math.abs(LongMath.checkedMultiply(a,d)-LongMath.checkedMultiply(b,c));
    }
    
    public int[] getAandBGivenP(int S1, int S2, int S3, int P) {
        
        //Slope is normally change Y / change X.  But we must use mod math
        
        long s3_s2 = (S3 - S2) % P;
        int s2_s1 = (S2 - S1) % P;
        
        BigInteger bi = BigInteger.valueOf(s2_s1);
        BigInteger mi = bi.modInverse(BigInteger.valueOf(P));
        
        int a = Ints.checkedCast(( LongMath.checkedMultiply(s3_s2, mi.intValue())) % P);
        if (a < 0) 
            a+=P;
        int b = (S2 - Ints.checkedCast(LongMath.checkedMultiply(a, S1) % P)) % P;
        if (b < 0)
            b+=P;
        
        return new int[] {a, b};
    }
    
    public int getNext(int S1, int S2, int S3, int S_last, int P) {
        int[] a_b = getAandBGivenP(S1, S2, S3, P);
        long a = a_b[0];
        long b = a_b[1];
        int n = Ints.checkedCast((a * S_last + b) % P);
       // log.debug("a {} b {} p {} produces {}", a,b,prime,n);
        Preconditions.checkState(n >= 0);
        return n;
    }
    
    static List<SortedSet<Integer>> allPrimes = new ArrayList<>(6);
    
    static {
        for(int d = 1; d <= 6; ++d)
        allPrimes.add(new TreeSet<>(Prime.generatePrimes(IntMath.pow(10,d))));
    }
    
    public Set<Integer> getPrimeFactorsUsingParraleogam(SortedSet<Integer> primes, InputData input, int maxSeed) {
        /*
         * The area of a parralelogram contained by three points must be a multiple of p.
         * 
         * All points (S_i, S_i+1) fall on lines given by
         * y=ax+b
         * y=ax+b-p
         * y=ax+b-2p
         * y=ax+b-3p
         * 
         * Thus the parrelogram of points from these lines must have an area P*N
         */
    
        Set<Integer> allPrimeFactors = new HashSet<>();

        for (int k = 0; k <= input.seedOutput.length - 4; ++k) {
            long det = determinant(input.seedOutput[k],
                    input.seedOutput[k + 1], input.seedOutput[k + 2],
                    input.seedOutput[k + 3]);
            Set<Integer> primeFactors = new HashSet<>();
            //log.debug("Det is {}", det);
            
            if (det == 0) {
                primeFactors = primes;
            } else {
                for (int p : primes) {
                    Preconditions.checkArgument(p > maxSeed);

                    if (det % p == 0) {
                        primeFactors.add(p);
                    }
                }
            }

            //log.debug("Prime factors for k {} = {}", k, primeFactors);
            if (k == 0) {
                allPrimeFactors = primeFactors;
            } else {
                allPrimeFactors = Sets.intersection(allPrimeFactors,
                        primeFactors);
            }
        }

        return allPrimeFactors;
    }
    
    @Override
    public String handleCase(InputData input) {

        int caseNumber = input.testCase;
        //log.info( "case {}.  Max prime {}  sequence {}", caseNumber, IntMath.pow(10,input.D), input.seedOutput);
        
        SortedSet<Integer> primes = allPrimes.get(input.D - 1);
        
        int nextNumber = -1;
        
        int maxSeed = Collections.max(Arrays.asList(ArrayUtils.toObject(input.seedOutput)));
        
        if (input.K >= 2 && input.seedOutput[0] == input.seedOutput[1]) {
            nextNumber = input.seedOutput[0];
        } else if (input.K >= 3) {
        
            Set<Integer> primesToCheck = input.K >= 4 ? getPrimeFactorsUsingParraleogam(primes.tailSet(maxSeed+1),input,maxSeed) : primes.tailSet(maxSeed+1);
                                
            for (int prime : primesToCheck) {                
                int n = getNext(input.seedOutput[0],
                        input.seedOutput[1], input.seedOutput[2], input.seedOutput[input.seedOutput.length - 1], prime);
                                                
                if (nextNumber != -1 && nextNumber != n) {
                    nextNumber = -1;
                    break;
                } else {
                    nextNumber = n;
                }
            }
        } 

      //   log.info("Done calculating answer case {}", caseNumber);
        
        if (nextNumber >= 0) {
            log.info("case {}.  Max prime {}  sequence {}", caseNumber,
                    IntMath.pow(10, input.D), input.seedOutput);
            return ("Case #" + caseNumber + ": " + nextNumber);
        } else {
            log.info("case {}.  Max prime {}  sequence {}", caseNumber,
                    IntMath.pow(10, input.D), input.seedOutput);

            return ("Case #" + caseNumber + ": I don't know.");
        }
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
        
        InputData  input = new InputData(testCase);
        
        input.D = scanner.nextInt();
        input.K = scanner.nextInt();
        
        input.seedOutput = new int[input.K];
        
        for(int i = 0; i < input.K; ++i) {
            input.seedOutput[i] = scanner.nextInt();
        }
        
        return input;
        
    }

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
           // args = new String[] { "sample.txt" };
         //   args = new String[] { "A-small-practice.in" };
            args = new String[] { "A-large-practice.in" };
         }
         log.info("Input file {}", args[0]);

         Main m = new Main();
         Runner.goSingleThread(args[0], m, m);
         //Runner.go(args[0], m, m, new InputData(-1));

        
       
    }

    
}