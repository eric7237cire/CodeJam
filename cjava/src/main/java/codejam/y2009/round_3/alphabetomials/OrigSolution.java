package codejam.y2009.round_3.alphabetomials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import codejam.utils.polynomial.AddTerms;
import codejam.utils.polynomial.CoefficientTerm;
import codejam.utils.polynomial.Polynomial;
import codejam.utils.polynomial.Term;
import codejam.utils.polynomial.VariableTerm;

public class OrigSolution
{

    public List<Integer> usePoly(InputData input) {
        List<Integer> totals = new ArrayList<>();


        Map<String, Integer> values = new HashMap<>();

       

        List<Map<VariableTerm, Term>> subsList = new ArrayList<>();
        
        for (int i = 0; i < input.d; ++i) {
            Map<VariableTerm, Term> subs = new HashMap<>();
            for (int chInt = 'a'; chInt <= 'z'; ++chInt) {
                char ch = (char) chInt;
                
                String varName = "" + ch;
                
                //Count of letter in the dictionary
                int count = StringUtils.countMatches(input.dictWords.get(i), varName);
                
                //Letter/variable will be substituted by letter + count 
                subs.put(new VariableTerm(varName), new AddTerms(
                        new CoefficientTerm(count),
                        new VariableTerm(varName)));
                
                values.put(varName, 0);
            }
            
            subsList.add(subs);
        }

        Polynomial orig = new Polynomial(input.polynomial);
        orig.doSimplify();
        
        Polynomial totalPoly = new Polynomial();

        for (int eachK = 1; eachK <= input.k; ++eachK) {
            //System.out.println("k " + eachK);
            totalPoly = new Polynomial();

            for (int i = 0; i < input.d; ++i) {
                //System.out.println("i " + i);
                Polynomial p = new Polynomial(orig);

                //log.info("Computing k {} before sub {}", eachK, p);
                p.substitute(subsList.get(i));
                //log.debug("Computing k {} after sub {}", eachK, p);
                totalPoly.addSelf(p);
                
                //log.info("Computing k {} after add {}", eachK, totalPoly);
                totalPoly.doSimplify();

                //log.info("Computing k {} after simplify {}", eachK, totalPoly);
                
            }

            totalPoly.doSimplify();

            //log.debug("Poly obj {} k {}", totalPoly, eachK);
            
            /**
             * Save the polynomial representing the total p(S).  The
             * sum of all permutations of the words in the dictionary (basically a=3,b=2,...)
             * using k words.
             * 
             * To compute k+1 the next round, it is as if we take all the words again and
             * add them.  
             * 
             * The trick is we keep the last round in polynomial form, ready to make another subsitition.
             * 
             * ie, the polynomial is a^2
             * 
             * for k = 1, and d= a=2; a=3 we do (a+2)^2 + (a+3)^2.
             * 
             * The total for k = 1 is the value of the polynomial with all vars = 0.
             * 
             * The variables is to be ready to substitute for the next round.
             * 
             * k=2 will be (a+2+2)^2 + (a+2+3)^2 + (a+3+2)^2 + (a+3+3)^2 
             */
            orig = new Polynomial(totalPoly);
            
            totals.add(totalPoly.evaluate(values) % 10009);
            //System.out.println(totals);
        }

        return totals;
    }

}
