package com.eric.codejam;

public class BinomialTerm extends AbstractTerm {
    private VariableTerm x;
    private VariableTerm y;
    @Override
    public String toString() {
        return "(" + x + " + " + y + ")" ;
    }
    public BinomialTerm(VariableTerm x, VariableTerm y) {
        super();
        this.x = x;
        this.y = y;
    }
    
    @Override
	public void multiply(Term mult) {
				
	}
	public VariableTerm getX() {
		return x;
	}
	public void setX(VariableTerm x) {
		this.x = x;
	}
	public VariableTerm getY() {
		return y;
	}
	public void setY(VariableTerm y) {
		this.y = y;
	}
    
    
}
