package com.eric.codejam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class PowerTerm extends AbstractTerm {
    private int degree;
    public Term getTerm() {
        return term;
    }

    private Term term;
    
    public int getDegree() {
        return degree;
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
				MultTerms mt = new MultTerms(
				(new CoefficientTerm(binomialCoeff.get(degree).get(rightPower))),
				(new PowerTerm(binomial.getX(), leftPower)),
				(new PowerTerm(binomial.getY(), rightPower)));
				add.terms.add(mt);
			}
			
			return add;
		}
		
		return super.simplify();
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + degree;
        result = prime * result + ((term == null) ? 0 : term.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PowerTerm other = (PowerTerm) obj;
        return Objects.equal(degree, other.degree) && 
                Objects.equal(term, other.term);
    }
    
    @Override
    public int evaluate(Map<String, Integer> values) {
        int r = term.evaluate(values);
        
        for(int d = 2; d <= degree; ++d) {
            r *= r;
        }
            
        
        return r;
    }
    
    public boolean canMultiply(Term rhs) {
        return rhs.canMultiplyAsRhs(this);
    }
    public Term multiply(Term rhs) {
        //concrete lhs
        return rhs.multiplyAsRhs(this);
    }
    
    public Term multiplyAsRhs(VariableTerm lhs) {
        Preconditions.checkArgument(term.equals(lhs));
        return new PowerTerm(lhs, degree + 1);
    }
    public boolean canMultiplyAsRhs(VariableTerm lhs) {
        return term.equals(lhs);
    }
    
    public Term multiplyAsRhs(PowerTerm lhs) {
        Preconditions.checkArgument( lhs.getTerm().equals(getTerm()) );
        return new PowerTerm(lhs.getTerm(), lhs.getDegree() + this.getDegree());
    }
    public boolean canMultiplyAsRhs(PowerTerm lhs) {        
        return lhs.getTerm().equals(this.getTerm());
    }
    
}
