package com.eric.codejam;

import java.util.ArrayList;
import java.util.List;

public class PowerTerm extends AbstractTerm {
    int degree;
    Term term;
    
    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public PowerTerm( Term term, int degree) {
        super();
        this.degree = degree;
        this.term = term;
    }

    @Override
    public String toString() {
        return degree != 1 ? term + "^" + degree : term.toString();
        
    }
    @Override
	public void multiply(Term mult) {
				
	}
    
    public void substitute(VariableTerm old, Term newTerm) {
    	if (term.equals(old)) {
    		term = newTerm;
    	} else {
    		term.substitute(old, newTerm);
    	}
    }
    
    public Term simplify() {
    	if (degree == 0) {
    		return new CoefficientTerm(1);
    	}
		if (term instanceof BinomialTerm && degree > 1) {
			BinomialTerm binomial = (BinomialTerm) term;
			List<List<Integer>> binomialCoeff = new ArrayList<>();
			binomialCoeff.add(new ArrayList<Integer>());
			binomialCoeff.get(0).add(1);
			
			for(int level=1; level<=6; ++level) {
				List<Integer> prevLevel = binomialCoeff.get(level-1);
				List<Integer> currentLevel = new ArrayList<>();
				currentLevel.add(1);
				for(int i = 1; i < prevLevel.size(); ++i) {
					currentLevel.add(prevLevel.get(i) + prevLevel.get(i-1));
				}
				currentLevel.add(1);
				binomialCoeff.add(currentLevel);
			}
			
			AddTerms add = new AddTerms();
			for(int leftPower = degree; leftPower >= 0; --leftPower) {
				int rightPower = degree - leftPower;
				MultTerms mt = new MultTerms();
				mt.terms.add(new CoefficientTerm(binomialCoeff.get(degree).get(rightPower)));
				mt.terms.add(new PowerTerm(binomial.getX(), leftPower));
				mt.terms.add(new PowerTerm(binomial.getY(), rightPower));
				add.terms.add(mt);
			}
			
			return add;
		}
		
		return super.simplify();
	}
    
}
