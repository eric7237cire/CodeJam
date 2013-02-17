package codejam.utils;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import codejam.utils.datastructures.graph.MincostMaxflow;
import codejam.utils.math.NumericLong;

public class FlowTest
{


    @Test
    public void test()
    {
        /*    0  1  2
         * __________
         * 0| 1  3  2
         * 1| 2  6  4
         * 2| 3  7  6
         * 
         * 
         * 
         * 
         */
        
        //0 1 2  ;  3 4 5
        //s = 6
        //t = 7
        
        MincostMaxflow<Long,Long> mmf = new MincostMaxflow<>(new NumericLong(0l),new NumericLong(0l));

        mmf.addArc(0, 3, new NumericLong(1l), new NumericLong(1l));
        mmf.addArc(0, 4, new NumericLong(1l), new NumericLong(3l));
        mmf.addArc(0, 5, new NumericLong(1l), new NumericLong(2l));
        
        mmf.addArc(1, 3, new NumericLong(1l), new NumericLong(2l));
        mmf.addArc(1, 4, new NumericLong(1l), new NumericLong(6l));
        mmf.addArc(1, 5, new NumericLong(1l), new NumericLong(4l));
        
        mmf.addArc(2, 3, new NumericLong(1l), new NumericLong(3l));
        mmf.addArc(2, 4, new NumericLong(1l), new NumericLong(7l));
        mmf.addArc(2, 5, new NumericLong(1l), new NumericLong(6l));
        
        mmf.addArc(6, 0, new NumericLong(1l), new NumericLong(0l));
        mmf.addArc(6, 1, new NumericLong(1l), new NumericLong(0l));
        mmf.addArc(6, 2, new NumericLong(1l), new NumericLong(0l));
        
        mmf.addArc(3, 7, new NumericLong(1l), new NumericLong(0l));
        mmf.addArc(4, 7, new NumericLong(1l), new NumericLong(0l));
        mmf.addArc(5, 7, new NumericLong(1l), new NumericLong(0l));
        
        Pair<Long,Long> res = mmf.getFlow(6, 7);
        
        assertEquals(3, (long)res.getLeft());
        assertEquals(10, (long)res.getRight());
    }
}
