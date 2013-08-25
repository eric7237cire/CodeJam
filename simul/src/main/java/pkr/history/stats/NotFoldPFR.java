package pkr.history.stats;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.FlopTurnRiverState;
import pkr.history.HandInfo;
import pkr.history.PlayerAction;
import pkr.history.PlayerAction.Action;
import pkr.history.Statistics;
import pkr.history.iPlayerStatistic;

public class NotFoldPFR implements iPlayerStatistic
{

    private static Logger log = LoggerFactory.getLogger(Statistics.class);
    
    int raisedToPlayer;
    int calledARaisedPreflop;
    
    //TODO avg fold 2 and call amt?
    double avgAmt;
    int nonAllInRaisePreflop;
    
    int nTapis;
    
    private String preFlopPlayer;
    
    
    public NotFoldPFR(String playerName) {
        super();
        this.preFlopPlayer = playerName;
    }
    
    @Override
    public String getId() {
        return "notfpfr";
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
    public void calculate(HandInfo handInfo) {
        
        FlopTurnRiverState[] ftrStates = handInfo.roundStates;
        //final boolean isPreFlopRaiser = StringUtils.equals(ftrStates[0].roundInitialBetter, preFlopPlayer);
        
        //final boolean playerAllin = ftrStates[0].allInBet.containsKey(preFlopPlayer);
        
        if (ftrStates[0] == null) {
            return;
        }
        
        int playerPosition = ftrStates[0].players.indexOf(preFlopPlayer);
        
        //int raiserPosition = ftrStates[0].roundInitialBetter != null ? ftrStates[0].players.indexOf( ftrStates[0].roundInitialBetter ) : -1; 
        
        if (ftrStates[0].players.size() < 2)
        {
            return;
        }
        
        if (ftrStates[0].players.size() < playerPosition)
        {
            log.warn("Problem");
            return;
        }
        List<Integer> actionIdx = ftrStates[0].playerPosToActions.get(playerPosition);
        
        
        
        for(int i = 0; i < actionIdx.size(); ++i)
        {
            PlayerAction currentAction = ftrStates[0].actions.get(actionIdx.get(i));
            
            
            if (currentAction.globalRaiseCount > 0 && currentAction.action != Action.FOLD)
            {
                log.debug("Player {} called a raise preflop", preFlopPlayer);
                raisedToPlayer++;
                calledARaisedPreflop++;
                return;
            }
            
            if (currentAction.globalRaiseCount > 0 && currentAction.action == Action.FOLD)
            {
                raisedToPlayer++;
                log.debug("Folded to a preflop raise {}", preFlopPlayer);
                return;
            }
        
           /*     
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
        }*/
        }
        
    }
}