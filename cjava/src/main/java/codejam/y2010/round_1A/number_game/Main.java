package codejam.y2010.round_1A.number_game;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import com.google.common.math.DoubleMath;

public class Main implements TestCaseHandler<InputData>,
        TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    static List<Range<Integer>> losingRanges;
    static {
        losingRanges = new ArrayList<>(1000000);
        double ratio = (1 + Math.sqrt(5)) / 2;
        
        int lastLowerBound = 1;
        losingRanges.add(Ranges.closed(1, 1));
        /**
         * I found this just by observing the pattern.  Each
         * entry corresponds to a starting number
         *  and the range (both below and above) when it loses.
         * 
         * It turns out it was following the golden ratio
         */
        for (int i = 1; i < 1000000; ++i) {
            int n = i + 1;
            //Found this rule to know when the lower bound got bumped
            if (losingRanges.get(lastLowerBound - 1).upperEndpoint() < n) {
                ++lastLowerBound;
            }
            
            //Upper bound was always increasing
            Range<Integer> losingRange = Ranges.closed(lastLowerBound, lastLowerBound + n - 1);
            
            losingRanges.add(losingRange);

            
            double min = (i+1) / ratio;
            
            //Say i is A (the greater number), what is the largest B that
            // A <= ratio * B
            double max = (i+1) * ratio;
            
            //log.debug("Range added {} min {} max {} i {}",
              //      Ranges.closed(lastLowerBound, lastLowerBound + n - 1), min, max, i);
            
            int minLosing = DoubleMath.roundToInt(min,RoundingMode.UP);
            int maxLosing = DoubleMath.roundToInt(max, RoundingMode.DOWN);
            
            Preconditions.checkState(losingRange.lowerEndpoint().equals(minLosing));
            Preconditions.checkState(losingRange.upperEndpoint().equals(maxLosing));
            
        }
    }
    
    
    public void testLosingRanges() {
        double ratio = (1 + Math.sqrt(5)) / 2;
        
        for(int a = 1; a <= 1200; ++a) {
            
            Range<Integer> losingRange = losingRanges.get(a-1);
            
            for(int b = 1; b <= 1200; ++b) {
                int A = Math.max(a,b);
                int B = Math.min(a,b);
                
                if (losingRange.contains(b)) {
                    Preconditions.checkState(!isWinningSimple(a,b));
                    Preconditions.checkState( a < b * ratio);
                } else {
                    Preconditions.checkState(isWinningSimple(A,B));
                    Preconditions.checkState( A >= B * ratio);
                }
            }
        }
    }
    

    @Override
    public String handleCase(InputData input) {

        // log.info("Starting calculating case {}", caseNumber);

        long count = 0;
        
        //List<Range<Integer>> dLook = losingRanges.subList(0, 100);
        //testLosingRanges();

        Range<Integer> bRange = Ranges.closed(input.B1, input.B2);

        for (int a = input.A1; a <= input.A2; ++a) {

            count += bRange.upperEndpoint() - bRange.lowerEndpoint() + 1;

            Range<Integer> losingRange = losingRanges.get(a - 1);
            Preconditions.checkState(losingRange != null);
            if (losingRange.isConnected(bRange)) {
                Range<Integer> inter = bRange.intersection(losingRange);
                count -= (inter.upperEndpoint() - inter.lowerEndpoint() + 1);
            }

        }

        log.info("Done calculating answer case {}. ", input.testCase, count);

        return ("Case #" + input.testCase + ": " + count);
    }

    static public boolean isWinningSimple(int A, int B) {
        if (B == 0) return true;
        if (A >= 2*B) return true;
        return !isWinningSimple(B, A-B);
      }
    
    public boolean isWinning(int A, int B) {

        int G = Math.max(A, B);
        int L = Math.min(A, B);

        if (G == L) {
            return false;
        }

        if (G % L == 0)
            return true;

        

        int notFlexCount = 0;
        while (L > 0) {
            boolean flexNode = false;
            if (G - L >= L) {
                flexNode = true;
            }

            if (!flexNode) {
                notFlexCount++;
            } else {
                return notFlexCount % 2 == 0;
            }
            // log.debug("G {} L {} flexible? {}", G, L, flexNode);
            int newG = L;
            L = G % L;
            G = newG;

            G = Math.max(G, L);
            L = Math.min(G, L);
        }
        log.debug("\n\n");

        return notFlexCount % 2 == 0;

    }

    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData input = new InputData(testCase);
        input.A1 = scanner.nextInt();
        input.A2 = scanner.nextInt();
        input.B1 = scanner.nextInt();
        input.B2 = scanner.nextInt();

        return input;

    }

    @Override
    public String[] getDefaultInputFiles()
    {
        return new String[] { "C-small-practice.in", "C-large-practice.in" };
    }

   
}