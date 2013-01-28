package codejam.y2009.round_3.alphabetomials;

import java.util.List;
import java.util.Map;

public class FastSolution {
    static  int MOD = 10009;
    
    static public int combineTerms(InputData in, int k, List<Map<String, Integer>> memo) {
        int total = 0;
        String[] terms = in.polynomial.split("\\+");
        
        for(String term : terms) {
            total += multiplySets(term, k, in, memo);
            total %= MOD;
        }
        
        
        return total;
    }
    /**
     * 
     * @param polyTerm  a single term, ie aabbccc
     * @param k how many times to multiply the set
     * @param in
     * @param memo index is k ; stores polyTerm->count
     * @return
     */
    static public int multiplySets(String polyTerm, int k, InputData in, List<Map<String, Integer>> memo) {
        
        if (memo.get(k-1).containsKey(polyTerm)) {
            return memo.get(k-1).get(polyTerm);
        }
        int L = polyTerm.length();
        
        int total = 0;
        
        if(k==1) {
            for(int w = 0; w < in.d; ++w) {
                total += evaluateTerm(polyTerm, w, in); 
            }
            memo.get(k-1).put(polyTerm, total);
            total %= MOD;
            return total;
        }
        
        /*
         * Take a polynomial like aab, we want (a1+a2)(a1+a2)(b1+b2)
         * In the end, we have 2^3 = 8 terms, in the form
         * a1*a1*b1   * 1
         * a1*a1      *     b2
         * a1*  *b1  *    a2
         *   *a1*b1  *  a2
         *   
         *   Which lends itself to iterate through all the bit sets up to 8
         * 000   *   111
         * 001   *   110
         */
        for (int bm = 0; bm < (1 << L); bm++)
        {
            StringBuffer letters1 = new StringBuffer();
            StringBuffer letters2 = new StringBuffer();
            for (int i = 0; i < L; i++)
            {
                if ( (bm & (1 << i)) != 0)
                    letters1.append( polyTerm.charAt(i) );
                else
                    letters2.append( polyTerm.charAt(i) );
            }           
            
            int sum1 = multiplySets(letters1.toString(), 1, in, memo);
            
            //say we have k = 5, then we treat a2 (or b2 or z2)
            //as  a2+a3+a4+a5, so we evaluate the second term like that
            int sum2 = multiplySets(letters2.toString(), k-1, in,memo);
            
            //For each word in the dictionary, evaluate
/*            for(int w = 0; w < in.d; ++w) {
                sum1 += evaluateTerm(letters1.toString(), w, in);
            }*/
            total += sum1*sum2;
            
            total %= MOD;
        }
        
        memo.get(k-1).put(polyTerm, total);
        return total;
    }
    
    static public int evaluateTerm(String polyTerm, int word, InputData in) {
        int prod = 1;
        for(int c = 0; c < polyTerm.length(); ++c) {
            prod *= in.wordLetterCount[word][polyTerm.charAt(c)-'a'];
        }
        return prod % MOD;
    }
}
