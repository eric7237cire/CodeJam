package codejam.y2011.round_1A.dominion;

public class Card {
    
    public Card(int c, int s, int t, int index) {
        super();
        C = c;
        S = s;
        T = t;
        this.index = index;
    }
    //cards to draw
    final int C;
    
    //score
    final int S;
    
    //extra turns
    final int T;
    
    final int index;
}
