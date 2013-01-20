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

import com.google.common.collect.Lists;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
       // return new String[] { "sample.in" };
     //    return new String[] { "B-small-practice.in" };
         return new String[] { "B-small-practice.in", "B-large-practice.in" };
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
    
    public void evalProbWokenUp(List<Fraction> probAwakeList)
    {
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
        }
    }

    public String handleCase(InputData in) {

        Collections.sort(in.activityList, new Comparator<Activity>(){

            @Override
            public int compare(Activity o1, Activity o2) {
                return o1.probAwake.compareTo(o2.probAwake);
            }
            
        });
        
       
        
        return String.format("Case #%d: %d", in.testCase, 0);
        
    }

}
