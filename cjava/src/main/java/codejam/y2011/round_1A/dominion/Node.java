package codejam.y2011.round_1A.dominion;

public class Node {
    int hand;
    int turns;
    int t;
    int c1;
    int c2;
    static int TOTAL_CARDS;
    
    public Node(int hand, int turns, int t, int c1, int c2) {
        super();
        this.hand = hand;
        this.turns = turns;
        this.t = t;
        this.c1 = c1;
        this.c2 = c2;
    }
    
    public void limit() {
        hand = Math.min(TOTAL_CARDS, hand);
        turns = Math.min(TOTAL_CARDS, turns);
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
        return "Node [hand=" + hand + ", turns=" + turns + ", t=" + t + ", c1="
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
        if (c1 != other.c1)
            return false;
        if (c2 != other.c2)
            return false;
        if (hand != other.hand)
            return false;
        if (t != other.t)
            return false;
        if (turns != other.turns)
            return false;
        return true;
    }
}
