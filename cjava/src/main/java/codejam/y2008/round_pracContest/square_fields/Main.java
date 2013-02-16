package codejam.y2008.round_pracContest.square_fields;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import codejam.utils.geometry.PointInt;
import codejam.utils.main.AbstractInputData;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main extends InputFilesHandler implements TestCaseHandler<AbstractInputData>, TestCaseInputScanner<AbstractInputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
       //super();
        super("B", 1,1);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);
        
        in.N = scanner.nextInt();
        in.K = scanner.nextInt();
        
        in.points = Lists.newArrayList();
        
        for(int i = 0; i < in.N; ++i) {
            in.points.add(new PointInt(scanner.nextInt(), scanner.nextInt()));
        }
        
        return in;
    }
    
    /**
     * For every combination of squares, return
     * the minimum length necesary to cover them
     * with 1 square
     * @param in
     * @return
     */
    int[] calculateSquares(InputData in) {
        int[] ret = new int[1 << in.N];
        ret[0] = Integer.MAX_VALUE;
        
        for(int combin = 1; combin < 1 << in.N; ++combin)
        {
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            
            for(int ptIdx = 0; ptIdx < in.N; ++ptIdx) {
                if ( (combin & 1 << ptIdx) == 0)
                    continue;
                
                minX = Math.min(minX, in.points.get(ptIdx).x());
                minY = Math.min(minY, in.points.get(ptIdx).y());
                maxX = Math.max(maxX, in.points.get(ptIdx).x());
                maxY = Math.max(maxY, in.points.get(ptIdx).y());
            }
            
        
            //Square is the larger of the space needed
            ret[combin] = Math.max(maxX-minX, maxY-minY);
        }
        
        return ret;
    }
    
    int[] calculateSquaresRequired(InputData in, int[] squareLenNeeded, int sz)
    {
        int[] req = new int[1 << in.N];
        
        req[0] = 0;
        /**
         * Go through every combination
         */
        for (int combin = 1; combin < (1 << in.N); combin++)
        {
            if (squareLenNeeded[combin] <= sz) {
                req[combin] = 1;
            }
            else
            {
                req[combin] = Integer.MAX_VALUE;
                
                /**
                 * Loop through each combination/subset less than i
                 */
                for (int subCombin = combin; subCombin != 0; subCombin = (subCombin - 1) & combin)
                {
                    if (squareLenNeeded[subCombin] > sz)
                        continue;
                
                    /**
                     * Pretty clever bmerry...
                     * 
                     * Basically the # of squares required is
                     * the number to cover sub, which = 1 
                     * + the number to cover the rest.
                     * 
                     * If i = 
                     * 101011
                     * and sub =
                     * 100001
                     * then x'oring is the ones still uncovered
                     * 001010
                     * 
                     * The other part is that because req is filled in
                     * order, all the subsets will have already been filled in.
                     * 
                     * 
                     */
                    req[combin] = Math.min(req[combin], 1 + req[combin ^ subCombin]);
                    
                    //At this point, we have a valid answer
                    Preconditions.checkState(req[combin] <= in.N);
                }
            }
        }
        
        return req;
    }
    
    @Override
    public String handleCase(AbstractInputData ain)
    {
        InputData in = (InputData) ain;
        
        int lowLen = 0;
        int highLen = 100000;
        
        int[] sqLenNeeded = calculateSquares(in);
        
        int allSquares = (1 << in.N) - 1; 
        
        while (true) {
            int midIdx = lowLen + (highLen - lowLen) / 2;

            int[] req = calculateSquaresRequired(in, sqLenNeeded, midIdx);
            
            if (req[allSquares] > in.K) {
                lowLen = midIdx;
            } else {
                highLen = midIdx;
            }

            Preconditions.checkState(lowLen <= highLen);

            if (highLen - lowLen <= 1)
                break;
        }
        
        
        
        
        return  String.format("Case #%d: %d", in.testCase, highLen);
        
    }
    

}