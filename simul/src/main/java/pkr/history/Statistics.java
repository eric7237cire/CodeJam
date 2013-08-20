package pkr.history;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class Statistics {

    protected static Logger log = LoggerFactory.getLogger(Statistics.class);
    
    public static List<iPlayerStatistic> list = Lists.newArrayList();
    
    public static class Vpip implements iPlayerStatistic
    {

        int denom;
        int num;
        
        private String preFlopPlayer;
        
        
        Vpip(String playerName) {
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

        @Override
        public String getValue() {
            return  formatPercent(num, denom);
        }

        @Override
        public void calculate(FlopTurnRiverState[] ftrStates) {
            if (preFlopPlayer.equals(ftrStates[0].playerBB)
                    && ftrStates[0].tableStakes == ftrStates[0].getCurrentBet(preFlopPlayer)
                    && ftrStates[0].roundInitialBetter == null
                    )
            {
                log.debug("Player {} is an unraised big  blind", preFlopPlayer);
                return;
            }
            
            ++denom;
            
            int playerBet = ftrStates[0].getCurrentBet(preFlopPlayer);
            final boolean playerAllin = ftrStates[0].allInBet.containsKey(preFlopPlayer);
            
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
    
    private static String formatPercent(double decimalNum, double decimalDenom)
    {
        if (Double.isNaN(decimalDenom) || Double.isNaN(decimalNum) || decimalDenom < 0.0001)
            return "n/a";
        
        
        
        return FlopTurnRiverState.df1.format(100.0 * decimalNum / decimalDenom) + "%";
    }
}
