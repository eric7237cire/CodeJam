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

public class ThreeBet implements iPlayerStatistic
{

    private static Logger log = LoggerFactory.getLogger(ThreeBet.class);
    
    //Opted to 3 bet
    int thBetNum;
    int nonAllInThBet;
    //Incoming pre flop raise
    int thBetDenom;
    
    public StringBuffer actionsDesc;
    
    double avgAmt;
    
    int thBetCallNum;
    int thBetFoldNum;
    int wasThBetCount;
    
    private String playerName;
    
    
    public ThreeBet(String playerName) {
        super();
        this.playerName = playerName;
        
        actionsDesc = new StringBuffer();
    }

    
    
    @Override
    public String getId() {
        return "3bet";
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("3bet : ");
        sb.append(Statistics.formatPercent(thBetNum, thBetDenom, true));        
        sb.append(" Avg amt : ");
        sb.append(Statistics.formatMoney(avgAmt, nonAllInThBet));
        sb.append(" Call : ");
        sb.append( Statistics.formatPercent(thBetCallNum, wasThBetCount, true));
        return sb.toString();
    }

    

    @Override
    public void calculate(HandInfo handInfo) {
        
        FlopTurnRiverState[] ftrStates = handInfo.roundStates;
        if (ftrStates[0] == null) {
            return;
        }
        if (ftrStates[0].players.size() < 2)
        {
            return;
        }
        
        int playerPosition = ftrStates[0].players.indexOf(playerName);
        
        if (ftrStates[0].players.size() < playerPosition)
        {
            log.warn("Problem 3 bet");
            return;
        }
        
        if (ftrStates[0].playerPosToActions.size() != ftrStates[0].players.size())
        {
            log.warn("Problem 3 bet");
        }
        
        List<Integer> actionIdx = ftrStates[0].playerPosToActions.get(playerPosition);
        
        final String link = DonkContLimped.buildLink(handInfo, playerName);
        
        for(int i = 0; i < actionIdx.size(); ++i)
        {
            //int startActionIndex = i == 0 ? 0 : actionIdx.get(i-1) ;
            int endActionIndex = actionIdx.get(i);
            
            PlayerAction currentAction = ftrStates[0].actions.get(endActionIndex);
            int globalRaiseCount = currentAction.globalRaiseCount;
            
            
            PlayerAction prevAction = i==0 ? null : ftrStates[0].actions.get(actionIdx.get(i-1));
            
            log.debug("Player {} action {}", playerName, endActionIndex);
            
            if (globalRaiseCount == 1)
            {
                log.debug("Player {} could have 3 bet", playerName);
                ++thBetDenom;
            }
            
            if (globalRaiseCount == 1 && (currentAction.action == Action.RAISE ||
                    currentAction.action == Action.ALL_IN ||
                    currentAction.action == Action.RAISE_ALL_IN
                    ) )
            {
                log.debug("Player {} has 3 bet.  raises : {} p action idx : {}", playerName, globalRaiseCount, i);
                ++thBetNum;
                
                DonkContLimped.buildMessage(actionsDesc,
                		currentAction,
                		" 3 bet to ",
                		currentAction.amountRaised,
                		link);
                
                if (currentAction.action == Action.RAISE) {
                    ++nonAllInThBet;
                    avgAmt += currentAction.amountRaised;
                }
            }
            
            if (globalRaiseCount == 2 && (prevAction == null || prevAction.action != Action.RAISE))
            {
                log.debug("Player {} can cold call a 3 bet", playerName);
                ++wasThBetCount;
                
                if (currentAction.action == Action.FOLD)
                {
                    log.debug("Player {} folded a 3 bet", playerName);
                    ++thBetFoldNum;
                } else {
                    ++thBetCallNum;
                }
            }
            
            if (globalRaiseCount == 2 && (prevAction != null && prevAction.action == Action.RAISE))
            {
                log.debug("Player {} can call a 3 bet.  raises: {} p action idx : {}", playerName, globalRaiseCount, i);
                ++wasThBetCount;
                
                if (currentAction.action == Action.FOLD)
                {
                    log.debug("Player {} folded their pfr to a  3 bet", playerName);
                    ++thBetFoldNum;
                } else {
                    ++thBetCallNum;
                    
                    DonkContLimped.buildMessage(actionsDesc,
                    		currentAction,
                    		" called a 3 bet of ",
                    		currentAction.incomingBetOrRaise,
                    		link);
                }
            }
            
            if (globalRaiseCount >= 2)
                break;
        }
        
       
                
        
    }



	public String getActionsDesc() {
		return StringEscapeUtils.escapeXml(actionsDesc.toString());
	}

}
