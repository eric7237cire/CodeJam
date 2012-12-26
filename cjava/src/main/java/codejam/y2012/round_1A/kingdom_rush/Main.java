package codejam.y2012.round_1A.kingdom_rush;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.BoundType;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultiset;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
        //return new String[] {"sample.in"};
      return new String[] { "B-small-practice.in", "B-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);
        in.N = scanner.nextInt();
        
        in.levelMin = Lists.newArrayList();
        
        for (int i = 0; i < in.N; ++i) {
            in.levelMin.add(new ImmutablePair<>(scanner.nextInt(), scanner.nextInt()));
        }

        // log.info("TestCase {} Grid {}", testCase, in.grid);
        return in;
    }

    public String handleCase(InputData in) {

        
        Ordering<Pair<Integer,Integer>> set2Ordering = Ordering.from(new Comparator<Pair<Integer,Integer>>() {

            @Override
            public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
                return ComparisonChain.start().compare(o1.getRight(), o2.getRight()).compare(o2.getLeft(), o1.getLeft()).result();
            }
            
        });
        
        TreeMultiset<Pair<Integer,Integer>> set2star = TreeMultiset.create( set2Ordering );
        
        TreeMultiset<Pair<Integer,Integer>> set1star  = TreeMultiset.create( new Comparator<Pair<Integer,Integer>>() {

            @Override
            public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
                return ComparisonChain.start().compare(o1.getLeft(), o2.getLeft()).compare(o2.getRight(), o1.getRight()).result();
            }
            
        });
        
        set1star.addAll(in.levelMin);
        set2star.addAll(in.levelMin);
        /*

sc: 5 cn: 4
6 9
14 18
# 14
# 9
7 13
5 7
# 5
7 14
         */
        
        
        int score = 0;
        int count = 0;
        
        while(!set2star.isEmpty() ) {

            Pair<Integer,Integer> nextSet2 = set2star.firstEntry().getElement();
            
            if (nextSet2.getRight() <= score) {
                ++count;
                                
                if (set1star.contains(nextSet2)) {
                    score++;
                    set1star.remove(nextSet2);
                }
                
                set2star.remove(nextSet2);
                score++;
                
                continue;
            }
            

            List<Pair<Integer,Integer>> set1Choices = 
                    new ArrayList<>(
                    set1star.headMultiset(new ImmutablePair<>(score, 0), BoundType.CLOSED)
                    );
                    
            Collections.sort(set1Choices, set2Ordering);
            
            if(nextSet2.getRight() > score) {
                                
                //Need to take from set1 with the highest possible round 2 score
                if (set1Choices.isEmpty() ) {
                    return String.format("Case #%d: Too Bad", in.testCase);
                }
                
                ++count;
                score += 1;
                
                Pair<Integer,Integer> round1 = set1Choices.remove(set1Choices.size() - 1);
                set1star.remove(round1);
                continue;
                
            }
            
        }
        
       //in.levelMin.subList()

       return String.format("Case #%d: %s", in.testCase, count);

    }

}
