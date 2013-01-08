package codejam.y2012.round_2.mountain_view;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import org.apache.commons.math3.fraction.Fraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Line;
import codejam.utils.geometry.Point;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
         return new String[] {"sample.in"};
      // return new String[] {"C-small-practice.in"};
       // return new String[] {"C-large-practice.in"};
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
        /*
         * Once maximum has been established, cannot go past it until we go past that point
         */
        boolean f = feasible(in.highest, 2, in.highest[0]);
        
        if (!f) 
            return String.format("Case #%d: Impossible", in.testCase);
        
        int[] heights = new int[in.N];
        Arrays.fill(heights, -1);
        
        //                    10,9 , 8, 7,6,7 , 8, 9,10
        //heights = new int[] {100,0,0,0,0, 44,67,84,99,100 };
        heights = new int[] {1000,992,951,827,702, 826,950,991,999,1000 };
        
        boolean check = checkSolution(in.highest, heights);
        
        return String.format("Case #%d: 0 1 2 3", in.testCase);
        
        
    }
    
    boolean checkSolution(int[] perceivedHighest, int heights[]) {
        Preconditions.checkArgument(perceivedHighest.length == heights.length - 1);
        
        for(int currentMtn = 1; currentMtn < heights.length; ++currentMtn) {
            
            //Make a line from currentMtn to its max
            int percievedMax = perceivedHighest[currentMtn-1];
            
            Line line = new Line(new Point(currentMtn, heights[currentMtn-1]),
                    new Point(percievedMax, heights[percievedMax-1]));
            
            for(int mtn = currentMtn+1; mtn < percievedMax; ++mtn) {
                double limit = line.getPointGivenX(mtn).getY();
                
                if (heights[mtn-1] >= limit) {
                    log.info("Failure peak {} between peak {} and max {} is too high.  Must be strictly less than {}", 
                            mtn, currentMtn, percievedMax, DoubleFormat.df3.format(limit));
                    return false;
                }
            }
            
            for(int mtn = percievedMax + 1; mtn <= heights.length; ++mtn) {
                double limit = line.getPointGivenX(mtn).getY();
                
                if (heights[mtn-1] > limit) {
                    log.info("Failure peak {} after peak {} and max {} is too high.  Must be strictly <= {}", mtn, currentMtn, percievedMax, DoubleFormat.df3.format(limit));
                    
                    Line maxPointLine = new Line(new Point(mtn, heights[mtn-1]),
                            new Point(percievedMax, heights[percievedMax-1]));
                    
                    double cur = maxPointLine.getPointGivenX(currentMtn).getY();
                    
                    log.info("Can change current mtn {} to <= {}", currentMtn, DoubleFormat.df3.format(cur));
                    
                    return false;
                }
            }
        }
        
        return true;
    }
    
    void feasible(int[] highest, int heights[], int currentElem, int previousMax) {
        
    }
    
    boolean feasible(int[] perceivedHighest, int currentElem, int previousMax) {
        
        //Always feasible at the end
        if (currentElem == perceivedHighest.length + 1)
            return true;
        
        int currentElemMax = perceivedHighest[currentElem-1];
        
        //Sanity check, max cannot be on or behind us
        if (currentElemMax <= currentElem)
            return false;
        
        //Once we get to a local maximum, it can be increased again
        if (currentElem == previousMax)
            previousMax = currentElemMax;
        
        //Current max cannot "go past" next mountain
        if (currentElemMax > previousMax) 
            return false;
        
        return feasible(perceivedHighest, currentElem+1, currentElemMax);
    }
    
    
    private boolean addConstraints(Simplex s, Map<Integer,Integer> assigned, InputData in) {
        List<Double> zeroCoef = Collections.nCopies(in.N, 0d);
        
        for(int i = 0; i < in.N - 1; ++i) {
            int max = in.highest[i] - 1;
            
            for(int j = i+1; j < max; ++j) {
                List<Double> coeffs = Lists.newArrayList(zeroCoef);
                
                coeffs.set(max, (double) j-i);
                coeffs.set(i, (double) max-j);
                coeffs.set(j, (double) i - max);
                
                double rhs = 1;
                
                int cof = 0;
                for(int all = 0; all < in.N; ++all) {
                    if (coeffs.get(all) != 0 && assigned.containsKey(all)) {
                        rhs -= coeffs.get(all) * assigned.get(all);
                        coeffs.set(all, 0d);
                        ++cof;
                    }
                }
                
                if (cof == 3) {
                    Preconditions.checkState(rhs >= 0);
                }
                
                s.addConstraintGTE(coeffs, rhs);
                
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
                
                double rhs = 0;
                
                int cof = 0;
                for(int all = 0; all < in.N; ++all) {
                    if (coeffs.get(all) != 0 && assigned.containsKey(all)) {
                        rhs -= coeffs.get(all) * assigned.get(all);
                        coeffs.set(all, 0d);
                        ++cof;
                    }
                }
                
                if (cof == 3 && rhs < 0) {
                    return false;
                }
                
                
                s.addConstraintGTE(coeffs, rhs);
                

                log.debug("Eq : {}Y{} + {}Y{} + {}Y{} >= 0",
                        j-i,
                        max,
                        max-j,
                        i,
                        i-max,
                        j);
            }
        }

        return true;
    }
    
    public String handleCase2(InputData in) {

        
        
        List<Double> zeroCoef = Collections.nCopies(in.N, 0d);
                
        
        
        //8 0 1 2 0
        //assigned.put(2, 1);
        //assigned.put(3, 2);
        Stack<Simplex> assignementsToTry = new Stack<>();
        Simplex s2 = new Simplex(in.N);
        boolean ok = addConstraints(s2, new HashMap<Integer,Integer>(), in);
                
        assignementsToTry.add(s2);
        Map<Integer,Integer> assigned = new HashMap<Integer,Integer>();
        
        int tries = 0;
        
        while(!assignementsToTry.isEmpty()) 
        {
            ++tries;
        
            Simplex simplex = assignementsToTry.pop();
        
            log.info("Tries {}", tries);
            List<Double> solution = Lists.newArrayList();        
            boolean f = simplex.doPhase1(solution);
            
            if (!f) {
                //return String.format("Case #%d: Impossible", in.testCase, solution.subList(0, in.N));
                continue;
            }
            
            int smallestNonIntIndex  = -1;
            double smallest = Double.MAX_VALUE;
            List<Integer> solInt = Lists.newArrayList();
            
            for(int solIndex = 0; solIndex < in.N; ++solIndex) {
                
                double sol = solution.get(solIndex);
                double solAsInt = Math.rint(sol);
                if (Math.abs(solAsInt - sol) > 1e-5 && sol < smallest) {
                    smallestNonIntIndex = solIndex;
                    smallest = solution.get(solIndex);
                }
                
                if (assigned.containsKey(solIndex)) {
                    solInt.add(assigned.get(solIndex));
                } else {
                    solInt.add((int) Math.rint(solution.get(solIndex))); 
                }
            }
        
            if (smallestNonIntIndex == -1) {
                return String.format("Case #%d: %s", in.testCase, Joiner.on(' ').join(solInt));
            } else {
                Simplex try1 = new Simplex(simplex);
                Simplex try2 = new Simplex(simplex);
                
                double nonIntSolution = solution.get(smallestNonIntIndex);
                
                Preconditions.checkState(nonIntSolution >= 0d);
                double  intSolutionLB =  Math.ceil(nonIntSolution);
                double  intSolutionUP =  Math.floor(nonIntSolution);
                
                List<Double> coeffsTry1 = Lists.newArrayList(zeroCoef);                
                List<Double> coeffsTry2 = Lists.newArrayList(zeroCoef);
                coeffsTry1.set(smallestNonIntIndex, 1d);
                coeffsTry2.set(smallestNonIntIndex, 1d);
                
                try2.addConstraintLTE(coeffsTry2, intSolutionUP);
                try1.addConstraintGTE(coeffsTry1, intSolutionLB);
                
                assignementsToTry.add(try1);
                assignementsToTry.add(try2);
            }
        }
        
        if (1==1)
        return String.format("Case #%d: Impossible", in.testCase);
        
        List<Double> solution = Lists.newArrayList();
        List<Double> solutions = solution.subList(0, in.N);
        
        log.info("Solutions {}", solutions);
        
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
