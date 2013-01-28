package codejam.y2009;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Scanner;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.y2009.round_3.alphabetomials.InputData;
import codejam.y2009.round_3.alphabetomials.Main;

public class SetMultTest {

    final static Logger log = LoggerFactory.getLogger(SetMultTest.class);
    
    public int multiplySets(String polyTerm, InputData in) {
        
        int L = polyTerm.length();
        
        int total = 0;
        
        for (int bm = 0; bm < (1 << L); bm++)
        {
            StringBuffer letters1 = new StringBuffer();
            StringBuffer letters2 = new StringBuffer();
            for (int i = 0; i < L; i++)
            {
                if ( (bm & (1 << i)) != 0)
                    letters1.append( polyTerm.charAt(i) );
                else
                    letters2.append( polyTerm.charAt(i) );
            }           
            
            int sum1 = 0;
            int sum2 = 0;
            //For each word in the dictionary, evaluate
            for(int w = 0; w < in.d; ++w) {
                sum1 += evaluateTerm(letters1.toString(), w, in);
                sum2 += evaluateTerm(letters2.toString(), w, in);
            }
            total += sum1*sum2;
        }
        
        return total;
    }
    
    public int evaluateTerm(String polyTerm, int word, InputData in) {
        int prod = 1;
        for(int c = 0; c < polyTerm.length(); ++c) {
            prod *= in.wordLetterCount[word][polyTerm.charAt(c)-'a'];
        }
        return prod;
    }
    
    @Test
    public void test2Sets() {
        
        StringBuffer test = new StringBuffer();
        test.append("aabbbb 2\n"); //polynomial  k
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
        for(int word = 0; word < input.dictWords.size(); ++word)
        {
            total += evaluateTerm(input.polynomial, word, input); //input.wordLetterCount[word]['a'-'a']*input.wordLetterCount[word]['a'-'a']*input.wordLetterCount[word]['b'-'a'];
        }
        
        
        List<Integer> u = m.doPerms(input);
        log.debug("Total {}", u);
        
        assertEquals(total, (int)u.get(0));
        
        
        int totalSq = multiplySets(input.polynomial, input);
        log.debug("Total sq {} ; k 2 {}", totalSq % 10009, u.get(1));
        /*
         * k=2 represents the case where we have the product of 2 sets
         */
        
        //String s = m.handleCase(input);
        
        //log.debug(" ans {}", s);
    }
}
