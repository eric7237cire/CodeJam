package codejam.y2012.round_final.twirling_freedom;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.math3.complex.Complex;

import codejam.utils.geometry.Point;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.polynomial.AddTerms;
import codejam.utils.polynomial.CoefficientTerm;
import codejam.utils.polynomial.MultTerms;
import codejam.utils.polynomial.Polynomial;
import codejam.utils.polynomial.Term;
import codejam.utils.polynomial.VariableTerm;

import com.google.common.collect.Lists;

public class Main extends InputFilesHandler 
    implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    public Main() 
    {
        super("D", 0, 0, 1);
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);

        in.N = scanner.nextInt();
        in.M = scanner.nextInt();

        in.stars = Lists.newArrayList();

        for (int i = 0; i < in.N; ++i) {

            in.stars.add(new PointInt(scanner.nextInt(),scanner.nextInt()));
        }
        return in;
    }

    public void compareComplexAndPoint() {
        Complex c = new Complex(4, 2);
        
        Complex i = new Complex(0, 1);
        Complex iNeg = new Complex(0, -1);
        
        Point p = new Point(c.getReal(), c.getImaginary());
        
        log.debug("Complex {}, p {}", c.multiply(iNeg), p.rotate(-Math.PI / 2));
        
        
        c = new Complex(5, 4);        
        Complex q = new Complex(14,7);
        
         p = new Point(c.getReal(), c.getImaginary());
         Point qPoint = new Point(q.getReal(), q.getImaginary());
        
        
        Complex p0 = c;
        //Complex p1 = iNeg.multiply(p0).add(i.multiply(q)).add(q);
        Complex p1 = iNeg.multiply(p0).add(q.multiply(new Complex(1, 1)));
        
        
        log.debug("Complex {} ; {}, p {}", 
                c.subtract(q).multiply(iNeg).add(q),
                p1,
                p.translate(qPoint).rotate(-Math.PI / 2).translate( qPoint.scale(-1)));
        
        
        p0 = new Complex(-3, 6);
        
        List<Complex> qList = Arrays.asList(
                new Complex(3, 8),
                new Complex(4, 2),
                new Complex(3, -8),
                new Complex(-1, 8),
                new Complex(1, 1),
                new Complex(-9, 5));
        
        List<Complex> pList = Lists.newArrayList();
        pList.add(p0);
        for(int qNum = 0; qNum < qList.size(); ++qNum)
        {
            Complex pLast = pList.get(pList.size() - 1);
            Complex qCur = qList.get(qNum);
            
            Complex pNext = iNeg.multiply(pLast).add(qCur.multiply(new Complex(1, 1)));
            
            pList.add(pNext);
            
            log.debug("q{}={} p{}={}", qNum, qCur, qNum+1, pNext);
        }
        
        //Complex p2  = iNeg.multiply(p1).add(q1.multiply(new Complex(1, 1)));
        
        //Complex p2_alt = p0.multiply(-1).add(q0).add(q1.multiply(i)).subtract(q0.multiply(i)).add(q1);
        
        
        
    }
    
    public void formula() {
        CoefficientTerm co = new CoefficientTerm(-1);
        Term m1 = MultTerms.buildMultTerm(co, new VariableTerm("i"),new VariableTerm("p"));
        Term m2 = MultTerms.buildMultTerm(new VariableTerm("q"),
                new AddTerms(new VariableTerm("i"), new CoefficientTerm(1)));
        
        Term add = new AddTerms(m1,m2);
        Polynomial p = new Polynomial();
        p.setAddTerms(add);
        //p.getAddTerms().add(m2);
        p.doSimplify();
        
        Polynomial p1 = new Polynomial(p);
        p1.substitute(new VariableTerm("q"),new VariableTerm("q_0"));
        p1.substitute(new VariableTerm("p"),new VariableTerm("p_0"));
        
        List<Polynomial> polys = Lists.newArrayList();
        polys.add(p1);
        
        
        
        for(int pNum = 2; pNum <= 12; ++pNum) {
            Polynomial px = new Polynomial(p);
            px.substitute(new VariableTerm("q"),new VariableTerm("q_" + (pNum-1)));
            px.substitute(new VariableTerm("p"), polys.get(polys.size()-1).getAddTerms());
            px.doSimplify();
            polys.add(px);
        }
        
        for(int pNum = 1; pNum <= 12; ++pNum) {
            log.debug("P{} = {}", pNum, polys.get(pNum-1).toString());
        }
        
        Complex p0 = new Complex(-3, 6);
        
        List<Complex> qList = Arrays.asList(
                new Complex(3, 8),
                new Complex(4, 2),
                new Complex(3, -8),
                new Complex(-1, 8),
                new Complex(1, 1),
                new Complex(-9, 5));
        
        for(int pNum = 0; pNum < 12; ++pNum) {
            Polynomial poly = polys.get(pNum);
            
            poly.substitute(new VariableTerm("p_0"), complexToTerm(p0));
            for(int q = 0; q < qList.size(); ++q) {
                poly.substitute(new VariableTerm("q_" + q), complexToTerm(qList.get(q)));
            }
            poly.doSimplify();
            
            log.debug("P{} = {}", pNum+1, poly.toString());
        }
        
        
    }
    
    public Term complexToTerm(Complex c) {
        return new AddTerms(new CoefficientTerm((int)c.getReal()),
                MultTerms.buildMultTerm(new VariableTerm("i"), 
                        new CoefficientTerm((int)c.getImaginary())));
    }
    
    public String handleCase(InputData in) {

        compareComplexAndPoint();
        
        formula();
        
        return String.format("Case #%d: ", in.testCase);
        
    }

}
