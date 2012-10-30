package com.eric.codejam;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class PolynomialTest {
    final static Logger log = LoggerFactory.getLogger(PolynomialTest.class);
    
    @Test
    public void testConstructor() {
        Polynomial p = new Polynomial("aaab+caac+c");
        
        assertEquals("a^3*b + a^2*c^2 + c", p.toString());
    }
    
    @Test
    public void testExpand() {
        Polynomial p = new Polynomial("aa");
        
        p.substitute(new VariableTerm("a"), new VariableTerm("a_0"));
        
        assertEquals("a_0^2", p.toString());
    }
}
