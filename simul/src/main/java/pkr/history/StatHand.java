package pkr.history;

import java.util.Map;

import com.google.common.collect.Maps;

public class StatHand {
    Map<String, StatPlayerHand> playerStats;
    
    StatHand() {
        playerStats = Maps.newHashMap();
    }
}
