package codejam.y2012.round_2.mountain_view;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Line;
import codejam.utils.geometry.Point;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;
import com.google.common.primitives.Ints;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
        // return new String[] {"sample.in"};
   //    return new String[] {"C-small-practice.in"};
       // return new String[] {"C-large-practice.in"};
        return new String[] { "C-small-practice.in", "C-large-practice.in" };
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
        Arrays.fill(heights, 0);
        
        
        assignSolution(in.highest, heights, in.N);
        //3 3 6 6 6 7
        
        boolean check = checkSolution(in.highest, heights);
        
        log.debug("Highest {}", in.highest);
        log.debug("Heights {}", heights);
        
        Preconditions.checkState(check);
        
        return String.format("Case #%d: %s", in.testCase, Ints.join(" ", heights));
        
        
    }
    
    int determineHighestPossible(int currentMtn, int[] perceivedHighest, int heights[]) {
        //Any connection from peaks < currentMtn to peaks after 
        int highestPossible = 1000000000;
        
        for(int mtn = 1; mtn < currentMtn; ++mtn) {
            int percievedMax = perceivedHighest[mtn-1];
            
            if (percievedMax <= currentMtn)
                continue;
            
            Line line = new Line(new Point(mtn, heights[mtn-1]),
                    new Point(percievedMax, heights[percievedMax-1]));
            
            double cur = line.getPointGivenX(currentMtn).getY();
            
            int heightInt = DoubleMath.isMathematicalInteger(cur) ?
             (int) Math.floor(cur) - 1 : (int) Math.floor(cur);

             highestPossible = Math.min(heightInt, highestPossible);

        }
        
        return highestPossible;
    }
    
    void assignSolution(int[] perceivedHighest, int heights[], int N) {
        
        /*
         * Set currentMax = N
         * find all peaks pointing to currentMax
         * 
         * Assign currentMax = seed, the rest = seed - 1
         * 
         * Adjust them as necessary, going from right to left
         * 
         * The lowest of these values becomes the next seed
         * 
         */
    
        
        for(int currentPercievedMax = N; currentPercievedMax >= 1; --currentPercievedMax) {
            
            //log.debug("Iteration currentMax {} heights {}", currentMax, heights);
            log.debug("Iteration currentMax {}", currentPercievedMax);
            
            if ( heights[currentPercievedMax-1] == 0) {
                int highestMax = determineHighestPossible(currentPercievedMax, perceivedHighest, heights);
                heights[currentPercievedMax-1] = highestMax;
            }
            
            
            int maxHeight = heights[currentPercievedMax-1] - 1;
            
            for(int currentMtn = currentPercievedMax - 1; currentMtn >= 1; --currentMtn) {
                int percievedMax = perceivedHighest[currentMtn-1];
                
                if (percievedMax != currentPercievedMax)
                    continue;
            
                int currentMaxHeight = maxHeight;
                heights[currentMtn - 1] = currentMaxHeight;
                                
                //Line from current to percievedMax
                Line line = new Line(new Point(currentMtn, heights[currentMtn-1]),
                        new Point(percievedMax, heights[percievedMax-1]));
                
                /*
                 * Here we check peaks after the maximum to make sure that currentMtn
                 * does not see them.  The line connecting the currentMtn and its max
                 * must be >= to these peaks.
                 */
                for(int mtnAfterPM = percievedMax + 1; mtnAfterPM <= N; ++mtnAfterPM) {
                    double limit = line.getPointGivenX(mtnAfterPM).getY();
                    
                    if (limit < 0) {
                        log.debug("Limit less than 0");
                    }
                    
                    if (heights[mtnAfterPM-1] > limit) {
                        // Failure peak 12 / h 999998572 between peak 11 / h 999998569 and 
                        //max 18 / h 999998582 is too high.  Must be strictly less than 999998570.857
                        log.debug("Adjusting.  found peak after per. max {} after cur peak {} and its max {} " +
                        		"that is too high.  Must be  <= {}", 
                                mtnAfterPM, currentMtn, percievedMax, DoubleFormat.df3.format(limit));
                        
                        Line maxPointLine = new Line(new Point(mtnAfterPM, heights[mtnAfterPM-1]),
                                new Point(percievedMax, heights[percievedMax-1]));
                        
                        double cur = maxPointLine.getPointGivenX(currentMtn).getY();
                        
                        if (cur < 0) {
                            log.debug("New point < 0");
                        }
                        
                        log.debug("Can change current mtn {} to <= {}", currentMtn, DoubleFormat.df3.format(cur));
            
                        int heightInt = (int) Math.floor(cur) - 1;
                        
                        currentMaxHeight = Math.min(heightInt, currentMaxHeight);
                        //No other mountains can get in the way
                       // break;
                    }
                }
                
                heights[currentMtn - 1] = currentMaxHeight;
            }

            //Do a second pass, verify peaks on same level
            for(int currentMtn = 1; currentMtn < currentPercievedMax; ++currentMtn) {
                int percievedMax = perceivedHighest[currentMtn-1];
                
                if (percievedMax != currentPercievedMax)
                    continue;
                
                //Line could have changed
                Line line = new Line(new Point(currentMtn, heights[currentMtn-1]),
                        new Point(percievedMax, heights[percievedMax-1]));
                
                //peaks between currentMtn and its percieved max, make sure none are in the way
                for(int mtn = currentMtn+1; mtn < percievedMax; ++mtn) {
                    double limit = line.getPointGivenX(mtn).getY();
                    
                    if (perceivedHighest[mtn-1] != currentPercievedMax)
                        continue;
                    
                    if (heights[mtn-1] >= limit) {
                        log.debug("Adjusting on SAME level peak {} between peak {} and max {} is too high.  Must be strictly less than {}", 
                                mtn, currentMtn, percievedMax, DoubleFormat.df3.format(limit));
                        
                        double cur = line.getPointGivenX(mtn).getY();
                        
                        int heightInt = (int) Math.floor(cur) - 1;
                        heights[mtn - 1] = heightInt;                        
                    }
                }

            }
            
            

        }
        
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
                    log.info("Failure peak {} / h {} between peak {} / h {} and max {} / h {} is too high." +
                    		"  Must be strictly less than {}", 
                            mtn, heights[mtn-1], currentMtn, heights[currentMtn-1], percievedMax, heights[percievedMax-1], DoubleFormat.df3.format(limit));
                    return false;
                }
            }
            
            for(int mtn = percievedMax + 1; mtn <= heights.length; ++mtn) {
                double limit = line.getPointGivenX(mtn).getY();
                
                if (heights[mtn-1] > limit) {
                    log.info("Failure peak {} after peak {} and max {} is too high.  Must be <= {}", mtn, currentMtn, percievedMax, DoubleFormat.df3.format(limit));
                    
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
        addConstraints(s2, new HashMap<Integer,Integer>(), in);
                
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
        
        return String.format("Case #%d: Impossible", in.testCase);
        
        /*if (1==1)
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
        
        return String.format("Case #%d: %s", in.testCase, Joiner.on(' ').join(solInt));*/
    }

}
