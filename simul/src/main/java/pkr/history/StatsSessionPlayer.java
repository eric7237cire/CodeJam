package pkr.history;

public class StatsSessionPlayer {
    public int totalHands;
    
    //not counting big blind, non raised
    public int vpipNumerator;
    public int vpipDenom;
    
    //Calling a pre flop raise
    public int callOpenNumerator;
    public int callOpenDenom;
    
    public int preFlopRaises;
    public int preFlopTapis;
    
    
    public static class RoundStats
    {
        //# of times seen flop/turn/river
        public int seen;
        
        public int checkedThrough;
        
        public int checkRaises;
        public int bets;
        
        public int reraises;
        public int allIn;
        
        //Calling in the round max 1, just calling big blind counts
        public int calls;
        public int folded;
        public int checks;
    }
    
    public RoundStats flopStats;
    
    public RoundStats turnStats;
    
    public RoundStats riverStats;
    
    public RoundStats[] roundStats;

    public StatsSessionPlayer() {
        super();
        flopStats = new RoundStats();
        turnStats = new RoundStats();
        riverStats = new RoundStats();
        
        roundStats = new RoundStats[] { flopStats, turnStats, riverStats };
    }
    
    
    
}
