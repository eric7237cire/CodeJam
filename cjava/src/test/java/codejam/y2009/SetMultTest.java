package codejam.y2009;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.y2009.round_3.alphabetomials.FastSolution;
import codejam.y2009.round_3.alphabetomials.InputData;
import codejam.y2009.round_3.alphabetomials.Main;

import com.google.common.collect.Lists;

public class SetMultTest {

    final static Logger log = LoggerFactory.getLogger(SetMultTest.class);
    
    
    
    @Test
    public void test2Sets() {
        
        StringBuffer test = new StringBuffer();
        test.append("aabbbb+aab+babbaa+aaaa 10\n"); //polynomial  k
        test.append("3\n"); //word count
        test.append("aabb\n");
        test.append("aabbb\n");
        test.append("aaabb\n");
        
        Main m = new Main();
        InputData input  = m.readInput( new Scanner(test.toString()), 1);
        
        
        
        
        /*
         * When k = 1 ; each word is evaluated once ;
         * total = 0  
         * for word 0 .. 2
         *  total += letterCount[ word 0 ][ 'a' ] * letterCount[ word 0 ][ 'a' ] * letterCount[ word 0 ][ 'b' ] 
         */
        int total = 0;
        
        
        
        String ans = m.handleCase(input);
        
        List<Map<String, Integer>> memo = Lists.newArrayList();
        for(int k = 1; k <= input.k; ++k) {
            memo.add(new HashMap<String,Integer>());
        }
        
        List<Integer> totals = Lists.newArrayList();
        
        for(int k = 1; k <= input.k; ++k) {
            total = FastSolution.combineTerms(input, k, memo);
            totals.add(total);
        }
        
        log.debug("Totals {} ; ans {}",  totals, ans);
        
        /*
         * k=2 represents the case where we have the product of 2 sets
         */
        
        //String s = m.handleCase(input);
        
        //log.debug(" ans {}", s);
    }
}
