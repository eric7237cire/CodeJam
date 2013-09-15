package codejam.utils.math;

import java.math.BigInteger;

import com.google.common.math.BigIntegerMath;

public class NumericBigInt implements Numeric<BigInteger>
{

    final BigInteger num;
    public NumericBigInt(BigInteger l) {
        this.num = l;
    }
    public NumericBigInt(long l) {
        this.num = BigInteger.valueOf(l);
    }

    @Override
    public Numeric<BigInteger> negate()
    {
        return new NumericBigInt(num.negate());
    }

    @Override
    public BigInteger getVal()
    {
        return num;
    }

    @Override
    public Numeric<BigInteger> fromInt(int i)
    {
        return new NumericBigInt((long)i);
    }

    @Override
    public Numeric<BigInteger> getMax()
    {
        //TODO?
        return new NumericBigInt( BigInteger.valueOf(Long.MAX_VALUE).pow(10));
    }

    @Override
    public int compareTo(Numeric<BigInteger> o)
    {
        return num.compareTo(o.getVal());
    }

    @Override
    public int hashCode()
    {
        return num.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NumericBigInt other = (NumericBigInt) obj;
        return num.equals(other.num);
    }

    @Override
    public Numeric<BigInteger> add(Numeric<BigInteger> rhs)
    {
        return new NumericBigInt(num.add(rhs.getVal()));
    }

    @Override
    public Numeric<BigInteger> subtract(Numeric<BigInteger> rhs)
    {
        return new NumericBigInt(num.subtract(rhs.getVal()));
    }

    @Override
    public Numeric<BigInteger> min(Numeric<BigInteger> rhs)
    {
        int cmp = compareTo(rhs);
        
        if (cmp < 0)
            return this;
        
        return rhs;
    }

    @Override
    public Numeric<BigInteger> multiply(Numeric<BigInteger> rhs)
    {
        return new NumericBigInt(num.multiply( rhs.getVal()));
    }

    @Override
    public <T> Numeric<BigInteger> multiplyNumeric(Numeric<T> rhs)
    {
        return new NumericBigInt(num.multiply(BigInteger.valueOf(rhs.longValue())));
    }

    @Override
    public long longValue()
    {
        return num.longValue();
    }

}
