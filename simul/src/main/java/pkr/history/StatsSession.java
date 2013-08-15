package pkr.history;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class StatsSession {
    Map<String, StatsSessionPlayer> playerSessionStats;

    List<String> currentPlayerList;
    
    StatsSession() {
        super();
        currentPlayerList = Lists.newArrayList();
        playerSessionStats = Maps.newHashMap();
    }
}
