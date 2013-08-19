package pkr.history;

public class StatsSessionPlayer {
    public int totalHands;
    
    //not counting big blind, non raised
    public int vpipNumerator;
    public int vpipDenom;
    
    //Calling a pre flop raise
    public int callOpenNumerator;
    //In a preflop raise situation (not the initial raiser)
    public int callOpenDenom;
    
    //excludes tapis
    public int preFlopRaises;
    public double preFlopRaiseTotalAmt;
    
    public int preFlopTapis;
    
    
    public static class RoundStats
    {
        //# of times seen flop/turn/river
        public int seen;
        
        public int checkedThrough;
        //Quand le jouer n'a pas misé en premier
        public int openedBySomeoneElse;
        
        //Quand personne à relancé au tour du joeur
        public int unopened;
        
        public int checksUnopened;
        public int bets;
        public int callReraise;
        public int betAllIn;
        public int reRaiseUnopened;
      //Folded to a reraise
        public int betFold;
        public int checkFold;
        
        //When pot is opened by someone else
        public int checksOpened;
        public int checkRaises;
        public int reRaiseOpened;
        //Calling in the round max 1, just calling big blind counts
        public int calls;
        public int folded;
        public int raiseCallAllIn;
        
        
        
        public double avgBetToPot;
        public double avgFoldToBetToPot;
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
