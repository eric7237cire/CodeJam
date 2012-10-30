package com.eric.codejam;

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
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        VariableTerm other = (VariableTerm) obj;
        return Objects.equal(name,other.name);
            
    }
    
    
}
