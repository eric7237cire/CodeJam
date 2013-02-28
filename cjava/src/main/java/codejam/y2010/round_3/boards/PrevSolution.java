package codejam.y2010.round_3.boards;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.mod.GCD;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;

public class PrevSolution
{
    final static Logger log = LoggerFactory.getLogger(Boards.class);

    final static int INVALID = IntMath.pow(10, 8); 
    
    int[][] memoize_mod_board_count;
    
    /*
     * The right idea but the wrong implementation.  Was trying to find lowest mod count.
     * 
     * The shortest path search was better obviously.
     */
    public int solve_mod(final int boardLengthNeeded, final int mod, int maxBoardIndex, int[] boardLengths, final int maxBoardsToAdd) {
        
        if (boardLengthNeeded == 0) {
            return 0;
        } 
        if (maxBoardIndex < 0) {
            return INVALID;
        }
        
        if ( memoize_mod_board_count[boardLengthNeeded][maxBoardIndex] >= 0) {
            return memoize_mod_board_count[boardLengthNeeded][maxBoardIndex];
        }
        
        
        int possibleBoardsToAdd = maxBoardsToAdd;
        
        //log.debug("Solve mod board len needed {} board index {}  mod {}", boardLengthNeeded, maxBoardIndex, mod);
        //Set<Integer> seenModValues = new HashSet<Integer>();
        int minCost = INVALID;
        
        minCost = solve_mod(boardLengthNeeded,mod,maxBoardIndex-1, boardLengths, maxBoardsToAdd);
        
        //diff = current sum - L
        
        
        int currentLengthNeeded = boardLengthNeeded;
        int numAdded = 0;
        
        while(true) {
            currentLengthNeeded -= boardLengths[maxBoardIndex];
            numAdded++;
            
            if (currentLengthNeeded < 0) {
                //We added too many, take a big log off the pile
                currentLengthNeeded += mod;
                numAdded --;
                possibleBoardsToAdd --;
            }
//            Preconditions.checkState(newMod >= 0);
            //Preconditions.checkState(newMod < mod);
            if (currentLengthNeeded == boardLengthNeeded) { //seenModValues.contains(currentLengthNeeded)) {
                //log.debug("Cycle length {}", numAdded);
                break;
            }
            
            if (possibleBoardsToAdd < 0) {
                break;
            }
          //  seenModValues.add(currentLengthNeeded);
            
            //int numTakenAway = (currentLengthNeeded - boardLengthNeeded) / mod;
            
            int cost =  numAdded + solve_mod( currentLengthNeeded , mod, maxBoardIndex -1, boardLengths,possibleBoardsToAdd);
            minCost = Math.min(minCost,cost);
            
        }
        
        memoize_mod_board_count[boardLengthNeeded][maxBoardIndex] = minCost;
            
        return minCost;
    }
    
    public int[] memo;
    //Bottom up DP for once.  Did pretty well for most of large too
    public int solve_iter(int targetLength, int[] boardLength) {

        memo = new int[targetLength + 1];
        // b a s e c a s e
        memo[0] = 0;
        // f i l l i n memo a r r a y

        for (int i = 1; i <= targetLength; ++i) {
            memo[i] = INVALID;

            for (int j = 0; j < boardLength.length; ++j) {
                if (boardLength[j] <= i) {
                    memo[i] = Math.min(memo[i], 1 + memo[i - boardLength[j]]);
                }
            }
        }
    
        return memo[targetLength];
    }
    
  //Solves ax + by = L.  Not used, mauvaise piste!
    static public void solve(int a, int b, long L) {
        int gcd_ab = IntMath.gcd(a,b);
        int[] st = GCD.gcdExtended(a,b);
        Preconditions.checkState(st[0] == gcd_ab);
        int s = st[1];
        int t = st[2];
        
        if (L % gcd_ab != 0) {
            log.debug("No solutions");
            return;
        }
        
        long x_0 = s*L / gcd_ab;
        long y_0 = t *L / gcd_ab;
        
        
        
        //where x is first positive 
        
        // 0 <= x_0 + b*k / gcd_ab
        //-x_0 <= b*k / gcd_ab
        //-x_0 * gcd_ab / b <= k

        //0 <= y_0 - a*k / gcd_ab
        //-y_0 <= -a*k / gcd_ab
        //y_0 >= a*k / gcd_ab
        //y_0 * gcd_ab / a >= k
        
        //long start = y_0 * gcd_ab / a - 1;
        BigInteger[] startBi = BigInteger.valueOf(-x_0 * gcd_ab).divideAndRemainder(BigInteger.valueOf(b));
        long start = startBi[0].longValue();
        if (startBi[1].longValue() > 0) {
            start++;
        }        
        
        log.debug("x_0 {} y_0 {} k {}", x_0, y_0, start);
        
        for(long k = start; k <= start +5; ++k) {
            long x = x_0 + b*k / gcd_ab;
            long y = y_0 - a*k  / gcd_ab;
            
            
            log.debug("Solution x: [{}] y: [{}] to {}x + {}y = {}.   x+y  = {}",x,y,a,b,L, x+y);
            Preconditions.checkState(a*x + b*y == L);
            
            //10000653330
        }
    }
}
