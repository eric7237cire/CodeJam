package codejam.y2008.round_3.endless_knight;

import java.util.Scanner;

import org.apache.commons.math3.fraction.Fraction;

import ch.qos.logback.classic.Level;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    
    public Main()
    {
        super("C", 1, 1, 1);
        (( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
    }
    
    /*
     * To calculate change of basis
     * Array2DRowRealMatrix m = new Array2DRowRealMatrix(
                new double[][] {
                        {2d, 1d},
                        {1d, 2d},
                });
        
        LUDecomposition lu = new LUDecomposition(m);
        RealMatrix inv = lu.getSolver().getInverse();
        
        log.debug("Inv {}", inv);
        
        for(int x = 0; x <= 5; ++x)
        {
            for(int y = 0; y <= 5; ++y) 
            {
        
                Array2DRowRealMatrix p1 = 
                        new Array2DRowRealMatrix(
                                new double[][] {
                                        {(double)x},
                                        {(double)y},
                                });
            
                RealMatrix r = inv.multiply(p1);
                log.debug("Result {},{} = {}", x,y ,r);
            }
        }
     */
   
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData in = new InputData(testCase);
       
        in.H = scanner.nextInt();
        in.W = scanner.nextInt();
        in.R = scanner.nextInt();
        
        in.rocksOrig = new PointInt[in.R];
        
        for(int i = 0; i < in.R; ++i)
        {
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            in.rocksOrig[i] = new PointInt(col, row);
            
        }
        return in;
    }

  void convertBasis(InputData in)
  {
      /*
      Fraction[][] basisMatrix = new Fraction[][] {
              {2d/3, -1d/3},
              {-1d/3, 2d/3}
      };*/
      Fraction a = new Fraction(2,3);
      Fraction b = new Fraction(-1,3);
      
      for(PointInt rock : in.rocksOrig)
      {
          Fraction x = a.multiply(rock.x()).add(b.multiply(rock.y()));
      }
  }
   
    @Override
    public String handleCase(InputData in) {
       
       convertBasis(in);

        return String.format("Case #%d: %s", in.testCase);
        
    }
}