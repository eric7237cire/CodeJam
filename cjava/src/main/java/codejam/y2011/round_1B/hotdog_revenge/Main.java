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

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleComparator;

import com.google.common.base.Preconditions;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>{

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
        
        final long len;        
        final int position;
                
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
            
            if (beginNext - endCurrent >= input.D) {
                currentConnectedRange++;
                continue;
            }
                
            
            
            //Find minimum distanceToStart (most negative)
            //Find maximum distanceToEnd
            double minDistanceToStart = Double.MAX_VALUE;
            double maxDistanceToEnd = Double.MIN_VALUE;
            
            for(int r = currentRange.getLeft(); r <= currentRange.getRight(); ++r) {
                RangeData range = rangeData.get(r);
                minDistanceToStart = Math.min(range.getDistanceToStart(), minDistanceToStart);
                maxDistanceToEnd = Math.max(range.getDistanceToEnd(), maxDistanceToEnd);
            }
            
            //Push next range to the right by startingAdj
            double startingAdj = endCurrent + input.D - beginNext; 
            for(int r = nextRange.getLeft(); r <= nextRange.getRight(); ++r) {
                RangeData range = rangeData.get(r);
                minDistanceToStart = Math.min(range.getDistanceToStart()+startingAdj, minDistanceToStart);
                maxDistanceToEnd = Math.max(range.getDistanceToEnd()+startingAdj, maxDistanceToEnd);
            }
            
            //Global adjustment
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
            
            currentConnectedRange = 0;
        }
        
        double minDistanceToStart = Double.MAX_VALUE;
        double maxDistanceToEnd = Double.MIN_VALUE;
        
        //Find largest distance
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
