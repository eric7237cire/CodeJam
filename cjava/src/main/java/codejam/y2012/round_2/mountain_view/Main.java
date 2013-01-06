package codejam.y2012.round_2.mountain_view;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.math3.fraction.Fraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
         return new String[] {"sample.in"};
        //return new String[] {"C-small-practice.in"};
       // return new String[] { "A-small-practice.in", "A-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);
        
        in.N = scanner.nextInt();
        in.highest = new int[in.N-1];
        
        for(int n = 0; n < in.N - 1; ++n) {
            in.highest[n] = scanner.nextInt();
        }
        
        return in;
    }

    
    
    public String handleCase(InputData in) {

        Simplex s = new Simplex(in.N);
        
        List<Double> zeroCoef = Collections.nCopies(in.N, 0d);
        
        
        for(int i = 0; i < in.N - 1; ++i) {
            int max = in.highest[i] - 1;
            
            for(int j = i+1; j < max; ++j) {
                List<Double> coeffs = Lists.newArrayList(zeroCoef);
                
                coeffs.set(max, (double) j-i);
                coeffs.set(i, (double) max-j);
                coeffs.set(j, (double) i - max);
                
                s.addConstraintGTE(coeffs, 1d);
                
                log.debug("Eq : {}Y{} + {}Y{} + {}Y{} >= 1",
                        j-i,
                        max,
                        max-j,
                        i,
                        i-max,
                        j);
            }
            
            for(int j = max + 1; j < in.N; ++j) {
                List<Double> coeffs = Lists.newArrayList(zeroCoef);
                
                coeffs.set(max, (double) j-i);
                coeffs.set(i, (double) max-j);
                coeffs.set(j, (double) i - max);
                
                s.addConstraintGTE(coeffs, 0d);
                

                log.debug("Eq : {}Y{} + {}Y{} + {}Y{} >= 0",
                        j-i,
                        max,
                        max-j,
                        i,
                        i-max,
                        j);
            }
        }
        
        List<Double> solution = Lists.newArrayList();
        
        boolean f = s.doPhase1(solution);
        
        if (!f) {
            return String.format("Case #%d: Impossible", in.testCase, solution.subList(0, in.N));            
        }
        
        List<Double> solutions = solution.subList(0, in.N);
        
        List<Fraction> solutionFra = Lists.transform(solutions, new Function<Double,Fraction>(){
           public Fraction apply(Double arg) {
               return new Fraction(arg);
           }
        });
        
        long lcm = 1;
        
        for(Fraction fr : solutionFra) {
            if (fr.getNumerator() == 0)
                continue;
            
            lcm = LongMath.checkedMultiply(lcm, fr.getDenominator() / LongMath.gcd(lcm, fr.getDenominator()));
        }
        
        List<Integer> solInt = Lists.newArrayList();
        for(Fraction fr : solutionFra) {
            solInt.add( Ints.checkedCast(LongMath.checkedMultiply(lcm, fr.getNumerator()) / fr.getDenominator()));
        }
        
        return String.format("Case #%d: %s", in.testCase, Joiner.on(' ').join(solInt));
    }

}
