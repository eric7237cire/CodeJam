package codejam.y2012.round_2.swinging_wild;

import codejam.utils.main.AbstractInputData;

public class InputData 
    extends AbstractInputData {
        
    /*
     * The first line of each test case contains the number N of vines.
     *  N lines describing the vines follow, each with a pair of integers
     *   di and li - the distance of the vine from your ledge, and
     *    the length of the vine, respectively. The last line of the 
     *    test case contains the distance D to the ledge with your one 
     *    true love. You start by holding the first vine in hand.
     */
        int N;
        int d[];
        int l[];
        int D;
        
        public InputData(int testCase) {
            super(testCase);
        }
}
