package codejam.y2012.round_1B.equal_sums;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.EnumerateSubsets;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
         return new String[] {"sample.in"};
        //return new String[] {"C-small-practice.in"};
      //  return new String[] { "A-small-practice.in", "A-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);
        in.N = scanner.nextInt();

        in.numbers = Lists.newArrayList();

        for (int i = 0; i < in.N; ++i) {
            in.numbers.add( scanner.nextLong() );
        }

        return in;
    }
    
    long sumSet(Set<Long> set) {
        long sum = 0;
        for (long l : set)
            sum += l;
        
        return sum;
    }
    
    long sumSet(List<Long> set) {
        long sum = 0;
        for (long l : set)
            sum += l;
        
        return sum;
    }
    
    public void bruteForce(InputData in) {
        List<Long> out = Lists.newArrayList();
        EnumerateSubsets<Long> es = new EnumerateSubsets<>(in.numbers, out);
        
        Map<Long, List<Long>> subsets = Maps.newHashMap();
        while(es.hasNext()) {
            out = es.next();
            
            long sum = sumSet(out);
            
            if (subsets.containsKey(sum)) {
                log.info("BruteForce\n set1 {}\nset2 {}\n", out, subsets.get(sum));
                return;
            }
            
            subsets.put(sum, Lists.newArrayList(out));
        }
        
        log.info("none");
    }
    
    public Pair<List<Long>, List<Long>> random(InputData in) {
                

        Map<Long, BitSet> map = Maps.newHashMap();
        
        Random r = new Random(0);
        
        //10 ^ 12 * 6 = X
        //500 choose 6 = Y
        //Since Y > X, pigeon hole says there is a solution
        
        //long[] set1 = new long[6]; 
        int count = 0;
        long sum  = 0;
        
        for (int i = 0; i < 3000000; ++i) {
         //   log.debug("Set1 {}", set1);
         //   log.debug("Set2 {}", set2);
            
            BitSet bs = new BitSet();
            count  = 0;
            sum = 0;
            while (count < 6) {
                int bit = r.nextInt(500);
                
                if (!bs.get(bit)) {
                    long n = in.numbers.get(bit);
                    sum += n;
                    bs.set(bit);
                    ++count;
                }
            }
            
            
            
            
            if (map.containsKey(sum)) {
                log.info("Found at {}", i);
                
                List<Long> existingList = new ArrayList<>();
                for (int b = bs.nextSetBit(0); b >= 0; b = bs.nextSetBit(b+1)) {
                    // operate on index i here
                    existingList.add(in.numbers.get(b));
                }
                List<Long> retList = new ArrayList<>();
                bs = map.get(sum);
                for (int b = bs.nextSetBit(0); b >= 0; b = bs.nextSetBit(b+1)) {
                    // operate on index i here
                    retList.add(in.numbers.get(b));
                }
                
                return new ImmutablePair<>(existingList, retList);
            }

            map.put(sum, bs);

        }

        return null;

    }

    public String handleCase(InputData in) {

       // bruteForce(in);
        
        if (in.N == 500) {
            Pair<List<Long>,List<Long>> ans = random(in);
            if (ans == null)
                return (String.format("Case #%d:\nImpossible", in.testCase));
            
            return (String.format("Case #%d:\n%s\n%s", in.testCase, Joiner.on(' ').join(ans.getLeft()),
                    Joiner.on(' ').join(ans.getRight()) ));
        }
        
        TreeMap<Long, Pair<Set<Long>, Set<Long> >> setDiffs = new TreeMap<>();
        
        Collections.sort(in.numbers, Ordering.natural().reverse());
        
        setDiffs.put( in.numbers.get(0) - in.numbers.get(1),
                new ImmutablePair<>(
                        (Set<Long>) Sets.newHashSet(in.numbers.get(1)),
                        (Set<Long>) Sets.newHashSet(in.numbers.get(0))));
        
        setDiffs.put( in.numbers.get(0) ,
                new ImmutablePair<>(
                        (Set<Long>) Sets.<Long>newHashSet(),
                        (Set<Long>) Sets.newHashSet(in.numbers.get(0))));
        
        setDiffs.put( in.numbers.get(1),
                new ImmutablePair<>(
                        (Set<Long>) Sets.<Long>newHashSet(),
                        (Set<Long>) Sets.newHashSet(in.numbers.get(1))));
        
        long sumRest = sumSet(in.numbers) - in.numbers.get(0) - in.numbers.get(1);
        
        for(int i = 2; i < in.N; ++i) {
            long num = in.numbers.get(i);
            sumRest -= num;
            if (setDiffs.containsKey(num)) {
                Pair<Set<Long>, Set<Long> > sets = setDiffs.get(num);
                sets.getLeft().add(num);
                return (String.format("Case #%d:\n%s\n%s", in.testCase, Joiner.on(' ').join(sets.getLeft()),
                        Joiner.on(' ').join(sets.getRight()) ));
            }
            
            log.debug("Set diff keys size {}", setDiffs.keySet().size());
            
            setDiffs.tailMap(sumRest).clear();
            
            TreeMap<Long, Pair<Set<Long>, Set<Long> >> toAdd = new TreeMap<>();
            
            toAdd.put( in.numbers.get(i),
                    new ImmutablePair<>(
                            (Set<Long>) Sets.<Long>newHashSet(),
                            (Set<Long>) Sets.newHashSet(in.numbers.get(i))));
            
            for(Map.Entry<Long, Pair<Set<Long>, Set<Long> >> me : setDiffs.entrySet()) {
                
                Preconditions.checkState(sumSet(me.getValue().getRight()) ==
                        sumSet(me.getValue().getLeft()) + me.getKey());
                
                long diffIfAddSmallerSet = Math.abs(me.getKey()-num);
                
                if (!setDiffs.containsKey(diffIfAddSmallerSet)) {
                    Set<Long> left = Sets.newHashSet(me.getValue().getLeft());
                    left.add(num);
                    
                    if (num > me.getKey()) {
                    toAdd.put(diffIfAddSmallerSet, new ImmutablePair<>(me.getValue().getRight(), left));
                    } else {
                        toAdd.put(diffIfAddSmallerSet, new ImmutablePair<>(left, me.getValue().getRight()));
                    }
                }
                
                long diffIfAddLargerSet = me.getKey() + num;
                
                if (!setDiffs.containsKey(diffIfAddLargerSet)) {
                    Set<Long> right = Sets.newHashSet(me.getValue().getRight());
                    right.add(num);
                    toAdd.put(diffIfAddLargerSet, new ImmutablePair<>(me.getValue().getLeft(), right));
                }
            }
            
            setDiffs.putAll(toAdd);
        }
        
        return (String.format("Case #%d:\nImpossible", in.testCase));

    }

}
