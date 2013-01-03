package codejam.y2011.round_2.ai_wars;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.main.AbstractInputData;

public class InputData 
    extends AbstractInputData {

   /*
    * InputData.java
Main.javaThe first line of the input gives the number of test cases, T. 
T test cases follow. Each test case starts with a single line
 containing two space-separated integers: P, the number of planets,
  and W, the number of wormholes. Your home planet is planet 0, and 
  the A.I.'s home planet is planet 1.

The second line of each test case will contain W space-separated 
pairs of comma-separated integers xi,yi. Each of these indicates 
that there is a two-way wormhole connecting planets xi and yi.
    */
        int P;
        int W;
        
        List<Pair<Integer,Integer>> wormHoles;
        
        
        public InputData(int testCase) {
            super(testCase);
        }
        
}
