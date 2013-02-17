package codejam.utils.math;

public interface Numeric<U> extends Comparable<Numeric<U>>
{
    public Numeric<U> negate();
    
    public U getVal();
    
    public Numeric<U> fromInt(int i);
    
    public Numeric<U> getMax();
    
    public Numeric<U> add(Numeric<U> rhs);
    
    public Numeric<U> subtract(Numeric<U> rhs);
    
    public Numeric<U> min(Numeric<U> rhs);
    
    public Numeric<U> multiply(Numeric<U> rhs);
    
    
    public <T> Numeric<U> multiplyNumeric(Numeric<T> rhs);
    
    public long longValue();
}
