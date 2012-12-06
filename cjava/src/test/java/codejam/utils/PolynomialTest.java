package codejam.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.polynomial.AddTerms;
import codejam.utils.polynomial.CoefficientTerm;
import codejam.utils.polynomial.MultTerms;
import codejam.utils.polynomial.Polynomial;
import codejam.utils.polynomial.PowerTerm;
import codejam.utils.polynomial.Term;
import codejam.utils.polynomial.VariableTerm;

public class PolynomialTest {
    final static Logger log = LoggerFactory.getLogger(PolynomialTest.class);
    
    @Test
    public void testConstructor() {
        Polynomial p = new Polynomial("aaab+caac+c");
        
        p.doSimplify();
        assertEquals("a^3*b + a^2*c^2 + c", p.toString());
    }
    
    @Test
    public void testConstructor2() {
        
        Polynomial p = new Polynomial("ehw+hwww");
        
        p.doSimplify();
        assertEquals("e*h*w + h*w^3", p.toString());
        
        p.substitute(new VariableTerm("w"), new AddTerms(new VariableTerm("f_0"), new VariableTerm("f_1")));
        
        p.doSimplify();
        
        assertEquals("e*f_0*h + e*f_1*h + f_0^3*h + 3*f_0^2*f_1*h + 3*f_0*f_1^2*h + f_1^3*h", p.toString());
    }
    
    @Test
    public void testMultConstructor() {
        MultTerms mul = (MultTerms) Polynomial.parseTerms("8(a_2 + a_1)^2");
        assertTrue(mul.getTerms().get(0) instanceof CoefficientTerm);
        assertTrue(mul.getTerms().get(1) instanceof PowerTerm);
        
        PowerTerm pt = (PowerTerm) mul.getTerms().get(1);
        assertEquals( 2, pt.getDegree() );
        
        AddTerms bt = (AddTerms) pt.getTerm();
        assertEquals( "a_1", ((VariableTerm) bt.getTerms().get(0)).getName() );
        assertEquals( "a_2", ((VariableTerm) bt.getTerms().get(1)).getName() );
        
        Polynomial p = new Polynomial();
        p.addSelf(mul);
        p.doSimplify();
        
        assertEquals( "8*a_1^2 + 16*a_1*a_2 + 8*a_2^2", p.toString());
        
    }
    
    @Test
    public void testMultConstructor2() {
        
        Polynomial p = new Polynomial("a_0*a_x + a_0*a_0");
        p.doSimplify();
        
        assertEquals( "a_0^2 + a_0*a_x", p.toString());
    }
    
    @Test
    public void testExpand1() {
        Polynomial p = new Polynomial("aa");
        
        p.substitute(new VariableTerm("a"), new VariableTerm("a_0"));
        
        p.doSimplify();
        
        assertEquals("a_0^2", p.toString());
        
        p.substitute(new VariableTerm("a_0"), new AddTerms(new VariableTerm("a_0"), new VariableTerm("a_1")));
        
        //assertEquals("(a_0 + a_1)^2", p.toString());
        
        p.doSimplify();
        
        assertEquals("a_0^2 + 2*a_0*a_1 + a_1^2", p.toString());
    }
    
    @Test
    public void testExpand2() {
        Polynomial p = new Polynomial("aa+bb+cc");
        
        p.substitute(new VariableTerm("a"), new VariableTerm("a_0"));
        p.substitute(new VariableTerm("b"), new VariableTerm("a_1"));
        p.substitute(new VariableTerm("c"), new VariableTerm("a_2"));
        
        p.doSimplify();
        assertEquals("a_0^2 + a_1^2 + a_2^2", p.toString());
        
        Polynomial p2 = new Polynomial(p);
        Polynomial p3 = new Polynomial(p);
        
        Map<VariableTerm, Term> subs = new HashMap<>();
        subs.put(new VariableTerm("a_0"), new AddTerms(new VariableTerm("a_0"), new VariableTerm("a_0")));
        subs.put(new VariableTerm("a_1"), new AddTerms(new VariableTerm("a_0"), new VariableTerm("a_1")));
        subs.put(new VariableTerm("a_2"), new AddTerms(new VariableTerm("a_0"), new VariableTerm("a_2")));
        p.substitute(subs);
        
        p.doSimplify();
        assertEquals("6*a_0^2 + 2*a_0*a_1 + 2*a_0*a_2 + a_1^2 + a_2^2", p.toString());
        
        subs = new HashMap<>();
        subs.put(new VariableTerm("a_0"), new AddTerms(new VariableTerm("a_1"), new VariableTerm("a_0")));
        subs.put(new VariableTerm("a_1"), new AddTerms(new VariableTerm("a_1"), new VariableTerm("a_1")));
        subs.put(new VariableTerm("a_2"), new AddTerms(new VariableTerm("a_1"), new VariableTerm("a_2")));
        p2.substitute(subs);
        
        subs = new HashMap<>();
        subs.put(new VariableTerm("a_0"), new AddTerms(new VariableTerm("a_2"), new VariableTerm("a_0")));
        subs.put(new VariableTerm("a_1"), new AddTerms(new VariableTerm("a_2"), new VariableTerm("a_1")));
        subs.put(new VariableTerm("a_2"), new AddTerms(new VariableTerm("a_2"), new VariableTerm("a_2")));
        p3.substitute(subs);
        
        //assertEquals("(a_0 + a_0)^2 + (a_0 + a_1)^2 + (a_0 + a_2)^2", p.toString());
        
        p.addSelf(p2);
        p.addSelf(p3);
        
        p.doSimplify();
        String s = p.toString();
        
        log.info(s);
        
        assertEquals("8*a_0^2 + 4*a_0*a_1 + 4*a_0*a_2 + 8*a_1^2 + 4*a_1*a_2 + 8*a_2^2", p.toString());
    }
    
    @Test
    public void testMultCoeff() {
        Term c1 = new CoefficientTerm(3);
        Term c2 = new CoefficientTerm(7);
        
        Term c3 = c1.multiply(c2);
        
        CoefficientTerm c3_c = (CoefficientTerm) c3;
        assertEquals(3*7, c3_c.getValue());
        
        Polynomial p = new Polynomial("5 * x_3 * 9");
        p.doSimplify();
        
        assertEquals("45*x_3", p.toString());
    }
    
    @Test
    public void testSubCoeff() {
        Polynomial p = new Polynomial("E10^2+2*E10*E12+E11^2+2*E11*E12+6*E12^2");
        
        Map<String, Integer> subs = new HashMap<String,Integer>();
        subs.put("E10", 1);
        subs.put("E11", 3);
        subs.put("E12", 2);
        
        p.substituteVals(subs);
        
        p.doSimplify();
        
        assertEquals("50", p.toString());
        
    }
    
    @Test
    public void testSimp() {
        Polynomial p = new Polynomial("a_0^2 + 7*a_0^2");
        p.doSimplify();
        assertEquals("8*a_0^2", p.toString());
    }
    
    
}
