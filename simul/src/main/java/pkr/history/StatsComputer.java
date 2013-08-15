package pkr.history;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;

public class StatsComputer
{

    public StatsComputer(List<FlopTurnRiverState[]> hands ) {

        stats = new StatsSession();
        
        for(FlopTurnRiverState[] ftrStates : hands)
        {
            for(String preFlopPlayer : ftrStates[0].players)
            {
                StatsSessionPlayer playerSesStat = getSessionStats(preFlopPlayer);
                
                playerSesStat.totalHands++;
                
                if (BooleanUtils.isTrue(ftrStates[0].hasFolded.get(preFlopPlayer)))
                {
                    playerSesStat.vpipDenom++;
                } else if (!preFlopPlayer.equals(ftrStates[0].playerBB)
                        || ftrStates[0].tableStakes > ftrStates[0].playerBets.get(preFlopPlayer)
                        ) 
                {
                    //Player is either not the big blind or had to put in extra
                    playerSesStat.vpipNumerator++;
                    playerSesStat.vpipDenom++;
                }
                
            }
        }
        
        stats.currentPlayerList = hands.get(hands.size()-1)[0].players;
    }
    
    public StatsSession stats = null;
    
    
    StatPlayerHand getHandStats(StatHand sh, String playerName)
    {
        StatPlayerHand ret = sh.playerStats.get(playerName);
        
        if (ret == null)
        {
            ret = new StatPlayerHand();
            sh.playerStats.put(playerName, ret);
        }
        
        return ret;
    }
    
    StatsSessionPlayer getSessionStats(String playerName)
    {
        StatsSessionPlayer ret = stats.playerSessionStats.get(playerName);
        
        if (ret == null)
        {
            ret = new StatsSessionPlayer();
            stats.playerSessionStats.put(playerName, ret);
        }
        
        return ret;
    }

}
