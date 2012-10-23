package com.eric.codejam;

import java.util.Comparator;

public class DoubleComparator implements Comparator<Double> {

    private final static double TOLERANCE = 0.0000001d;
    
    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Double o1, Double o2) {
        if (Math.abs(o1 - o2) < TOLERANCE) {
            return 0;
        }
        
        return Double.compare(o1,o2);
    }
    
    static public int compareStatic(Double o1, Double o2) {
        return new DoubleComparator().compare(o1, o2);
    }
}