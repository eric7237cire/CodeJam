package codejam.y2009.round_final.marbles;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    int N;
    
    //value is a color id
    int[] locColor;
    
    //Index is color id, values are the colors;
    List<Pair<Integer,Integer>> colorLoc;
    
    Map<String, Integer> colorToId;
    
    //List of colors
    String s[]; 
    
    public InputData(int testCase) {
        super(testCase);
    }

}