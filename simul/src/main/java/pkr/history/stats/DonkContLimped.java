package pkr.history.stats;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.FlopTurnRiverState;
import pkr.history.HandInfo;
import pkr.history.PlayerAction;
import pkr.history.PlayerAction.Action;
import pkr.history.Statistics;
import pkr.history.iPlayerStatistic;

import com.google.common.base.Preconditions;

public class DonkContLimped implements iPlayerStatistic
{

    private static Logger log = LoggerFactory.getLogger(Statistics.class);
    
    //actions[ condition -- limped/aggres/not aggres ] [ call/bet/raise/fold ]
    public int[][] actions;
    public int[][] actionPossible;
    public StringBuffer[][] actionsDesc;
    
    //Categories
    public static final int LIMPED = 0;
    public static final int IS_AGGRES = 1;
    public static final int NOT_AGGRES = 2;
    
    //Calling a bet, not a raise or reraise
    public static final int CALL = 0;
    public static final int BET = 1;
    //Just folding to a bet
    public static final int FOLD = 2;
    
    //Any all in
    public static final int ALL_IN = 3;
    //2nd raise
    public static final int RAISE = 4;
    //Any re-raise ( 3rd or + raises)
    public static final int RERAISE = 5;
    public static final int CHECK_RAISE = 6;
    
    
    //Folding a reraise after betting / raising
    public static final int FOLD_RAISE = 7;
        
    public int[] count ;
    
    private String playerName;
    private int round;
    
    public DonkContLimped(String playerName, int round) {
        super();
        this.playerName = playerName;
        this.round = round;
        this.actions = new int[3][9];
        this.actionPossible = new int[3][9];
        this.actionsDesc = new StringBuffer[3][9];
        for(int i = 0; i < 3; ++i)
        {
            for(int j = 0; j < 9; ++j)
            {
                this.actionsDesc[i][j] = new StringBuffer();
            }
        }
        this.count = new int[] {0,0,0};
        //No preflop
        Preconditions.checkArgument(round >= 1 && round <= 3);
    }

    
    @Override
    public String getId() {
        return "dcl";
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        
        
        for(int i = 0; i < 3; ++i)
        {
            switch(i){
            case 0:
                sb.append("Limp");
                break;
            case 1:
                sb.append("Is Agg.");
                break;
            case 2:
                sb.append("Not Agg.");
                break;
            }
            
            sb.append("(");
            sb.append(count[i]);
            sb.append(")");
            
            sb.append(" [B ");
            sb.append(Statistics.formatPercent(actions[i][BET], actionPossible[i][BET], true));
            sb.append(" | R ");
            sb.append(Statistics.formatPercent(actions[i][RAISE], actionPossible[i][RAISE], true));
            
            sb.append(" | Call B ");
            sb.append(Statistics.formatPercent(actions[i][CALL], actionPossible[i][CALL], true));
            
            sb.append(" | Fold B ");
            sb.append(Statistics.formatPercent(actions[i][FOLD], actionPossible[i][FOLD], true));
            sb.append(" | Fold R ");
            sb.append(Statistics.formatPercent(actions[i][FOLD_RAISE], actionPossible[i][FOLD_RAISE], true));
            
            sb.append(" | RR ");
            sb.append(Statistics.formatPercent(actions[i][RERAISE], actionPossible[i][RERAISE], true));
            sb.append(" | C/R ");
            sb.append(Statistics.formatPercent(actions[i][CHECK_RAISE], actionPossible[i][CHECK_RAISE], true));
            
            sb.append(" | allin ");
            sb.append(Statistics.formatPercent(actions[i][ALL_IN], actionPossible[i][ALL_IN], true));
            
            sb.append("]");
            if (i < 2)
            sb.append("\n");
        }
        
        
        
        return sb.toString();
    }

    public static String buildLink(HandInfo handInfo, String playerName)
    {
        StringBuffer sb = new StringBuffer();
       
        sb
        .append("  <a href=\"handsLog.xhtml#hand_" )
        .append(handInfo.handIndex)
        .append("\"  target=\"_blank\" >")
        .append("Hand ")
        .append(handInfo.handIndex)
        .append(" line ")
        .append(handInfo.startingLine)
        .append("</a> ");
        
        sb
        //.append(" winner ")
        //.append(handInfo.winnerPlayerName)  
        //.append(" round ")
        //.append( Statistics.roundToStr(handInfo.winRound))
        .append(" ");
        
        if (handInfo.winnerPlayerName[0].equals(playerName) ||
        		StringUtils.equals(handInfo.winnerPlayerName[1], playerName))
        {
        	sb.append("<span style=\"color:green\">");
        } else {
        	sb.append("<span style=\"color:red\">");
        }
        sb.append( handInfo.winDesc );
        sb.append("</span>");
        sb.append( "<br/>");
        
        return (sb.toString());
        //return StringEscapeUtils.escapeXml(sb.toString());
    }
    
    public static String lineEnd = "<br/>";
    
    private void calculateCheckRaise(int type, 
            PlayerAction prevAction, PlayerAction currentAction, String link) 
    {
        if (prevAction != null &&
                prevAction.action == Action.CHECK)
        {
            ++actionPossible[type][CHECK_RAISE];
        } else {
            return;
        }
        
        if (currentAction.globalRaiseCount >= 1 && (currentAction.action == Action.RAISE || currentAction.action == Action.RAISE_ALL_IN))
        {
            log.debug("Player {} did check raise. type {}", playerName, type);
            
            ++actions[type][CHECK_RAISE];
            
            StringBuffer actionText = new StringBuffer();
            
            actionText.append(" check raised ")
            .append(Statistics.formatMoney(currentAction.incomingBetOrRaise))
            .append(" to ");
            
            buildMessage(
					actionsDesc[type][CHECK_RAISE],
					currentAction,
					actionText.toString() ,
					currentAction.amountRaised,
					link);
            
        }
        
    }
    
    private void calculateAllIn(int type, 
            PlayerAction prevAction, PlayerAction currentAction, String link)
    {
        if (currentAction.action == Action.CALL_ALL_IN
                || currentAction.action == Action.RAISE_ALL_IN
                || currentAction.action == Action.ALL_IN
                )
        {
            ++actions[type][ALL_IN];
        }
    }
    
    private static void addAmount(StringBuffer sb, PlayerAction action, int amt)
    {
    	if (action.action == Action.RAISE_ALL_IN) {
        	sb.append(" (allin) " );
        }
        
        if (amt >= 0) {
        	sb.append(Statistics.formatMoney(
        			amt));
        } else {
        	sb.append("Unknown amt");
        }
    }
    
    public static void buildMessage(StringBuffer sb, 
    		PlayerAction action,
    		String actionText,
    		int moneyAmount,
    		String link)
    {
    	sb.append("Player " )
    	.append(action.playerName)
    	.append(actionText);
    	
    	addAmount(sb, action, moneyAmount);
    	//.append(Statistics.f;ormatMoney(moneyAmount))
    	sb.append(" into pot ")
		.append(Statistics.formatMoney(action.pot))
		.append( " with ")
		.append(action.playersLeft)
		.append(" players ")
		.append(lineEnd)
    	.append(link)
    	.append(lineEnd);
    }

    @Override
    public void calculate(HandInfo handInfo) {
        
        FlopTurnRiverState[] ftrStates = handInfo.roundStates;
        FlopTurnRiverState ftr = ftrStates[round];
        
        //Vérification que la tournée est en bonne état
        if (ftr == null)
            return;
        
        int playerPos = ftr.players.indexOf(playerName);
        
        if (playerPos == -1)
        {
            log.debug("Player {} not in round {}", playerName, round);
            return;
        }
        Preconditions.checkState(playerPos >= 0, playerName);
        
        if (ftr.players.size() < 2)
        {
            return;
        }
        /////////////////////////////////
        
        List<Integer> actionIdx = ftr.playerPosToActions.get(playerPos);
        
        final boolean estLimped = ftr.prevTourneeAgresseur == null;
        final boolean estAgresseur = !estLimped ?  ftr.prevTourneeAgresseur.equals(playerName) : false;
        
        final int type = estLimped ? LIMPED : (estAgresseur ? IS_AGGRES : NOT_AGGRES);
        
        final String link =   buildLink(handInfo, playerName);
                
        
        ++actionPossible[type][ALL_IN];
        ++count[type];
        //boolean didGoAllIn = false;
        
        log.debug("DonkContLimt START limped? {} agresseur {} player {} round {}",estLimped
                , estAgresseur, playerName,Statistics.roundToStr(round));
        
        for(int i = 0; i < actionIdx.size(); ++i)
        {
            PlayerAction currentAction = ftr.actions.get(actionIdx.get(i));
                        
            PlayerAction prevAction = i==0 ? null : ftr.actions.get(actionIdx.get(i-1));
            
            log.debug("Player {} action {} raise count {}", playerName, actionIdx.get(i), currentAction.globalRaiseCount);
            
            calculateCheckRaise(type, prevAction, currentAction, link);
            
            calculateAllIn(type, prevAction, currentAction, link);
            
            if (currentAction.globalRaiseCount == 0)
            {
                
                log.debug("Player {} can bet.  type {}", playerName, type);
                ++actionPossible[type][BET];
                
                
                if (currentAction.action == Action.RAISE || currentAction.action == Action.RAISE_ALL_IN)
                {
                    log.debug("Player {} did bet. type {}", playerName, type);
                    ++actions[type][BET];
                    
                    buildMessage(
							actionsDesc[type][BET],
							currentAction,
							" bet " ,
							currentAction.amountRaised,
							link);
                    
                    
                } 
               
            }        
            else if (currentAction.globalRaiseCount == 1)
            {
                
                log.debug("Player {} can limp call / fold / raise.  type {}", playerName, type);
                ++actionPossible[type][CALL];
                ++actionPossible[type][FOLD];
                ++actionPossible[type][RAISE];
               
                
                if (currentAction.action == Action.CALL || currentAction.action == Action.CALL_ALL_IN)
                {
                    log.debug("Player {} did call. type {}", playerName, type);
                    ++actions[type][CALL];
                    
                    
                    buildMessage(
							actionsDesc[type][CALL],
							currentAction,
							" call " ,
							currentAction.incomingBetOrRaise,
							link);
                    
                } else if (currentAction.action == Action.FOLD) 
                {
                    log.debug("Player {} did fold. type {}", playerName, type);
                    
                    ++actions[type][FOLD];
                    
                    buildMessage(
							actionsDesc[type][FOLD],
							currentAction,
							" folded to bet of " ,
							currentAction.incomingBetOrRaise,
							link);
                    
                } else if (currentAction.action == Action.RAISE || currentAction.action == Action.RAISE_ALL_IN)
                {
                    log.debug("Player {} did raise. type {}", playerName, type);
                    
                    ++actions[type][RAISE];
                    
					buildMessage(
							actionsDesc[type][RAISE],
							currentAction,
							" raise a bet of " + 
							(Statistics.formatMoney(currentAction.incomingBetOrRaise)) +
							" to ",
							currentAction.amountRaised,
							link);
                    		
                    
                }             
            
            } else if (currentAction.globalRaiseCount >= 2) {
                
                boolean foldToRaise = false;
                if (currentAction.globalRaiseCount == 2 &&
                        prevAction != null &&
                        prevAction.action == Action.RAISE)
                {
                    log.debug("Player {} was check raised", playerName);
                    ++actionPossible[type][FOLD_RAISE];
                    
                    if (currentAction.action == Action.FOLD)
                    {
                        foldToRaise = true;
                        ++actions[type][FOLD_RAISE];
                    }
                } else {
                    log.debug("Player {} facing a raise", playerName);
                    ++actionPossible[type][FOLD_RAISE];
                    
                    if (currentAction.action == Action.FOLD)
                    {
                        ++actions[type][FOLD_RAISE];
                        foldToRaise = true;
                        
                    }
                }
                
                //if (foldToRaise)
                {
                	StringBuffer actionText = new StringBuffer();
                    
                	if (!foldToRaise) {
                		actionText.append(" did not");
                	}
                    if (currentAction.incomingBetOrRaise == currentAction.playerAmtPutInPotThisRound + 1)
                    {
                    	actionText.append(" fold to an all in");
                    } else {
                    	actionText.append(" fold to a raise of ")
                        .append(Statistics.formatMoney(currentAction.incomingBetOrRaise));
                    }
                    
                    actionText
                            .append(" having put in ");
                    
                    buildMessage(
							actionsDesc[type][FOLD_RAISE],
							currentAction,
							actionText.toString() ,
							currentAction.playerAmtPutInPotThisRound,
							link);
                }
                
                if (currentAction.playersLeft > 1)
                {
                    log.debug("Player {} fac can re-raise.  type {}", playerName, type);
                    
                    ++actionPossible[type][RERAISE];
                    
                    if (currentAction.action == Action.RAISE || currentAction.action == Action.RAISE_ALL_IN)
                    {
                        log.debug("Player {} did re-raise. type {}", playerName, type);
                        
                        ++actions[type][RERAISE];
                    
                        StringBuffer actionText = new StringBuffer();
                        
                        actionText		.append(" reraise a raise of ")
                        		.append(Statistics.formatMoney(currentAction.incomingBetOrRaise))
                        		.append(" to ");
                        
                        buildMessage(
    							actionsDesc[type][RERAISE],
    							currentAction,
    							actionText.toString() ,
    							currentAction.amountRaised,
    							link);
                        
                    }
                }
            }
            
        }
        
             
       
                
        
    }


    /**
     * @return the actions
     */
    public String getActionStr(int category, int action) {
        return Statistics.formatPercent( actions[category][action], actionPossible[category][action], true);
    }
    
    public String getActionStr(int action) {
        return Statistics.formatPercent( actions[LIMPED][action]+actions[NOT_AGGRES][action]
                +actions[IS_AGGRES][action], 
                actionPossible[LIMPED][action]+actionPossible[NOT_AGGRES][action]
                        +actionPossible[IS_AGGRES][action], true);
    }

    public String getActionDesc(int a, int b) {
        //
       // return StringEscapeUtils.escapeEcmaScript(actionsDesc[a][b].toString());
        return StringEscapeUtils.escapeXml(actionsDesc[a][b].toString());
       // return (actionsDesc[a][b].toString());
    }


    public int getCount(int type)
    {
        return count[type];
    }


    public String getTypeStr(int type)
    {
        switch(type)
        {
    case 0:
        return("Limp");
    case 1:
        return("Is Agg.");
    case 2:
        return("Not Agg.");
    }
        return "";
    }
    

}

