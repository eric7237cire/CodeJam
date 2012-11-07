package com.eric.codejam;

public class Interval {
    int internalEven;
    int totalEven;
    int oddRight;
    int oddLeft;
    int evenRight;
    int evenLeft;
    boolean isEvenSpanning;
    
    int left;
    int right;
    
    Interval() {
        
    }
    Interval(int i) {
        left = i;
        right = i;
        if (BruteForce.isPalin(i)) {
            isEvenSpanning = false;
            internalEven = 0;
            oddRight = 1;
            oddLeft = 1;
        } else {
            isEvenSpanning = true;
            internalEven = 0;
            totalEven = 1;
            evenLeft = 1;
            evenRight = 1;
        }
    }
    
    static Interval combin(Interval lhs, Interval rhs) {
        Interval total = new Interval();
        total.left = lhs.left;
        total.right = rhs.right;
        
        if (lhs.isEvenSpanning && !rhs.isEvenSpanning) {
            total.isEvenSpanning = false;
            total.evenLeft = lhs.evenLeft + rhs.evenLeft;
            total.internalEven += lhs.evenRight * rhs.evenLeft;
            total.internalEven += lhs.oddRight * rhs.oddLeft;
            total.internalEven += lhs.internalEven + rhs.internalEven;
            total.totalEven = lhs.totalEven + rhs.totalEven + lhs.oddRight * rhs.oddLeft + lhs.evenRight * rhs.evenLeft;
            total.oddLeft = lhs.oddLeft + rhs.oddLeft;
            total.oddRight = lhs.evenRight + rhs.oddRight; 
                    //lhs.oddRight * rhs.oddRight;
        }
        
        return total;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + evenLeft;
        result = prime * result + evenRight;
        result = prime * result + internalEven;
        result = prime * result + (isEvenSpanning ? 1231 : 1237);
        result = prime * result + left;
        result = prime * result + oddLeft;
        result = prime * result + oddRight;
        result = prime * result + right;
        result = prime * result + totalEven;
        return result;
    }
    /* (non-Javadoc)
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
        if (evenLeft != other.evenLeft)
            return false;
        if (evenRight != other.evenRight)
            return false;
        if (internalEven != other.internalEven)
            return false;
        if (isEvenSpanning != other.isEvenSpanning)
            return false;
        if (left != other.left)
            return false;
        if (oddLeft != other.oddLeft)
            return false;
        if (oddRight != other.oddRight)
            return false;
        if (right != other.right)
            return false;
        if (totalEven != other.totalEven)
            return false;
        return true;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Interval [internalEven=" + internalEven + ", totalEven="
                + totalEven + ", oddRight=" + oddRight + ", oddLeft=" + oddLeft
                + ", evenRight=" + evenRight + ", evenLeft=" + evenLeft
                + ", isEvenSpanning=" + isEvenSpanning + ", left=" + left
                + ", right=" + right + "]";
    }
}
