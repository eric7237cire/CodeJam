package com.eric.codejam;

import java.util.Map;

public interface Term {
    public void substitute(VariableTerm old, Term newTerm);
    
    public void multiply(Term mult);
    
    public void add(Term addTerm);
    
    public Term simplify();
        
    public int evaluate(Map<String, Integer> values);
}
