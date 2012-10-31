package com.eric.codejam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rits.cloning.Cloner;

public class PolynomialTest {
    final static Logger log = LoggerFactory.getLogger(PolynomialTest.class);
    
    @Test
    public void testConstructor() {
        Polynomial p = new Polynomial("aaab+caac+c");
        
        p.simplify();
        assertEquals("a^3*b + a^2*c^2 + c", p.toString());
    }
    
    @Test
    public void testMultConstructor() {
        MultTerms mul = new MultTerms("8(a_2 + a_1)^2");
        assertEquals(8, mul.getCoeff());
        assertTrue(mul.getTerms().get(0) instanceof PowerTerm);
        
        PowerTerm pt = (PowerTerm) mul.getTerms().get(0);
        assertEquals( 2, pt.degree );
        
        BinomialTerm bt = (BinomialTerm) pt.term;
        assertEquals( "a_2", ((VariableTerm) bt.getX()).name );
        assertEquals( "a_1", ((VariableTerm) bt.getY()).name );
        
        Polynomial p = new Polynomial();
        p.add(mul);
        p.doSimplify();
        
        assertEquals( "8a_1^2 + 16a_1*a_2 + 8a_2^2", p.toString());
        
    }
    
    @Test
    public void testMultConstructor2() {
        MultTerms mul = new MultTerms("a_0*(a_x + a_0)");
        
        Polynomial p = new Polynomial();
        p.add(mul);
        p.doSimplify();
        
        assertEquals( "a_0^2 + a_0*a_x", p.toString());
    }
    
    @Test
    public void testExpand1() {
        Polynomial p = new Polynomial("aa");
        
        p.substitute(new VariableTerm("a"), new VariableTerm("a_0"));
        
        p.simplify();
        
        assertEquals("a_0^2", p.toString());
        
        p.substitute(new VariableTerm("a_0"), new BinomialTerm(new VariableTerm("a_0"), new VariableTerm("a_1")));
        
        assertEquals("(a_0 + a_1)^2", p.toString());
        
        p.doSimplify();
        
        assertEquals("a_0^2 + 2a_0*a_1 + a_1^2", p.toString());
    }
    
    @Test
    public void testExpand2() {
        Polynomial p = new Polynomial("aa+bb+cc");
        
        p.substitute(new VariableTerm("a"), new VariableTerm("a_0"));
        p.substitute(new VariableTerm("b"), new VariableTerm("a_1"));
        p.substitute(new VariableTerm("c"), new VariableTerm("a_2"));
        
        p.simplify();
        assertEquals("a_0^2 + a_1^2 + a_2^2", p.toString());
        
        Cloner cloner=new Cloner();
        Polynomial p2 = cloner.deepClone(p);
        Polynomial p3 = cloner.deepClone(p);
        
        Map<VariableTerm, Term> subs = new HashMap<>();
        subs.put(new VariableTerm("a_0"), new BinomialTerm(new VariableTerm("a_0"), new VariableTerm("a_0")));
        subs.put(new VariableTerm("a_1"), new BinomialTerm(new VariableTerm("a_0"), new VariableTerm("a_1")));
        subs.put(new VariableTerm("a_2"), new BinomialTerm(new VariableTerm("a_0"), new VariableTerm("a_2")));
        p.substitute(subs);
        
        subs = new HashMap<>();
        subs.put(new VariableTerm("a_0"), new BinomialTerm(new VariableTerm("a_1"), new VariableTerm("a_0")));
        subs.put(new VariableTerm("a_1"), new BinomialTerm(new VariableTerm("a_1"), new VariableTerm("a_1")));
        subs.put(new VariableTerm("a_2"), new BinomialTerm(new VariableTerm("a_1"), new VariableTerm("a_2")));
        p2.substitute(subs);
        
        subs = new HashMap<>();
        subs.put(new VariableTerm("a_0"), new BinomialTerm(new VariableTerm("a_2"), new VariableTerm("a_0")));
        subs.put(new VariableTerm("a_1"), new BinomialTerm(new VariableTerm("a_2"), new VariableTerm("a_1")));
        subs.put(new VariableTerm("a_2"), new BinomialTerm(new VariableTerm("a_2"), new VariableTerm("a_2")));
        p3.substitute(subs);
        
        assertEquals("(a_0 + a_0)^2 + (a_0 + a_1)^2 + (a_0 + a_2)^2", p.toString());
        
        p.add(p2);
        p.add(p3);
        
        p.doSimplify();
        String s = p.toString();
        
        log.info(s);
        
        assertEquals("8a_0^2 + 4a_0*a_1 + 4a_0*a_2 + 8a_1^2 + 4a_1*a_2 + 8a_2^2", p.toString());
    }
    
    
}
