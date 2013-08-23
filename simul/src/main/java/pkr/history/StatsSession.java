package pkr.history;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class StatsSession {
    public Map<String, StatsSessionPlayer> playerSessionStats;

    public List<String> currentPlayerList;
    
    StatsSession() {
        super();
        currentPlayerList = Lists.newArrayList();
        playerSessionStats = Maps.newHashMap();
    }

    /**
     * @return the currentPlayerList
     */
    public List<String> getCurrentPlayerList() {
        return currentPlayerList;
    }

    /**
     * @param currentPlayerList the currentPlayerList to set
     */
    public void setCurrentPlayerList(List<String> currentPlayerList) {
        this.currentPlayerList = currentPlayerList;
    }

    /**
     * @return the playerSessionStats
     */
    public Map<String, StatsSessionPlayer> getPlayerSessionStats() {
        return playerSessionStats;
    }
}
