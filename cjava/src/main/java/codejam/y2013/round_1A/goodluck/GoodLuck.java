package codejam.y2013.round_1A.goodluck;

import java.math.BigInteger;
import java.util.Arrays;
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
import codejam.utils.utils.PermutationWithRepetition;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;


public class GoodLuck extends InputFilesHandler implements
TestCaseHandler<InputData>, TestCaseInputScanner<InputData> 
{
    public GoodLuck()
    {
        super("C", 1, 1);
        //setLogInfo();
        setLogDebug();
        
        
    }
    
    public String[] getDefaultInputFiles()
    {

        return new String[] {
               // "sample.in",
                "C-small-practice-1.in"
                //,"C-small-practice-2.in" 
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
        
        in.productValues = new int[in.nProductSets][in.productSetSize];
        
        for(int i = 0; i < in.productValues.length; ++i)
        {
            for(int j = 0; j < in.productValues[i].length; ++j)
            {
                in.productValues[i][j] = scanner.nextInt();
            }
            
        }
        
        
        return in;
    }
    


    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData in) 
    {
        return handleCaseBruteForce(in);
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
                    int[] ret = runSimul(numChosen, in);
                
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
            log.info("d");
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
    
    int[] runSimul(int[] numChosen, InputData in)
    {
        int[] ret = new int[in.productSetSize];
        
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
