package codejam.utils.polynomial;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public abstract class AbstractTerm implements Term {

    protected static Logger log = LoggerFactory.getLogger(AbstractTerm.class);
    
    @Override
    public Term substitute(VariableTerm old, Term newTerm) {
        return null;
    }
    
    @Override
    public Term substitute(Map<VariableTerm, Term> terms) {
        return null;
    }

    @Override
    public String getFirstNonCoefPart() {
        return null;
    }
    @Override
    public String getCoefPart() {
        return null;
    }
    @Override
    public int getDegree() {
        return 0;
    }
	
    
	public Term simplify() {
		return null;
	}
 
	public Term multiply(Term rhs) {
	    throw new UnsupportedOperationException("ex");
	}
    
    public Term multiplyAsRhs(CoefficientTerm lhs) {
        Preconditions.checkArgument(lhs.getValue() == 1);
        return this;
    }
    public boolean canMultiplyAsRhs(CoefficientTerm lhs) {
        return lhs.getValue() == 1;
    }
    
    public Term multiplyAsRhs(VariableTerm lhs) {
        throw new UnsupportedOperationException("ex");
    }
    
    public boolean canMultiply(Term rhs) {
        return false;
    }
    
    
    
    public boolean canMultiplyAsRhs(VariableTerm lhs) {
        return false;
    }
    
    
    
    public boolean canMultiplyAsRhs(MultTerms lhs) {
        return false;
    }
    public Term multiplyAsRhs(MultTerms lhs) {
        throw new UnsupportedOperationException("ex");
    }
    
    public boolean canMultiplyAsRhs(PowerTerm lhs) {
        return false;
    }
    public Term multiplyAsRhs(PowerTerm lhs) {
        throw new UnsupportedOperationException("ex");
    }
    
    public boolean canMultiplyAsRhs(AddTerms lhs) {
        return false;
    }
    public Term multiplyAsRhs(AddTerms lhs) {
        throw new UnsupportedOperationException("ex");
    }
    
    
    public boolean canAdd(Term rhs) {
        return false;
    }
    public Term add(Term rhs) {
        throw new UnsupportedOperationException("ex");
    }
    
    public boolean canAddAsRhs(CoefficientTerm lhs) {
        return false;
    }
    public Term addAsRhs(CoefficientTerm lhs) {
        throw new UnsupportedOperationException("ex");
    }
    
    public boolean canAddAsRhs(VariableTerm lhs) {
        return false;
    }
    public Term addAsRhs(VariableTerm lhs) {
        throw new UnsupportedOperationException("ex");
    }
    
   
    
    public boolean canAddAsRhs(MultTerms lhs) {
        return false;
    }
    public Term addAsRhs(MultTerms lhs) {
        throw new UnsupportedOperationException("ex");
    }
    
    public boolean canAddAsRhs(PowerTerm lhs) {
        return false;
    }
    public Term addAsRhs(PowerTerm lhs) {
        throw new UnsupportedOperationException("ex");
    }
    
    public boolean canAddAsRhs(AddTerms lhs) {
        return false;
    }
    public Term addAsRhs(AddTerms lhs) {
        throw new UnsupportedOperationException("ex");
    }
    @Override
    public String getNonCoefPart() {
        return getFirstNonCoefPart();
    }
}
