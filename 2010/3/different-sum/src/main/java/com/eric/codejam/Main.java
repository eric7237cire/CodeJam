package com.eric.codejam;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.Runner;
import com.eric.codejam.main.Runner.TestCaseInputScanner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    


    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

        int base = input.B;
        int n = input.N;
        
        String s = Long.toString(n,base);
        
        boolean[][] fd = new boolean[s.length()][base];
        
        int count = count(n,n, base,fd, new ArrayList<String>());
        for(int i = 0; i < s.length(); ++i){
            //Arrays.fill(fd[i], -1);
        }
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + caseNumber + ": " + count);
    }
    
    public int count(long n, long maxNum, final int b, boolean[][] forbiddenDigits, List<String> prevNums) {
        int count = 0;
        
        if (n == 0) {
           // log.debug("Prev nums\n{}", StringUtils.join(prevNums, "\n"));
            
            return 1;
        }
        
        if (n < 0)
            return 0;
        
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
            count = count + count(n - topNum, topNum, b, forbiddenDigits, prevNums);
            prevNums.remove(s);
            for (int c = 0; c < s.length(); ++c) {
                if (!Character.isDigit(s.charAt(c)))
                    continue;

                forbiddenDigits[c][Character.digit(s.charAt(c), b)] = false;
            }
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