package codejam.y2012.round_3.quality_food;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.geometry.PointLong;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.y2012.round_3.quality_food.InputData.Food;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.math.BigIntegerMath;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    
    public Main()
    {
        super("C", 0, 0, 1);
    }
    
    
   
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData in = new InputData(testCase);
       
        in.M = scanner.nextLong();
        in.F = scanner.nextLong();
        in.N = scanner.nextInt();
        
        in.foodTypes = new Food[in.N];
        
        for(int i = 0; i < in.N; ++i) {
            in.foodTypes[i] = new Food(scanner.nextLong(),scanner.nextLong()+1);
        }
        
        return in;
    }

  
    @Override
    public String handleCase(InputData in) {
       
        /**
         * First, find cheapest food for a particular time frame
         */
        
        Arrays.sort(in.foodTypes, new Comparator<Food>() {

            @Override
            public int compare(Food o1, Food o2)
            {
               return ComparisonChain.start().compare(o1.price,o2.price).result();
            }
            
        });
        
        int j = 0;
        List<Food> cheapestFoodForTime = Lists.newArrayList();
        for(int i = 0; i < in.N; ++i) 
        {
            if (j > 0 && (in.foodTypes[j - 1].price == in.foodTypes[i].price || 
                    in.foodTypes[j - 1].time >= in.foodTypes[i].time)) {
                continue;
            }
            
            cheapestFoodForTime.add(in.foodTypes[i]);
            ++j;
        }
        
        //Now we want to construct the "critical points" where the slope of the cost
        //for days function changes

        List<Pair<Long,BigInteger>> criticalPoints = Lists.newArrayList();
        
        criticalPoints.add(new ImmutablePair<>(0L, BigInteger.ZERO));
        
        for(int i = 0; i < cheapestFoodForTime.size(); ++i) {
            Pair<Long,BigInteger> lastCritPoint = criticalPoints.get(i);
            
            Food food = cheapestFoodForTime.get(i);
            
            /**
             * The slope will be the cost of the food,
             * X is number of days
             * Y is the total cost
             * 
             * So the slope is food.price as each day we
             * want to eat must increase the price by this amount
             */
            
            long slope = food.price;
            
            long x = food.time;
            
            long deltaX = x - lastCritPoint.getLeft();
            
            BigInteger y = lastCritPoint.getRight().add( 
                    BigInteger.valueOf(deltaX).multiply(BigInteger.valueOf(slope)) );
            
            criticalPoints.add(new ImmutablePair<>(x, y));
        }
        
        
        
        return String.format("Case #%d: ", in.testCase);
    }
}