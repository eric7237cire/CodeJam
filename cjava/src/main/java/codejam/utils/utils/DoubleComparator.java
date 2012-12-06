package codejam.utils.utils;

import java.util.Comparator;

public class DoubleComparator implements Comparator<Double> {

    public static final double TOLERANCE =  0.000002d;
    
    private final double tolerance ;
    public DoubleComparator() {
        tolerance = 0.000002d;
    }
    
    public DoubleComparator(double tolerance) {
        this.tolerance = tolerance;
    }
    
    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Double o1, Double o2) {
        if (Math.abs(o1 - o2) < tolerance) {
            return 0;
        }
        
        return Double.compare(o1,o2);
    }
    
    static public int compareStatic(Double o1, Double o2) {
        return new DoubleComparator().compare(o1, o2);
    }
}