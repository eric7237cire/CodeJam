package mod;

import com.google.common.base.Preconditions;

public class GCD {

    /**
     * Extended Euclidean Algorithm
     * Returns x, y such that xa + yb = gcd(a,b)
     * @param a
     * @param b
     * @return gcd, x, y
     */
    public static int[] gcdExtended(int aa, int bb) {
        //compute r_i = ax_i + by_i
        int a = Math.max(aa,bb);
        int b = Math.min(aa,bb);
        
        //step 1 a = a * 1 + b * 0
        //step 2 b = a * 0 + b * 1
        int x_back2 = 1; //x1
        int y_back2 = 0; //y1
        
        int x_back1 = 0; //x2
        int y_back1 = 1; //y2
                
        int r_back2 = aa;
        int r_back1 = bb;
        
        while(true) {
            int q = r_back2 / r_back1;
            int r = r_back2 % r_back1;
            
            if (r == 0) {
                return new int[] {r_back1, x_back1, y_back1};                
            }
            
            r_back2 = r_back1;
            r_back1 = r;
            
            int x = x_back2 - q * x_back1;
            int y = y_back2 - q * y_back1;
            
            y_back2 = y_back1;
            x_back2 = x_back1;
            
            y_back1 = y;
            x_back1 = x;
                    
            
            Preconditions.checkState(r >= 0);
            if (r == 0) {
                return new int[] {q, x, y};                
            }
        }
        
        
    }
}
