package codejam.utils.math;

import com.google.common.math.LongMath;

public class NumericLong implements Numeric<Long>
{

    final long num;
    public NumericLong(Long l) {
        this.num = l;
    }
    public NumericLong(int l) {
        this.num = l;
    }

    @Override
    public Numeric<Long> negate()
    {
        return new NumericLong(num * -1);
    }

    @Override
    public Long getVal()
    {
        return num;
    }

    @Override
    public Numeric<Long> fromInt(int i)
    {
        return new NumericLong((long)i);
    }

    @Override
    public Numeric<Long> getMax()
    {
        return new NumericLong(Long.MAX_VALUE);
    }

    @Override
    public int compareTo(Numeric<Long> o)
    {
        return Long.compare(num, o.getVal());
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (num ^ (num >>> 32));
        return result;
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
        NumericLong other = (NumericLong) obj;
        if (num != other.num)
            return false;
        return true;
    }

    @Override
    public Numeric<Long> add(Numeric<Long> rhs)
    {
        return new NumericLong(LongMath.checkedAdd(num, rhs.getVal()));
    }

    @Override
    public Numeric<Long> subtract(Numeric<Long> rhs)
    {
        return new NumericLong(LongMath.checkedSubtract(num, rhs.getVal()));
    }

    @Override
    public Numeric<Long> min(Numeric<Long> rhs)
    {
        int cmp = compareTo(rhs);
        
        if (cmp < 0)
            return this;
        
        return rhs;
    }

    @Override
    public Numeric<Long> multiply(Numeric<Long> rhs)
    {
        return new NumericLong(LongMath.checkedMultiply(num, rhs.getVal()));
    }

    @Override
    public <T> Numeric<Long> multiplyNumeric(Numeric<T> rhs)
    {
        return new NumericLong(LongMath.checkedMultiply(num, rhs.longValue()));
    }

    @Override
    public long longValue()
    {
        return num;
    }

    
}
