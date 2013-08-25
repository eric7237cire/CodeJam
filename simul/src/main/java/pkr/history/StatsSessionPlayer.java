package pkr.history;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import pkr.history.stats.DonkContLimped;
import pkr.history.stats.NotFoldPFR;
import pkr.history.stats.Pfr;
import pkr.history.stats.ThreeBet;
import pkr.history.stats.Vpip;

import com.google.common.collect.Maps;


//class stat
//round[0].isInitial + 
public class StatsSessionPlayer {
    
    //stat id ==> obj
    public Map<String, iPlayerStatistic> stats;
    public int totalHands;
    
    
    
    
    
    private String playerName;

    public StatsSessionPlayer(String pplayerName) {
        super();
        
        
        this.playerName = pplayerName;
        stats = Maps.newHashMap();
        
        List<iPlayerStatistic> lst = Arrays.asList(
                new Vpip(playerName),
                new Pfr(playerName),
                new NotFoldPFR(playerName),
                new ThreeBet(playerName));
         
        for(iPlayerStatistic s : lst)
        {
            stats.put(s.getId(), s);
        }
        
        stats.put("dcl1", new DonkContLimped(playerName, 1));
        stats.put("dcl2", new DonkContLimped(playerName, 2));
        stats.put("dcl3", new DonkContLimped(playerName, 3));
                
    }
    
    public String getStatValue(String statId)
    {
        return stats.get(statId).toString();
    }

    /**
     * @return the totalHands
     */
    public int getTotalHands() {
        return totalHands;
    }

    /**
     * @return the stats
     */
    public Map<String, iPlayerStatistic> getStats() {
        return stats;
    }
    
    
    
}
