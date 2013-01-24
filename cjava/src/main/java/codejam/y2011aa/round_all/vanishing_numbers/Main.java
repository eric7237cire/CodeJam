package codejam.y2011aa.round_all.vanishing_numbers;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.fraction.BigFraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.Lists;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
        return new String[] {"sample.in"};
      // return new String[] { "A-small-practice.in" };
      //return new String[] { "C-small-practice.in", "C-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);
        in.N = scanner.nextInt();
        
        in.numbers = Lists.newArrayList();

        for(int i = 0; i < in.N; ++i) {
            in.numbers.add(scanner.nextBigDecimal());
        }
        return in;
    }

    BigDecimal fracToBigDecimal(BigFraction frac)
    {
        BigDecimal bigDec = new BigDecimal(frac.getNumerator(), MathContext.UNLIMITED);
        bigDec = bigDec.setScale(154).divide( new BigDecimal(frac.getDenominator(), MathContext.UNLIMITED), RoundingMode.HALF_EVEN);
        return bigDec;
    }
    boolean isInInterval(BigDecimal number, BigFraction left, BigFraction right)
    {
        BigDecimal leftBD = fracToBigDecimal(left);
        BigDecimal rightBD = fracToBigDecimal(right);
        
        if (number.compareTo(leftBD) >= 0 && number.compareTo(rightBD) <= 0)
            return true;
        
        return false;
    }
        
    public String handleCase(InputData in) {

        List< Pair<BigFraction, BigFraction> > intervals = Lists.newArrayList();
        
        intervals.add( new ImmutablePair<>(new BigFraction(0), new BigFraction(1)));
        
       // List<BigDecimal> found = Lists.newArrayList();
        
        StringBuffer ans =  new StringBuffer();
        ans.append(String.format("Case #%d:\n", in.testCase)); 
        
        for(int i = 0; i < 53; ++i) {
            
            List< Pair<BigFraction, BigFraction> > nextIntervals = Lists.newArrayList();
            
            List<BigDecimal> eliminated = Lists.newArrayList();
            
            for(int intervalIdx = 0; intervalIdx < intervals.size(); ++intervalIdx) {
                Pair<BigFraction, BigFraction> interval = intervals.get(intervalIdx);
                
                if (interval.getLeft().equals(interval.getRight()))
                    continue;
                
                BigFraction intervalLen = (interval.getRight().subtract(interval.getLeft())).divide(3);
                BigFraction elimStart = interval.getLeft().add(intervalLen);
                BigFraction elimEnd = interval.getRight().subtract(intervalLen);
                
               // 
                
                for(int n = 0; n < in.numbers.size(); ++n) {
                    
                    if (isInInterval(in.numbers.get(n),elimStart,elimEnd)) {
                        
                        
                        log.debug("Eliminate {} between {} and {}", 
                                in.numbers.get(n),
                                elimStart.doubleValue(),elimEnd.doubleValue());
                        
                        isInInterval(in.numbers.get(n),elimStart,elimEnd);
                        
                        eliminated.add(in.numbers.get(n));
                        in.numbers.remove(n);
                        --n;
                    }
                }
                
                nextIntervals.add(new ImmutablePair<>(interval.getLeft(), elimStart));
                nextIntervals.add(new ImmutablePair<>(elimEnd, interval.getRight()));
            }
            
            Collections.sort(eliminated);
            
            for(BigDecimal elim : eliminated) {
            //found.add(in.numbers.get(n));
             ans.append(elim);
             ans.append("\n");
            }
            
            if (in.numbers.isEmpty())
                break;
            
            intervals.clear();
            
            outer:
            for(int intervalIdx = 0; intervalIdx < nextIntervals.size(); ++intervalIdx) {
                Pair<BigFraction, BigFraction> interval = nextIntervals.get(intervalIdx);
                
                for(int n = 0; n < in.numbers.size(); ++n) {
                    if (isInInterval(in.numbers.get(n), interval.getLeft(), interval.getRight())) {
                        intervals.add(interval);
                        continue outer;
                    }
                }
            }
            
            //intervals = nextIntervals;
        }
        
        log.debug("Numbers left : {} -- {}", in.numbers.size(), in.numbers);
        Collections.sort(in.numbers);
        
        for(BigDecimal bd : in.numbers) {
            ans.append(bd);
            ans.append("\n");
        }
        ans.deleteCharAt(ans.length()-1);
        
        return  ans.toString();
    }
}
