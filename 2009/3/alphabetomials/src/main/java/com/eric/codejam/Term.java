package com.eric.codejam;

import java.util.Map;

public interface Term {
    public Term substitute(VariableTerm old, Term newTerm);
    public Term substitute(Map<VariableTerm,Term> terms);
    
    
    public Term simplify();
        
    public int evaluate(Map<String, Integer> values);
    
    
    public boolean canMultiply(Term rhs);
    public Term multiply(Term rhs);
    
    public boolean canMultiplyAsRhs(CoefficientTerm lhs);
    public Term multiplyAsRhs(CoefficientTerm lhs);
    
    public boolean canMultiplyAsRhs(VariableTerm lhs);
    public Term multiplyAsRhs(VariableTerm lhs);
    
    
    public boolean canMultiplyAsRhs(MultTerms lhs);
    public Term multiplyAsRhs(MultTerms lhs);
    
    public boolean canMultiplyAsRhs(PowerTerm lhs);
    public Term multiplyAsRhs(PowerTerm lhs);
    
    public boolean canMultiplyAsRhs(AddTerms lhs);
    public Term multiplyAsRhs(AddTerms lhs);
    
    public boolean canAdd(Term rhs);
    public Term add(Term rhs);
    
    public boolean canAddAsRhs(CoefficientTerm lhs);
    public Term addAsRhs(CoefficientTerm lhs);
    
    public boolean canAddAsRhs(VariableTerm lhs);
    public Term addAsRhs(VariableTerm lhs);
    
    
    public boolean canAddAsRhs(MultTerms lhs);
    public Term addAsRhs(MultTerms lhs);
    
    public boolean canAddAsRhs(PowerTerm lhs);
    public Term addAsRhs(PowerTerm lhs);
    
    public boolean canAddAsRhs(AddTerms lhs);
    public Term addAsRhs(AddTerms lhs);
    
    
    //For sorting
    public String getNonCoefPart();
    public String getFirstNonCoefPart();
    public String getCoefPart();
    public int getDegree();
}
