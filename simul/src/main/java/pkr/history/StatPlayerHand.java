package pkr.history;

public class StatPlayerHand {
    boolean foldedPreflop;
    
    boolean voluntarilyPutMoneyInPot;
    
    boolean foldedTurn;
    
    boolean foldedRiver;

    @Override
    public String toString()
    {
        return "StatPlayerHand [foldedPreflop=" + foldedPreflop + ", voluntarilyPutMoneyInPot=" + voluntarilyPutMoneyInPot + ", foldedTurn=" + foldedTurn
                + ", foldedRiver=" + foldedRiver + "]";
    }
}
