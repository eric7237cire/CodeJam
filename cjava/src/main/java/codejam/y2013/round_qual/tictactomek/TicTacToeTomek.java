package codejam.y2013.round_qual.tictactomek;

import java.util.Scanner;

import codejam.utils.datastructures.BitSetInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;


public class TicTacToeTomek extends InputFilesHandler implements
TestCaseHandler<InputData>, TestCaseInputScanner<InputData> 
{
    public TicTacToeTomek()
    {
        super("A", 1, 1);
        //setLogInfo();
        
    }

    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
        InputData in = new InputData(testCase);
        
        for(int r = 0; r < 4; ++r)
        {
            String inputLine = scanner.next();
            if (inputLine.length() < 4) {
                inputLine = scanner.next();
            }
            
            for(int c = 0; c < 4; ++c)
            {
                char ch = inputLine.charAt(c);
                int bitLoc = r * 4 + c;
                switch(ch)
                {
                case 'X':
                    in.x.set(bitLoc);
                    break;
                case 'O':
                    in.o.set(bitLoc);
                    break;
                case 'T':
                    in.o.set(bitLoc);
                    in.x.set(bitLoc);
                    break;
                }
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
        int[] masks = new int[10];
        
        //horizontal mask
        for(int i = 0; i < 4; ++i)
            masks[0] |= 1 << i; 
        
        for(int i = 1; i <= 3; ++i)
            masks[i] = masks[0] << 4 * i;
        
        //vertical
        for(int i = 0; i < 4; ++i)
            masks[4] |= 1 << 4*i; 
        
        for(int i = 1; i <= 3; ++i)
            masks[4+i] = masks[4] << 4 * i;
        
        masks[8] = 1 | 1 << 5 | 1 << 10 | 1 << 15;
        
        masks[9] = 1 << 3 | 1 << 6 | 1 << 9 | 1 << 12;
        
        for(int m = 0; m <= 9; ++m)
        {
            if ( (masks[m] & in.x.getBits()) == masks[m])
            {
                return String.format("Case #%d: X won", 
                        in.testCase);
            }
            
            if ( (masks[m] & in.o.getBits()) == masks[m])
            {
                return String.format("Case #%d: O won", 
                        in.testCase);
            }
        }
        
        if (Integer.bitCount(in.x.getBits() | in.o.getBits()) == 16) 
        {
            return String.format("Case #%d: Draw", 
                    in.testCase);
        } else {
            return String.format("Case #%d: Game has not completed", 
                    in.testCase);
        }
       
    }

}
