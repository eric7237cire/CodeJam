package com.eric.codejam;

import java.util.Map;

public class CoefficientTerm extends AbstractTerm {
    private int value;

    public int getValue() {
        return value;
    }



    public CoefficientTerm(int value) {
         this.value = value;
    }

	

    public boolean canMultiply(Term rhs) {
        return rhs.canMultiplyAsRhs(this);
    }
	public Term multiply(Term rhs) {
	    //concrete lhs
	    return rhs.multiplyAsRhs(this);
	}
	
	public Term multiplyAsRhs(CoefficientTerm lhs) {
	    return new CoefficientTerm(lhs.value * value);
	}
	public boolean canMultiplyAsRhs(CoefficientTerm lhs) {
        return true;
    }

	@Override
    public boolean canMultiplyAsRhs(AddTerms lhs) {
        return lhs.canMultiplyAsRhs(this);        
    }

    @Override
    public Term multiplyAsRhs(AddTerms lhs) {
        return lhs.multiplyAsRhs(this);
    }



    @Override
    public Term multiplyAsRhs(MultTerms lhs) {
        return lhs.multiplyAsRhs(this);
    }



    @Override
    public boolean canAddAsRhs(MultTerms lhs) {
        return lhs.canAddAsRhs(this);
    }

    @Override
	public String toString() {
		return Integer.toString(value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
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
		CoefficientTerm other = (CoefficientTerm) obj;
		if (value != other.value)
			return false;
		return true;
	}
    
	@Override
    public int evaluate(Map<String, Integer> values) {
	    return value;
	}
}
