package codejam.y2011.round_1A.dominion;

import com.google.common.base.Objects;

public class Node {
    final int hand;
    final int turns;
    final int t;
    final int c1;
    final int c2;
        
    public Node(int hand, int turns, int t, int c1, int c2, int TOTAL_CARDS) {
        super();
        this.hand = Math.min(TOTAL_CARDS, hand);
        this.turns = Math.min(TOTAL_CARDS, turns);
        this.t = t;
        this.c1 = c1;
        this.c2 = c2;
        
    }
    
    public void limit() {
        
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + c1;
        result = prime * result + c2;
        result = prime * result + hand;
        result = prime * result + t;
        result = prime * result + turns;
        return result;
    }
    @Override
    public String toString() {
        return "Node [hand=" + hand + ", turns=" + turns +  ", c1="
                + c1 + ", c2=" + c2 + "]";
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node other = (Node) obj;
        
        return Objects.equal(c1, other.c1) &&
                Objects.equal(c2, other.c2) &&
                Objects.equal(hand, other.hand) &&
                Objects.equal(t, other.t) &&
                Objects.equal(turns, other.turns);
        
    }
}
