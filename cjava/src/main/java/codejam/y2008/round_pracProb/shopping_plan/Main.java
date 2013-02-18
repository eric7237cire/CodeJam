package codejam.y2008.round_pracProb.shopping_plan;

import java.util.Arrays;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.BitSetInt;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
       //super();
        super("D", 0,1);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);
        
        in.num_items = scanner.nextInt();
        in.num_stores = scanner.nextInt();
        in.price_of_gas = scanner.nextInt();
        
        in.perishableMask = new BitSetInt();
        in.itemNameToIndex = Maps.newHashMap();
        
        for(int i = 0; i < in.num_items; ++i) {
            String itemName = scanner.next();
            
            
            if (itemName.endsWith("!")) {
                itemName = itemName.substring(0, itemName.length()-1);
                in.perishableMask.set(i);
            }
            
            in.itemNameToIndex.put(itemName, i);
            
            
        }
        
        in.storeLoc = new PointInt[in.num_stores+1];
        in.storeItemPrice = new int[in.num_stores][in.num_items];
        in.storeAvailableItemMask = new BitSetInt[in.num_stores];
        
        in.storeLoc[in.num_stores] = new PointInt(0,0);
        
        /**
         * Calculate which items are available at which store
         * and store their prices
         */
        for(int s = 0; s < in.num_stores; ++s) {
            in.storeLoc[s] = new PointInt(scanner.nextInt(),scanner.nextInt());
            
            String restOfLine = scanner.nextLine().trim();
            Iterable<String> storeItemsStrings = Splitter.on(' ').split(restOfLine);
            
            BitSetInt availableItemsMask = new BitSetInt();
            
            for(String storeItemsStr : storeItemsStrings) {
                String[] str = storeItemsStr.split(":");
                
                int itemIndex = in.itemNameToIndex.get(str[0]);
                int itemPrice = Integer.parseInt(str[1]);
                
                in.storeItemPrice[s][itemIndex] = itemPrice;
                
                availableItemsMask.set(itemIndex);
            }
            
            in.storeAvailableItemMask[s] = availableItemsMask;
        }
        
        Preconditions.checkState(in.num_items <= 15);
        in.storeItemsPrice  = new int[in.num_stores][1 << in.num_items];
        
        /**
         * Go through each possible combination of items and 
         * calculate their price.
         */
        for(int s = 0; s < in.num_stores; ++s) {
            BitSetInt items = new BitSetInt(in.storeAvailableItemMask[s]);
            
            int iter = 0;
            while(items.getBits() > 0) {
                ++iter;
                Preconditions.checkState(iter < 100000);
                
                int totalPrice = 0;
                for(int item = 0; item < in.num_items; ++item) {
                    if (items.isSet(item)) {
                        totalPrice += in.storeItemPrice[s][item];
                    }
                }
                
                in.storeItemsPrice[s][items.getBits()] = totalPrice;
                
                //Compute next combination of items
                items.setAllBits( (items.getBits() - 1) & in.storeAvailableItemMask[s].getBits());
                
            }
        }
        
        
        return in;
    }
    
    double dist(int s1, int s2, InputData in) {
        return in.storeLoc[s1].distance(in.storeLoc[s2]) * in.price_of_gas;
    }
    
    
    double go(int currentLocation,int itemsBoughtMask,int boughtInt, double[][][] memo, InputData in)
    {
        int home = in.num_stores;
        boolean bought = boughtInt != 0;
        
        //Finished state, everything bought and at home
        if(currentLocation==home && itemsBoughtMask==(1<<in.num_items)-1) return 0.0;
        
        if(memo[currentLocation][itemsBoughtMask][boughtInt] >= 0)
            return memo[currentLocation][itemsBoughtMask][boughtInt];
        
        double res = Double.MAX_VALUE;
       
        
        if (!bought && currentLocation != home) {
            /*
             * nm is what still needs to be bought
             */
            int nm = (~itemsBoughtMask)
                    & in.storeAvailableItemMask[currentLocation].getBits();

            // Loop through every combination of nm
            for (int buy = nm; buy != 0; buy = (buy - 1) & nm) {

                // Each combination has already been precalculated
                double cost = in.storeItemsPrice[currentLocation][buy];
                if ((in.perishableMask.getBits() & buy) != 0) {
                    //Buy items and go home.  
                    res = Math.min(res, 
                            cost + dist(currentLocation, home, in)
                            + go(home, itemsBoughtMask | buy, 0, memo, in));
                } else {
                    //Buy items and stay put
                    res = Math.min(res, cost + go(currentLocation,
                                                    itemsBoughtMask | buy, 1,
                                                    memo, in));
                }
            }
        } else {
            // Already have bought something, so go elsewhere
            for (int to = 0; to <= in.num_stores; to++) {
                if (to == currentLocation)
                    continue;

                res = Math.min(
                        res,
                        dist(currentLocation, to, in)
                                + go(to, itemsBoughtMask, 0, memo, in));
            }
        }
        
        
        memo[currentLocation][itemsBoughtMask][boughtInt] = res;
        return res;
    }
    
    @Override
    public String handleCase(InputData in)
    {
        /**
         * mincost = memo[currentLocation][items bought bit mask][just purchased at currentLocation] 
         */
        double [][][] memo = new double[in.num_stores+1][1 << in.num_items][2];
        for(double[][] d2 : memo) {
            for(double[] d1 : d2){
                Arrays.fill(d1, -1);
            }
        }
        
        double ans = go(in.num_stores,0,0,memo,in);
        
        return  String.format("Case #%d: %s", in.testCase, DoubleFormat.df6.format(ans));
        
    }
    

}