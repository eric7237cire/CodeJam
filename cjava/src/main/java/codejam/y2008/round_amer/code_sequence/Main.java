package codejam.y2008.round_amer.code_sequence;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.y2008.round_amer.code_sequence.Decoder.OffsetData;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
        //return new String[] { "sample.in"};
        //return new String[] { "B-small-practice.in" };
        //return new String[] { "B-large-practice.in" };
        return new String[] { "B-small-practice.in", "B-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);
        input.N = scanner.nextInt();
        input.sequence = new int[input.N];
        for(int i = 0; i < input.N; ++i) {
            input.sequence[i] = scanner.nextInt();
        }
        return input;
    }
    
    public static void example() {
        /**
         * Generates a sequence that has a key for each bit.  The next
         * number in the sequence is the sum of the keys that have a 1 bit
         * in n.
         * 
         */
        int mod = 1007;
        Generator g = new Generator(10, mod, 0);
        
        List<Integer> buf = Lists.newArrayList();
        
        // Generate a sequenc
        for(int i = 0; i < 10; ++i) {
            buf.add(g.next());
        }
        

        List<OffsetData> od = null;
        
        /**
         * "Level" is power of 2.   
         */
        for(int level = 0; 1 << level+1 <= buf.size(); ++level) {
            od = Decoder.getPossibleOffset_kn(od, level, mod, buf);
            
            Set<Integer> set = new HashSet<>();
            for(OffsetData o : od) {
                set.add(o.next);
            }
            if (set.size() == 1 && set.iterator().next() >= 0) {
                log.debug("Level {}", level);
                log.debug("Found next in sequence : " + set.iterator().next());
            }
        }
    }

    @Override
    public String handleCase(InputData input) {
        
        List<Integer> buf = Arrays.asList(ArrayUtils.toObject(input.sequence));
        
        if (buf.size() <= 3) {
            return String.format("Case #%d: UNKNOWN", input.testCase);
        }
        
        int mod = 10007;
        
        
        
        List<OffsetData> od = null;
        
        /**
         * "Level" is power of 2.   
         */
        for(int level = 0; 1 << level+1 <= buf.size(); ++level) {
            od = Decoder.getPossibleOffset_kn(od, level, mod, buf);
            
            Set<Integer> set = new HashSet<>();
            for(OffsetData o : od) {
                set.add(o.next);
            }
            if (set.size() == 1 && set.iterator().next() >= 0) {
                log.debug("Level {}", level);
                return String.format("Case #%d: %d", input.testCase, set.iterator().next());
            }
        }
        
        
        
       
        return String.format("Case #%d: UNKNOWN", input.testCase);
    }
    

}
