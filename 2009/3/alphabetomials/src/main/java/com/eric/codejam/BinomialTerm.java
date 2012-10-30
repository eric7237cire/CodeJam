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
    
    
}
