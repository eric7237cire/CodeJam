package com.eric.codejam;

public class Interval {
   
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
           
            oddRight = 1;
            oddLeft = 1;
        } else {
            isEvenSpanning = true;
            
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
            total.evenRight = lhs.oddLeft + rhs.evenRight;
           
            total.totalEven = lhs.totalEven + rhs.totalEven + lhs.oddRight * rhs.oddLeft + lhs.evenRight * rhs.evenLeft;
            total.oddLeft = lhs.oddLeft + rhs.oddLeft;
            total.oddRight = lhs.evenRight + rhs.oddRight; 
                    //lhs.oddRight * rhs.oddRight;
        }
        
        if (!lhs.isEvenSpanning && rhs.isEvenSpanning) {
            total.isEvenSpanning = false;
            total.evenLeft = lhs.evenLeft + rhs.oddLeft;
            total.evenRight = lhs.evenRight + rhs.evenRight;
            total.totalEven = lhs.totalEven + rhs.totalEven + lhs.oddRight * rhs.oddLeft + lhs.evenRight * rhs.evenLeft;
            total.oddLeft = lhs.oddLeft + rhs.evenLeft;
            total.oddRight = lhs.oddRight + rhs.oddRight;
        }
        
        if (!lhs.isEvenSpanning && !rhs.isEvenSpanning) {
            total.isEvenSpanning = true;
            total.evenLeft = lhs.evenLeft + rhs.oddLeft;
            total.evenRight = lhs.oddRight + rhs.evenRight;
            total.totalEven = lhs.totalEven + rhs.totalEven + lhs.oddRight * rhs.oddLeft + lhs.evenRight * rhs.evenLeft;
            total.oddLeft = lhs.oddLeft + rhs.evenLeft;
            total.oddRight = lhs.evenRight + rhs.oddRight;
        }
        
        if (lhs.isEvenSpanning && rhs.isEvenSpanning) {
            total.isEvenSpanning = true;
            total.evenLeft = lhs.evenLeft + rhs.evenLeft;
            total.evenRight = lhs.evenRight + rhs.evenRight;
            
            total.totalEven = lhs.totalEven + rhs.totalEven + lhs.oddRight * rhs.oddLeft + lhs.evenRight * rhs.evenLeft;
            total.oddLeft = lhs.oddLeft + rhs.oddLeft;
            total.oddRight = lhs.oddRight + rhs.oddRight;
        }
        return total;
    }
    
    /**
     * 
     * @param lhs smaller 1-19 
     * @param big bigger  1-49
     * @return
     */
    static Interval subtract(Interval lhs, Interval big) {
        Interval total = new Interval();
        total.left = lhs.right+1;
        total.right = big.right;
        
        if (lhs.isEvenSpanning && !big.isEvenSpanning) {
            total.isEvenSpanning = false;
            total.evenLeft = lhs.evenLeft + big.evenLeft;
            total.evenRight = lhs.oddLeft + big.evenRight;
           
            total.totalEven = lhs.totalEven + big.totalEven + lhs.oddRight * big.oddLeft + lhs.evenRight * big.evenLeft;
            total.oddLeft = lhs.oddLeft + big.oddLeft;
            total.oddRight = lhs.evenRight + big.oddRight; 
                    //lhs.oddRight * rhs.oddRight;
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
            total.oddRight = big.oddRight - lhs.oddRight ;
        }
        
        total.totalEven = big.totalEven - lhs.totalEven -  lhs.oddRight * total.oddLeft - lhs.evenRight * total.evenLeft;
        
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
       // result = prime * result + internalEven;
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
        return "Interval [totalEven="
                + totalEven + ", oddRight=" + oddRight + ", oddLeft=" + oddLeft
                + ", evenRight=" + evenRight + ", evenLeft=" + evenLeft
                + ", isEvenSpanning=" + isEvenSpanning + ", left=" + left
                + ", right=" + right + "]";
    }
}
