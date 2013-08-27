package pkr.history.stats;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
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
    
    int[] posRaisedToPlayer;
    int[] posNotFoldARaise;
    
    
    private String preFlopPlayer;
    
    private StringBuffer[] actionsDesc;
    
    public NotFoldPFR(String playerName) {
        super();
        this.preFlopPlayer = playerName;
        
        actionsDesc = new StringBuffer[FlopTurnRiverState.MAX_PLAYERS];
        posNotFoldARaise = new int[FlopTurnRiverState.MAX_PLAYERS];
        posRaisedToPlayer = new int[FlopTurnRiverState.MAX_PLAYERS];
        for(int i = 0; i < actionsDesc.length; ++i)
        {
            actionsDesc[i] = new StringBuffer();
        }
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
        
        return sb.toString();
    }
    
    /**
     * @return the actionsDesc
     */
    public String getActionsDesc(int posIndex) {
        return StringEscapeUtils.escapeXml(actionsDesc[posIndex].toString());
    }
    
    public String getPercentage(int posIndex) {
        return Statistics.formatPercent(posNotFoldARaise[posIndex], posRaisedToPlayer[posIndex], true);
    }
    

    @Override
    public void calculate(HandInfo handInfo) {
        
        FlopTurnRiverState[] ftrStates = handInfo.roundStates;
       
        if (ftrStates[0] == null) {
            return;
        }
        
        int playerPosition = ftrStates[0].players.indexOf(preFlopPlayer);
        
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
        
        String link = DonkContLimped.buildLink(handInfo);
        int posIndex = Vpip.getPositionIndex(ftrStates[0].players.size(), playerPosition);
        
        for(int i = 0; i < actionIdx.size(); ++i)
        {
            PlayerAction currentAction = ftrStates[0].actions.get(actionIdx.get(i));
            
            
            if (currentAction.globalRaiseCount > 0 && currentAction.action != Action.FOLD)
            {
                log.debug("Player {} did not fold a preflop raise", preFlopPlayer);
                actionsDesc[posIndex].append("Player did not fold a preflop raise of ")
                .append(currentAction.incomingBetOrRaise)
                .append(link)
                .append(DonkContLimped.lineEnd);
                
                raisedToPlayer++;
                calledARaisedPreflop++;
                posRaisedToPlayer[posIndex]++;
                posNotFoldARaise[posIndex]++;
                return;
            }
            
            if (currentAction.globalRaiseCount > 0 && currentAction.action == Action.FOLD)
            {
                raisedToPlayer++;
                actionsDesc[posIndex].append("Player folded a preflop raise of ")
                .append(currentAction.incomingBetOrRaise)
                .append(link)
                .append(DonkContLimped.lineEnd);
                
                posRaisedToPlayer[posIndex]++;
                                
                log.debug("Folded to a preflop raise {}", preFlopPlayer);
                return;
            }
        
           
        }
        
    }

    
}