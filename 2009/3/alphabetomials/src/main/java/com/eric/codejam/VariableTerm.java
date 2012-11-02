package com.eric.codejam;

import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class VariableTerm extends AbstractTerm {
    private String name;
    
    public String getName() {
        return name;
    }
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
    
    public boolean canMultiply(Term rhs) {
        return rhs.canMultiplyAsRhs(this);
    }
    public Term multiply(Term rhs) {
        //concrete lhs
        return rhs.multiplyAsRhs(this);
    }
    
    public Term multiplyAsRhs(VariableTerm lhs) {
        return new PowerTerm(lhs, 2);
    }
    public boolean canMultiplyAsRhs(VariableTerm lhs) {
        return lhs.equals(this);
    }
    
    public Term multiplyAsRhs(PowerTerm lhs) {
        Preconditions.checkArgument( ((VariableTerm) lhs.getTerm()).getName().equals(name) );
        return new PowerTerm(lhs.getTerm(), lhs.getDegree() + 1);
    }
    public boolean canMultiplyAsRhs(PowerTerm lhs) {        
        return lhs.getTerm().equals(this);
    }
    
   
    @Override
    public int evaluate(Map<String, Integer> values) {
        return values.get(name);
    }
    @Override
    public String getNonCoefPart() {
        return name;
    }
    @Override
    public String getCoefPart() {
        
        return  null;
    }
    @Override
    public int getDegree() {
        return 1;
    }
}
