package codejam.y2010.round_final.letter_stamper;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.FlowEdge;
import codejam.utils.datastructures.FlowNetwork;
import codejam.utils.datastructures.FordFulkerson;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.math.DoubleMath;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles{

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
       //return new String[] {"sample.in"};
//        return new String[] {"D-small-practice.in"};
        return new String[] {"A-small-practice.in", "A-large-practice.in"};
    }
    

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        

        InputData in = new InputData(testCase);
        in.S = scanner.next();
        
        return in;
    }
    
  
    
    /*
     * 0 ABC ABC ==> C AB CAB (4)
     * 1 ACB ACB ==> B AC BAC (2)
     * 2 BAC BAC ==> C BA CBA (5)
     * 3 BCA BCA ==> A    ABC (0)
     * 4 CAB CAB ==> B    BCA (3)
     * 5 CBA CBA ==> A    ACB (1)
     * 
     * left is top of stack
     */
    int[] pushTransition = new int[] { 4, 2, 5, 0, 3, 1 };  
    int[][] pushable       = new int[][] { {3, 5}, {1, 4}, {0, 2} };
    /*
     * 0 ABC ABC ==> A  BCA (3)
     * 1 ACB ACB ==> A  CBA (5)
     * 2 BAC BAC ==> B  ACB (1)
     * 3 BCA BCA ==> B  CAB (4)
     * 4 CAB CAB ==> C  ABC (0)
     * 5 CBA CBA ==> C  BAC (2)
     * 
     * left is top of stack
     */
    int[] popTransition = new int[] { 3, 5, 1, 4, 0, 2};
    int[][] topLetterToStack       = new int[][] { {0, 1}, {2, 3}, {4, 5} };
    
    //Stack type to letter
    int[][] popsNeeded = new int[][] { 
            { 0, 1, 2 }, 
            { 0, 2, 1 },
            { 1, 0, 2 }, 
            { 2, 0, 1 },
            { 1, 2, 0 }, 
            { 2, 1, 0 } 
            };
    
    int solve(String s, final int currentIndex, int stackType, int stackHeight, int[][][] memo) {
        if (currentIndex >= s.length())
            return stackHeight;
        
        if (stackHeight > s.length())
            return 10000000;
        
        if (memo[currentIndex][stackType][stackHeight] >= 0)
            return memo[currentIndex][stackType][stackHeight];
        
        final char nextChar = s.charAt(currentIndex);
        
   
        
        if (stackHeight == 0) {            
                        
            int stackChoice1 = topLetterToStack[ (int)nextChar - 'A'][0];
            int stackChoice2 = topLetterToStack[ (int)nextChar - 'A'][1];
            
            //Push onto the 2 stacks possible and print immediately
            int cost1 = 2 + solve(s, currentIndex+1, stackChoice1, 1,memo);
            int cost2 = 2 + solve(s, currentIndex+1, stackChoice2, 1,memo);
            
            return memo[currentIndex][stackType][stackHeight] = Math.min(cost1, cost2);
        }
    
        int stackPossible1 = topLetterToStack[ (int) nextChar - 'A'][0];
        int stackPossible2 = topLetterToStack[ (int) nextChar - 'A'][1];
        
        if (stackType == stackPossible1 || stackType == stackPossible2) {
            //Letter is on stack, print
            return memo[currentIndex][stackType][stackHeight] = 1 + solve(s, currentIndex+1, stackType, stackHeight,memo);
        }
        
        //We can either push or pop
        int pushChoice1 = pushable[ (int) nextChar - 'A'][0];
        int pushChoice2 = pushable[ (int) nextChar - 'A'][1];
        
        int costPush = 100000000;
        
        //Pushing is restricted based on the stack type.  If we push, we push and print
        if (stackType == pushChoice1) {
            costPush = 2 + solve(s, currentIndex + 1, pushTransition[pushChoice1], stackHeight+1,memo);
        } else if (stackType == pushChoice2) {
            costPush = 2 + solve(s, currentIndex + 1, pushTransition[pushChoice2], stackHeight+1,memo);
        }
        
        int popsNeededForNextChar = popsNeeded[stackType][ (int) nextChar - 'A'];
        
        int costPop = 1000000000;
        
        if (popsNeededForNextChar >= stackHeight) {
            //We just pop the rest of the stack
            costPop = stackHeight + solve(s, currentIndex, stackType, 0,memo);
        } else if (popsNeededForNextChar + 1 == stackHeight) {
            //Similiar to the stack empty case, we can switch states
            int stackChoice1 = topLetterToStack[ (int)nextChar - 'A'][0];
            int stackChoice2 = topLetterToStack[ (int)nextChar - 'A'][1];
            
            int cost1 = popsNeededForNextChar + 1 + solve(s, currentIndex+1, stackChoice1, 1,memo);
            int cost2 = popsNeededForNextChar + 1 + solve(s, currentIndex+1, stackChoice2, 1,memo);
            costPop = Math.min(cost1, cost2);
             
        } else {
            int nextStackState = popsNeededForNextChar == 2 ? popTransition[popTransition[stackType]] : popTransition[stackType];
            costPop = popsNeededForNextChar + 1 + solve(s, currentIndex+1, nextStackState, stackHeight - popsNeededForNextChar,memo);
        }
        
        
        return memo[currentIndex][stackType][stackHeight] = Math.min(costPop, costPush);
        
        
    }
    
    public String handleCaseBottomUp(InputData in) {
        return "todo";
    }

    @Override
    public String handleCase(InputData in) {
        
        int[][][] memo = new int[in.S.length()][6][in.S.length()];
        for(int i = 0; i < in.S.length(); ++i)
            for(int j = 0; j < in.S.length(); ++j)
                for(int k = 0; k < 6; ++k)
                    memo[i][k][j] = -1;
        
        int min = solve(in.S, 0, 0, 0, memo);
        
        return String.format("Case #%d: %d", in.testCase, min);
    }
}