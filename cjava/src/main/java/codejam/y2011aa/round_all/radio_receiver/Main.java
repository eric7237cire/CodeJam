package codejam.y2011aa.round_all.radio_receiver;

import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;
import com.google.common.math.LongMath;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
      // super();
        super("C", 1,1);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);
        
        in.N = scanner.nextInt();
        in.posTimeList = Lists.newArrayList();
        
        for(int i = 0; i < in.N; ++i) {
            in.posTimeList.add(new ImmutablePair<>(scanner.nextLong(), scanner.nextLong()));
        }
        
        return in;
    }
    
    
    public boolean isPossible(double D, InputData in)
    {
        /**
         * Keep track of the possible range
         */
        double left = in.posTimeList.get(0).getLeft() - D;
        double right = in.posTimeList.get(0).getLeft() + D;
        
        for(int i = 1; i < in.posTimeList.size(); ++i) {
            long timePassed = in.posTimeList.get(i).getRight() - in.posTimeList.get(i-1).getRight();
            Preconditions.checkState(timePassed > 0);
            
            /**
             * We can move up to timePassed * 1 units of distance, so we have
             * left - timePassed, right + timePassed
             * 
             * but it must be within D of the current transmitter 
             */
            
            double leftPossible = left - timePassed;
            double rightPossible = right + timePassed;
            
            double leftNeeded = in.posTimeList.get(i).getLeft() - D;
            double rightNeeded = in.posTimeList.get(i).getLeft() + D;
            
            //Intersect the ranges
            left = Math.max(leftPossible, leftNeeded);
            right = Math.min(rightPossible, rightNeeded);
            
            if (left > right) {
                return false;
            }
        }
        
        return true;
    }
    
    
    @Override
    public String handleCase(InputData in)
    {
        Collections.sort(in.posTimeList, new Comparator<Pair<Long,Long>>() {

            public int compare(Pair<Long, Long> o1, Pair<Long, Long> o2)
            {
                return Long.compare(o1.getRight(), o2.getRight());
            }
            
        });
        
        
        double lo = 0;
        double hi = LongMath.pow(10, 9);  //109
        
        /**
         * In seach low is impossible, high is possible
         */
        while (true) {
            double mid = lo + (hi - lo) / 2;

            boolean possible = isPossible(mid, in);
           // log.debug("Possible? {} D = {}", possible, mid);

            if (!possible) {
                lo = mid;
            } else {
                hi = mid;
            }

            Preconditions.checkState(lo <= hi);

            if (hi - lo < 1e-4)
                break;
        }
        
        /**
         * Answer is either a whole number or .5
         */
        double round = DoubleMath.roundToLong(hi*2, RoundingMode.HALF_EVEN);
        round /= 2;
        return  String.format("Case #%d: %s", in.testCase, DoubleFormat.df6.format(round));
        
    }
    

}