package codejam.y2012.round_final.upstairs_downstairs;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.math.Fraction;

import ch.qos.logback.classic.Level;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

public class UpstairsDownstairs extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    public UpstairsDownstairs()
    {
        super("B", 1, 1);
        (( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
    }
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);

        in.N = scanner.nextInt();
        in.K = scanner.nextInt();
        
        in.prob = new Fraction[in.N];
        in.limit = new int[in.N];
        in.activityList = Lists.newArrayList();
        
        for (int i = 0; i < in.N; ++i) {
            String[] s = scanner.next().split("/");
            in.prob[i] =  Fraction.getFraction(Integer.parseInt(s[0]),Integer.parseInt(s[1]));
            in.limit[i] = scanner.nextInt();
            in.activityList.add(new Activity(in.prob[i], in.limit[i]));
        }
        return in;
    }
    
    class Activity {
        Fraction probAwake;
        int limit;
        public Activity(Fraction probAwake, int limit) {
            super();
            this.probAwake = probAwake;
            this.limit = limit;
        }
    }
    
    
    
    /**
     * 
     * @param in
     * @return list of probabilities awake, sorted by noisiest/greatest first
     */
    List<Fraction> createCombinedActivityList(InputData in)
    {
        List<Fraction> ret = Lists.newArrayList();
        
        for(int i = 0; i < in.activityList.size(); ++i) {
            Activity activ = in.activityList.get(i);
            for(int j = 0; j < activ.limit; ++j) {
                ret.add(activ.probAwake);
            }
        }
        
        Collections.sort(ret, Ordering.natural().reverse());
        
        return ret;
    }

    public String handleCase(InputData in) {


        List<Fraction> combinedList = createCombinedActivityList(in);
        
        //Prefixes size 0 to k
        
        /**
         * We will calculate for each prefix the probability of 
         * the 3 possible states: awake, asleep, and awoke.
         * 
         * Awake means he was never asleep.
         * Asleep means he was awake at most once
         * Awoke is when he goes from asleep to awake
         */
        double[] prefixProbAwake = new double[in.K+1];
        double[] prefixProbAsleep = new double[in.K+1];
        double[] prefixProbAwoke  = new double[in.K+1];
        
        prefixProbAwake[0] = 1;
        prefixProbAsleep[0] = 0;
        prefixProbAwoke[0] = 0;
        
        for(int prefixSize = 1; prefixSize <= in.K; ++prefixSize)
        {
            double p = combinedList.get(prefixSize-1).doubleValue();
            
            //prob he was awake * prop being woken up = prob still awake
            prefixProbAwake[prefixSize] =
                    prefixProbAwake[prefixSize-1] * p;             
            
            //Can be asleep either by being put asleep after being awake
            //+  staying asleep
            prefixProbAsleep[prefixSize] = (1 - p) * prefixProbAwake[prefixSize-1]
                    + (1-p) * prefixProbAsleep[prefixSize-1];
            
            //prob asleep then woken + Once you have been woken up you stay awake
            prefixProbAwoke[prefixSize] = prefixProbAsleep[prefixSize-1]*p +
                    prefixProbAwoke[prefixSize-1];
            
            /*log.debug("Prefix size {}.  p act {} pAsleep {} pAwake {} pAwoke {}",
                    prefixSize,
                    DoubleFormat.df3.format(p),
                   DoubleFormat.df3.format( prefixProbAsleep[prefixSize] ),
                   DoubleFormat.df3.format(prefixProbAwake[prefixSize]),
                           DoubleFormat.df3.format(prefixProbAwoke[prefixSize]));
            */
                        
        }
        
      
        
        /**
         * Precalulate for the quitest activities at the end.
         * Calculate the probability if already asleep, hero is woken up.
         * 
         * To do that, we need the probability that he stays asleep which
         * is (1-p0)(1-p1)(1-p2)...
         * 
         * 1 - that is the probability he will be woken up
         *         
         */ 
        double[] suffixProbSleepToWokenUp = new double[in.K+1];
        

        /**
         * This will be 
         * 
         * p(falls alseep) * p(woken up later) +
         * p(stays awake) * p( awake->woken up later)
         */
        double[] suffixProbAwakeToWokenUp = new double[in.K+1];
        
        //  (1-p0)(1-p1)...
        double[] suffixProbStayAsleep = new double[in.K+1];
        
        
        suffixProbStayAsleep[0] = 1;
        suffixProbSleepToWokenUp[0] = 0;
        suffixProbAwakeToWokenUp[0] = 0;
        
        for(int suffixSize = 1; suffixSize <= in.K; ++suffixSize)
        {
            Fraction p = combinedList.get(combinedList.size() - suffixSize);
            
            //Easy prob still asleep * (1-pAwake)
            suffixProbStayAsleep[suffixSize] = suffixProbStayAsleep[suffixSize-1] * (1-p.doubleValue());
            
            suffixProbSleepToWokenUp[suffixSize] = 1 - suffixProbStayAsleep[suffixSize];
            
            //to go from awake to woken up
            //1)  go to sleep * next prob sleep to woken up
            //2)  stay awake * next prob awake to woken up
            suffixProbAwakeToWokenUp[suffixSize] = (1-p.doubleValue()) * suffixProbSleepToWokenUp[suffixSize-1]
                    + p.doubleValue() * suffixProbAwakeToWokenUp[suffixSize-1];
            
            
            log.debug("Prob awke-->woken up {} ",
                    suffixProbAwakeToWokenUp[suffixSize]        
                    );
            
        }
        
        
        double minProbWokenUp = 9000;
        
        /**
         * Where the magic happens.
         * 
         * We take the probability of each state from the prefix and 
         * multiply it by the values calculated for suffix asleep-->woken and awake-->woken
         * to get pisto presto, the probability he is woken.
         * 
         * We also add in the probability that the prefix itself woke him up 
         */
        for(int prefixSize = 0; prefixSize < in.K; ++prefixSize) {
            int suffixSize = in.K - prefixSize;
            
            double prob = prefixProbAwake[prefixSize] * suffixProbAwakeToWokenUp[suffixSize]
                    + prefixProbAsleep[prefixSize] * suffixProbSleepToWokenUp[suffixSize]
                            + prefixProbAwoke[prefixSize];
            
            minProbWokenUp = Math.min(minProbWokenUp, prob);
        }
        
        String ret =  String.format("Case #%d: %s", in.testCase, DoubleFormat.df6.format(minProbWokenUp));
      
        return ret;
        
    }

}
