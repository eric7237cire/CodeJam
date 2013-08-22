package pkr.history.stats;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.FlopTurnRiverState;
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
    
    public static final int LIMPED = 0;
    public static final int IS_AGGRES = 1;
    public static final int NOT_AGGRES = 2;
    
    public static final int CALL = 0;
    public static final int BET = 1;
    public static final int FOLD = 2;
    //Excludes bet all in
    public static final int ALL_IN = 3;
    public static final int RAISE = 4;
    public static final int RERAISE = 5;
    public static final int CHECK_RAISE = 6;
    
    public static final int FOLD_CR = 7;
    public static final int FOLD_RAISE = 8;
        
    public int[] count ;
    
    private String playerName;
    private int round;
    
    public DonkContLimped(String playerName, int round) {
        super();
        this.playerName = playerName;
        this.round = round;
        this.actions = new int[3][9];
        this.actionPossible = new int[3][9];
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
            
            sb.append(" [bet ");
            sb.append(Statistics.formatPercent(actions[i][BET], actionPossible[i][BET], true));
            sb.append(" call ");
            sb.append(Statistics.formatPercent(actions[i][CALL], actionPossible[i][CALL], true));
            sb.append(" raise ");
            sb.append(Statistics.formatPercent(actions[i][RAISE], actionPossible[i][RAISE], true));
            sb.append(" fold ");
            sb.append(Statistics.formatPercent(actions[i][FOLD], actionPossible[i][FOLD], true));
            sb.append(" allin ");
            sb.append(Statistics.formatPercent(actions[i][ALL_IN], actionPossible[i][ALL_IN], true));
            
            sb.append("]");
            if (i < 2)
            sb.append("\n");
        }
        
        
        
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
        
        final int type = estLimped ? LIMPED : (estAgresseur ? IS_AGGRES : NOT_AGGRES);
        
        
        ++actionPossible[type][ALL_IN];
        ++count[type];
        boolean didGoAllIn = false;
        
        log.debug("DonkContLimt START limped? {} agresseur {} player {} round {}",estLimped
                , estAgresseur, playerName,Statistics.roundToStr(round));
        
        for(int i = 0; i < actionIdx.size(); ++i)
        {
            PlayerAction currentAction = ftr.actions.get(actionIdx.get(i));
                        
            PlayerAction prevAction = i==0 ? null : ftr.actions.get(actionIdx.get(i-1));
            
            log.debug("Player {} action {} raise count {}", playerName, actionIdx.get(i), currentAction.globalRaiseCount);
            
            if (currentAction.action == Action.CALL_ALL_IN
                    || currentAction.action == Action.RAISE_ALL_IN
                    || currentAction.action == Action.ALL_IN
                    )
            {
                didGoAllIn = true;
            }
            
            if (currentAction.globalRaiseCount == 0)
            {
                
                log.debug("Player {} can bet.  type {}", playerName, type);
                ++actionPossible[type][BET];
                
                
                if (currentAction.action == Action.RAISE || currentAction.action == Action.RAISE_ALL_IN)
                {
                    log.debug("Player {} did bet. type {}", playerName, type);
                    ++actions[type][BET];
                } else if (currentAction.action == Action.ALL_IN)
                {
                    log.debug("Player {} did all in after no raises. type {}", playerName, type);
                    ++actions[type][ALL_IN];
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
                } else if (currentAction.action == Action.FOLD) 
                {
                    log.debug("Player {} did fold. type {}", playerName, type);
                    
                    ++actions[type][FOLD];
                } else if (currentAction.action == Action.RAISE || currentAction.action == Action.RAISE_ALL_IN)
                {
                    log.debug("Player {} did raise. type {}", playerName, type);
                    
                    ++actions[type][RAISE];
                }             
            
            } else if (currentAction.globalRaiseCount >= 2) {
                
                if (currentAction.globalRaiseCount == 2 &&
                        prevAction != null &&
                        prevAction.action == Action.RAISE)
                {
                    log.debug("Player {} was check raised", playerName);
                    ++actionPossible[type][FOLD_CR];
                    
                    if (currentAction.action == Action.FOLD)
                    {
                        ++actions[type][FOLD_CR];
                    }
                }
                
                log.debug("Player {} fac can limp call / fold / re - raise.  type {}", playerName, type);
                
                ++actionPossible[type][RERAISE];
                
                if (currentAction.action == Action.RAISE || currentAction.action == Action.RAISE_ALL_IN)
                {
                    log.debug("Player {} did re-raise. type {}", playerName, type);
                    
                    ++actions[type][RERAISE];
                } 
            }
            
        }
        
        if (didGoAllIn)
        {
            ++actions[type][ALL_IN];
        }
        
       
                
        
    }

}

