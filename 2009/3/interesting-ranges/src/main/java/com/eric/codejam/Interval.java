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
    
    private Interval() {
        
    }
    Interval(int i) {
        left = i;
        right = i;
        if (BruteForce.isPalin(i)) {
            isEvenSpanning = false;
            internalEven = 0;
        } else {
            isEvenSpanning = true;
            totalEven = 1;
        }
    }
    
    static Interval combin(Interval lhs, Interval rhs) {
        Interval total = new Interval();
        total.left = lhs.left;
        total.right = rhs.right;
        
    }
}
