package codejam.y2012.round_final.upstairs_downstairs;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.math.Fraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
        return new String[] { "sample.in" };
     //    return new String[] { "B-small-practice.in" };
       //  return new String[] { "B-small-practice.in", "B-large-practice.in" };
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
    
    public double evalProbWokenUp(List<Fraction> probAwakeList)
    {
        double probWokenUp = 0;
        
        for(int i = 0; i < probAwakeList.size(); ++i) {
            double m = 1;
            
            //Activities before i kept our hero awake
            for(int j = 0; j < i; ++j ) 
            {
                m *= probAwakeList.get(j).doubleValue();
            }
            
            //Activity i put hero asleep
            m *= (1-probAwakeList.get(i).doubleValue());
            
            //Calculate probability that rest of activities
            //keep hero asleep
            double stayAsleep = 1;
            for(int j = i+1; j < probAwakeList.size(); ++j ) 
            {
                stayAsleep *= (1-probAwakeList.get(j).doubleValue());
            }
            
            //Any of the subsequent activies woke hero up
            m *= (1 - stayAsleep);
            
            probWokenUp += m;
        }
        
        return probWokenUp;
    }
    
    class RetInfo {
        double probWokenUpIfStartAwake;
        
        double probWokenUpIfStartAsleep;
    }
    
    public RetInfo evalSuffix(List<Fraction> suffix) {
        
        
        return null;
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
        
        double[] prefixProbKeptAwake = new double[in.K+1];
        
        prefixProbKeptAwake[0] = 1;
        
        for(int prefixSize = 1; prefixSize <= in.K; ++prefixSize)
        {
            double p = combinedList.get(prefixSize-1).doubleValue(); 
            prefixProbKeptAwake[prefixSize] = prefixProbKeptAwake[prefixSize-1] 
                    * p;
            
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
        
        //  (1-p0)(1-p1)...
        double[] suffixProbStayAsleep = new double[in.K+1];
        suffixProbStayAsleep[0] = 1;
        suffixProbSleepToWokenUp[0] = 0;
        
        for(int suffixSize = 1; suffixSize <= in.K; ++suffixSize)
        {
            Fraction p = combinedList.get(combinedList.size() - suffixSize);
            suffixProbStayAsleep[suffixSize] = suffixProbStayAsleep[suffixSize-1] * p.doubleValue();
            
            suffixProbSleepToWokenUp[suffixSize] = 1 - suffixProbStayAsleep[suffixSize]; 
        }
        
        double[] suffixProbAwakeToWokenUp = new double[in.K+1];
        
        
       
        double minProbWokenUp = 1;
        
        for(int q = 0; q <= in.K; ++q) {
            //Take K-q noisiest activities
            
            List<Fraction> chosenActivies = Lists.newArrayList();
            
            int noisyLeft = in.K - q;
            int curActivity = 0;
            
            while(noisyLeft > 0) {
                Activity loudActivity = in.activityList.get(curActivity);
                int repeats = Math.min(noisyLeft, loudActivity.limit);
                
                for(int r = 0; r < repeats; ++r) {
                    chosenActivies.add(loudActivity.probAwake);
                }
                
                noisyLeft -= repeats;
                ++curActivity;
            }
            
            int quietLeft = q;
            curActivity = in.activityList.size() - 1;
            
            List<Fraction> chosenQuiet = Lists.newArrayList();
            
            while(quietLeft > 0) {
                Activity quietActivity = in.activityList.get(curActivity);
                int repeats = Math.min(quietLeft, quietActivity.limit);
                
                for(int r = 0; r < repeats; ++r) {
                    chosenQuiet.add(quietActivity.probAwake);
                }
                
                quietLeft -= repeats;
                --curActivity;
            }
            
            //Put them in order from loudest to quietest
            ;
            
            chosenActivies.addAll(Lists.reverse(chosenQuiet));
            
            for(int ca = 0; ca < chosenActivies.size(); ++ca) {
                log.debug("ca {} = {}", ca, DoubleFormat.df3.format(chosenActivies.get(ca).doubleValue()));
            }
            
            Preconditions.checkState(chosenActivies.size() == in.K);
            
            double probWoken = evalProbWokenUp(chosenActivies);
            
            log.debug("Prob {}", probWoken);
            
            minProbWokenUp = Math.min(minProbWokenUp, probWoken);
        }
        
        return String.format("Case #%d: %s", in.testCase, DoubleFormat.df6.format(minProbWokenUp));
        
    }

}
