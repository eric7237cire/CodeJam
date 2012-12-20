package codejam.utils.utils;

import java.util.Iterator;

public class PermutationWithRepetition<T> implements Iterator<T[]> {
    
    private T[] possible;
    private T[] out;
    private int[] combin;
    private boolean hasNext = true;
    
        
    private PermutationWithRepetition(T[] in, T[] out) {
        
        this.possible = in;
        this.out = out;
        combin = new int[out.length];

    }
    
    void doOutput() {
        for(int i = 0; i < combin.length; ++i) {
            out[i] = possible[combin[i]];
        }
    }

    /**
     * Create a new permutations object.
     *
     * @param <T> the type
     * @param in the source array
     * @param out the target array
     * @return the generated permutations object
     */
    public static <T> PermutationWithRepetition<T> create(T[] in, T[] out) {
        return new PermutationWithRepetition<T>(in, out);
    }



    /**
     * Go to the next lineup, and if available, fill the target array.
     *
     * @return if a new lineup is available
     */
    public T[] next() {
        if (!hasNext) {
            return out;
        }
        
        doOutput();
        
        boolean fullLoop = true;
        //Try all permutations.  This loop finds the first position that can be incremented
        for (int pos = 0; pos < combin.length; ++pos) {
            combin[pos]++;
            
            //Incremented, we are done
            if (combin[pos] < possible.length) {
                fullLoop = false;
                break;
            }
            
            //This position is now zero, look to increment combin
            combin[pos] = 0;
        }

        if (fullLoop) {
            hasNext = false;
        }
        return out;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public void remove() {
       
        
    }
}
