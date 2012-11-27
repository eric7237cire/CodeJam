package com.eric.codejam;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.polynomial.Polynomial;
import com.eric.codejam.polynomial.VariableTerm;
import com.eric.codejam.test.TesterBase;
import com.eric.codejam.utils.Prime;

/**
 * Unit test for simple App.
 */
public class AppTest extends TesterBase {

    final static Logger log = LoggerFactory.getLogger(AppTest.class);

    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public AppTest() {
        super();
    }

    @Override
    protected String getOutput(String testCaseData) throws IOException {

        Main m = new Main();

        InputData input = m.readInput(new BufferedReader(new StringReader(
                testCaseData)), 1);

        String output = m.handleCase(1, input);

        log.info(output);
        return output.trim();
    }

    @Test
    public void test() {
        Polynomial p = new Polynomial("a*s + b");
        Map<String, Integer> subs = new HashMap();
        subs.put("a", 3);
        subs.put("s", 2);
        subs.put("b", 7);
        int ans = p.evaluate(subs);
        assertEquals(13, ans);
        
        for(int k=0; k < 10; ++k) {
            p.doSimplify();
            log.info(p.toString());
            Polynomial p2 = new Polynomial("a*s + b");
            p2.substitute(new VariableTerm("s"), p.getAddTerms());
            p = p2;
        }
        
        
        
    }
    
    @Test
    public void testPrimes()  {
        List<Integer> p4 = Prime.generatePrimes(10000);
        log.debug("P4 size {}",p4.size());
        List<Integer> p6 = Prime.generatePrimes(1000000);
        log.debug("P6 size {}",p6.size());
    }

}
