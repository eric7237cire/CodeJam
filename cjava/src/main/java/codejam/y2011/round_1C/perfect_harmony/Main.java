package codejam.y2011.round_1C.perfect_harmony;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    
    
    @Override
    public String handleCase(InputData in) {
     
        //our frequency has to be <= anothers frequency
        
        long[] gcdInc = new long[in.N];
        //long[] gcdDes = new long[in.N];
        //BigInteger[] prodDes = new BigInteger[in.N];
        BigInteger[] lcmDes  = new BigInteger[in.N];
        
        Arrays.sort(in.freq);
        
        gcdInc[0] = in.freq[0];
        for(int freq = 1; freq < in.N; ++freq) {
            gcdInc[freq] = BigInteger.valueOf(gcdInc[freq-1]).gcd(BigInteger.valueOf(in.freq[freq])).longValue();
        }
        
//        gcdDes[in.N - 1] = in.freq[in.N - 1];
//        prodDes[in.N - 1] = BigInteger.valueOf(in.freq[in.N - 1]);
        lcmDes[in.N - 1] = BigInteger.valueOf(in.freq[in.N - 1]);
        for(int freq = in.N - 2; freq >= 0; --freq) {
//            gcdDes[freq] = BigInteger.valueOf(gcdDes[freq+1]).gcd(BigInteger.valueOf(in.freq[freq])).longValue();
//            prodDes[freq] = BigInteger.valueOf(in.freq[freq]).multiply(prodDes[freq+1]);
            
            BigInteger gcd = BigInteger.valueOf(in.freq[freq]).gcd( lcmDes[freq+1]);
            lcmDes[freq] = lcmDes[freq+1].multiply(BigInteger.valueOf(in.freq[freq])).divide(gcd);
        }
        
        //Position     0    1   2   3   4
        //Frequencies     0   1   2   3
        log.debug("gcd asc: {}", gcdInc);
//        log.debug("gcd des: {}", gcdDes);
//        log.debug("Prod des: {}", (Object) prodDes);
        log.debug("LCM des: {}", (Object) lcmDes);
        log.debug("Freq: {}", in.freq);
        for(int guessPosition = 0; guessPosition <= in.N; ++guessPosition) {
            //Try to find a    in.freq[other-1]  <= frequency <= in.freq[other]
            //long low = otherFreq == 0 ? in.L : Math.max(in.L, in.freq[otherFreq-1]);
            
            //LCM of all frequences greater than our guess range
            BigInteger lcm = guessPosition == in.N ? lcmDes[in.N - 1] : lcmDes[guessPosition];
            
            //GCD of all frequences lesser
            long gcd = guessPosition == 0 ? 1 : gcdInc[guessPosition - 1];
            
            log.debug("In position {} lcm {} gcd {}", guessPosition, lcm, gcd);
            
            BigInteger[] div= lcm.divideAndRemainder(BigInteger.valueOf(gcd));
            if (!div[1].equals(BigInteger.ZERO))
                continue;
            
            BigInteger fac = BigInteger.valueOf(in.L).divide()
        }
          
        return String.format("Case #%d: ", in.testCase);
        
    }

}
