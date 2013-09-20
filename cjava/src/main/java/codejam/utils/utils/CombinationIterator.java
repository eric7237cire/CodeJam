package codejam.utils.utils;

import java.util.Iterator;

final public class CombinationIterator //implements Iterator<Long> {
{
    private int n;
    //private int k;
    private long next;

    public CombinationIterator(int n, int k) {
        this.n = n;
      //  this.k = k;
        next = (1L << k) - 1;
    }

    //@Override
    public boolean hasNext() {
        return (next & (1L << n)) == 0;
    }

    //@Override
    public long next() {
        // Gosper's hack, described by Knuth, referenced in
        // http://en.wikipedia.org/wiki/Combinatorial_number_system#Applications
        long result = next;
        long x = next;
        long u = x & -x;
        long v = u + x;
        x = v + (((v ^ x) / u) >> 2);
        next = x;
        return result;
    }

    //@Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}