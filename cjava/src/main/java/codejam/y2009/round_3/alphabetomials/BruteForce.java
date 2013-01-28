package codejam.y2009.round_3.alphabetomials;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BruteForce
{
    final static Logger log = LoggerFactory.getLogger(Main.class);


    public int evalP(String word, InputData input) {
        int total = 0;
        int term = 1;
        for (int c = 0; c < input.polynomial.length(); ++c) {
            char ch = input.polynomial.charAt(c);
            if (ch == '+') {
                total += term;
                term = 1;
                continue;
            }
            int count = StringUtils.countMatches(word, "" + ch);
            term *= count;
        }

        total += term;
        return total;
    }
    
    public List<Integer> doPerms(InputData input) {
        List<Integer> totals = new ArrayList<>();

        /**
         * k is how many words from the dictionary to combine into a new word.
         * Every permutation is calculated and counted.
         */
        for (int eachK = 1; eachK <= input.k; ++eachK) {
            
            Integer[] combin = new Integer[eachK];
            int total = 0;
            

            for (int i = 0; i < eachK; ++i) {
                combin[i] = 0;
            }

            /*
             * Permutations<Integer> perm = Permutations.create(dictArray,
             * combin, eachK);
             */

            while (true) {

                String word = "";
                for (Integer comIndex : combin) {
                    word += input.dictWords.get(comIndex) + " ";
                }

                final int wordEval = evalP(word,input);
                total += wordEval;

                total %= 10009;

                List<Integer> p_counts = new ArrayList<>();
                p_counts.add(StringUtils.countMatches(word, "a"));
                // p_counts.add(StringUtils.countMatches(word, "b"));

                log.debug("Perm {} p({}) {} total {}", new Object[] {(Object) combin,
                        p_counts, wordEval, total});

                boolean fullLoop = true;
                //Try all permutations.  This loop finds the first position that can be incremented
                for (int pos = 0; pos < eachK; ++pos) {
                    combin[pos]++;
                    
                    //Incremented, we are done
                    if (combin[pos] < input.d) {
                        fullLoop = false;
                        break;
                    }
                    
                    //This position is now zero, look to increment combin
                    combin[pos] = 0;
                }

                if (fullLoop) {
                    break;
                }

            }

            totals.add(total);
        }

        return totals;
    }

}
