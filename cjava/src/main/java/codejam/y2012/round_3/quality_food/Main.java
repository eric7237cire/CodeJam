package codejam.y2012.round_3.quality_food;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.y2012.round_3.quality_food.InputData.Food;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    
    public Main()
    {
        super("C", 1, 1, 0);
        //(( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
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
    
    /*
    private Long getSingleDayCost(List<Food> cheapestFoodForTime, long time)
    {
        for(int i = 0; i < cheapestFoodForTime.size(); ++i)
        {
            if (cheapestFoodForTime.get(i).time >= time) {
                return cheapestFoodForTime.get(i).price;
            }
        }
        
        return null;
    }*/
    
    /**
     * See comments for critical points.
     * 
     * Basically, we do linear interpolation
     * 
     * time aka delivery size is the x coordinate ; cost is the y
     */
    BigInteger getSingleDeliveryCost(List<Pair<Long,BigInteger>> criticalPoints, long time)
    {
        for(int i = 1; i < criticalPoints.size(); ++i)
        {
            Pair<Long,BigInteger> lastCritPoint = criticalPoints.get(i-1);
            Pair<Long,BigInteger> critPoint = criticalPoints.get(i);
            
            if (time == critPoint.getLeft()) {
                return critPoint.getRight();
            }
            
            if (time >= lastCritPoint.getLeft() && time < critPoint.getLeft())
            {
                BigInteger slope = critPoint.getRight().subtract(lastCritPoint.getRight())
                        .divide(BigInteger.valueOf(critPoint.getLeft()-lastCritPoint.getLeft()));
                
                //y0 + slope * deltaX
                return lastCritPoint.getRight().add( slope.multiply( BigInteger.valueOf(time - lastCritPoint.getLeft())) ); 
            }
        }
        
        //Impossible / really expensive
        return BigInteger.valueOf(Long.MAX_VALUE);
    }
    
    /*
     * Trying the other way, todo
     */
    /*
    private long getLargestDeliverySize(long moneyForSingleDelivery, List<Pair<Long,BigInteger>> criticalPoints, long mostResistantFoodTimeToStale) {
        
        long lo = 0;
        long hi = mostResistantFoodTimeToStale+1;
    //  invariant lo costs <= money avail hi costs more
        
        Preconditions.checkState(getSingleDeliveryCost(criticalPoints, lo).longValue() <= moneyForSingleDelivery);
        Preconditions.checkState(getSingleDeliveryCost(criticalPoints, hi).longValue() > moneyForSingleDelivery);
        
        while (true) {
            long mid = lo + (hi - lo) / 2;

            long cost = getSingleDeliveryCost(criticalPoints, mid).longValue();

            if (cost <= moneyForSingleDelivery) {
                lo = mid;
            } else {
                hi = mid;
            }

            Preconditions.checkState(lo <= hi);

            if (hi - lo <= 1)
                break;
        }
        
        return lo;
    }*/
    
    BigInteger costOfDaysWorthFood( long D, List<Pair<Long,BigInteger>> criticalPoints, InputData in )
    {
        BigInteger minCost = BigInteger.valueOf(Long.MAX_VALUE);
        
        for(int i = 1; i < criticalPoints.size(); ++i)
        {
            //Each critical point gives us a guess of what the delivery size should be
            
            //left is X / time
            long deliverySizeGuess = criticalPoints.get(i).getLeft() ;
            
            if (deliverySizeGuess < 1)
                continue;
            
            deliverySizeGuess = Math.min(deliverySizeGuess, D);                
            
            BigInteger cost = costOfDaysWorthFood(D, deliverySizeGuess, criticalPoints, in);
           
            minCost = minCost.min(cost);
            
            
        }
        
        return minCost;
    }
    
    BigInteger divideRoundUp(BigInteger n, BigInteger div) {
        BigInteger[] res = n.divideAndRemainder(div);
        
        if (res[1].compareTo(BigInteger.ZERO) > 0) {
            return res[0].add(BigInteger.ONE);
        }
        
        return res[0];
    }
    
    BigInteger costOfDaysWorthFood( long D, long deliverySizeGuess, List<Pair<Long,BigInteger>> criticalPoints, InputData in )
    {
        
        if (D <= 0)
            return BigInteger.valueOf(0);
        
        deliverySizeGuess = Math.min(deliverySizeGuess, D);
        
        if (deliverySizeGuess <= 0)
            return BigInteger.valueOf(Long.MAX_VALUE);
        
        //Given this guess, find out how many deliveries that represents
        BigInteger nDeliveries = divideRoundUp(BigInteger.valueOf(D), BigInteger.valueOf(deliverySizeGuess ));
        
        //Now that we know that, find 2 values L, S such that L == S + 1 and
        // k * L + k_2 * S = D (# of days)
        // k + k_2 = nDeliveries
        BigInteger largeDeliverySize = divideRoundUp(BigInteger.valueOf(D), nDeliveries);
        
        //  (k + k_2) * L = D + extra
        // extra == k_2 since S == L - 1 ; each extra we want to remove is k--, k_2++
        BigInteger overflowD = nDeliveries.multiply( largeDeliverySize );
        
        BigInteger smallDeliverySize = largeDeliverySize.subtract(BigInteger.ONE);
        
        BigInteger nSmallDeliveries = overflowD.subtract( BigInteger.valueOf(D) );
        BigInteger nLargeDeliveries = nDeliveries.subtract( nSmallDeliveries );
        
        Preconditions.checkState(largeDeliverySize.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) <= 0);
        Preconditions.checkState(smallDeliverySize.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) <= 0);
        
        BigInteger largeCost = getSingleDeliveryCost(criticalPoints, largeDeliverySize.longValue());
        BigInteger smallCost = getSingleDeliveryCost(criticalPoints, smallDeliverySize.longValue());
        
        BigInteger cost = largeCost.multiply(nLargeDeliveries)
                .add( smallCost.multiply(nSmallDeliveries) )
                .add( BigInteger.valueOf(in.F).multiply( nDeliveries));
        
        
        log.debug("For D days {} Delivery size {} , {}  number deliveries {} = {} + {}   cost {}", D, 
                largeDeliverySize, smallDeliverySize, nDeliveries, nLargeDeliveries, nSmallDeliveries, cost);
        
        return cost;
    }
    

    /*
    long numberOfDays(long deliverySize, InputData in)
    {
        long moneyPerDelivery = in.M / deliverySize;
        return 0;
        
    }*/
    
    long getMaxDays( List<Pair<Long,BigInteger>> criticalPoints, InputData in) {
        
        long lo = 0;
        long hi = in.M+1;
    //  invariant lo can afford lo days, cannot afford hi days
        
        BigInteger bigIntM = BigInteger.valueOf(in.M);
        
        log.info("{}", costOfDaysWorthFood(lo, criticalPoints, in));
        log.info("{}", costOfDaysWorthFood(hi, criticalPoints, in));
        
        //Check invariants
        Preconditions.checkState(costOfDaysWorthFood(lo, criticalPoints, in).compareTo( BigInteger.valueOf(in.M)) <= 0);
        Preconditions.checkState(costOfDaysWorthFood(hi, criticalPoints, in).compareTo((BigInteger.valueOf(in.M))) > 0);
        
        while (true) {
            long mid = lo + (hi - lo) / 2;
            BigInteger cost = costOfDaysWorthFood(mid, criticalPoints, in);
            log.debug("Cost of surviving {} days = {}", mid, cost);
            if (cost.compareTo(bigIntM) <= 0) {
                lo = mid;
            } else {
                hi = mid;
            }

            Preconditions.checkState(lo <= hi);

            if (hi - lo <= 1)
                break;
        }
        
        return lo;
    }
  
    /**
     * A "Criticial Point" is when the slope changes
     * 
     * Given a graph where Y is cost and X is the delivery size (which is equal to the time it takes to go stale)
     * 
     * The slope is the unit cost per meal
     * 
     * In plain english, as the delivery size grows, we are obliged to buy more expensive prices of food
     * that takes longer to go bad.
     * 
     * @return
     */
    List<Pair<Long,BigInteger>> getCriticalPoints(List<Food> cheapestFoodForTime)
    {
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
            log.debug("Add crit point {}", criticalPoints.get(criticalPoints.size()-1));
        }
        
        return criticalPoints;
    }
    
    List<Food> getCheapestFoodForTime(InputData in) 
    {
        List<Food> cheapestFoodForTime = Lists.newArrayList();
        
        /**
         * the food choices are sorted cheapest first, 
         * then greatest time to stale.
         * 
         * That means every time we hit a point later
         * than the last added, it is the cheapest food
         * for that time to stale value.
         */
        cheapestFoodForTime.add(in.foodTypes[0]);
        
        for(int i = 1; i < in.N; ++i) 
        {
            Food lastMinPoint = cheapestFoodForTime.get(cheapestFoodForTime.size() -1);
            
            Food curPoint = in.foodTypes[i];
            
            if (curPoint.price == lastMinPoint.price)
                continue;
            
            if (curPoint.time <= lastMinPoint.time)
                continue;
            
            cheapestFoodForTime.add(in.foodTypes[i]);
            
        }
        
        return cheapestFoodForTime;
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
               return ComparisonChain.start().compare(o1.price,o2.price).compare(o2.time,o1.time).result();
            }
            
        });
        
        List<Food> cheapestFoodForTime = getCheapestFoodForTime(in);
        
        for (Food food : cheapestFoodForTime) {
            log.debug("Cheapest food for time {}", food);
        }
        
        //Now we want to construct the "critical points" where the slope of the cost
        //for days function changes

        List<Pair<Long,BigInteger>> criticalPoints = getCriticalPoints(cheapestFoodForTime);
        
        log.debug("Money avail {}, {}", in.M,costOfDaysWorthFood(5071, criticalPoints, in));

        
       long maxDays = getMaxDays(criticalPoints, in);
        
        return String.format("Case #%d: %d ", in.testCase, maxDays);
    }
}