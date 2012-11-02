package com.eric.codejam;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class CoefficientTerm extends AbstractTerm {
    private int value;

    public int getValue() {
        return value;
    }

    public CoefficientTerm(int value) {
        this.value = value;
    }

    @Override
    public boolean canMultiply(Term rhs) {
        // concrete lhs
        return rhs.canMultiplyAsRhs(this);
    }

    @Override
    public Term multiply(Term rhs) {
        // concrete lhs
        return rhs.multiplyAsRhs(this);
    }

    @Override
    public Term multiplyAsRhs(CoefficientTerm lhs) {
        return new CoefficientTerm(lhs.value * value);
    }

    @Override
    public boolean canMultiplyAsRhs(CoefficientTerm lhs) {
        return true;
    }

    @Override
    public boolean canAdd(Term rhs) {
        return rhs.canAddAsRhs(this);
    }

    @Override
    public Term add(Term rhs) {
        return rhs.addAsRhs(this);
    }

    @Override
    public boolean canAddAsRhs(CoefficientTerm lhs) {
        return true;
    }

    @Override
    public Term addAsRhs(CoefficientTerm lhs) {
        return new CoefficientTerm(value+lhs.value);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + value;
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
        CoefficientTerm other = (CoefficientTerm) obj;
        if (value != other.value)
            return false;
        return true;
    }

    @Override
    public int evaluate(Map<String, Integer> values) {
        return value;
    }

    @Override
    public String getNonCoefPart() {
        return null;
    }

    @Override
    public String getCoefPart() {

        return Integer.toString(value);
    }

    @Override
    public int getDegree() {
        return 1;
    }
}
