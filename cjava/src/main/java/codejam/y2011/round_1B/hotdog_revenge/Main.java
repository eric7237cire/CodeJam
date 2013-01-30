package codejam.y2011.round_1B.hotdog_revenge;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleComparator;

import com.google.common.base.Preconditions;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, 
TestCaseInputScanner<InputData> {

    public Main() {
        super("B",false,true,true);
    }

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);
        input.C = scanner.nextInt();
        input.D = scanner.nextInt();
        input.posCount = new ArrayList<>(input.C);
        for(int i = 0; i < input.C; ++i) {
            input.posCount.add(new ImmutablePair<>(scanner.nextInt(), scanner.nextInt()));
        }
        return input;
    }
    
    static class RangeData {
        double rangeStart;
        double rangeEnd;
        
        //Length required to spread out
        final long len;        
        
        //Where vendors start
        final int position;
                
        //Minimum space between
        final int D;
        
        RangeData(Pair<Integer,Integer> posVendors, int D) {
            position = posVendors.getLeft();
            int vendors = posVendors.getRight();
            this.D = D;
            
            this.len = (long)D * (vendors - 1);
            rangeStart = (double) position - len / 2D;
            rangeEnd = (double) position + len / 2D;
            
            double distToStart = getDistanceToStart(); 
            double distToEnd = getDistanceToEnd();
            
            Preconditions.checkState(distToStart <= 0);
            Preconditions.checkState(distToEnd >= 0);
            Preconditions.checkState(-distToStart == distToEnd);
        }
        
        double getDistanceToStart() {
            return rangeStart - position;
        }
        double getDistanceToEnd() {
            return rangeEnd - position;
        }
        
        public void setRangeStart(double newRangeStart) {
            rangeStart = newRangeStart;
            rangeEnd = len+newRangeStart;
        }

        @Override
        public String toString() {
            return "Range [" + rangeStart + ", " + rangeEnd + "]";
        }
    }

    @Override
    public String handleCase(InputData input) {
        List<RangeData> rangeData = new ArrayList<>();
        
        List<Pair<Integer,Integer>> connectedRanges = new ArrayList<>();
        
        //Ranges are already sorted on input
        for(int i = 0; i < input.posCount.size(); ++i) {
            Pair<Integer,Integer> posVendor = input.posCount.get(i);
            rangeData.add(new RangeData(posVendor, input.D));
            connectedRanges.add(new ImmutablePair<>(i,i));
        }
        
        int currentConnectedRange = 0;
        
        while(currentConnectedRange < connectedRanges.size() - 1) 
        {
            Pair<Integer,Integer> currentRange = connectedRanges.get(currentConnectedRange);
            Pair<Integer,Integer> nextRange = connectedRanges.get(currentConnectedRange+1);
            
            //do they intersect
            double endCurrent = rangeData.get(currentRange.getRight()).rangeEnd;
            double beginNext = rangeData.get(nextRange.getLeft()).rangeStart;
            
            //Not intersecting, move on
            if (beginNext - endCurrent >= input.D) {
                currentConnectedRange++;
                continue;
            }
                
            
            
            //Find minimum distanceToStart (most negative)
            //Find maximum distanceToEnd
            double minDistanceToStart = Double.MAX_VALUE;
            double maxDistanceToEnd = Double.MIN_VALUE;
            
            //Search all the connected vendors to see which one has to travel the most to the edges
            for(int r = currentRange.getLeft(); r <= currentRange.getRight(); ++r) {
                RangeData range = rangeData.get(r);
                minDistanceToStart = Math.min(range.getDistanceToStart(), minDistanceToStart);
                maxDistanceToEnd = Math.max(range.getDistanceToEnd(), maxDistanceToEnd);
            }
            
            //Push next range to the right by startingAdj, this is the minimimum distance needed between vendors
            double startingAdj = endCurrent + input.D - beginNext; 
            for(int r = nextRange.getLeft(); r <= nextRange.getRight(); ++r) {
                RangeData range = rangeData.get(r);
                //Check it's vendors to see how far they are from the beginning and end of their range
                minDistanceToStart = Math.min(range.getDistanceToStart()+startingAdj, minDistanceToStart);
                maxDistanceToEnd = Math.max(range.getDistanceToEnd()+startingAdj, maxDistanceToEnd);
            }
            
            /**
             * Global adjustment.  Basically we want to make the max distance
             * a vendor has to move to the left = max distance a vendor has to move
             * to the right.
             * 
             * Basically, by always pushing the next range to the right, we have
             * to move everyone back.  (unless max=-min, where adjustment==0)
             */
            double adjustment = (maxDistanceToEnd+minDistanceToStart) / 2;
            
            //Do adjustment
            for(int r = currentRange.getLeft(); r <= currentRange.getRight(); ++r) {
                RangeData range = rangeData.get(r);
                range.setRangeStart(range.rangeStart - adjustment);
            }
            for(int r = nextRange.getLeft(); r <= nextRange.getRight(); ++r) {
                RangeData range = rangeData.get(r);
                range.setRangeStart(range.rangeStart + startingAdj - adjustment);
            }
            
            //Remove both connected ranges and add in new one
            connectedRanges.remove(currentConnectedRange+1);
            connectedRanges.set(currentConnectedRange, new ImmutablePair<>(currentRange.getLeft(), nextRange.getRight()));
            
            //Start looking from 0
            currentConnectedRange = Math.max(0, currentConnectedRange-1);
        }
        
        double minDistanceToStart = Double.MAX_VALUE;
        double maxDistanceToEnd = Double.MIN_VALUE;
        
        //Find largest distance vendors had to move
        for(int r = 0; r < rangeData.size(); ++r) {
            RangeData range = rangeData.get(r);
            minDistanceToStart = Math.min(range.getDistanceToStart(), minDistanceToStart);
            maxDistanceToEnd = Math.max(range.getDistanceToEnd(), maxDistanceToEnd);
        }
        
        Preconditions.checkState(DoubleComparator.compareStatic(-minDistanceToStart, maxDistanceToEnd) == 0);
        
        DecimalFormat df = new DecimalFormat("0.######");
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return "Case #" + input.testCase + ": " + df.format(maxDistanceToEnd);
        
    }
    
    

}
