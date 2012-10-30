package com.eric.codejam;

import java.util.Map;

import com.google.common.base.Objects;

public class VariableTerm extends AbstractTerm {
    String name;
    
    public VariableTerm(String name) {
        super();
        this.name = name;
    }
    @Override
    public String toString() {
        
        
        return name;
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VariableTerm other = (VariableTerm) obj;
        return Objects.equal(name,other.name);
            
    }
    
    @Override
	public void multiply(Term mult) {
				
	}
    
    @Override
    public int evaluate(Map<String, Integer> values) {
        return values.get(name);
    }
}
