package com.eric.codejam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

public class BinomialTerm extends AddTerms {
    
    @Override
    public String toString() {
        return "(" + super.toString() + ")" ;
    }
    public BinomialTerm(VariableTerm x, VariableTerm y) {
        super(Arrays.<Term>asList(x,y));
        
    }
    
    public VariableTerm getX() {
		return (VariableTerm) getTerms().get(0);
	}
	
	public VariableTerm getY() {
		return (VariableTerm) getTerms().get(1);
	}
	
    
}
