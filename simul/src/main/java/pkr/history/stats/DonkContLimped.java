package pkr.history.stats;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import pkr.history.FlopTurnRiverState;
import pkr.history.PlayerAction;
import pkr.history.Statistics;
import pkr.history.iPlayerStatistic;
import pkr.history.PlayerAction.Action;

public class DonkContLimped implements iPlayerStatistic
{

    private static Logger log = LoggerFactory.getLogger(Statistics.class);
    
    int donkBet;
    int canDonkBet;
    
    int donkCall;
    int canDonkCall;
    
    int donkRaise;
    int canDonkRaise;
    
    int donkFold;
    int canDonkFold;
    
    int contBet;
    int canContBet;
    
    int contCall;
    int canContCall;
    
    int contRaise;
    int canContRaise;
    
    //Was the prev round aggressor and folded a donk bet
    int contFold;
    int canContFold;
    
    int limpBet;
    int canLimpBet;
    
    int limpCall;
    int canLimpCall;
    
    int limpRaise;
    int canLimpRaise;
    
    int limpFold;
    int canLimpFold;
    
    private String playerName;
    private int round;
    
    public DonkContLimped(String playerName, int round) {
        super();
        this.playerName = playerName;
        this.round = round;
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
        
        sb.append("Limp bet ");
        sb.append(Statistics.formatPercent(limpBet, canDonkBet, true));
        sb.append(" call ");
        sb.append(Statistics.formatPercent(limpCall, canDonkCall, true));
        sb.append(" raise ");
        sb.append(Statistics.formatPercent(limpRaise, canDonkRaise, true));
        sb.append(" fold ");
        sb.append(Statistics.formatPercent(limpFold, canDonkFold, true));
        
        sb.append("\n");
        
        sb.append("Cont bet ");
        sb.append(Statistics.formatPercent(contBet, canDonkBet, true));
        sb.append(" call ");
        sb.append(Statistics.formatPercent(contCall, canDonkCall, true));
        sb.append(" raise ");
        sb.append(Statistics.formatPercent(contRaise, canDonkRaise, true));
        sb.append(" fold ");
        sb.append(Statistics.formatPercent(contFold, canDonkFold, true));
        
        sb.append("\n");
        
        sb.append("Donk bet ");
        sb.append(Statistics.formatPercent(donkBet, canDonkBet, true));
        sb.append(" call ");
        sb.append(Statistics.formatPercent(donkCall, canDonkCall, true));
        sb.append(" raise ");
        sb.append(Statistics.formatPercent(donkRaise, canDonkRaise, true));
        sb.append(" fold ");
        sb.append(Statistics.formatPercent(donkFold, canDonkFold, true));
        
        
        
        return sb.toString();
    }

    

    @Override
    public void calculate(FlopTurnRiverState[] ftrStates) {
        
        FlopTurnRiverState ftr = ftrStates[round];
        
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
        
        List<Integer> actionIdx = ftr.playerPosToActions.get(playerPos);
        
        final boolean estLimped = ftr.agresseur == null;
        final boolean estAgresseur = !estLimped ?  ftr.agresseur.equals(playerName) : false;
        
        for(int i = 0; i < actionIdx.size(); ++i)
        {
            PlayerAction currentAction = ftr.actions.get(actionIdx.get(i));
                        
            PlayerAction prevAction = i==0 ? null : ftr.actions.get(actionIdx.get(i-1));
            
            log.debug("Player {} action {}", playerName, actionIdx.get(i));
            
            if (currentAction.globalRaiseCount == 0)
            {
                if (estLimped)
                {
                    log.debug("Player {} can limp bet", playerName);
                    ++canLimpBet;
                    
                    if (currentAction.action == Action.RAISE)
                    {
                        log.debug("Player {} did limp bet", playerName);
                        ++limpBet;
                    }
                } else if (estAgresseur)
                {
                    log.debug("Player {} can continuation bet", playerName);
                    ++canContBet;
                    
                    if (currentAction.action == Action.RAISE)
                    {
                        log.debug("Player {} did continuation bet", playerName);
                        ++contBet;
                    }
                } else {
                    log.debug("Player {} can donk bet", playerName);
                    ++canDonkBet;
                    
                    if (currentAction.action == Action.RAISE)
                    {
                        log.debug("Player {} did donk bet", playerName);
                        ++donkBet;
                    }
                }
            }
        
            else if (currentAction.globalRaiseCount == 1)
            {
                if (estLimped)
                {
                    //TODO all in?
                    log.debug("Player {} can limp call / fold / raise", playerName);
                    ++canLimpCall;
                    ++canLimpFold;
                    ++canLimpRaise;
                    
                    if (currentAction.action == Action.CALL)
                    {
                        log.debug("Player {} did limp bet", playerName);
                        ++limpCall;
                    } else if (currentAction.action == Action.FOLD) 
                    {
                        ++limpFold;
                    } else if (currentAction.action == Action.RAISE)
                    {
                        ++limpRaise;
                    }
                } else if (estAgresseur)
                {
                    log.debug("Player {} can cont call / fold / raise", playerName);
                    ++canContCall;
                    ++canContFold;
                    ++canContRaise;
                    
                    if (currentAction.action == Action.CALL)
                    {
                        log.debug("Player {} did cont bet", playerName);
                        ++contCall;
                    } else if (currentAction.action == Action.FOLD) 
                    {
                        ++contFold;
                    } else if (currentAction.action == Action.RAISE)
                    {
                        ++contRaise;
                    }
                } else {
                    log.debug("Player {} can donk call / fold / raise", playerName);
                    ++canDonkCall;
                    ++canDonkFold;
                    ++canDonkRaise;
                    
                    if (currentAction.action == Action.CALL)
                    {
                        log.debug("Player {} did donk bet", playerName);
                        ++donkCall;
                    } else if (currentAction.action == Action.FOLD) 
                    {
                        ++donkFold;
                    } else if (currentAction.action == Action.RAISE)
                    {
                        ++donkRaise;
                    }
                }
            } else {
                break;
            }
            
        }
        
       
                
        
    }

}

