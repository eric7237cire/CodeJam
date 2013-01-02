package codejam.y2011.round_2.airport_walkways;

import codejam.utils.main.AbstractInputData;

public class InputData 
    extends AbstractInputData {

    /*
     * The first line of the input gives the number of test cases, T.
     *  T test cases follow. Each test case begins with a line containing
five integers: X (the length of the corridor, in meters),
 S (your walking speed, in meters per second), 
 R (your running speed, in meters per second),
  t (the maximum time you can run, in seconds) and N (the number of walkways).

Each of the next N lines contains three integers: 
Bi, Ei and wi - the beginning and end of the walkway
 (in meters from your starting point) and the speed of the walkway (in meters per second).
     */
        int X;
        int S;
        int R;
        int t;
        int N;
        
        Walkway[] walkways;
        
        public InputData(int testCase) {
            super(testCase);
        }
        
        static class Walkway {
            int B;
            int E;
            int w;
        }
}
