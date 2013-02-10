package codejam.y2011.round_1A.killer_word;

import java.util.List;

import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {
    int N;
    int M;
    
    public InputData(int testCase) {
        super(testCase);
    }
    List<String> words;
    List<String> guessLists;
}
