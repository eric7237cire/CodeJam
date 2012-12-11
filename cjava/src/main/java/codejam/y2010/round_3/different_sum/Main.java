package codejam.y2010.round_3.different_sum;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.LargeNumberUtils;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.math.DoubleMath;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    


    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String handleCase(InputData input) {
        int caseNumber = input.testCase;
        int base = input.B;
        long n = input.N;
        
        log.info("Starting case {}", caseNumber);
        int sumCount = Ints.checkedCast(solve(n,base) % MOD);
        log.info("Finished case {}", caseNumber);
        
        
        
        return ("Case #" + caseNumber + ": " + sumCount);
    }
    
   
    
    public static final int MAX_DIMENSION = 70;
    public static final int MAX_SINGLE_DIGIT_SUM = (MAX_DIMENSION-1) * MAX_DIMENSION / 2;
    
    public static final int MOD = 1000000007;
                                  
    
    
    
    public static class TermCount {
        int termCount;
        boolean hasALeadingZeroDigit;
        
        @Override
        public int hashCode() {
            return Objects.hashCode(termCount, hasALeadingZeroDigit);
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TermCount other = (TermCount) obj;
            return 
                    Objects.equal(termCount, other.termCount) &&
                    Objects.equal(hasALeadingZeroDigit, other.hasALeadingZeroDigit);
        }
        public TermCount(int termCount, boolean hasALeadingZeroDigit) {
            this.termCount = termCount;
            this.hasALeadingZeroDigit = hasALeadingZeroDigit;
        }
        @Override
        public String toString() {
            return "TC [k=" + termCount + ", f="
                    + hasALeadingZeroDigit + "]";
        }
        
    }
    
    public static class OutgoingTermCount {
        Map<TermCount, Long> frequency;
        
        public OutgoingTermCount() {
            frequency = new HashMap<>();
        }
        
        public void add(TermCount tc, long countLong) {
            
            if (frequency.containsKey(tc)) {
                countLong = LongMath.checkedAdd(countLong, frequency.get(tc));                
            }
            
            frequency.put(tc, countLong % Main.MOD);            
        }
       
    }
    
    int perm(int n, int k) {
        Preconditions.checkArgument(k <= n);
        //log.debug("n {} k {}", n, k);
        return permutations[n][k];
        
    }
    
    static  public int getMaxColumn(long n, int base) {
        double l = Math.log10(n+1) / Math.log10(base);
        
        int maxCol = DoubleMath.roundToInt(l,RoundingMode.UP);
        
        return maxCol;
    }
    
    static public int getMaxCarryOver(int base) {
        //sum 1 to base-1
        
        int sum = (base-1) * (base) / 2;
        sum /= base;
        
        return sum;
    }
    
    
    public void combineCounts(int[] distinctDigitCounts, OutgoingTermCount prevColumnCount, OutgoingTermCount tally) {
       // log.debug("Combine {} {}", columnCount.set.elementSet().size(), prevColumnCount.frequency.size());
        
        
        for(int  distinctDigitCount = 0; distinctDigitCount < distinctDigitCounts.length; ++distinctDigitCount) {
            int colTermFreq = distinctDigitCounts[distinctDigitCount];
            int cur_k = distinctDigitCount;
            
            if (colTermFreq == INVALID)
                continue;
            
            for(TermCount prevColTermCount : prevColumnCount.frequency.keySet()) {
                long tcFreq = prevColumnCount.frequency.get(prevColTermCount);
                int in_k = prevColTermCount.termCount;
                boolean in_hasLeadZeroTerm = prevColTermCount.hasALeadingZeroDigit;
                
               if (tcFreq == 0) {
                   continue;
               }
                
                if (in_hasLeadZeroTerm) {
                    
                    if (in_k >= cur_k && cur_k > 0) {
                        //join current column with previous with no leading zero term in current column
                        //only possible if there is at least one digit in current column
                        
                        long p = LongMath.checkedMultiply(cur_k, //choose a digit to attach to the leading zero term 
                                perm(in_k-1, cur_k-1)) % MOD; //attach rest of current columns digits
                        
                        tally.add(new TermCount(cur_k,false), 
                                LongMath.checkedMultiply(LongMath.checkedMultiply(p, tcFreq) % MOD, colTermFreq));
                    }
                    
                    if (in_k >= cur_k + 1) {
                        //join with current leading zero
                        long p = LongMath.checkedMultiply(cur_k+1, //choose a digit to attach to the leading zero term 
                                perm(in_k-1, cur_k+1-1)) % MOD;  //attach rest of current columns digits (including leading zero digit) 
                                 
                        tally.add(new TermCount(cur_k+1,true), 
                                LongMath.checkedMultiply(LongMath.checkedMultiply(p, tcFreq) % MOD, colTermFreq));
                    }
                    
                } else {
                    
                    if (in_k >= cur_k) {
                        ////join current column with previous with no leading zero term in current column
                        
                        int p = perm(in_k, cur_k); //attach rest of current columns digits
                        
                        tally.add(new TermCount(cur_k,false), 
                                LongMath.checkedMultiply(LongMath.checkedMultiply(p, tcFreq) % MOD, colTermFreq));
                    }
                    
                    if (in_k >= cur_k + 1) {
                        //join with current leading zero
                        int p = perm(in_k, cur_k+1);  //attach rest of current columns digits (including leading zero digit) 
                                 
                        tally.add(new TermCount(cur_k+1,true), 
                                LongMath.checkedMultiply(LongMath.checkedMultiply(p, tcFreq) % MOD, colTermFreq));
                    }
                }

            }

        }

    }
    
    public long solve(final long n, final int base) {
        
        final int maxCol = getMaxColumn(n,base);
        final int maxCarryOver = getMaxCarryOver(base);
        final int maxColumnSum = (base-1)*base / 2;
        
        OutgoingTermCount[][] outTermCounts = new OutgoingTermCount[maxCol][maxCarryOver+1];
        
        //Initialize first column
        
        List<Integer> columnDigits = new ArrayList<>();
        long cn = n;
        
        while(cn > 0) {
            columnDigits.add( (int) (cn % base));
            cn /= base;
        }
        
        int columnDigit = Ints.checkedCast(n % base);
        
        for(int outgoingCarry = 0; outgoingCarry <= maxCarryOver; ++ outgoingCarry) {
            int columnSum = outgoingCarry * base + columnDigit;
            
            if (columnSum > maxColumnSum)
                break;
                                 
            //  [ base (0 index == base 2)] [sum] [distinct terms (0 index = 0 terms) ]
            int[] distinctDigitCounts = singleColCountsNew[base-2][columnSum];
            
            Preconditions.checkState(distinctDigitCounts != null);
                            
            if ( (long)columnSum > n)  {
                break;
            }
            
            OutgoingTermCount outTermCount = new OutgoingTermCount();
            for(int  distinctDigitCount = 0; distinctDigitCount < distinctDigitCounts.length; ++distinctDigitCount) {
                if (distinctDigitCounts[distinctDigitCount] == INVALID)
                    continue;
                        
                outTermCount.frequency.put(new TermCount(distinctDigitCount,false), (long) distinctDigitCounts[distinctDigitCount]);
                
                outTermCount.frequency.put(new TermCount(distinctDigitCount+1,true), (long) distinctDigitCounts[distinctDigitCount]);
            }
            
            outTermCounts[0][outgoingCarry] = outTermCount;
        }
        
        
        for(int column = 2; column <= maxCol; ++column) {
            
            log.debug("Column {}",column);
            
            columnDigit = columnDigits.get(column-1);
            
            for(int outgoingCarry = 0; outgoingCarry <= maxCarryOver; ++ outgoingCarry) {
                boolean atLeastOneValid = false;
                OutgoingTermCount outTermCount = new OutgoingTermCount();
                                
                for(int incomingCarry = 0; incomingCarry <= maxCarryOver; ++incomingCarry) {
                    int columnSum = outgoingCarry * base + columnDigit - incomingCarry;
                    
                    if (columnSum < 0 || columnSum > maxColumnSum)
                        continue;
                    
                    OutgoingTermCount incomingTermCount = outTermCounts[column-1 - 1][incomingCarry];
                    
                    if (incomingTermCount == null)
                        continue;
                
                    atLeastOneValid = true;
                    
                    //  [ base (0 index == base 2)] [sum] [distinct terms (0 index = 0 terms) ]
                    int[] singleColCount = singleColCountsNew[base-2][columnSum];
                    
                    combineCounts(singleColCount, incomingTermCount,outTermCount);
                    
                    //OutgoingTermCount checkTermCount = new OutgoingTermCount();
                    //combineCounts(singleColCount, incomingTermCount,checkTermCount);
                    
                  
                   // log.debug("n {} column {} next carry {} in carry {} count {} new total count {}",n, column, 
                     //       outgoingCarry, incomingCarry, checkTermCount.getSumCount(), outTermCount.getSumCount());
                    
                }
                
                if (outTermCount.frequency.size() == 0 && !atLeastOneValid) {
                    outTermCount = null;
                }
                
                outTermCounts[column-1][outgoingCarry] = outTermCount;
            }
            
            for(int incomingCarry = 0; incomingCarry <= maxCarryOver; ++incomingCarry) {
                outTermCounts[column-2][incomingCarry] = null;
            }
        }
        
        long count = 0;
        
        int outgoingCarry = 0;
       // for(int outgoingCarry = 0; outgoingCarry <= maxCarryOver; ++ outgoingCarry) {
            OutgoingTermCount outTermCount = outTermCounts[maxCol-1][outgoingCarry];
            
            
            for(TermCount tc : outTermCount.frequency.keySet() ) {
                if (tc.hasALeadingZeroDigit)
                    continue;
                
                count = LongMath.checkedAdd(count,outTermCount.frequency.get(tc));
            }
       // }
        
        return count;
        
    }
    
   

    
    public static final int INVALID = 0;
    public int [][][] getSumTermArray() {
        
        //  [ base (0 index == base 2)] [sum] [distinct terms (0 index = 0 terms) ]
        int [][][] array = new int [MAX_DIMENSION-1][][];

        //seed base 2
        array[0] = new int[2][];
        array[0][0] = new int[] {1};
        array[0][1] = new int[] {INVALID, 1};
        
        for(int base = 3; base <= MAX_DIMENSION; ++base) {
            int maxSum = (base-1)*base / 2;
            
            //We do not know what the # of max terms is.  We know it must be less than base + 1
            array[base-2] = new int[maxSum+1][base+1];
            
            for(int sum = 0; sum < array[base-2].length; ++sum) {
               // Arrays.fill(array[base-2][sum], INVALID);
            }
            
            //Take all sums possible from previous base and add digit
            int[][] prevBase = array[base - 2 - 1];
            for (int sum = 0; sum < prevBase.length; ++sum) {

                for (int termCount = 0; termCount < prevBase[sum].length; ++termCount) {
                    if (prevBase[sum][termCount] != INVALID) {

                        // Add digit to previous bases counts

                        array[base - 2][sum + base - 1][termCount + 1] += prevBase[sum][termCount];
                        array[base - 2][sum + base - 1][termCount + 1] %= MOD;

                        // What is possible in prev base still possible

                        array[base - 2][sum][termCount] += prevBase[sum][termCount];
                        array[base - 2][sum][termCount] %= MOD;

                    }
                }
            }

            //shorten all arrays
            for(int sum = 0; sum < array[base-2].length; ++sum) {
                int termCount = array[base-2][sum].length -1;
                for( ;termCount >= 0; termCount--) {
                    if (array[base-2][sum][termCount] != INVALID) {
                        break;
                    }
                }
                
                //remove termCount + 1 to length.  so new length is termCount + 1
                array[base-2][sum] = Arrays.copyOf(array[base-2][sum], termCount+1);
            }
            
        }

        return array;
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData  input = new InputData(testCase);
        input.N = scanner.nextLong();
        input.B = scanner.nextInt();
        return input;
    }


    

    int[][] permutations;
    int[][][] singleColCountsNew;
    
    public Main() {
        singleColCountsNew = getSumTermArray();
    
        permutations = LargeNumberUtils.generateModedPerum(70, MOD);
    }
    
    
    
    
}