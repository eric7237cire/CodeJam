package codejam.y2011.round_3.dire_straights;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Line;
import codejam.utils.geometry.Point;
import codejam.utils.geometry.Polygon;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;

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

        in.cards = Lists.newArrayList();

        for (int i = 0; i < in.N; ++i) {

            in.cards.add(scanner.nextInt());
        }
        return in;
    }

    
    public String handleCase(InputData in) {

        List<List<Integer>> straights = Lists.newArrayList();
        
        Collections.sort(in.cards);
        
        for(int card : in.cards) {
            
            List<Integer> shortestStraight = null;
            int shortestStraightLen = Integer.MAX_VALUE;
            
            for( List<Integer> straight : straights) {
                if (straight.get(straight.size() - 1) == card - 1 && straight.size() < shortestStraightLen)
                {
                    shortestStraightLen = straight.size();
                    shortestStraight = straight;
                }
            }
            
            if (shortestStraight == null) {
                List<Integer> newStraight = Lists.newArrayList();
                newStraight.add(card);
                straights.add(newStraight);
            } else {
                
                shortestStraight.add(card);
            }
        }
    
        int minSize = Integer.MAX_VALUE;
        for( List<Integer> straight : straights) {
            minSize = Math.min(straight.size(), minSize);
        }
        
        return String.format("Case #%d: %d", in.testCase, minSize == Integer.MAX_VALUE ? 0 : minSize);
        
    }

}
