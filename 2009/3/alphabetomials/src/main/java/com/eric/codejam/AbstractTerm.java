package com.eric.codejam;

import java.util.Map;

public abstract class AbstractTerm implements Term {

    public void substitute(VariableTerm old, Term newTerm) {
        
    }
    
    

	@Override
	abstract public void multiply(Term mult);
	
	public void add(Term addTerm) {
		
	}
    
	public Term simplify() {
		return null;
	}
    
}
