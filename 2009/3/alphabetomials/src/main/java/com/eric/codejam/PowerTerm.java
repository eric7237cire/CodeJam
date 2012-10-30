package com.eric.codejam;

public class PowerTerm implements Term {
    int degree;
    Term term;
    
    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public PowerTerm( Term term, int degree) {
        super();
        this.degree = degree;
        this.term = term;
    }

    @Override
    public String toString() {
        return degree != 1 ? term + "^" + degree : term.toString();
        
    }
    
    
}
