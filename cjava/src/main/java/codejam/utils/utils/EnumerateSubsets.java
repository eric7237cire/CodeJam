package codejam.utils.utils;

import java.util.Iterator;
import java.util.List;

import codejam.utils.datastructures.BitSetInt;

public class EnumerateSubsets<T> implements Iterator<List<T>> {

    public EnumerateSubsets() {

    }

    private List<T> output;
    private List<T> source;
    int subSets;
    int current = 0;

    public EnumerateSubsets(List<T> in, List<T> out) {

        this.source = in;
        this.output = out;
        subSets = (1 << in.size()) - 1;

    }

    /**
     * Go to the next lineup, and if available, fill the target array.
     * 
     * @return if a new lineup is available
     */
    public List<T> next() {
        if (current > subSets) {
            return output;
        }

        ++current;

        BitSetInt bs = new BitSetInt(current);
        output.clear();
        for (int pos = 0; pos < source.size(); ++pos) {
            if (bs.isSet(pos)) {
                output.add(source.get(pos));
            }

        }
        return output;
    }

    @Override
    public boolean hasNext() {
        return current < subSets;
    }

    @Override
    public void remove() {

    }
}
