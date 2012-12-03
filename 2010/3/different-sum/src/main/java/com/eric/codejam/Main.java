package com.eric.codejam;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.Runner;
import com.eric.codejam.main.Runner.TestCaseInputScanner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.math.DoubleMath;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    


    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

        int base = input.B;
        int n = input.N;
        
        String s = Long.toString(n,base);
        
        boolean[][] fd = new boolean[s.length()][base];
        
        int[] counts = count(n,n, base,fd, new ArrayList<String>(), new int[10], n);
        
        int termCount = counts[1];
        int sumCount = counts[0];
        
        log.debug("term count {} sum count {}", termCount, sumCount);
        for(int i = 0; i < s.length(); ++i){
            //Arrays.fill(fd[i], -1);
        }
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + caseNumber + ": " + sumCount);
    }
    
    
    
    public int[] count(long n, long maxNum, final int b, boolean[][] forbiddenDigits, List<String> prevNums, int[] checkTermCount, final long orig ) {
        int[] count = new int[] {0,0};
        
        if (n == 0) {
            //log.debug("Prev nums\n{}", StringUtils.join(prevNums, "\n"));
            
            if (orig == 113) {
            int col1DigitSum = 0;
            int col2DigitSum = 0;
            int col3DigitSum = 0;
            for(String s : prevNums) {
                int digit1 = Character.digit(s.charAt(s.length()-1), 10);
                char nextDigitChar = s.length() > 1 ? s.charAt(s.length()-2) : ' ';
                
                int digit2 = nextDigitChar == ' ' ? 0 : Character.digit(nextDigitChar, 10);
                
                char digit3Char = s.length() > 2 ? s.charAt(s.length()-3) : ' ';
                int digit3 = digit3Char == ' ' ? 0 : Character.digit(digit3Char,10);
                
                col2DigitSum+=digit2;
                col1DigitSum+=digit1;
                col3DigitSum+=digit3;
                
            }
            
            //Preconditions.checkState(singleDigitSum % 10 == 0);
            if (col3DigitSum == 1)
                checkTermCount[col1DigitSum / 10]++;
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
    
    public static final int MAX_DIMENSION = 12;
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
            TermCount other = (TermCount) obj;
            return Objects.equal(leadingZeroCount, other.leadingZeroCount) &&
                    Objects.equal(termCount, other.termCount) &&
                    Objects.equal(hasAnyZerosAsDigits, other.hasAnyZerosAsDigits);
        }
        public TermCount(int termCount, int leadingZeroCount, boolean hasAnyZerosAsDigits) {
            super();
            this.termCount = termCount;
            this.leadingZeroCount = leadingZeroCount;
            this.hasAnyZerosAsDigits = hasAnyZerosAsDigits;
        }
        @Override
        public String toString() {
            return "TermCount [termCount=" + termCount + ", leadingZeroCount="
                    + leadingZeroCount + ", hasAnyZerosAsDigits="
                    + hasAnyZerosAsDigits + "]";
        }
        
    }
    
    public static class OutgoingTermCount {
        Map<TermCount, Long> frequency;
        
        public OutgoingTermCount() {
            frequency = new HashMap<>();
        }
        
        public void add(TermCount tc, long count) {
            if (!frequency.containsKey(tc)) {
                frequency.put(tc,count);
            } else {
                frequency.put(tc, count + frequency.get(tc));
            }
        }
        
        public long getSumCount() {
            long count = 0;
            for(TermCount tc : frequency.keySet() ) {
                
                count += frequency.get(tc);
            }
            return count;
        }
    }
    
    static long perm(int n, int k) {
        Preconditions.checkArgument(k <= n);
        return LongMath.factorial(n) / LongMath.factorial(n-k);
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
        long div = n % LongMath.pow(base,column);
        int digit = Ints.checkedCast(div / LongMath.pow(base, column-1));
        
        Preconditions.checkState(digit >= 0 && digit < base);
        
        return digit;
        
    }
    
    static public void combineCounts(SingleColumnCounts columnCount, OutgoingTermCount prevColumnCount, OutgoingTermCount tally) {
        for(Integer colTermCount : columnCount.set.elementSet()) {
            int colTermFreq = columnCount.set.count(colTermCount);
            
            for(TermCount prevColTermCount : prevColumnCount.frequency.keySet()) {
                long tcFreq = prevColumnCount.frequency.get(prevColTermCount);
                
                /*Case 1.  Fix terms from this column to terms from previous column that do not have leading zeros
                */
                if (colTermCount <= prevColTermCount.termCount) {
                    //Choose which prev column terms to attach a digit to the left 
                    long p = perm(prevColTermCount.termCount, colTermCount);
                    int zeroLeadingTerms = prevColTermCount.termCount - colTermCount;
                    
                    tally.add(new TermCount(colTermCount, zeroLeadingTerms, prevColTermCount.hasAnyZerosAsDigits), p * tcFreq * colTermFreq);
                    
                }
                
                //Case 2.  Create a term with trailing zeros.  This means any term with leading zeros cannot be used
                if (colTermCount - 1 <= prevColTermCount.termCount && !prevColTermCount.hasAnyZerosAsDigits) {
                    long p = colTermCount * perm(prevColTermCount.termCount, colTermCount-1);
                    int zeroLeadingTerms = prevColTermCount.termCount - (colTermCount-1);
                    
                    tally.add(new TermCount(colTermCount, zeroLeadingTerms, true), p * tcFreq * colTermFreq);
                }
                
                //Case 3.  Attach a term to a prev col term that had leading zeros
                if (colTermCount - 1 <= prevColTermCount.termCount && prevColTermCount.leadingZeroCount > 0) {
                    long p = perm(prevColTermCount.leadingZeroCount, 1) * perm(prevColTermCount.termCount, colTermCount-1);
                    int zeroLeadingTerms = prevColTermCount.termCount - (colTermCount-1) - 1;
                    
                    tally.add(new TermCount(colTermCount, zeroLeadingTerms, true), p * tcFreq * colTermFreq);
                }
                
                
            }
            
          //Case 2 again in case prev column is completely empty.  Create a term with trailing zeros.  This means any term with leading zeros cannot be used
            if (colTermCount == 1 && prevColumnCount.frequency.isEmpty()) {
                tally.add(new TermCount(1,0, true), 1);
            }
        }
        
        if (columnCount.set.isEmpty()) {
            for(TermCount prevColTermCount : prevColumnCount.frequency.keySet()) {
                long tcFreq = prevColumnCount.frequency.get(prevColTermCount);
                tally.add(new TermCount(0, prevColTermCount.leadingZeroCount+prevColTermCount.termCount, prevColTermCount.hasAnyZerosAsDigits), tcFreq);
            }
        }
      
    }
    
    public long solve(final long n, SingleColumnCounts[][] termCounts, final int base) {
        
        final int maxCol = getMaxColumn(n,base);
        final int maxCarryOver = getMaxCarryOver(base);
        final int maxColumnSum = (base-1)*base / 2;
        
        OutgoingTermCount[][] outTermCounts = new OutgoingTermCount[maxCol][maxCarryOver+1];
        
        //Initialize first column
        
        int columnDigit = Ints.checkedCast(n % base);
        
        for(int outgoingCarry = 0; outgoingCarry <= maxCarryOver; ++ outgoingCarry) {
            int columnSum = outgoingCarry * base + columnDigit;
            
            SingleColumnCounts singleColCount = termCounts[columnSum][base-1];
            
            if (singleColCount == null)
                break;
            
            if ( (long)columnSum > n)  {
                break;
            }
            
            OutgoingTermCount outTermCount = new OutgoingTermCount();
            for(Integer termCount : singleColCount.set) {
                outTermCount.frequency.put(new TermCount(termCount,0,false), (long) singleColCount.set.count(termCount));
            }
            
            outTermCounts[0][outgoingCarry] = outTermCount;
        }
        
        
        for(int column = 2; column <= maxCol; ++column) {
            
            columnDigit = getDigitInColumn(n,column,base);
            
            for(int outgoingCarry = 0; outgoingCarry <= maxCarryOver; ++ outgoingCarry) {
                
                OutgoingTermCount outTermCount = new OutgoingTermCount();
                                
                for(int incomingCarry = 0; incomingCarry <= maxCarryOver; ++incomingCarry) {
                    int columnSum = outgoingCarry * base + columnDigit - incomingCarry;
                    
                    if (columnSum < 0 || columnSum > maxColumnSum)
                        continue;
                    
                    OutgoingTermCount incomingTermCount = outTermCounts[column-1 - 1][incomingCarry];
                    
                    if (incomingTermCount == null)
                        continue;
                    
                    SingleColumnCounts singleColCount = termCounts[columnSum][base-1];
                    
                    combineCounts(singleColCount, incomingTermCount,outTermCount);
                    
                    if (n>=113)
                    log.debug("n {} column {} next carry {} in carry {} new sum count {}",n, column, 
                            outgoingCarry, incomingCarry, outTermCount.getSumCount());
                    
                }
                
                outTermCounts[column-1][outgoingCarry] = outTermCount;
            }
        }
        
        long count = 0;
        
        int outgoingCarry = 0;
       // for(int outgoingCarry = 0; outgoingCarry <= maxCarryOver; ++ outgoingCarry) {
            OutgoingTermCount outTermCount = outTermCounts[maxCol-1][outgoingCarry];
            
            
            for(TermCount tc : outTermCount.frequency.keySet() ) {
                
                count += outTermCount.frequency.get(tc);
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
    
    public SingleColumnCounts[][] getSumTermArray() {
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
                    }
                }

                array[total][digit] = new SingleColumnCounts(HashMultiset.create(tokenCount));
                //for(int d = digit; d < MAX_DIMENSION; ++d) {
                   
                //}
            }

        }

        return array;
    }
    
    public int[] getSumTermCount(int maxColumn, int nextColumnDigit, int columnDigit, int maxDigit, final int base) {
        
        int[] count = new int[] {0,0};
        if (maxDigit == 0) {
            return count;
        }
        Preconditions.checkArgument(maxDigit > 0 && maxDigit <= 70);
        
        //in base 10
        int maxN = nextColumnDigit * base + columnDigit;
        
        for(int digit = maxDigit; digit >= 1; --digit) 
        {
            int rest = maxN - digit;
            
            if(rest < 0)
                continue;
            
            if (rest == 0) {
                count[0] ++;
                count[1] ++;
                continue;
            }
            
            int subNextColDigit = rest / base;
            int subColDigit = rest % base;
            
            int[] subCount = getSumTermCount(maxColumn,subNextColDigit, subColDigit, digit - 1, base);
            
            if(subCount[1] == 0)
                continue;
            
            count[0]+=subCount[0];
            count[1]+= subCount[0]+subCount[1];
            
        }
        
        return count;
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData  input = new InputData(testCase);
        input.N = scanner.nextInt();
        input.B = scanner.nextInt();
        return input;
    }


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            args = new String[] { "sample.txt" };
           // args = new String[] { "B-small-practice.in" };
//            args = new String[] { "B-large-practice.in" };
         }
         log.info("Input file {}", args[0]);

         Main m = new Main();
         Runner.goScanner(args[0], m, m);
         //Runner.go(args[0], m, m, new InputData(-1));

        
       
    }

    
}