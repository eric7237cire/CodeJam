package pkr.history.stats;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.FlopTurnRiverState;
import pkr.history.Statistics;
import pkr.history.iPlayerStatistic;

public class ColdCall implements iPlayerStatistic
{

    private static Logger log = LoggerFactory.getLogger(Statistics.class);
    
    int raisedToPlayer;
    int calledARaisedPreflop;
    
    //TODO avg fold 2 and call amt?
    double avgAmt;
    int nonAllInRaisePreflop;
    
    int nTapis;
    
    private String preFlopPlayer;
    
    
    public ColdCall(String playerName) {
        super();
        this.preFlopPlayer = playerName;
    }

    static Vpip create(String playerName, int round)
    {
        return new Vpip(playerName);
    }
    
    @Override
    public String getId() {
        return "coldcall";
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Not f in Raised pf : ");
        sb.append(Statistics.formatPercent(calledARaisedPreflop, raisedToPlayer));
        sb.append(" (");
        sb.append(calledARaisedPreflop);
        sb.append("/");
        sb.append(raisedToPlayer);
        sb.append(") ");
        
        //Tapis : ");
        //sb.append(nTapis);
        return sb.toString();
    }

    @Override
    public void calculate(FlopTurnRiverState[] ftrStates) {
        
        final boolean isPreFlopRaiser = StringUtils.equals(ftrStates[0].roundInitialBetter, preFlopPlayer);
        
        final boolean playerAllin = ftrStates[0].allInBet.containsKey(preFlopPlayer);
        
        int playerPosition = ftrStates[0].players.indexOf(preFlopPlayer);
        
        int raiserPosition = ftrStates[0].roundInitialBetter != null ? ftrStates[0].players.indexOf( ftrStates[0].roundInitialBetter ) : -1; 
        
        
        
        if (!isPreFlopRaiser &&
                ftrStates[0].roundInitialBetter != null &&
                BooleanUtils.isNotTrue(ftrStates[0].hasFolded.get(preFlopPlayer))
                ) 
        {
            log.debug("Player {} called an initial raise", preFlopPlayer);
            raisedToPlayer++;
            calledARaisedPreflop++;
        } else if (BooleanUtils.isTrue(ftrStates[0].foldedToBetOrRaise.get(preFlopPlayer))) {
            raisedToPlayer++;
            log.debug("Folded to a preflop raise {}", preFlopPlayer);
        }
        
                
        int playerBet = ftrStates[0].getCurrentBet(preFlopPlayer);
        
        if (isPreFlopRaiser)
        {            
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