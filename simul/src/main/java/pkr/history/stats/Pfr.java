package pkr.history.stats;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.FlopTurnRiverState;
import pkr.history.HandInfo;
import pkr.history.Statistics;
import pkr.history.iPlayerStatistic;

public class Pfr implements iPlayerStatistic
{

    private static Logger log = LoggerFactory.getLogger(Statistics.class);
    
    int unraisedToPlayer;
    int raisedPreflop;
    
    double avgAmt;
    int nonAllInRaisePreflop;
    
    int nTapis;
    
    private String preFlopPlayer;
    
    
    public Pfr(String playerName) {
        super();
        this.preFlopPlayer = playerName;
    }

    
    @Override
    public String getId() {
        return "pfr";
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Pfr : ");
        sb.append(Statistics.formatPercent(raisedPreflop, unraisedToPlayer));
        sb.append(" (");
        sb.append(raisedPreflop);
        sb.append("/");
        sb.append(unraisedToPlayer);
        sb.append(") Avg amt : ");
        sb.append(Statistics.formatMoney(avgAmt, nonAllInRaisePreflop));
        sb.append(" Tapis : ");
        sb.append(nTapis);
        return sb.toString();
    }

    @Override
    public void calculate(HandInfo handInfo) {
        
        FlopTurnRiverState[] ftrStates = handInfo.roundStates;
        
        final boolean isPreFlopRaiser = StringUtils.equals(ftrStates[0].roundInitialBetter, preFlopPlayer);
        
        final boolean playerAllin = ftrStates[0].allInMinimum.containsKey(preFlopPlayer);
        
        int playerPosition = ftrStates[0].players.indexOf(preFlopPlayer);
        
        int raiserPosition = ftrStates[0].roundInitialBetter != null ? ftrStates[0].players.indexOf( ftrStates[0].roundInitialBetter ) : -1; 
        
        if (isPreFlopRaiser || raiserPosition > playerPosition || raiserPosition == -1 )
        {
            log.debug("Player {} could have preflop raised");
            ++unraisedToPlayer;
        }
                
        int playerBet = ftrStates[0].getCurrentBet(preFlopPlayer);
        
        if (isPreFlopRaiser)
        {
            ++raisedPreflop;
            
            if (!playerAllin)
            {
                ++nonAllInRaisePreflop;
                avgAmt += playerBet;
            } else {
                nTapis++;
            }
        }
        
    }
}