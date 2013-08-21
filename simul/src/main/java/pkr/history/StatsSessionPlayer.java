package pkr.history;

import java.util.Map;

import pkr.history.stats.ColdCall;
import pkr.history.stats.Pfr;
import pkr.history.stats.Vpip;

import com.google.common.collect.Maps;


//class stat
//round[0].isInitial + 
public class StatsSessionPlayer {
    
    public Map<String, iPlayerStatistic> stats;
    public int totalHands;
    
    //not counting big blind, non raised
    public int vpipNumerator;
    public int vpipDenom;
    
    //Suivre ou relance une relancement avant le flop
    public int notFoldRaisedPreflop;
    //In a preflop raise situation (not the initial raiser)
    public int raisedPreflopDenom;
    
    //Suivre ou relance une relancement avant le flop
    public int notFoldReraisedPreflop;
    public int reraisedPreflopDenom;
    
    //excludes tapis
    public int preFlopRaises;
    public double preFlopRaiseTotalAmt;    
    public int preFlopTapis;
    public int preFlopReraise;
    
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
    
    private String playerName;

    public StatsSessionPlayer(String pplayerName) {
        super();
        flopStats = new RoundStats();
        turnStats = new RoundStats();
        riverStats = new RoundStats();
        
        this.playerName = pplayerName;
        stats = Maps.newHashMap();
        
        iPlayerStatistic s = new Vpip(playerName);
        stats.put(s.getId(), s);
        
        s = new Pfr(playerName);
        stats.put(s.getId(), s);
        
        s = new ColdCall(playerName);
        stats.put(s.getId(), s);
        
        roundStats = new RoundStats[] { flopStats, turnStats, riverStats };
    }
    
    public String getStatValue(String statId)
    {
        return stats.get(statId).toString();
    }
    
    
    
}
