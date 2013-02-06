package codejam.y2008.round_pracProb.shopping_plan;

import java.util.Map;

import codejam.utils.datastructures.BitSetInt;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    
    int num_items;
    int num_stores;
    int price_of_gas;
    
    //Item name to item index
    Map<String, Integer> itemNameToIndex;
    
    PointInt[] storeLoc;
    //storeItemPrice[store Index][item index] = price 
    int [][] storeItemPrice;
    
    //storeItemPrice[store Index][items index binary 100011..] = price
    int [][] storeItemsPrice;
    
    /**
     * Indexed by store, value is a bitmask of
     * which items can be bought
     */
    BitSetInt [] storeAvailableItemMask;
    
    BitSetInt perishableMask;
    
    public InputData(int testCase) {
        super(testCase);
    }

}