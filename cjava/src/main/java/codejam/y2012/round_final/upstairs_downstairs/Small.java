package codejam.y2012.round_final.upstairs_downstairs;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.math.Fraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.utils.DoubleFormat;
import codejam.y2012.round_final.upstairs_downstairs.Main.Activity;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class Small {

    final static Logger log = LoggerFactory.getLogger(Small.class);
    
    static public double evalProbWokenUp(List<Fraction> probAwakeList)
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
    
    static public String handleCase(InputData in) {

        Collections.sort(in.activityList, new Comparator<Activity>(){

            @Override
            public int compare(Activity o1, Activity o2) {
                return o2.probAwake.compareTo(o1.probAwake);
            }
            
        });
        
       
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
