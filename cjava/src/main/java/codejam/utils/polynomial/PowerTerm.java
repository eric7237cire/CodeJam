package codejam.utils.polynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class PowerTerm extends AbstractTerm {
    final private int degree;
    public Term getTerm() {
        return term;
    }

    final private Term term;
    
    public int getDegree() {
        return degree;
    }


    public PowerTerm( Term term, int degree) {
        super();
        Preconditions.checkArgument(degree > 1);
        this.degree = degree;
        this.term = term;
    }

    @Override
    public String toString() {
        return degree != 1 ? term + "^" + degree : term.toString();
        
    }
    
    @Override
    public PowerTerm substitute(Map<VariableTerm, Term> termsToSub) {
        Term value = termsToSub.get(term);
        
        if (value != null) {
            return new PowerTerm(value, getDegree());
        } else {
            Term sub = term.substitute(termsToSub);
            if (sub != null) {
                return new PowerTerm(sub, getDegree());
            }
        }
        
        return null;
    }
    
    public Term substitute(VariableTerm old, Term newTerm) {
    	if (term.equals(old)) {
    		return new PowerTerm(newTerm, getDegree());
    	} else {
    		Term sub = term.substitute(old, newTerm);
    		if (sub != null) {
    		    return new PowerTerm(sub, getDegree());
    		}
    	}
    	
    	return null;
    }
    
    static int ipow(int base, int exp)
    {
        int result = 1;
        while (exp > 0)
        {
            if ((exp & 1) != 0)
                result *= base;
            exp >>= 1;
            base *= base;
        }

        return result;
    }
    
    public Term simplify() {
    	if (degree == 0) {
    		return new CoefficientTerm(1);
    	}
    	
    	if (term instanceof CoefficientTerm) {
    	    return new CoefficientTerm(ipow( ((CoefficientTerm) term).getValue(), degree ));
    	}
    	if (degree == 1) {
            return term;
        }
		if (term instanceof AddTerms && degree > 1) {
		    AddTerms binomial = (AddTerms) term;
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
			
			List<Term> addTerms = new ArrayList<>();
			
			for(int leftPower = degree; leftPower >= 0; --leftPower) {
				int rightPower = degree - leftPower;
				Term mt = MultTerms.buildMultTerm(
				(new CoefficientTerm(binomialCoeff.get(degree).get(rightPower))),
				createPowerTerm(binomial.getTerms().get(0), leftPower),
				createPowerTerm(binomial.getTerms().get(1), rightPower));
				addTerms.add(mt);
			}
			
			return new AddTerms(addTerms);
		}
		
		return super.simplify();
	}
    
    private static Term createPowerTerm(Term term, int power) {
        if (power > 1) {
            return new PowerTerm(term, power);
        }
        if (power == 1)
            return term;
        Preconditions.checkState(power == 0);
        return new CoefficientTerm(1);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(term, degree);        
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
    @Override
    public boolean canAdd(Term rhs) {
        return rhs.canAddAsRhs(this);
    }
    @Override
    public Term add(Term rhs) {
        return rhs.addAsRhs(this);
    }
    
    
    @Override
    public boolean canAddAsRhs(PowerTerm lhs) {
        return  lhs.getTerm().equals(getTerm()) ;
    }


    @Override
    public Term addAsRhs(PowerTerm lhs) {
        Preconditions.checkArgument( lhs.getTerm().equals(getTerm()) );
        return MultTerms.buildMultTerm(new CoefficientTerm(2), this);
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
    
    @Override
    public String getFirstNonCoefPart() {
        return term.getFirstNonCoefPart();
    }
    @Override
    public String getCoefPart() {
        return null;
        //return  term.getCoefPart();
    }
    @Override
    public String getNonCoefPart() {
        return toString();
    }
    
    
}
