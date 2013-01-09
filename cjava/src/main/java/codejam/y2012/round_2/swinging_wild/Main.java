package codejam.y2012.round_2.swinging_wild;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Scanner;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.Maps;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
      //   return new String[] {"sample.in"};
        return new String[] { "A-small-practice.in", "A-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);
        
        /*
         * The first line of each test case contains the number N of vines.
         *  N lines describing the vines follow, each with a pair of integers
         *   di and li - the distance of the vine from your ledge, and
         *    the length of the vine, respectively. The last line of the 
         *    test case contains the distance D to the ledge with your one 
         *    true love. You start by holding the first vine in hand.
         */
        
        in.N = scanner.nextInt();
        in.d = new int[in.N];
        in.l = new int[in.N];
        
        for(int n = 0; n < in.N; ++n) {
            in.d[n] = scanner.nextInt();
            in.l[n] = scanner.nextInt();
        }
        
        in.D = scanner.nextInt();

        return in;
    }

    
    
    public String handleCase(InputData in) {

        TreeMap<Integer, Integer> locLenMap = new TreeMap<>();
        
        Map<Integer, Integer> maxLocationRange = Maps.newHashMap();
        
        for(int n = 0; n < in.N; ++n) {
            locLenMap.put(in.d[n], in.l[n]);
            maxLocationRange.put(in.d[n], 0);
        }
        
        //Pair means location, +/- range
        
        Map<Integer, Integer> toVisitLocRange = Maps.newHashMap();
                
        
        toVisitLocRange.put(in.d[0], in.d[0]);
        
        while(!toVisitLocRange.isEmpty()) {
            Map.Entry<Integer,Integer> locRange = toVisitLocRange.entrySet().iterator().next();
            
            toVisitLocRange.remove(locRange.getKey());
            
            if (maxLocationRange.get(locRange.getKey()) >= locRange.getValue())
                continue;
            
            maxLocationRange.put(locRange.getKey(), locRange.getValue());
            
            //We are at the maximum length possible for this location
            if (locRange.getValue() == locLenMap.get(locRange.getKey())) {
                locLenMap.remove(locRange.getKey());
            }
            
            if (in.D - locRange.getKey() <= locRange.getValue()) {
                return String.format("Case #%d: YES", in.testCase);
            }
            
            //NavigableMap<Integer,Integer> visitable = locLenMap.subMap(locRange.getKey() - locRange.getValue(),true, locRange.getKey() + locRange.getValue(), true);
            
            NavigableMap<Integer,Integer> visitable = locLenMap.subMap(locRange.getKey(), false, locRange.getKey() + locRange.getValue(), true);
            
            
            for(Map.Entry<Integer,Integer> locLen : visitable.entrySet()) {
                int interval = Math.abs(locLen.getKey() - locRange.getKey());
                int length = locLen.getValue();
                
                int range = Math.min(interval, length);
                
                Integer currentRange = toVisitLocRange.get(locLen.getKey());
                
                int currentMax = maxLocationRange.get(locLen.getKey());
                
                if (range > currentMax && (currentRange == null || range > currentRange)) {
                    toVisitLocRange.put(locLen.getKey(), range);
                }
            }
            
        }
       
        return String.format("Case #%d: NO", in.testCase);
    }

}
