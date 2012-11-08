package com.eric.codejam;

import java.math.BigInteger;
import java.util.Objects;

import com.google.common.math.IntMath;
import com.google.common.math.LongMath;

public class Interval {

    BigInteger totalEven;
    int oddRight;
    int oddLeft;
    int evenRight;
    int evenLeft;
    boolean isEvenSpanning;

    BigInteger left;
    BigInteger right;
    BigInteger size; // right + left - 1

    Interval() {
        left = BigInteger.ZERO;
        right = BigInteger.ZERO;
    }

    Interval(int i) {
        this();
        
        size = BigInteger.ONE;
        if (BruteForce.isPalin(i)) {
            isEvenSpanning = false;
            totalEven = BigInteger.ZERO;
            size = BigInteger.ONE;
            
            oddRight = 1;
            oddLeft = 1;
        } else {
            isEvenSpanning = true;
            totalEven = BigInteger.ONE;
            size = BigInteger.ONE;
            evenLeft = 1;
            evenRight = 1;
        }
    }

    static Interval createEmpty(int space) {
        Interval ret = new Interval();
        ret.size = BigInteger.valueOf(space);
        ret.totalEven = BigInteger.valueOf(space).multiply( 
                BigInteger.valueOf(space).add(BigInteger.ONE)).divide(BigInteger.valueOf(2));
        ret.oddRight = 0;
        ret.oddLeft = 0;
        ret.evenRight = space;
        ret.evenLeft = space;
        ret.isEvenSpanning = true;

        return ret;
    }

    static Interval combin(Interval lhs, Interval rhs) {
        Interval total = new Interval();
        total.left = lhs.left;
        total.right = lhs.right.add(rhs.size);

        if (lhs.isEvenSpanning && !rhs.isEvenSpanning) {
            total.isEvenSpanning = false;
            total.evenLeft = lhs.evenLeft + rhs.evenLeft;
            total.evenRight = lhs.oddLeft + rhs.evenRight;
            total.oddLeft = lhs.oddLeft + rhs.oddLeft;
            total.oddRight = lhs.evenRight + rhs.oddRight;
            // lhs.oddRight * rhs.oddRight;
        }

        if (!lhs.isEvenSpanning && rhs.isEvenSpanning) {
            total.isEvenSpanning = false;
            total.evenLeft = lhs.evenLeft + rhs.oddLeft;
            total.evenRight = lhs.evenRight + rhs.evenRight;
            total.oddLeft = lhs.oddLeft + rhs.evenLeft;
            total.oddRight = lhs.oddRight + rhs.oddRight;
        }

        if (!lhs.isEvenSpanning && !rhs.isEvenSpanning) {
            total.isEvenSpanning = true;
            total.evenLeft = lhs.evenLeft + rhs.oddLeft;
            total.evenRight = lhs.oddRight + rhs.evenRight;
            total.oddLeft = lhs.oddLeft + rhs.evenLeft;
            total.oddRight = lhs.evenRight + rhs.oddRight;
        }

        if (lhs.isEvenSpanning && rhs.isEvenSpanning) {
            total.isEvenSpanning = true;
            total.evenLeft = lhs.evenLeft + rhs.evenLeft;
            total.evenRight = lhs.evenRight + rhs.evenRight;
            total.oddLeft = lhs.oddLeft + rhs.oddLeft;
            total.oddRight = lhs.oddRight + rhs.oddRight;
        }

        total.totalEven = lhs.totalEven
                .add(rhs.totalEven)
                .add(BigInteger.valueOf((LongMath.checkedMultiply(lhs.oddRight,
                        rhs.oddLeft))))
                .add(BigInteger.valueOf(LongMath.checkedMultiply(lhs.evenRight,
                        rhs.evenLeft)));

        return total;
    }

    /**
     * 
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
            total.evenLeft = big.evenLeft - lhs.evenLeft;
            total.evenRight = big.evenRight - lhs.oddLeft;

            total.oddLeft = big.oddLeft - lhs.oddLeft;
            total.oddRight = big.oddRight - lhs.evenRight;

        }

        if (!lhs.isEvenSpanning && big.isEvenSpanning) {
            total.isEvenSpanning = false;
            total.evenLeft = big.oddLeft - lhs.oddLeft;
            total.evenRight = big.evenRight - lhs.oddRight;
            total.oddLeft = big.evenLeft - lhs.evenLeft;
            total.oddRight = big.oddRight - lhs.evenRight;
        }

        if (!lhs.isEvenSpanning && !big.isEvenSpanning) {
            total.isEvenSpanning = true;
            total.evenLeft = big.oddLeft - lhs.oddLeft;
            total.evenRight = big.evenRight - lhs.evenRight;

            total.oddLeft = big.evenLeft - lhs.evenLeft;
            total.oddRight = big.oddRight - lhs.oddRight;
        }

        if (lhs.isEvenSpanning && big.isEvenSpanning) {
            total.isEvenSpanning = true;
            total.evenLeft = big.evenLeft - lhs.evenLeft;
            total.evenRight = big.evenRight - lhs.evenRight;

            total.oddLeft = big.oddLeft - lhs.oddLeft;
            total.oddRight = big.oddRight - lhs.oddRight;
        }

        total.totalEven = big.totalEven.subtract(lhs.totalEven)
                .subtract(BigInteger.valueOf(lhs.oddRight * total.oddLeft))
                .subtract(BigInteger.valueOf(lhs.evenRight * total.evenLeft));

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
