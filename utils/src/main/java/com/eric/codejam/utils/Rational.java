package com.eric.codejam.utils;

import com.google.common.base.Preconditions;

public class Rational {
    int numerator;
    int denom;
    public Rational(int numerator, int denom) {
        super();
        this.numerator = numerator;
        this.denom = denom;
    }
    
    public static Rational fromInt(int numerator) {
        return new Rational(numerator, 1);
    }
    
    
    public int multiplyToInt(int num) {
        int numerator = num * this.numerator;
        
        int ret = numerator / this.denom;
        
        int check = numerator % this.denom;
        
        Preconditions.checkArgument(check == 0);
        
        return ret;
    }

    @Override
    public String toString() {
        return "" + numerator + " / " + denom ;
    }
}