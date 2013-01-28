package codejam.y2012.round_1A.kingdom_rush;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.BoundType;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Iterables;
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
        
        in.levels = Lists.newArrayList();
        
        /**
         * Number of points(stars) prereq to play level 1 ; Number to play level 2
         */
        for (int i = 0; i < in.N; ++i) {
            in.levels.add(new Level(scanner.nextInt(), scanner.nextInt()));
        }

        // log.info("TestCase {} Grid {}", testCase, in.grid);
        return in;
    }
    
    public static class Level
    {
        int oneStarPrereq;
        int twoStarPrereq;
        public Level(int oneStarPrereq, int twoStarPrereq) {
            super();
            this.oneStarPrereq = oneStarPrereq;
            this.twoStarPrereq = twoStarPrereq;
        }
        
        boolean oneStarBeaten;
    }
    
    public String handleCase(InputData in) {
        
        LinkedList<Level> levelList = new LinkedList<>(in.levels);
        
        //Sort by highest twoStarPreq
        Collections.sort(levelList, new Comparator<Level>() {

            @Override
            public int compare(Level o1, Level o2) {
                return ComparisonChain.start().compare(o2.twoStarPrereq, o1.twoStarPrereq)
                        .result();
            }
            
        });
        
        
        int count = 0;
        int score = 0;
        Level level = null;
        
        while(!levelList.isEmpty()) {
        
            final int currentScore = score;
            //Take any 2 star level possible
            int levelIdx = Iterables.indexOf(levelList, new Predicate<Level>() {
                public boolean apply(Level level) {
                    return level.twoStarPrereq <= currentScore;
                }
            });
            
            if (levelIdx != -1) {
                ++count;
                level = levelList.get(levelIdx); 
                score += level.oneStarBeaten ? 1 : 2;
                levelList.remove(levelIdx);
                continue;
            }
            
            //Otherwise take 1st possible 1 star with highest 2 star requirement
            levelIdx = Iterables.indexOf(levelList, new Predicate<Level>() {
                public boolean apply(Level lvl) {
                    return lvl.oneStarPrereq <= currentScore && !lvl.oneStarBeaten;
                }
            });
            
            if (levelIdx == -1) {
                return String.format("Case #%d: Too Bad", in.testCase);
            } else {
                ++count;
                ++score;
                levelList.get(levelIdx).oneStarBeaten = true;
            }
        }
        
        return String.format("Case #%d: %s", in.testCase, count);
    }

    public String handleCase2(InputData in) {

        /**
         * The idea is we put the levels in 2 sets.
         * 
         * One is ordered by least set 2 star prereq first ; breaking ties by higher 1 star.
         * 
         * TODO why 1 star sort? 
         */
        Ordering<Level> set2Ordering = Ordering.from(new Comparator<Level>() {

            @Override
            public int compare(Level o1, Level o2) {
                return ComparisonChain.start().compare(o1.twoStarPrereq, o2.twoStarPrereq)
                        .compare(o2.oneStarPrereq, o1.oneStarPrereq).result();
            }
            
        });
        
        TreeMultiset<Level> set2star = TreeMultiset.create( set2Ordering );
        
        /**
         * If we need to play a 1 star, play the one with the highest 2 star req.
         * This is because we may be able to play a lower 2 star req in 1 round later on.
         */
        TreeMultiset<Level> set1star  = TreeMultiset.create( new Comparator<Level>() {

            @Override
            public int compare(Level o1, Level o2) {
                return ComparisonChain.start().compare(o1.oneStarPrereq, o2.oneStarPrereq)
                        .compare(o2.twoStarPrereq, o1.twoStarPrereq).result();
            }
            
        });
        
        set1star.addAll(in.levels);
        set2star.addAll(in.levels);
                
        
        int score = 0;
        int count = 0;
        
        while(!set2star.isEmpty() ) {

            Level nextSet2 = set2star.firstEntry().getElement();
            
            if (nextSet2.twoStarPrereq <= score) {
                ++count;
                                
                if (set1star.contains(nextSet2)) {
                    score++;
                    set1star.remove(nextSet2);
                }
                
                set2star.remove(nextSet2);
                score++;
                
                continue;
            }
            

            List<Level> set1Choices = 
                    new ArrayList<>(
                    set1star.headMultiset(new Level(score, 0), BoundType.CLOSED)
                    );
                    
            Collections.sort(set1Choices, set2Ordering);
            
            if(nextSet2.twoStarPrereq > score) {
                                
                //Need to take from set1 with the highest possible round 2 score
                if (set1Choices.isEmpty() ) {
                    return String.format("Case #%d: Too Bad", in.testCase);
                }
                
                ++count;
                score += 1;
                
                Level round1 = set1Choices.remove(set1Choices.size() - 1);
                set1star.remove(round1);
                continue;
                
            }
            
        }
        
       //in.levelMin.subList()

       return String.format("Case #%d: %s", in.testCase, count);

    }

}
