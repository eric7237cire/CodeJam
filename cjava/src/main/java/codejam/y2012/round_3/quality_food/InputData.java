package codejam.y2012.round_3.quality_food;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    //Amount of money
    long M;
    
    //Delivery fee
    long F;
    
    int N;
    
    static class Food {
        final long price, time;

        public Food(long price, long time) {
            this.price = price;
            this.time = time;
        }

        @Override
        public String toString()
        {
            return "Food [price=" + price + ", time/delivery size=" + time + "]";
        }
        
        
    }
    /**
     * Price, Time to stale
     */
    Food[] foodTypes;
    
    public InputData(int testCase) {
        super(testCase);
    }

}