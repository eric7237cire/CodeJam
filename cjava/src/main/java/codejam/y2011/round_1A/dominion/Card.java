package codejam.y2011.round_1A.dominion;

import com.google.common.collect.ComparisonChain;

public class Card implements Comparable<Card> {
    
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

    @Override
    public int compareTo(Card o2)
    {
        return ComparisonChain.start().compare(S, o2.S).compare(index, o2.index).result();
    }
}
