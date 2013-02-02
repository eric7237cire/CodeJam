package codejam.y2011.round_1A.dominion;

import java.util.ArrayList;
import java.util.List;

import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {
    int N;
    int M;
    

    List<Card> hand;
    List<Card> deck;
    
    List<Card> c0_cards;
    List<Card> c1_cards;
    List<Card> c2_cards;
    List<Card> t_cards ;
    
    public InputData(int testCase) {
        super(testCase);
        hand = new ArrayList<>();
        deck = new ArrayList<>();
    }
}
