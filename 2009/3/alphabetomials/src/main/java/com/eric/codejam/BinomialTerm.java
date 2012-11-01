package com.eric.codejam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public Term multiply(Term rhs) {
        return rhs.multiplyAsRhs(this);
    }
    @Override
    public boolean canMultiply(Term rhs) {
        return rhs.canMultiply(this);
    }
    @Override
    public boolean canMultiplyAsRhs(BinomialTerm lhs) {
        return true;
    }
    @Override
    public boolean canMultiplyAsRhs(VariableTerm lhs) {
        return true;
    }
    
    @Override
    public Term multiplyAsRhs(VariableTerm lhs) {
        BinomialTerm rhs = this;
        
        List<Term> terms = new ArrayList<>();
        MultTerms m = new MultTerms(
        (lhs),
        (rhs.getX()));
        terms.add(m);

        m = new MultTerms(
        (lhs),
        (rhs.getY()));
        terms.add(m);

        return new AddTerms(terms);
    }
    @Override
    public Term multiplyAsRhs(BinomialTerm lhs) {
        BinomialTerm rhs = this;
        
        List<Term> terms = new ArrayList<>();
        MultTerms m = new MultTerms(
        (lhs.getX()),
        (rhs.getX()));
        terms.add(m);

        m = new MultTerms(
        (lhs.getX()),
        (rhs.getY()));
        terms.add(m);

        m = new MultTerms(
        (lhs.getY()),
        (rhs.getX()));
        terms.add(m);

        m = new MultTerms(
        (lhs.getY()),
        (rhs.getY()));
        terms.add(m);

        return new AddTerms(terms);
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
    
	@Override
    public int evaluate(Map<String, Integer> values) {
	    return x.evaluate(values) + y.evaluate(values);
	}
}
