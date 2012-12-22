package codejam.y2011.round_1C.perfect_harmony;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.math.BigIntegerMath;
import com.google.common.math.LongMath;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Direction;
import codejam.utils.utils.GridChar;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles{

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String[] getDefaultInputFiles() {
       return new String[] {"sample.in"};
       // return new String[] {"B-small-practice.in", "B-large-practice.in"};
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

       for (long i=3; i<=m; i+=2)
          if (n%i==0)
             return false;

       return true;
    }
    
    @Override
    public String handleCase(InputData in) {
     
        //our frequency has to be <= anothers frequency
        
        BigInteger[] gcdDec = new BigInteger[in.N];
        //long[] gcdDes = new long[in.N];
        //BigInteger[] prodDes = new BigInteger[in.N];
        BigInteger[] lcmAsc  = new BigInteger[in.N];
        
        Arrays.sort(in.freq);
        
        lcmAsc[0] = BigInteger.valueOf(in.freq[0]); 
        for(int freq = 1; freq < in.N; ++freq) {
            
            BigInteger gcd = BigInteger.valueOf(in.freq[freq]).gcd( lcmAsc[freq-1]);
            lcmAsc[freq] = lcmAsc[freq-1].multiply(BigInteger.valueOf(in.freq[freq])).divide(gcd);
        }
        
        gcdDec[in.N-1] = BigInteger.valueOf(in.freq[in.N - 1]);
        for(int freq = in.N - 2; freq >= 0; --freq) {
            gcdDec[freq] = gcdDec[freq+1].gcd(BigInteger.valueOf(in.freq[freq]));
        }
        
        //Position     0    1   2   3   4
        //Frequencies     0   1   2   3
        log.debug("gcd des: {}", (Object)gcdDec);
//        log.debug("gcd des: {}", gcdDes);
//        log.debug("Prod des: {}", (Object) prodDes);
        log.debug("LCM asc: {}", (Object) lcmAsc);
        log.debug("Freq: {}", in.freq);
        for(int guessPosition = 0; guessPosition <= in.N; ++guessPosition) {
            //Try to find a    in.freq[other-1]  <= frequency <= in.freq[other]
            //long low = otherFreq == 0 ? in.L : Math.max(in.L, in.freq[otherFreq-1]);
            
            //LCM of all frequences less than our position
            BigInteger lcm = guessPosition == 0 ? BigInteger.ONE : lcmAsc[guessPosition-1];
            
            //GCD of all frequences greater
            BigInteger gcd = guessPosition == in.N ? null : gcdDec[guessPosition];
            
            
            log.debug("In position {} lcm {} gcd {}", guessPosition, lcm, gcd);
            
            if (gcd == null) {
//                BigInteger low = lcm.divide(BigInteger.valueOf(in.L));
//                if (low.equals(BigInteger.ZERO)) {
//                    return String.format("Case #%d: NO", in.testCase);
//                }
                
                long lowK1 = BigInteger.valueOf(in.L).divide(lcm).longValue();
                
                if (lowK1 == 0)
                    lowK1 = 1;
                
                long uppK1 = LongMath.divide(in.H, lcm.longValue(), RoundingMode.UP);
                
                log.info("Lower bound k1 {} upp {} ", lowK1, uppK1);
                
                for(long k1 = lowK1; k1 <= uppK1; ++k1) {
                    
                    BigInteger ans = lcm.multiply(BigInteger.valueOf(k1));
                    
                    if (BigInteger.valueOf(in.L).compareTo(ans) <= 0 && BigInteger.valueOf(in.H).compareTo(ans) >= 0) {
                        return String.format("Case #%d: %s", in.testCase, ans.toString());
                    }
                }

//                BigInteger ans = low.multiply(lcm);
//
//                if (BigInteger.valueOf(in.L).compareTo(ans) >= 0 && BigInteger.valueOf(in.H).compareTo(ans) <= 0) {
//                    return String.format("Case #%d: %s", in.testCase, ans.toString());
//                }

            } else {
                
                if (BigInteger.valueOf(in.L).compareTo(gcd) > 0)
                    continue; 
                
                if (BigInteger.valueOf(in.H).compareTo(lcm) < 0) 
                    continue;
                
                BigInteger[] div = gcd.divideAndRemainder(lcm);
                if (!div[1].equals(BigInteger.ZERO))
                    continue;

                log.info("In position {} lcm {} gcd {}.  div = {}", guessPosition, lcm, gcd, div[0]);

                //P = k1 * lcm
                //P = gcd / k2
                //C = k1 * k2
                
                long lowK1 = BigInteger.valueOf(in.L).divide(lcm).longValue();
                long lowK2 = LongMath.divide(gcd.longValue(), in.H, RoundingMode.UP);
                
                if (lowK1 == 0)
                    lowK1 = 1;
                
                if (lowK2 == 0)
                    lowK2 = 1;
                
                long uppK1 = LongMath.divide(in.H, lcm.longValue(), RoundingMode.UP);
                long uppK2 = LongMath.divide(gcd.longValue(), in.L, RoundingMode.UP);
                long C = div[0].longValue();
                
                long tryK2 = uppK2;
                long tryK1 = lowK1;
                
                log.info("Lower bound k2 {} up {}  C {}", lowK2, uppK2, C);
                log.info("Lower bound k1 {} upp {}  C {}", lowK1, uppK1, C);
                
                log.info("Is prime testing");
                
                boolean isPrime = isPrime(gcd.longValue());
                
                if (isPrime) {
                    uppK2 = 1;
                    tryK2 = 1;
                }

                while (tryK2 >= lowK2 && tryK1 <= uppK1) {
                    //log.info("Tryk2 {} k1 {}", tryK2, tryK1);
                    
                    if (C % tryK2 != 0) {
                        tryK2--;
                    } else {
                        long k1 = C / tryK2;

                        BigInteger ans = lcm.multiply(BigInteger.valueOf(k1));

                        if (BigInteger.valueOf(in.L).compareTo(ans) <= 0 && BigInteger.valueOf(in.H).compareTo(ans) >= 0) {
                            return String.format("Case #%d: %s", in.testCase, ans.toString());
                        }

                        tryK2--;
                    }

                    
                    if (C % tryK1 != 0) {
                        tryK1++;
                    } else {
                        long k2 = C / tryK1;

                        BigInteger ans = lcm.multiply(BigInteger.valueOf(tryK1));

                        if (BigInteger.valueOf(in.L).compareTo(ans) <= 0 && BigInteger.valueOf(in.H).compareTo(ans) >= 0) {
                            return String.format("Case #%d: %s", in.testCase, ans.toString());
                        }
                        
                        tryK1++;
                    }

                }

            }

        }

        return String.format("Case #%d: NO", in.testCase);
        
    }

}
