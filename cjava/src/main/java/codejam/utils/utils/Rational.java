package codejam.utils.utils;

import com.google.common.base.Preconditions;

public class Rational {
    final int numerator;
    final int denom;
    public Rational(int numerator, int denom) {
        super();
        this.numerator = numerator;
        this.denom = denom;
    }
    
    public static Rational fromInt(int numerator) {
        return new Rational(numerator, 1);
    }
    
    public Rational multiply(Rational r) {
        return new Rational(numerator * r.numerator, denom * r.denom);
    }
    
    public Rational divide(Rational r) {
        return new Rational(numerator * r.denom, denom * r.numerator);
    }
    
    public Rational minus(Rational r) {
        Rational a = new Rational(numerator * r.denom, denom * r.denom);
        Rational b = new Rational(r.numerator * denom, r.denom * denom);
        
        return new Rational(a.numerator - b.numerator, a.denom);
    }
    
    public Rational add(Rational r) {
        Rational a = new Rational(numerator * r.denom, denom * r.denom);
        Rational b = new Rational(r.numerator * denom, r.denom * denom);
        
        return new Rational(a.numerator + b.numerator, a.denom);
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