package pkr.history.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.FlopTurnRiverState;
import pkr.history.HandInfo;
import pkr.history.Statistics;
import pkr.history.iPlayerStatistic;

public class Vpip implements iPlayerStatistic
{

    private static Logger log = LoggerFactory.getLogger(Statistics.class);
    
    int denom;
    int num;
    
    private String preFlopPlayer;
    
    
    public Vpip(String playerName) {
        super();
        this.preFlopPlayer = playerName;
    }

    static Vpip create(String playerName, int round)
    {
        return new Vpip(playerName);
    }
    
    @Override
    public String getId() {
        return "vpip";
    }

    @Override
    public String toString() {
        return "Vpip : " + getValue();
    }

    public String getValue() {
        return  Statistics.formatPercent(num, denom, true);
    }

    @Override
    public void calculate(HandInfo handInfo) {
        
        FlopTurnRiverState[] ftrStates = handInfo.roundStates;
        
        int playerPosition = ftrStates[0].players.indexOf(preFlopPlayer);
        int playerBet = ftrStates[0].playerBets[playerPosition];
        
        if (preFlopPlayer.equals(ftrStates[0].playerBB)
                && ftrStates[0].tableStakes == playerBet
                && ftrStates[0].roundInitialBetter == null
                )
        {
            log.debug("Player {} is an unraised big  blind", preFlopPlayer);
            return;
        }
        
        ++denom;
        
        
        final boolean playerAllin = ftrStates[0].allInMinimum[playerPosition] > 0;
        
        if ( (!preFlopPlayer.equals(ftrStates[0].playerBB) && playerBet >= ftrStates[0].tableStakes)
                ||
                (preFlopPlayer.equals(ftrStates[0].playerBB) && playerBet > ftrStates[0].tableStakes)
                ||
                //Supposons qu'un tapis est une relance
                playerAllin
                )
        {
            log.debug("Player {} entered pot for VPIP.  table stakes {}  player bet {} bb {}", preFlopPlayer,
                    ftrStates[0].tableStakes,
                    ftrStates[0].getCurrentBet(preFlopPlayer),
                    ftrStates[0].playerBB
                    );
            num++;
        }
    }
    
}

