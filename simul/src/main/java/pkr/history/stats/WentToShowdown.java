package pkr.history.stats;

import org.apache.commons.lang3.ArrayUtils;

import pkr.history.FlopTurnRiverState;
import pkr.history.HandInfo;
import pkr.history.Statistics;
import pkr.history.iPlayerStatistic;

import com.google.common.base.Preconditions;

public class WentToShowdown implements iPlayerStatistic {

    // private static Logger log = LoggerFactory.getLogger(Statistics.class);

    private String playerName;

    int inRound;
    int wentToShowDown;
    int wonAtShowdown;

    public WentToShowdown(String playerName) 
    {
        super();
        this.playerName = playerName;
    }

    @Override
    public String getId() 
    {
        return "wtsd";
    }

    public String getWTSD() {
        return Statistics.formatPercent(wentToShowDown, inRound);
    }

    public String getWonAtSD() {
        return Statistics.formatPercent(wonAtShowdown, wentToShowDown);
    }

    @Override
    public void calculate(HandInfo handInfo) 
    {

        FlopTurnRiverState[] ftrStates = handInfo.roundStates;

        Preconditions.checkState(ftrStates[0].players.contains(playerName));

        ++inRound;

        if (handInfo.losersPlayerName == null)
            return;
        
        if (handInfo.losersPlayerName.contains(playerName)) {
            ++wentToShowDown;
        } else if (ArrayUtils.contains(handInfo.winnerPlayerName, playerName)) {
            ++wentToShowDown;
            ++wonAtShowdown;
        }

    }
}
