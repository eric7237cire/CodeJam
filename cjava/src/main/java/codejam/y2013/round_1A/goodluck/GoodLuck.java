package codejam.y2013.round_1A.goodluck;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.datastructures.graph.MincostMaxflow;
import codejam.utils.datastructures.graph.MincostMaxflow2;
import codejam.utils.datastructures.graph.MincostMaxflow2.Edge;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.math.NumericBigInt;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.CombinationIterator;
import codejam.utils.utils.CombinationsWithRepetition;
import codejam.utils.utils.IntegerPair;
import codejam.utils.utils.LargeNumberUtils;
import codejam.utils.utils.PermutationWithRepetition;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;


public class GoodLuck extends InputFilesHandler implements
TestCaseHandler<InputData>, TestCaseInputScanner<InputData> 
{
    public GoodLuck()
    {
        super("C", 1, 1);
       // setLogInfo();
        setLogDebug();
        
        
    }
    
    public static void main(String[] args) {
    	
    }
    //CombinationsWithRepetition
    public static void main2(String[] args) {
    	
    	final int N = 7;
    	final int M = 10;
    	
    	Integer[] nums = new Integer[M-1];
    	for(int i = 2; i <= M; ++i ) {
    		nums[i-2] = i;
    	}
    	Integer[] out = new Integer[N];
    	
    	PermutationWithRepetition<Integer> pr = PermutationWithRepetition.create(nums, out);
    	
    	//Integer[] t1 = new Integer[] {3,3,3};
    	Integer[] t1 = new Integer[] {2,2,2,2,2,3,3};
    	Integer[] t2 = new Integer[] {2,2,2,3,4,4, 5};
    	
    	log.debug("t1 est {}", IntMath.factorial(N) / ( IntMath.factorial(5) * IntMath.factorial(2)));
    	log.debug("t1 est {}", IntMath.factorial(N) / ( IntMath.factorial(3) * IntMath.factorial(2)));
    	
    	int count1 = 0;
    	int count2 = 0;
    	int total = 0;
    	
    	while(pr.hasNext()) {
    		Integer[] iter = pr.next();
    		
    		Arrays.sort(iter);
    		//log.debug("{}", (Object)iter);
    		if (Arrays.equals(iter, t1)) {
    			++count1;
    		}
    		
    		if (Arrays.equals(iter, t2)) {
				++count2;
    		}
    		
    		++total;
    	}
    	
    	log.info("Count 1: [{}] count 2: [{}] total {}", count1, count2, total);
    }
    
    public double getProb(int needed, int total)
    {
        int limit = IntMath.pow(2, total);
        
        int[] counts = new int[total+1];
        
        for(int i = 0; i < limit; ++i)
        {
            int bitCount = Integer.bitCount(i);
            counts[bitCount]++;
            
        }
        
        log.debug("{}", counts);
        return 1.0 * counts[needed] / limit;
    }
    
    public String[] getDefaultInputFiles()
    {

        return new String[] {
               "sample.in"
                //"C-small-practice-1.in"
               // "C-small-practice-2.in" 
                };
        // return new String[] { "C-small-practice.in", "C-large-practice-1.in"
        // };
        // return new String[] { "C-small-practice.in"};

    }

    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
        InputData in = new InputData(testCase);
        //R N M K
        //nSets
        //nAvail
        //between 2 and M
        //Choose K sets, each number has .5 probability
        in.nProductSets = scanner.nextInt();
        in.numbersChosen = scanner.nextInt();
        in.maxNumber = scanner.nextInt();
        in.productSetSize = scanner.nextInt();
        
        in.productValues = new long[in.nProductSets][in.productSetSize];
        
        for(int i = 0; i < in.productValues.length; ++i)
        {
            for(int j = 0; j < in.productValues[i].length; ++j)
            {
                in.productValues[i][j] = scanner.nextLong();
            }
            
        }
        
        
        return in;
    }
    

    final int[] factors = new int[] {2, 3,5,7};
    
    private int[] getFactors(long num)
    {
     
        int[] mins = new int[factors.length];

        
        int[] needed = new int[factors.length];
        for (int fIdx = 0; fIdx < factors.length; ++fIdx) 
        {
            int factor = factors[fIdx];
            while (num % factor == 0) 
            {
                ++needed[fIdx];
                num /= factor;
            }

            mins[fIdx] = Math.max(mins[fIdx], needed[fIdx]);
        }
        Preconditions.checkArgument(num == 1);
        return mins;
    }
    public int[] getRequired(long[] products)
    {
        int[] mins = new int[factors.length];
        
        for(int i=0; i < products.length; ++i)
        {
            int[] needed = getFactors(products[i]);
            log.info("For product {}, need 2,3,5,7  {}", products[i], needed);
                
            for(int fIdx = 0; fIdx < needed.length; ++fIdx)
            {
                mins[fIdx] = Math.max(mins[fIdx], needed[fIdx]);
            }
            
        }
        
        
        log.info("For products {} need 2,3,5,7 {}", products, mins);
        
        
        return mins;
        
    }
    
    private double probToGenerateProduct(int[] numbers, long product)
    {
        int[] factorsInProduct = getFactors(product);
        
        int totalCombinations = IntMath.pow(2,numbers.length);
        int hits = 0;
        
        for(int i = 0; i < totalCombinations; ++i)
        {
            int[] factCount = new int[4];
            
            for(int nIdx = 0; nIdx < numbers.length; ++nIdx)
            {
                if ( (i & 1 << nIdx ) == 0)
                {
                    continue;
                }
                switch(numbers[nIdx])
                {
                case 2:
                    factCount[0]++;
                    break;
                case 3:
                    factCount[1]++;
                    break;
                case 4:
                    factCount[0] += 2;
                    break;
                case 5:
                    factCount[2]++;
                    break;
                case 6:
                    factCount[0]++;
                    factCount[1]++;
                    break;
                case 7:
                    factCount[3]++;
                    break;
                case 8:
                    factCount[0]+=3;
                    break;
                }
            }
            
            if (false) log.debug("Factors 2,3,5,7 of {} with choice {} = {}",
                    numbers,
                    factCount, Integer.toBinaryString(i)); 
            
            if (Arrays.equals(factCount, factorsInProduct))
            {
                ++hits;
            }
        }
        
        double ret = 1.0 * hits / totalCombinations;
        log.debug("Prob to get product {} with {} : {} / {} = {}%",
                product, numbers, hits, totalCombinations, ret*100);
        return ret;
    }
    
    
    public String handleCaseFaster(InputData in) 
    {
        for(int i= 1; i <=12; ++i)
        {
            getProb(1, i);
        }
        
        int[][] combin = LargeNumberUtils.generateModedCombin(12, 1000);
        
        log.debug("{}", combin);
        
        probToGenerateProduct(new int[] {3,3, 2, 2, 3}, 9);
        probToGenerateProduct(new int[] {3,3, 2, 2, 3}, 27);
        probToGenerateProduct(new int[] {3,3, 2, 2, 3}, 54);
        probToGenerateProduct(new int[] {3,3, 2, 2, 3}, 108);
        probToGenerateProduct(new int[] {3,3, 2, 2, 3}, 1);
        //if (in != null) return "fah";
        
        StringBuffer ans = new StringBuffer();
        
        ans.append(String.format("Case #%d:\n", in.testCase));
        
        log.debug("Reps {} Product size {}", in.nProductSets, in.productSetSize);
        

        int n = in.maxNumber-1;
        int k = in.numbersChosen;
       // int possibleChoicesAmount = IntMath.factorial( n+k-1)
        //        / ( IntMath.factorial(k) * IntMath.factorial(n-1));
        
        //For each set of products, loop through all possibilities of N choices from 2 to M.
        for(int r = 0; r < in.nProductSets; ++r)
        {
            
            Arrays.sort(in.productValues[r]);
            log.info("Product set {}: {}", r, in.productValues[r]);
            
            int[] requiredFactorCounts = getRequired(in.productValues[r]);
            
            List<Integer> requiredFactors = Lists.newArrayList();
            
            
            //5,7
            for(int fIdx = 2; fIdx < factors.length; ++fIdx)
            {
                for(int rep = 0; rep < requiredFactorCounts[fIdx]; ++rep)
                {
                    requiredFactors.add(factors[fIdx]);
                }
            }
            
            int[] choices = new int[in.numbersChosen];
            
            for(int i = 0; i < choices.length; ++i)
            {
                choices[i] = 2;
            }
            
            int leftoverToChoose = in.numbersChosen - requiredFactors.size();
            
            Preconditions.checkState(leftoverToChoose >= 0);
            
            //End of choices are the required ones
            for(int i = 0; i < requiredFactors.size(); ++i)
            {
                choices[leftoverToChoose + i] = requiredFactors.get(i);
            }
            
            
            int maxHits = -1;
            double bestProb = 0;
            int[] bestAnswer = Arrays.copyOf(choices, choices.length);
            
            int possibleChoicesAmount = Ints.checkedCast(LongMath.factorial( n+leftoverToChoose-1)
                    / ( LongMath.factorial(leftoverToChoose) * LongMath.factorial(n-1)));
            
            log.debug("Possible choices of N {}  N {} leftover {}", 
                    possibleChoicesAmount, n, leftoverToChoose);

            
            
            for(int i = 0; i < possibleChoicesAmount; ++i)
            {
                log.debug("{} of {} : nums {} prod targ {}", i, possibleChoicesAmount, choices, in.productValues[r]);
            
                int count2 = 0;
                int count3 = 0;
                for(int ci = 0; ci < choices.length; ++ci)
                {
                    switch(choices[ci])
                    {
                    case 2:
                        count2++;
                        break;
                    case 3:
                        count3++;
                        break;
                    case 4:
                        count2+=2;
                        break;
                    case 6:
                        count3+=2;
                        count2++;
                        break;
                    case 8:
                        count2+=3;
                        break;
                    }
                }
                
                if (count2 < requiredFactorCounts[0])
                {
                    log.debug("not enough 2s");
                    CombinationsWithRepetition.next_combo(choices, leftoverToChoose, 2, in.maxNumber);
                    continue;
                }
                if (count3 < requiredFactorCounts[1])
                {
                    log.debug("not enough 3s");
                    CombinationsWithRepetition.next_combo(choices, leftoverToChoose, 2, in.maxNumber);
                    continue;
                }
               // out = pr.next();
                
                /*
                int hits = 0;
                for(int iter = 0; iter < 40000; ++iter)
                {
                    int[] ret = runSimul(choices, in);
                
                    Arrays.sort(ret);
                    if(Arrays.equals(ret, in.productValues[r]))
                    {
                        hits++;
                    }
                    //log.debug("Ret {}", (Object) ret);
                }
                
                if(hits > 0) log.debug("case {} {} has {} hits", r, (Object) choices, hits);
                if (hits > maxHits && hits > 0)
                {
                    bestAnswer = Arrays.copyOf(choices, choices.length);
                    maxHits = hits;
                }*/
                
                double probTotal = 1;
                for(int pIdx = 0; pIdx < in.productValues[r].length; ++pIdx)
                {
                    double prod = probToGenerateProduct(choices, in.productValues[r][pIdx]);
                    probTotal *= prod;
                }
                
                log.debug("Prob total {} for {}", probTotal, choices);
                
                if (probTotal >= bestProb)
                {
                    bestProb = probTotal;
                    log.debug("New best choices {} for product {}", choices, in.productValues[r]);
                    bestAnswer = Arrays.copyOf(choices, choices.length);
                }
                
                CombinationsWithRepetition.next_combo(choices, leftoverToChoose, 2, in.maxNumber);
            }
            
            
            ans.append(Ints.join("", bestAnswer));
            ans.append("\n");
            log.info(Ints.join("", bestAnswer));
        }
        return ans.toString();
    }

    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData in) 
    {
        return handleCaseFaster(in);
        //return handleCaseBruteForce(in);
    }
    public String handleCaseBruteForce(InputData in) 
    {
        
        StringBuffer ans = new StringBuffer();
        
        ans.append(String.format("Case #%d:\n", in.testCase));
        
        log.debug("Reps {} Product size {}", in.nProductSets, in.productSetSize);
        

        int n = in.maxNumber-1;
        int k = in.numbersChosen;
        final int possibleChoicesAmount = IntMath.factorial( n+k-1)
                / ( IntMath.factorial(k) * IntMath.factorial(n-1));
        
        for(int r = 0; r < in.nProductSets; ++r)
        {
            log.debug("Product set {}", r);
            Arrays.sort(in.productValues[r]);
            Integer[] ints = new Integer[in.maxNumber-1];
            
            for(int num = 2; num <= in.maxNumber; ++num)
            {
                ints[num-2] = num;
            }
            
            
            int[] choices = new int[in.numbersChosen];
            
            for(int i = 0; i < choices.length; ++i)
            {
                choices[i] = 2;
            }
            
            
            
            log.debug("Possible choices of N {}", possibleChoicesAmount);

            int maxHits = -1;
            int[] bestAnswer = null;
            
            for(int i = 0; i < possibleChoicesAmount; ++i)
            {
            
                int[] numChosen = choices;
                
               // out = pr.next();
                log.debug("nums {} prod targ {}", (Object)numChosen, in.productValues[r]);
                
                int hits = 0;
                for(int iter = 0; iter < 40000; ++iter)
                {
                    long[] ret = runSimul(numChosen, in);
                
                    Arrays.sort(ret);
                    if(Arrays.equals(ret, in.productValues[r]))
                    {
                        hits++;
                    }
                    //log.debug("Ret {}", (Object) ret);
                }
                
                if(hits > 0) log.debug("case {} {} has {} hits", r, (Object) numChosen, hits);
                if (hits > maxHits)
                {
                    bestAnswer = Arrays.copyOf(numChosen, numChosen.length);
                    maxHits = hits;
                }
                
                CombinationsWithRepetition.next_combo(choices, choices.length, 2, in.maxNumber);
            }
            
            
            ans.append(Ints.join("", bestAnswer));
            ans.append("\n");
            log.info(Ints.join("", bestAnswer));
        }
        return ans.toString();
    }
    
    int genRand(int min, int max)
    {
        return min + (int)(Math.random() * ((max - min) + 1));
    }
    
    int[] chooseNums(InputData in)
    {
        
        //choose N numbers
        int[] numChosen = new int[in.numbersChosen];
        
        for(int i = 0; i < numChosen.length; ++i)
        {
            numChosen[i] = genRand(2, in.maxNumber);
        }
        
        return numChosen;
    }
    
    long[] runSimul(int[] numChosen, InputData in)
    {
        long[] ret = new long[in.productSetSize];
        
        //choose N numbers
                
        for(int k = 0; k < in.productSetSize; ++k)
        {
            int product = 1;
            
            for(int i = 0; i < numChosen.length; ++i)
            {
                int use = genRand(1, 2);
                if (use == 1)
                    continue;
                
                product *= numChosen[i];
            }
            
            ret[k] = product;
        }
        
        return ret;
    }
    
    public String handleUsingSolutionN2(InputData in)
    {
        return String.format("Case #%d: %d", 
                in.testCase, 3
                );
    }
    
   

   
}
