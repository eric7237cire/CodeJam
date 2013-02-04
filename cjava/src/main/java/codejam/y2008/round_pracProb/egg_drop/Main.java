package codejam.y2008.round_pracProb.egg_drop;

import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.BitSetInt;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
       super();
        //super("B", true,true);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);
        
        in.F = scanner.nextLong();
        in.D = scanner.nextLong();
        in.B = scanner.nextLong();
        
        return in;
    }
    
    long findFMax(long D, long B) {
        if (D == 0 || B == 0)
            return 0;
        
        if (B == 1) {
            /**
             * If we can break only 1 egg, then we can test D levels,
             * starting at the first floor and going up
             */
            return D;
        }
        if (B == 2) {
            /**
             * We make a partition like D-1, D-2, D-3, D-4 to 1
             * Say D is 6, so F(6, 2)
             *   F(6, 2) = F(5,1) + 1 + F(5,2)  == 5 + 1 + 15 == 21
             *   F(5, 2) = F(4,1) + 1 + F(4,2)  == 4 + 1 + 10 == 15
             *   F(4, 2) = F(3,1) + 1 + F(3,2)  == 3 + 1 + 6 == 10
             *   F(3, 2) = F(2,1) + 1 + F(2,2)  == 2 + 1 + 3 == 6 
             *   F(2, 2) = F(1,1) + 1 + F(1,2)  == 3
             *   F(1, 1) =                      == 1
             *   
             *   which is the sum formula
             */
            return D*(D+1)/2;
        }
        return 3;
    }
    
    long findDMin(long F, long B)
    {
        Preconditions.checkArgument(F >= 0);
        Preconditions.checkArgument(B >= 0);
        long last = -2;
        
        int d = 1;
        
        while(true) {
            long f = findFMax(d, B); 
            
            if (f >= F || f == -1) {
                return d;
            }
            
            ++d;
            Preconditions.checkState(f > last);
            last = f;
        }
        
    }
    
    long findBMin(long F, long D) {
        Preconditions.checkArgument(F >= 0);
        Preconditions.checkArgument(D >= 0);
        long last = -2;
        
        int b = 1;
        
        while(true) {
            long f = findFMax(D, b); 
            
            if (f >= F || f == -1) {
                return b;
            }
            
            ++b;
            Preconditions.checkState(f > last);
            last = f;
        }
    }
        
    @Override
    public String handleCase(InputData in)
    {
        long Fmax = findFMax(in.D,in.B);
        long Dmin = findDMin(in.F,in.B);
        long Bmin = findBMin(in.F,in.D);
        
        return  String.format("Case #%d: %d %d %d", in.testCase, Fmax, Dmin,Bmin);
        
    }
    

}