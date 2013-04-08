package codejam.y2013.round_online.babyheight;

import java.util.Scanner;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;


public class BabyHeight extends InputFilesHandler implements
TestCaseHandler<InputData>, TestCaseInputScanner<InputData> 
{
    public BabyHeight()
    {
        super("B", 1, 0);
        setLogInfo();
        
    }

    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
        InputData in = new InputData(testCase);
        
        String gender = scanner.next();
        String[] hStr = scanner.next().split("[\'\"]");
        String[] hStr2 = scanner.next().split("[\'\"]");
        
        int pHeight = Integer.parseInt(hStr[0], 10) * 12 +
                Integer.parseInt(hStr[1], 10) +
                Integer.parseInt(hStr2[0], 10) * 12 +
                Integer.parseInt(hStr2[1], 10);
        
        if (gender.equals("G"))
            pHeight -= 5;
        else
            pHeight += 5;
        
        if (pHeight % 2 == 1)
        {
            in.hStart = pHeight / 2 - 3;
            in.hEnd = pHeight / 2 + 4;
        } else {
            in.hStart = pHeight / 2 - 4;
            in.hEnd = pHeight / 2 + 4;
        }
        
        return in;
    }

    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData in) 
    {
        
        
        
        return String.format("Case #%d: %d'%d\" to %d'%d\"", 
                in.testCase, in.hStart/12, in.hStart%12, in.hEnd/12, in.hEnd%12);
       
    }

}
