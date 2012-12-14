package codejam.y2008.round_amer.code_sequence;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.y2008.round_amer.code_sequence.Decoder.OffsetData;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

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

    @Override
    public String handleCase(InputData input) {
        
        List<Integer> buf = Arrays.asList(ArrayUtils.toObject(input.sequence));
        
        if (buf.size() <= 3) {
            return String.format("Case #%d: UNKNOWN", input.testCase);
        }
        
        int mod = 10007;
        
        
        
        List<OffsetData> od = null;
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
