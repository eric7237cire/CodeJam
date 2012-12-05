package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.Runner;
import com.eric.codejam.main.Runner.TestCaseInputScanner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;
import com.eric.codejam.utils.LargeNumberUtils;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.google.common.math.BigIntegerMath;
import com.google.common.math.DoubleMath;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, TestCaseInputReader<InputData> {

    


    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

        int base = input.B;
        long n = input.N;
        
        log.info("Starting case {}", caseNumber);
        int sumCount = Ints.checkedCast(solve(n,base) % MOD);
        log.info("Finished case {}", caseNumber);
        
        
        
        return ("Case #" + caseNumber + ": " + sumCount);
    }
    
    SingleColumnCounts[][] singleColCounts;
    
    public int[] count(long n, long maxNum, final int b, boolean[][] forbiddenDigits, List<String> prevNums, int[] checkTermCount, final long orig ) {
        int[] count = new int[] {0,0};
        
        if (n == 0) {
           // log.debug("Prev nums\n{}", StringUtils.join(prevNums, "\n"));
            
            if (orig == 53 && b == 4) {
            int col1DigitSum = 0;
            int col2DigitSum = 0;
            int col3DigitSum = 0;
            Set<Integer> col3Digits = new HashSet<>();
            for(String s : prevNums) {
                int digit1 = Character.digit(s.charAt(s.length()-1), 10);
                char nextDigitChar = s.length() > 1 ? s.charAt(s.length()-2) : ' ';
                
                int digit2 = nextDigitChar == ' ' ? 0 : Character.digit(nextDigitChar, 10);
                
                char digit3Char = s.length() > 2 ? s.charAt(s.length()-3) : ' ';
                int digit3 = digit3Char == ' ' ? 0 : Character.digit(digit3Char,10);
                
                col2DigitSum+=digit2;
                col1DigitSum+=digit1;
                col3DigitSum+=digit3;
                
                col3Digits.add(digit3);
                
            }
            
            if (col3Digits.containsAll(Sets.newHashSet(1,2))) {
            checkTermCount[1]++;
            } else if (col3Digits.containsAll(Sets.newHashSet(2))) {
                checkTermCount[2]++;
            } else if (col3Digits.containsAll(Sets.newHashSet(3))) {
                checkTermCount[0]++;
            } else {
                throw new RuntimeException("bah");
            }
            
            //Preconditions.checkState(singleDigitSum % 10 == 0);
            //if (col3DigitSum == 1)
                //checkTermCount[col1DigitSum / 10]++;
            }
            return new int[] {1, prevNums.size()};
        }
        
        if (n < 0)
            return new int[] {0,0};
        
        topLoop: for (long topNum = 1; topNum <= maxNum; ++topNum) {
            String s = StringUtils.leftPad(Long.toString(topNum, b),
                    forbiddenDigits.length);
           // log.debug("n {} topNum {} s [{}]", n, topNum,s);
            for (int c = 0; c < s.length(); ++c) {
                if (!Character.isDigit(s.charAt(c)))
                    continue;

                if (forbiddenDigits[c][Character.digit(s.charAt(c), b)])
                    continue topLoop;
            }

            for (int c = 0; c < s.length(); ++c) {
                if (!Character.isDigit(s.charAt(c)))
                    continue;

                forbiddenDigits[c][Character.digit(s.charAt(c), b)] = true;
            }
            prevNums.add(s);
            int[] subCounts = count(n - topNum, topNum, b, forbiddenDigits, prevNums, checkTermCount, orig);
            count[0] += subCounts[0];
            count[1] += subCounts[1];
            prevNums.remove(s);
            for (int c = 0; c < s.length(); ++c) {
                if (!Character.isDigit(s.charAt(c)))
                    continue;

                forbiddenDigits[c][Character.digit(s.charAt(c), b)] = false;
            }
        }
        
        return count;
    }
    
    public static final int MAX_DIMENSION = 70;
    public static final int MAX_SINGLE_DIGIT_SUM = (MAX_DIMENSION-1) * MAX_DIMENSION / 2;
    
    public static final int MOD = 1000000007;
                                  
    
    public static class SingleColumnCounts  {
        public Multiset<Integer> set;
        public SingleColumnCounts() {
            set = HashMultiset.create();
        }
        public SingleColumnCounts(Multiset<Integer> set) {
            this.set = set;
        }
    }
    
    public static class TermCount {
        int termCount;
        //int leadingZeroCount;
        //boolean hasAnyZerosAsDigits;
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
            super();
            this.termCount = termCount;
            //this.leadingZeroCount = leadingZeroCount;
            //this.hasAnyZerosAsDigits = hasAnyZerosAsDigits;
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
        
        public long getSumCount() {
            long count = 0;
            for(TermCount tc : frequency.keySet() ) {
                
                count = LongMath.checkedAdd(count, frequency.get(tc));
            }
            return count;
        }
    }
    
    int perm(int n, int k) {
        Preconditions.checkArgument(k <= n);
        
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
    
    static public int getDigitInColumn(long n, int column, int base) {
       //n = base ^ col -1 * digit 
        
        //divide n by base ^ col
        long div = BigInteger.valueOf(n).mod( BigInteger.valueOf(base).pow(column) ).longValue();
        int digit = Ints.checkedCast(div / LongMath.pow(base, column-1));
        
        Preconditions.checkState(digit >= 0 && digit < base);
        
        return digit;
        
    }
    
    public void combineCounts(int[] distinctDigitCounts, OutgoingTermCount prevColumnCount, OutgoingTermCount tally) {
       // log.debug("Combine {} {}", columnCount.set.elementSet().size(), prevColumnCount.frequency.size());
        
        
        for(int  distinctDigitCount = 1; distinctDigitCount <= distinctDigitCounts.length; ++distinctDigitCount) {
            int colTermFreq = distinctDigitCounts[distinctDigitCount-1];
            int cur_k = distinctDigitCount;
            
            if (colTermFreq == 0)
                continue;
            
            for(TermCount prevColTermCount : prevColumnCount.frequency.keySet()) {
                long tcFreq = prevColumnCount.frequency.get(prevColTermCount);
                int in_k = prevColTermCount.termCount;
                boolean in_f = prevColTermCount.hasALeadingZeroDigit;
                
               if (tcFreq == 0) {
                   continue;
               }
                
                if (in_f) {
                    
                    if (in_k >= cur_k) {
                        //join with no current leading zero
                        
                        long p = cur_k * //choose a digit to attach to the leading zero term 
                                perm(in_k-1, cur_k-1); //attach rest of current columns digits
                        
                        tally.add(new TermCount(cur_k,false), 
                                LongMath.checkedMultiply(LongMath.checkedMultiply(p, tcFreq) % MOD, colTermFreq));
                    }
                    
                    if (in_k >= cur_k + 1) {
                        //join with current leading zero
                        long p = (cur_k+1) * //choose a digit to attach to the leading zero term 
                                perm(in_k-1, cur_k+1-1);  //attach rest of current columns digits (including leading zero digit) 
                                 
                        tally.add(new TermCount(cur_k+1,true), 
                                LongMath.checkedMultiply(LongMath.checkedMultiply(p, tcFreq) % MOD, colTermFreq));
                    }
                    
                } else {
                    
                    if (in_k >= cur_k) {
                        //join with no current leading zero
                        
                        long p = perm(in_k, cur_k); //attach rest of current columns digits
                        
                        tally.add(new TermCount(cur_k,false), 
                                LongMath.checkedMultiply(LongMath.checkedMultiply(p, tcFreq) % MOD, colTermFreq));
                    }
                    
                    if (in_k >= cur_k + 1) {
                        //join with current leading zero
                        long p = perm(in_k, cur_k+1);  //attach rest of current columns digits (including leading zero digit) 
                                 
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
        
        int columnDigit = Ints.checkedCast(n % base);
        
        for(int outgoingCarry = 0; outgoingCarry <= maxCarryOver; ++ outgoingCarry) {
            int columnSum = outgoingCarry * base + columnDigit;
            
            if (columnSum > maxColumnSum)
                break;
            
            
            
            int[] distinctDigitCounts = singleColCountsNew[columnSum][base-2];
            
            if (distinctDigitCounts == null)
                break;
            
            if ( (long)columnSum > n)  {
                break;
            }
            
            OutgoingTermCount outTermCount = new OutgoingTermCount();
            for(int  distinctDigitCount = 1; distinctDigitCount <= distinctDigitCounts.length; ++distinctDigitCount) {
                outTermCount.frequency.put(new TermCount(distinctDigitCount,false), (long) distinctDigitCounts[distinctDigitCount-1]);
                
                outTermCount.frequency.put(new TermCount(distinctDigitCount+1,true), (long) distinctDigitCounts[distinctDigitCount-1]);
            }
            
            if (columnSum == 0) {
                outTermCount.frequency.put(new TermCount(1,true), 1L);
            }
            
            
            outTermCounts[0][outgoingCarry] = outTermCount;
        }
        
        
        for(int column = 2; column <= maxCol; ++column) {
            
            log.debug("Column {} n {} base {} max carry over {}",column,n,base, maxCarryOver);
            
            columnDigit = getDigitInColumn(n,column,base);
            
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
                    
                    int[] singleColCount = singleColCountsNew[columnSum][base-2];
                    
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
    
    public long count(long n, SingleColumnCounts[][] termCounts, int base) {
        
        long count = 0;
        int column = 2;
        
        int maxSum = (base-1)*base / 2;
        
        if (maxSum >= Ints.checkedCast(n)) {
        SingleColumnCounts start = termCounts[Ints.checkedCast(n)][base-1];
        
        if (start != null) {
            count += start.set.size();
        }
        }
        
        for(int i = 1; i <= maxSum; ++i) {
            long sum = i * IntMath.pow(base, column-1);
            
            if (sum > n)
                break;
            
            SingleColumnCounts ten = termCounts[i][base-1];
            
            int sumSingleCol = Ints.checkedCast(n - sum);
            
            if (sumSingleCol > maxSum) {
                continue;
            }
            SingleColumnCounts ones = termCounts[sumSingleCol][base-1];
            
            if (sumSingleCol == 0) {
                count += ten.set.count(1);
                continue;
            }
            
            for(Integer tenTokenCount : ten.set.elementSet()) {
                for(Integer oneTokenCount : ones.set.elementSet()) {
                    //Tens must be <= ones + 1
                    if (tenTokenCount > oneTokenCount + 1)
                        continue;
                    
                    int countTen = ten.set.count(tenTokenCount);
                    int countOne = ones.set.count(oneTokenCount);
                    //n! / (n-k)!
                    long p = perm(oneTokenCount+1,tenTokenCount);
                    log.debug("10s [{}] 1s [{}] p {} 1s {} x {} 10s {} x {}",
                            sum,sumSingleCol,p,oneTokenCount,countOne,tenTokenCount,countTen);
                    count += p * countTen * countOne;
                    
 
                }
            }
        }
        
        return count;
        
    }

    /**
     * [target sum][base][distinct digits] = count
     * @return
     */
    public SingleColumnCounts[][] getSumTermArrayOld() {
        //int[][][] array
        
        //Determine max next digit
        
        //Sum all digits from 0 to base - 1
         
        // [sum] [max digit] [token counts]
        SingleColumnCounts[][] array = new SingleColumnCounts[MAX_SINGLE_DIGIT_SUM+1][MAX_DIMENSION];

        for (int total = 0; total <= MAX_SINGLE_DIGIT_SUM; ++total) {

            Multiset<Integer> tokenCount = HashMultiset.create();

            for (int digit = 1; digit < MAX_DIMENSION; ++digit) {
                int rest = total - digit;
                if (rest < 0) {
                    array[total][digit] = new SingleColumnCounts(tokenCount);
                    continue;
                }

                if (rest == 0) {
                    tokenCount.add(1);

                } else {

                    SingleColumnCounts subCount = array[rest][digit - 1];

                    if (subCount == null)
                        continue;

                    for (Multiset.Entry<Integer> tokens : subCount.set.entrySet()) {
                        if (tokenCount.count(tokens.getElement()+1) >= MOD) {
                            tokenCount.setCount(tokens.getElement()+1,tokenCount.count(tokens.getElement()+1) % MOD);
                        }
                        tokenCount.add(tokens.getElement()+1,tokens.getCount() % MOD );
                        if (tokenCount.count(tokens.getElement()+1) >= MOD) {
                            tokenCount.setCount(tokens.getElement()+1,tokenCount.count(tokens.getElement()+1) % MOD);
                        }
                    }
                }

                array[total][digit] = new SingleColumnCounts(HashMultiset.create(tokenCount));
                //for(int d = digit; d < MAX_DIMENSION; ++d) {
                   
                //}
            }

        }

        return array;
    }

    public int [][][] getSumTermArray() {
        
        int INVALID = -10;
        //  [ base (0 index == base 2)] [sum] [distinct terms (0 index = 0 terms) ]
        int [][][] array = new int [MAX_DIMENSION-1][][];

        //seed base 2
        array[0] = new int[2][];
        array[0][0] = new int[] {0};
        array[0][1] = new int[] {INVALID, 1};
        
        for(int base = 3; base <= MAX_DIMENSION; ++base) {
            int maxSum = (base-1)*base / 2;
            array[base-2] = new int[maxSum+1][base+2];
            
            for(int sum = 0; sum < array[base-2].length; ++sum) {
                Arrays.fill(array[base-2][sum], INVALID);
            }
            
            //Take all sums possible from previous base and add digit
            int[][] prevBase = array[base-2-1];
            for(int sum = 0; sum < prevBase.length; ++sum) {
                
                for(int termCount = 0; termCount < prevBase[sum].length; ++termCount) {
                    if (prevBase[sum][termCount] != INVALID) {
                        
                        //Add digit
                        if (array[base-2][sum+base-1][termCount+1] == INVALID) {
                            array[base-2][sum+base-1][termCount+1] = prevBase[sum][termCount];
                        } else {
                            array[base-2][sum+base-1][termCount+1] += prevBase[sum][termCount];
                            array[base-2][sum+base-1][termCount+1] %= MOD;
                        }
                        
                        //What is possible in prev base still possible
                        if (array[base-2][sum][termCount] == INVALID) {
                            array[base-2][sum][termCount] = prevBase[sum][termCount];
                        } else {
                            array[base-2][sum][termCount] += prevBase[sum][termCount];
                            array[base-2][sum][termCount] %= MOD;
                        }
                    }
                }
            }

            Preconditions.checkState(array[base - 2][base - 1][1] != INVALID);
            array[base - 2][base - 1][1]++;
            
            //shorten all arrays
            for(int sum = 0; sum < array[base-2].length; ++sum) {
                int termCount = array[base-2][sum].length -1;
                for( ;termCount >= 0; termCount--) {
                    if (array[base-2][sum][termCount] != INVALID) {
                        break;
                    }
                }
                
                //remove termCount + 1 to length
                array[base-2][sum] = Arrays.copyOf(array[base-2][sum], termCount+1);
            }
            
            for(int sum = 0; sum < array[base-2].length; ++sum) {
                
                for(int termCount = 1 ;termCount < array[base-2][sum].length; termCount++) {
                    if (array[base-2][sum][termCount] != INVALID)
                        Preconditions.checkState(singleColCounts[sum][base-1].set.count(termCount) ==array[base-2][sum][termCount]);
                        
                }
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


    @Override
    public InputData readInput(BufferedReader br, int testCase)
            throws IOException {
        InputData  input = new InputData(testCase);
        String[] line = br.readLine().split(" ");
        input.N = Long.parseLong(line[0]);
        input.B = Integer.parseInt(line[1]);
        return input;
    }

    int[][] permutations;
    int[][][] singleColCountsNew;
    public Main() {
        singleColCounts = getSumTermArrayOld();
        singleColCountsNew = getSumTermArray();
        permutations = LargeNumberUtils.generateModedPerum(70, MOD);
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
//            args = new String[] { "sample.txt" };
            args = new String[] { "D-large.in" };
           // args = new String[] { "B-small-practice.in" };
          //  args = new String[] { "D-large-practice.in" };
         }
         log.info("Input file {}", args[0]);

         Main m = new Main();
         Runner.goScanner(args[0], m, m);
        // Runner.go(args[0], m, m, new InputData(-1), 5);

        
       
    }

    
    public void oldcombineCounts(int[] distinctDigitCounts, OldOutgoingTermCount prevColumnCount, OldOutgoingTermCount tally) {
        // log.debug("Combine {} {}", columnCount.set.elementSet().size(), prevColumnCount.frequency.size());
         
         boolean isEmpty = true;
         
         for(int  distinctDigitCount = 1; distinctDigitCount <= distinctDigitCounts.length; ++distinctDigitCount) {
             int colTermFreq = distinctDigitCounts[distinctDigitCount-1];
             int colTermCount = distinctDigitCount;
             
             if (colTermFreq > 0)
                 isEmpty = false;
             
             for(OldTermCount prevColTermCount : prevColumnCount.frequency.keySet()) {
                 long tcFreq = prevColumnCount.frequency.get(prevColTermCount);
                 
                 /*Case 1.  Fix terms from this column to terms from previous column that do not have leading zeros
                 */
                 if (colTermCount <= prevColTermCount.termCount) {
                     //Choose which prev column terms to attach a digit to the left 
                     long p = perm(prevColTermCount.termCount, colTermCount);
                     int zeroLeadingTerms = prevColTermCount.termCount - colTermCount;
                     
                     tally.add(new OldTermCount(colTermCount, IntMath.checkedAdd(zeroLeadingTerms,prevColTermCount.leadingZeroCount), prevColTermCount.hasAnyZerosAsDigits), 
                             LongMath.checkedMultiply(LongMath.checkedMultiply(p, tcFreq) % MOD, colTermFreq));
                     
                 }
                 
                 //Case 2.  Create a term with trailing zeros.  This means any term with leading zeros cannot be used
                 if (colTermCount - 1 <= prevColTermCount.termCount && !prevColTermCount.hasAnyZerosAsDigits) {
                     long p = LongMath.checkedMultiply(colTermCount, perm(prevColTermCount.termCount, colTermCount-1)) % MOD;
                     int zeroLeadingTerms = prevColTermCount.termCount - (colTermCount-1);
                     
                     tally.add(new OldTermCount(colTermCount, zeroLeadingTerms, true), LongMath.checkedMultiply(LongMath.checkedMultiply(p, tcFreq) % MOD, colTermFreq));
                 }
                 
                 //Case 3.  Attach a term to a prev col term that had leading zeros
                 if (colTermCount - 1 <= prevColTermCount.termCount && prevColTermCount.leadingZeroCount > 0) {
                     long p =LongMath.checkedMultiply( LongMath.checkedMultiply(colTermCount, perm(prevColTermCount.leadingZeroCount, 1)),
                              perm(prevColTermCount.termCount, colTermCount-1) ) % MOD;
                     int zeroLeadingTerms = prevColTermCount.termCount - (colTermCount-1) ;
                     
                     tally.add(new OldTermCount(colTermCount, zeroLeadingTerms, true), LongMath.checkedMultiply(LongMath.checkedMultiply(p, tcFreq) % MOD, colTermFreq));
                 }
                 
                 
             }
             
           //Case 2 again in case prev column is completely empty.  Create a term with trailing zeros.  This means any term with leading zeros cannot be used
             if (colTermCount == 1 && prevColumnCount.frequency.isEmpty()) {
                 tally.add(new OldTermCount(1,0, true), 1);
             }
         }
         
         if (isEmpty) {
             for(OldTermCount prevColTermCount : prevColumnCount.frequency.keySet()) {
                 long tcFreq = prevColumnCount.frequency.get(prevColTermCount);
                 tally.add(new OldTermCount(0, IntMath.checkedAdd( prevColTermCount.leadingZeroCount,prevColTermCount.termCount), prevColTermCount.hasAnyZerosAsDigits), tcFreq);
             }
         }
       
     }
     
     public long oldsolve(final long n, final int base) {
         
         final int maxCol = getMaxColumn(n,base);
         final int maxCarryOver = getMaxCarryOver(base);
         final int maxColumnSum = (base-1)*base / 2;
         
         OldOutgoingTermCount[][] outTermCounts = new OldOutgoingTermCount[maxCol][maxCarryOver+1];
         
         //Initialize first column
         
         int columnDigit = Ints.checkedCast(n % base);
         
         for(int outgoingCarry = 0; outgoingCarry <= maxCarryOver; ++ outgoingCarry) {
             int columnSum = outgoingCarry * base + columnDigit;
             
             if (columnSum > maxColumnSum)
                 break;
             
             
             
             int[] distinctDigitCounts = singleColCountsNew[columnSum][base-2];
             
             if (distinctDigitCounts == null)
                 break;
             
             if ( (long)columnSum > n)  {
                 break;
             }
             
             OldOutgoingTermCount outTermCount = new OldOutgoingTermCount();
             for(int  distinctDigitCount = 1; distinctDigitCount <= distinctDigitCounts.length; ++distinctDigitCount) {
                 outTermCount.frequency.put(new OldTermCount(distinctDigitCount,0,false), (long) distinctDigitCounts[distinctDigitCount-1]);
             }
             
             
             outTermCounts[0][outgoingCarry] = outTermCount;
         }
         
         
         for(int column = 2; column <= maxCol; ++column) {
             
             log.debug("Column {} n {} base {} max carry over {}",column,n,base, maxCarryOver);
             
             columnDigit = getDigitInColumn(n,column,base);
             
             for(int outgoingCarry = 0; outgoingCarry <= maxCarryOver; ++ outgoingCarry) {
                 boolean atLeastOneValid = false;
                 OldOutgoingTermCount outTermCount = new OldOutgoingTermCount();
                                 
                 for(int incomingCarry = 0; incomingCarry <= maxCarryOver; ++incomingCarry) {
                     int columnSum = outgoingCarry * base + columnDigit - incomingCarry;
                     
                     if (columnSum < 0 || columnSum > maxColumnSum)
                         continue;
                     
                     OldOutgoingTermCount incomingTermCount = outTermCounts[column-1 - 1][incomingCarry];
                     
                     if (incomingTermCount == null)
                         continue;
                 
                     atLeastOneValid = true;
                     
                     int[] singleColCount = singleColCountsNew[columnSum][base-2];
                     
                     oldcombineCounts(singleColCount, incomingTermCount,outTermCount);
                     
                     //OutgoingTermCount checkTermCount = new OutgoingTermCount();
                     //combineCounts(singleColCount, incomingTermCount,checkTermCount);
                     
                    /* if (n==53)
                     log.debug("n {} column {} next carry {} in carry {} count {} new total count {}",n, column, 
                             outgoingCarry, incomingCarry, checkTermCount.getSumCount(), outTermCount.getSumCount());*/
                     
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
             OldOutgoingTermCount outTermCount = outTermCounts[maxCol-1][outgoingCarry];
             
             
             for(OldTermCount tc : outTermCount.frequency.keySet() ) {
                 
                 count = LongMath.checkedAdd(count,outTermCount.frequency.get(tc));
             }
        // }
         
         return count;
         
     }
 
    public static class OldTermCount {
        int termCount;
        int leadingZeroCount;
        boolean hasAnyZerosAsDigits;
        
        @Override
        public int hashCode() {
            return Objects.hashCode(termCount, leadingZeroCount, hasAnyZerosAsDigits);
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            OldTermCount other = (OldTermCount) obj;
            return Objects.equal(leadingZeroCount, other.leadingZeroCount) &&
                    Objects.equal(termCount, other.termCount) &&
                    Objects.equal(hasAnyZerosAsDigits, other.hasAnyZerosAsDigits);
        }
        public OldTermCount(int termCount, int leadingZeroCount, boolean hasAnyZerosAsDigits) {
            super();
            this.termCount = termCount;
            this.leadingZeroCount = leadingZeroCount;
            this.hasAnyZerosAsDigits = hasAnyZerosAsDigits;
        }
        @Override
        public String toString() {
            return "OldTermCount [termCount=" + termCount + ", leadingZeroCount="
                    + leadingZeroCount + ", hasAnyZerosAsDigits="
                    + hasAnyZerosAsDigits + "]";
        }
        
    }
    
    public static class OldOutgoingTermCount {
        Map<OldTermCount, Long> frequency;
        
        public OldOutgoingTermCount() {
            frequency = new HashMap<>();
        }
        
        public void add(OldTermCount tc, long countLong) {
            
            if (frequency.containsKey(tc)) {
                countLong = LongMath.checkedAdd(countLong, frequency.get(tc));                
            }
            
            frequency.put(tc, countLong % Main.MOD);
            
        }
        
        public long getSumCount() {
            long count = 0;
            for(OldTermCount tc : frequency.keySet() ) {
                
                count = LongMath.checkedAdd(count, frequency.get(tc));
            }
            return count;
        }
    }
}