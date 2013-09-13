package codejam.y2013.round_qual.lawnmower;

import java.util.Scanner;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Grid;


public class LawnMower extends InputFilesHandler implements
TestCaseHandler<InputData>, TestCaseInputScanner<InputData> 
{
    public LawnMower()
    {
        super("B", 1, 1);
        //setLogInfo();
        
    }

    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
        InputData in = new InputData(testCase);
        
        int n,m;
        
        //height
        n = scanner.nextInt();
        m = scanner.nextInt();
        
        in.grid = Grid.buildFromScanner(scanner, n, m, Grid.fromScannerInt, 0);
        
        return in;
    }

    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData in) 
    {
        for(int r = 0; r < in.grid.getRows(); ++r)
        {
            for(int c = 0; c < in.grid.getCols(); ++c)
            {
                int sqValue = in.grid.getEntry(r,c);
                
                boolean okVert = true;
                
                //ligne vertical
                for(int cc = 0; cc < in.grid.getCols(); ++cc)
                {
                    int val = in.grid.getEntry(r, cc);
                    
                    if (val > sqValue) {
                        okVert = false; 
                        break;
                    }
                }
                
                boolean okHoriz = true;
                
                //ligne vertical
                for(int rr = 0; rr < in.grid.getRows(); ++rr)
                {
                    int val = in.grid.getEntry(rr, c);
                    
                    if (val > sqValue) {
                        okHoriz = false; 
                        break;
                    }
                }
                
                if (!okVert && !okHoriz) 
                {
                    return String.format("Case #%d: NO", 
                            in.testCase);
                    
                }
            }
        }
       
        return String.format("Case #%d: YES", 
                in.testCase);
        
       
    }

}
