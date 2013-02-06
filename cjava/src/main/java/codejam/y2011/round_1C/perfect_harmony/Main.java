package codejam.y2011.round_1C.perfect_harmony;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;
import com.google.common.math.LongMath;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles{

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String[] getDefaultInputFiles() {
       //return new String[] {"sample.in"};
        return new String[] {"C-small-practice.in", "C-large-practice.in"};
    }
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
        InputData in = new InputData(testCase);
        in.N = scanner.nextInt();
        in.L = scanner.nextLong();
        in.H = scanner.nextLong();
        in.freq = new long[in.N];
        
        for(int i = 0; i < in.N; ++i) {
            in.freq[i] = scanner.nextLong();
        }
        
        return in;
    }
    
    
    public boolean isPrime (long n)
    {
       if (n<=1) return false;
       if (n==2) return true;
       if (n%2==0) return false;
       long m=LongMath.sqrt(n, RoundingMode.UP);

       //100 000 000 / 2 iterations is OK!
       for (long i=3; i<=m; i+=2) {
           
          if (n%i==0)
             return false;
       }

       return true;
    }
    
    public List<Long> getDivisors(long n) {
        ArrayList<Long> ret = new ArrayList<Long>();
        long m=LongMath.sqrt(n, RoundingMode.UP);
        
        for(long d = 1; d <= m; ++d) {
            if (n%d == 0) {
                ret.add(d);
                ret.add(n / d);
            }
        }
        
        Collections.sort(ret, Ordering.natural().reverse());
        
        return ret;
    }
    
    @Override
    public String handleCase(InputData in) {
     
        //our frequency has to be <= anothers frequency
        
        long[] gcdDec = new long[in.N];
        
        
        Arrays.sort(in.freq);
        
        /**
         * Calculate least common multiple
         * lcmAsc[i] = lcm of frequences 0 .. i inclusive
         */
        BigInteger[] lcmAsc  = new BigInteger[in.N];
        lcmAsc[0] = BigInteger.valueOf(in.freq[0]); 
        for(int freq = 1; freq < in.N; ++freq) {
            //LCM(A, B) = A * B / gcd(A, B)
            BigInteger gcd = BigInteger.valueOf(in.freq[freq]).gcd( lcmAsc[freq-1]);
            lcmAsc[freq] = lcmAsc[freq-1].multiply(BigInteger.valueOf(in.freq[freq])).divide(gcd);
        }
        
        gcdDec[in.N-1] = in.freq[in.N - 1];
        for(int freq = in.N - 2; freq >= 0; --freq) {
            gcdDec[freq] = BigInteger.valueOf(gcdDec[freq+1]).gcd(BigInteger.valueOf(in.freq[freq])).longValue();
        }
        
        //Position     0    1   2   3   4
        //Frequencies     0   1   2   3
        log.debug("gcd des: {}", (Object)gcdDec);

        log.debug("LCM asc: {}", (Object) lcmAsc);
        log.debug("Freq: {}", in.freq);
        for(int guessPosition = 0; guessPosition <= in.N; ++guessPosition) {
            //Try to find a    in.freq[other-1]  <= frequency <= in.freq[other]
            //long low = otherFreq == 0 ? in.L : Math.max(in.L, in.freq[otherFreq-1]);
            
            //LCM of all frequences less than our position
            BigInteger lcm = guessPosition == 0 ? BigInteger.ONE : lcmAsc[guessPosition-1];
            
            //GCD of all frequences greater
            long gcd = guessPosition == in.N ? -1 : gcdDec[guessPosition];
            
            
            log.debug("In position {} lcm {} gcd {}", guessPosition, lcm, gcd);
            
            if (gcd == -1) {
                
                long lowK1 = BigInteger.valueOf(in.L).divide(lcm).longValue();
                
                if (lowK1 == 0)
                    lowK1 = 1;
                
                long uppK1 = LongMath.divide(in.H, lcm.longValue(), RoundingMode.UP);
                
                log.debug("Lower bound k1 {} upp {} ", lowK1, uppK1);
                
                for(long k1 = lowK1; k1 <= uppK1; ++k1) {
                    
                    BigInteger ans = lcm.multiply(BigInteger.valueOf(k1));
                    
                    if (BigInteger.valueOf(in.L).compareTo(ans) <= 0 && BigInteger.valueOf(in.H).compareTo(ans) >= 0) {
                        return String.format("Case #%d: %s", in.testCase, ans.toString());
                    }
                }

            } else {
                
                if (in.L > gcd)
                    continue; 
                
                if (BigInteger.valueOf(in.H).compareTo(lcm) < 0) 
                    continue;
                
                BigInteger[] div = BigInteger.valueOf(gcd).divideAndRemainder(lcm);
                
                if (!div[1].equals(BigInteger.ZERO))
                    continue;
               
                //P = k1 * lcm
                //P = gcd / k2
                //C = k1 * k2
                
                
                long uppK2 = LongMath.divide(gcd, in.L, RoundingMode.UP);
                
                long C =   div[0].longValue(); //gcd / lcm.longValue();
                
                List<Long> k2Divisors = getDivisors(gcd);
                
                log.debug("In position {} lcm {} gcd {}. sq gcd{}  ", guessPosition,
                        lcm, gcd);

                for (long k2 : k2Divisors) {

                    if (k2 > uppK2)
                        continue;

                    if (C % k2 != 0)
                        continue;

                    Preconditions.checkState(C % k2 == 0);
                    long k1 = C / k2;

                    BigInteger ans = lcm.multiply(BigInteger.valueOf(k1));

                    if (BigInteger.valueOf(in.L).compareTo(ans) <= 0 && BigInteger.valueOf(in.H).compareTo(ans) >= 0) {
                        return String.format("Case #%d: %s", in.testCase, ans.toString());
                    }

                }

            }

        }

        return String.format("Case #%d: NO", in.testCase);
        
    }

}
