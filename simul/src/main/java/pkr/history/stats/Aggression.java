package pkr.history.stats;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.FlopTurnRiverState;
import pkr.history.HandInfo;
import pkr.history.PlayerAction;
import pkr.history.Statistics;
import pkr.history.iPlayerStatistic;

public class Aggression implements iPlayerStatistic
{

    private static Logger log = LoggerFactory.getLogger(Statistics.class);
    
    private String playerName;
    
    public int calls;
    public int folds;
    public int betsOrRaises;
    
    
    //(total bet + total raise) / (total bet + total raise + total call + total fold) * 100,

    public Aggression(String playerName) 
    {
        super();
        this.playerName = playerName;
    }
    @Override
    public String getId() 
    {
        return "agg";
    }

    public String getPerc() 
    {
        return Statistics.formatPercent(betsOrRaises, betsOrRaises + calls + folds); 
    }
    
    @Override
    public void calculate(HandInfo handInfo) 
    {
        
        FlopTurnRiverState[] ftrStates = handInfo.roundStates;    
        
        
        
        for(int round = 1; round < ftrStates.length; ++round)
        {
            FlopTurnRiverState ftr = ftrStates[round];
            
            if (ftr == null) 
            {
                continue;
            }
            int playerPos = ftr.players.indexOf(playerName);
            
            if (playerPos == -1)
            {
                log.debug("Player {} not in round {}", playerName, round);
                return;
            }
            
            List<Integer> actionIdxList = ftr.playerPosToActions.get(playerPos);
            
            for(int a = 0; a < actionIdxList.size(); ++a)
            {
                int actionIdx = actionIdxList.get(a);
                PlayerAction action = ftr.actions.get(actionIdx);
                
                switch(action.action) 
                {
                case FOLD:
                    ++folds;
                    break;
                case CALL:
                case CALL_ALL_IN:
                    ++calls;
                    break;
                case RAISE_ALL_IN:
                case ALL_IN:
                case RAISE:
                    ++betsOrRaises;
                    break;
                case CHECK:
                    break;
                }
            }
        }
    }
}