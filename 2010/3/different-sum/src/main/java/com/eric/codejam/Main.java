package com.eric.codejam;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.Runner;
import com.eric.codejam.main.Runner.TestCaseInputScanner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
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
        
        int[] counts = count(n,n, base,fd, new ArrayList<String>());
        
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
    
    public int[] count(long n, long maxNum, final int b, boolean[][] forbiddenDigits, List<String> prevNums) {
        int[] count = new int[] {0,0};
        
        if (n == 0) {
            //log.debug("Prev nums\n{}", StringUtils.join(prevNums, "\n"));
            
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
            int[] subCounts = count(n - topNum, topNum, b, forbiddenDigits, prevNums);
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
    
    public static final int MAX_DIMENSION = 10;
    public static final int MAX_SINGLE_DIGIT_SUM = (MAX_DIMENSION-1) * MAX_DIMENSION / 2;
    
    public static final int MOD = 1000000007;
    
    public static class TokenCounts  {
        public Multiset<Integer> set;
        public TokenCounts() {
            set = HashMultiset.create();
        }
        public TokenCounts(Multiset<Integer> set) {
            this.set = set;
        }
    }
    
    long perm(int n, int k) {
        return LongMath.factorial(n) / LongMath.factorial(n-k);
    }
    
    public long count(long n, TokenCounts[][] termCounts, int base) {
        
        long count = 0;
        int column = 2;
        
        int maxSum = (base-1)*base / 2;
        
        TokenCounts start = termCounts[Ints.checkedCast(n)][base-1];
        
        if (start != null) {
            count += start.set.size();
        }
        
        for(int i = 1; i <= maxSum; ++i) {
            long sum = i * IntMath.pow(base, column-1);
            
            if (sum > n)
                break;
            
            TokenCounts ten = termCounts[i][base-1];
            
            int sumSingleCol = Ints.checkedCast(n - sum);
            
            TokenCounts ones = termCounts[sumSingleCol][base-1];
            
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
    
    public TokenCounts[][] getSumTermArray() {
        //int[][][] array
        
        //Determine max next digit
        
        //Sum all digits from 0 to base - 1
         
        // [sum] [max digit] [token counts]
        TokenCounts[][] array = new TokenCounts[MAX_SINGLE_DIGIT_SUM+1][MAX_DIMENSION];

        for (int total = 0; total <= MAX_SINGLE_DIGIT_SUM; ++total) {

            Multiset<Integer> tokenCount = HashMultiset.create();

            for (int digit = 1; digit < MAX_DIMENSION; ++digit) {
                int rest = total - digit;
                if (rest < 0) {
                    array[total][digit] = new TokenCounts(tokenCount);
                    continue;
                }

                if (rest == 0) {
                    tokenCount.add(1);

                } else {

                    TokenCounts subCount = array[rest][digit - 1];

                    if (subCount == null)
                        continue;

                    for (Multiset.Entry<Integer> tokens : subCount.set.entrySet()) {
                        if (tokenCount.count(tokens.getElement()+1) >= MOD) {
                            tokenCount.setCount(tokens.getElement()+1,tokenCount.count(tokens.getElement()+1) % MOD);
                        }
                        tokenCount.add(tokens.getElement()+1,tokens.getCount() % MOD );
                    }
                }

                array[total][digit] = new TokenCounts(HashMultiset.create(tokenCount));
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