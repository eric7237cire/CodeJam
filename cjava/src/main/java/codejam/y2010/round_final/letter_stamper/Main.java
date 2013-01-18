package codejam.y2010.round_final.letter_stamper;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles{

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
       //return new String[] {"sample.in"};
       // return new String[] {"A-small-practice.in"};
        return new String[] {"A-small-practice.in", "A-large-practice.in"};
    }
    

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        

        InputData in = new InputData(testCase);
        in.S = scanner.next();
        
        return in;
    }
    
  
    
    int[][] charsToStack = new int [][] {
            { -1, 0, 1}, //A B ; A C
            { 2, -1, 3}, //B A B C
            {4, 5, -1}, // C A  C B
    };
    
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
    int[] stackToTopLetter = new int[] { 0, 0, 1, 1, 2, 2 };
    
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
        
        //index -- state -- stack height
        int[][][] bottUp = new int[in.S.length()][6][Math.max(3,in.S.length())];
        for(int i = 0; i < in.S.length(); ++i)
            for(int j = 0; j < in.S.length(); ++j)
                for(int k = 0; k < 6; ++k)
                    bottUp[i][k][j] = 1000000000;
        
        int curIdx = in.S.length() - 1;
        int curChar = (int) in.S.charAt(curIdx) - 'A';
        
        //Initialize last character
        
        //Stack height = 0
        for(int stackState = 0; stackState < 6; ++stackState) {
            //No matter what the stack state, it will cost 3.  push,print,pop
            bottUp[curIdx][stackState][0] = 3;
        }
        
        //Stack height 1
        for(int stackState = 0; stackState < 6; ++stackState) {
            if (popsNeeded[stackState][curChar] == 0)
                //print, pop
                bottUp[curIdx][stackState][1] = 2; 
            else 
                //We need to push a 2nd character, and pop it x 2, print
                bottUp[curIdx][stackState][1] = 4;
        }
        
        //Stack height 2
        for(int stackState = 0; stackState < 6; ++stackState) {
            if (popsNeeded[stackState][curChar] < 2)
                //print, pop, pop
                bottUp[curIdx][stackState][2] = 3; 
            else 
                //pop, pop, push, print, pop
                bottUp[curIdx][stackState][2] = 5;
        }
        
        for(int stackHeight = 3; stackHeight < in.S.length(); ++stackHeight) {
            //Pop x stackheiht + print
            for(int stackState = 0; stackState < 6; ++stackState) { 
                bottUp[curIdx][stackState][stackHeight] = 1 + stackHeight;
            }
        }
        
        //General case        
        for( curIdx = in.S.length() - 2; curIdx >= 0; --curIdx) {
        
            curChar = (int) in.S.charAt(curIdx) - 'A';
            int maxCurrentStackHeight = Math.min(in.S.length() - curIdx+1, in.S.length()-1);
            
            //Height 0
            for(int stackState = 0; stackState < 6; ++stackState) {
                
                int stackState1 = topLetterToStack[curChar][0];
                int stackState2 = topLetterToStack[curChar][1];
                
                //Sanity check, the 2 states are equivalent when stack height = 1
                Preconditions.checkState(bottUp[curIdx+1][stackState1][1] 
                        == bottUp[curIdx+1][stackState2][1]);
                
                //Note the stackState is ignored, only the curChar matters
                bottUp[curIdx][stackState][0] = 2 + bottUp[curIdx+1][stackState1][1];
            }
            
            //Height 1
            for(int stackState = 0; stackState < 6; ++stackState) {
                if (popsNeeded[stackState][curChar] == 0)
                    //print
                    bottUp[curIdx][stackState][1] = 1 + bottUp[curIdx+1][stackState][1]; 
                else {
                    //We need to push a 2nd character
                    int stackTopChar = stackToTopLetter[stackState];

                    
                    int nextStackState = charsToStack[curChar][stackTopChar];
                    Preconditions.checkState(nextStackState >= 0);
                    
                    int pushCost = 2 + bottUp[curIdx+1][nextStackState][2];
                    
                    //Or we can always pop
                    int popCost = bottUp[curIdx][stackState][0] + 1;
                    
                    bottUp[curIdx][stackState][1] = Math.min(pushCost, popCost);
                }
            }
            
            //Height 2+
            for(int stackHeight = 2; stackHeight <= maxCurrentStackHeight; ++stackHeight) {
                for(int stackState = 0; stackState < 6; ++stackState) {
                    
                    //Letter is on top, shortest path is to print 
                    if (popsNeeded[stackState][curChar] == 0)
                    {
                        //print
                        bottUp[curIdx][stackState][stackHeight] = 1 + bottUp[curIdx+1][stackState][stackHeight];
                        continue;
                    }
                    
                    int popsNeededForNextChar = popsNeeded[stackState][curChar];
                    int stackStateAfterPop = popsNeededForNextChar == 2 ? popTransition[popTransition[stackState]] : popTransition[stackState];

                    //We use the state already calculated since we are going in order of height
                    int costPop = popsNeededForNextChar 
                            + bottUp[curIdx][stackStateAfterPop][stackHeight-popsNeededForNextChar];
                    
                    //We can always push the 3rd character of a stack
                    int pushChoice1 = pushable[ curChar ][0];
                    int pushChoice2 = pushable[ curChar ][1];
                    
                    int costPush = 100000000;
                    
                    //Pushing is restricted based on the stack type.  If we push, we push and print
                    if (stackState == pushChoice1 && stackHeight < in.S.length() - 1) {
                        costPush = 2 + 
                                bottUp[curIdx+1][ pushTransition[pushChoice1] ][ stackHeight+1 ];
                    } else if (stackState == pushChoice2  && stackHeight < in.S.length() - 1) {
                        costPush = 2 + bottUp[curIdx+1][ pushTransition[pushChoice2] ] [ stackHeight+1 ];
                    }
                    
                    bottUp[curIdx][stackState][stackHeight] = Math.min(costPop,costPush);
                    
                }
            }
        }
        
        

        return String.format("Case #%d: %d", in.testCase, bottUp[0][0][0]);

    }

    @Override
    public String handleCase(InputData in) {
        
        
        if (in.testCase > 0)
           return handleCaseBottomUp(in);
        
        int[][][] memo = new int[in.S.length()][6][in.S.length()];
        for(int i = 0; i < in.S.length(); ++i)
            for(int j = 0; j < in.S.length(); ++j)
                for(int k = 0; k < 6; ++k)
                    memo[i][k][j] = -1;
        
        int min = solve(in.S, 0, 0, 0, memo);
        
        
        return String.format("Case #%d: %d", in.testCase, min) + "\n" + handleCaseBottomUp(in);
    }
}