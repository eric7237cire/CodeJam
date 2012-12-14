package codejam.y2008.round_amer.test_passing;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;
import com.google.common.math.IntMath;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
       // return new String[] { "sample.in"};
        return new String[] { "C-small-practice.in" };
        //return new String[] { "B-large-practice.in" };
        //return new String[] { "B-small-practice.in", "B-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);
        input.M = scanner.nextInt();
        input.Q = scanner.nextInt();
        
        input.prob = new double[input.Q][4];
        for(int i = 0; i < input.Q; ++i) {
            input.prob[i] = new double[] {scanner.nextDouble(),scanner.nextDouble(),scanner.nextDouble(),scanner.nextDouble() };
        }
        return input;
    }

    @Override
    public String handleCase(InputData input) {
        
        int[] currentQ = new int[input.Q];
        
        for(int q = 0; q < input.Q; ++q) {
            //Arrays.sort(input.prob[q], Ordering.<double>natural().reverse());
           // Arrays.sort(input.prob[q]);
           // currentQ[q] = 0;
        }
        
        int perms = IntMath.pow(4,input.Q);
        List<Double> evList = new ArrayList<>();
        
        for(int i = 0; i < perms; ++i) {
            double p = 1;
            
            for(int q = 0; q < input.Q; ++q) {
                p *= input.prob[q][currentQ[q]];
            }
            evList.add(p);
            
            //Increment
            for(int q = 0; q < input.Q; ++q) {
                currentQ[q]++;
                if (currentQ[q] > 3) {
                    currentQ[q] = 0;
                } else {
                    break;
                }
            }
        }
        
        Collections.sort(evList, Ordering.natural().reverse());
        
        double ev = 0;
        for(int m = 0; m < input.M; ++m) {
            
            ev += evList.get(m);
            
            if (ev + 0.0000001d >= 1)
                break;
            
            
        }
        
        /*
       Arrays.sort(input.prob, new Comparator<double[]>() {

        @Override
        public int compare(double[] o1, double[] o2) {
            ComparisonChain cc = ComparisonChain.start();
            for(int i = 0; i < 4; ++i) {
                cc = cc.compare(o1[i], o2[i]);
                if (cc.result() != 0)
                    return cc.result();
            }
            
            return cc.result();
        }
           
       });*/
        
        
        DecimalFormat df = new DecimalFormat("0.######");
        df.setRoundingMode(RoundingMode.HALF_UP);
        
        return String.format("Case #%d: %s", input.testCase, df.format(ev));
    }
    

}
