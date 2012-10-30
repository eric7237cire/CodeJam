package com.eric.codejam;

public class CoefficientTerm extends AbstractTerm {
    int value;

    public CoefficientTerm(int value) {
         this.value = value;
    }

	@Override
	public void multiply(Term mult) {
		if (mult instanceof CoefficientTerm) {
			this.value *= ((CoefficientTerm) mult).value;
		}
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
    
    
}
