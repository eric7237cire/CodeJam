package codejam.y2009.round_1A.collecting_cards;

import java.util.Scanner;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import ch.qos.logback.classic.Level;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;

public class CollectingCards extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    public CollectingCards()
    {
        
        super("C", 1, 1);
        (( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
    }
	@Override
    public InputData readInput(Scanner scanner, int testCase) {
       InputData in = new InputData(testCase);
       in.C = scanner.nextInt();
       in.N = scanner.nextInt();
       return in;
    }
	
	

	/**
	 * How many ways if I already have x kinds of cards to end up with y
	 * @param x
	 * @param y
	 * @return
	 */
	public double T(int x, int y, InputData in)
	{
	    long totalPacks = LongMath.binomial(in.C, in.N);
	    
	    Preconditions.checkState(totalPacks != Long.MAX_VALUE);
	    
	    int nNewCards = y - x;
	    int nExistingCards = in.N - nNewCards;
	    
	    //Choosing y-x new cards from the new cards available C - x 
	    long newCards = LongMath.binomial(in.C - x, nNewCards);
	    
	    Preconditions.checkState(newCards != Long.MAX_VALUE);
	    
	    //State is impossible
	    if (nExistingCards > x)
	        return 0;
	    
	    //Choosing the remaining cards in the pack from cards that are already obtained
	    long existingCards = LongMath.binomial(x, nExistingCards);
	    
	    Preconditions.checkState(existingCards != Long.MAX_VALUE);
	    
	   
	    return 1.0d * newCards *existingCards / totalPacks;
	}
	
	/**
	 * Follows the solution 
	 */
    @Override
    public String handleCase(InputData in) {
        
        double[][] coef = new double[in.C+1][in.C+1];
        double[] rhs = new double[in.C+1];
        /**
         * Coefs go from E(0) E(1) ... E(C)
         */
        
        //If we have C cards, we need 0 packs
        coef[in.C][in.C] = 1d;
        rhs[in.C] = 0d;
        
        for(int x = 0; x < in.C; ++x)
        {
            //If we have x cards, then calculate expected value of having
            //y cards after getting a new pack
            int yUpperBound = Math.min(in.C, x + in.N);
            for(int y = x; y <= yUpperBound; ++y)
            {
                double t = T(x, y, in);
                coef[x][y] = t;
            }
            
            //Moving the +1 to rhs
            rhs[x] = -1;
            
            //Moving E(x) to lhs
            coef[x][x] --;
        }
        
        Array2DRowRealMatrix mat = new Array2DRowRealMatrix(coef);
        
        LUDecomposition lu = new LUDecomposition(mat);
        
        RealMatrix m = lu.getSolver().solve( new Array2DRowRealMatrix(rhs));
        
        double ans = m.getEntry(0, 0);
        
        return "Case #" + in.testCase + ": " + DoubleFormat.df6.format(ans);

    }
}