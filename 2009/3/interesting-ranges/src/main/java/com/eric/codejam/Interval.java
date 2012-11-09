package com.eric.codejam;

import java.math.BigInteger;
import java.util.Objects;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;

public class Interval {

    BigInteger palinsCovered;
    
    BigInteger totalEven;
    BigInteger oddRight;
    BigInteger oddLeft;
    BigInteger evenRight;
    BigInteger evenLeft;
    boolean isEvenSpanning;

    BigInteger left;
    BigInteger right;
    BigInteger size; // right + left - 1

    Interval() {
        left = BigInteger.ZERO;
        right = BigInteger.ZERO;
        size = BigInteger.ZERO;
        
        oddRight = BigInteger.ZERO;
        oddLeft = BigInteger.ZERO;
        evenRight = BigInteger.ZERO;
        evenLeft = BigInteger.ZERO;
        
        palinsCovered = BigInteger.ZERO;
        totalEven = BigInteger.ZERO;
    }

    Interval(int i) {
        this(BigInteger.valueOf(i));        
    }
    
    Interval(BigInteger i) {
        this();
        
        size = BigInteger.ONE;
        
        if (BruteForce.isPalin(i)) {
            isEvenSpanning = false;
            totalEven = BigInteger.ZERO;
            size = BigInteger.ONE;
            palinsCovered = BigInteger.ONE;
            
            oddRight = BigInteger.ONE;
            oddLeft = BigInteger.ONE;
        } else {
            isEvenSpanning = true;
            totalEven = BigInteger.ONE;
            palinsCovered = BigInteger.ZERO;
            size = BigInteger.ONE;
            evenLeft = BigInteger.ONE;
            evenRight = BigInteger.ONE;
        }
    }

    static Interval createEmpty(int space) {
       return createEmpty(BigInteger.valueOf(space));
    }
    
    static Interval createEmpty(BigInteger space) {
        Interval ret = new Interval();
        Preconditions.checkArgument(space.compareTo(BigInteger.ZERO) >= 0);
        ret.size = space;
        ret.totalEven = space.multiply( 
                space.add(BigInteger.ONE)).divide(BigInteger.valueOf(2));
        ret.oddRight = BigInteger.ZERO;
        ret.oddLeft = BigInteger.ZERO;
        ret.evenRight = space;
        ret.evenLeft = space;
        ret.isEvenSpanning = true;
        ret.palinsCovered = BigInteger.ZERO;

        return ret;
    }

    static Interval combin(Interval lhs, Interval rhs) {
        if (rhs.size.compareTo(BigInteger.ZERO) == 0) {
            return lhs;
        }
        if (lhs.size.compareTo(BigInteger.ZERO) == 0) {
            return rhs;
        }
        Interval total = new Interval();
        total.left = lhs.left;
        total.right = lhs.right.add(rhs.size);
        total.size = lhs.size.add(rhs.size);
        total.palinsCovered = lhs.palinsCovered.add(rhs.palinsCovered);

        if (lhs.isEvenSpanning && !rhs.isEvenSpanning) {
            total.isEvenSpanning = false;
            total.evenLeft = lhs.evenLeft.add(rhs.evenLeft);
            total.evenRight = lhs.oddLeft.add(rhs.evenRight);
            total.oddLeft = lhs.oddLeft.add(rhs.oddLeft);
            total.oddRight = lhs.evenRight.add(rhs.oddRight);
            // lhs.oddRight * rhs.oddRight;
        }

        if (!lhs.isEvenSpanning && rhs.isEvenSpanning) {
            total.isEvenSpanning = false;
            total.evenLeft = lhs.evenLeft.add(rhs.oddLeft);
            total.evenRight = lhs.evenRight.add(rhs.evenRight);
            total.oddLeft = lhs.oddLeft.add(rhs.evenLeft);
            total.oddRight = lhs.oddRight.add(rhs.oddRight);
        }

        if (!lhs.isEvenSpanning && !rhs.isEvenSpanning) {
            total.isEvenSpanning = true;
            total.evenLeft = lhs.evenLeft.add(rhs.oddLeft);
            total.evenRight = lhs.oddRight.add(rhs.evenRight);
            total.oddLeft = lhs.oddLeft.add(rhs.evenLeft);
            total.oddRight = lhs.evenRight.add(rhs.oddRight);
        }

        if (lhs.isEvenSpanning && rhs.isEvenSpanning) {
            total.isEvenSpanning = true;
            total.evenLeft = lhs.evenLeft.add(rhs.evenLeft);
            total.evenRight = lhs.evenRight.add(rhs.evenRight);
            total.oddLeft = lhs.oddLeft.add(rhs.oddLeft);
            total.oddRight = lhs.oddRight.add(rhs.oddRight);
        }

        total.totalEven = lhs.totalEven
                .add(rhs.totalEven)
                .add(lhs.oddRight.multiply(
                        rhs.oddLeft))
                .add(lhs.evenRight.multiply(
                        rhs.evenLeft));

        return total;
    }

    /**
     *  lhs combin rhs = big
     *  big subt   lhs = rhs
     * @param lhs
     *            smaller 1-19
     * @param big
     *            bigger 1-49
     * @return
     */
    static Interval subtract(Interval lhs, Interval big) {
        Interval total = new Interval();
        total.left = lhs.right.add(BigInteger.ONE);
        total.right = big.right;

        if (lhs.isEvenSpanning && !big.isEvenSpanning) {
            total.isEvenSpanning = false;
            total.evenLeft = big.evenLeft.subtract(lhs.evenLeft);
            total.evenRight = big.evenRight.subtract(lhs.oddLeft);

            total.oddLeft = big.oddLeft.subtract(lhs.oddLeft);
            total.oddRight = big.oddRight.subtract(lhs.evenRight);

        }

        if (!lhs.isEvenSpanning && big.isEvenSpanning) {
            total.isEvenSpanning = false;
            total.evenLeft = big.oddLeft.subtract(lhs.oddLeft);
            total.evenRight = big.evenRight.subtract(lhs.oddRight);
            total.oddLeft = big.evenLeft.subtract(lhs.evenLeft);
            total.oddRight = big.oddRight.subtract(lhs.evenRight);
        }

        if (!lhs.isEvenSpanning && !big.isEvenSpanning) {
            total.isEvenSpanning = true;
            total.evenLeft = big.oddLeft.subtract(lhs.oddLeft);
            total.evenRight = big.evenRight.subtract(lhs.evenRight);

            total.oddLeft = big.evenLeft.subtract(lhs.evenLeft);
            total.oddRight = big.oddRight.subtract(lhs.oddRight);
        }

        if (lhs.isEvenSpanning && big.isEvenSpanning) {
            total.isEvenSpanning = true;
            total.evenLeft = big.evenLeft.subtract(lhs.evenLeft);
            total.evenRight = big.evenRight.subtract(lhs.evenRight);

            total.oddLeft = big.oddLeft.subtract(lhs.oddLeft);
            total.oddRight = big.oddRight.subtract(lhs.oddRight);
        }

        total.totalEven = big.totalEven.subtract(lhs.totalEven)
                .subtract(lhs.oddRight.multiply( total.oddLeft))
                .subtract(lhs.evenRight.multiply(total.evenLeft));

        return total;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(evenLeft, evenRight, isEvenSpanning, oddLeft,
                oddRight, totalEven);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Interval other = (Interval) obj;
        return Objects.equals(evenLeft, other.evenLeft)
            && Objects.equals(evenRight, other.evenRight)
            &&  Objects.equals(isEvenSpanning, other.isEvenSpanning)
            &&  Objects.equals(oddLeft, other.oddLeft)
            &&  Objects.equals(oddRight, other.oddRight)
            &&  Objects.equals(totalEven, other.totalEven);
            
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Interval [totalEven=" + totalEven + ", oddRight=" + oddRight
                + ", oddLeft=" + oddLeft + ", evenRight=" + evenRight
                + ", evenLeft=" + evenLeft + ", isEvenSpanning="
                + isEvenSpanning + ", left=" + left + ", right=" + right + "]";
    }
}
