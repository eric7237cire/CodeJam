package pkr.history.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.FlopTurnRiverState;
import pkr.history.HandInfo;
import pkr.history.Statistics;
import pkr.history.iPlayerStatistic;

public class Vpip implements iPlayerStatistic
{

    private static Logger log = LoggerFactory.getLogger(Vpip.class);
    
    public int played;
    public int moneyIn;
    
    //sb bb btn-2 btn-1 btn
    //sb bb btn-1 btn
    //sb bb btn
    //sb bb
    public int [] posMoneyIn;
    public int [] posPlayed;
    
    public static int SB_POS = 0;
    public static int BB_POS = 1;
    public static int BTN_POS = 2;
    public static int MID_POS = 3;
    public static int EARLY_POS = 4;
    
    private String preFlopPlayer;
    
    
    public Vpip(String playerName) {
        super();
        this.preFlopPlayer = playerName;
        
        posMoneyIn = new int[FlopTurnRiverState.MAX_PLAYERS];
        posPlayed = new int[FlopTurnRiverState.MAX_PLAYERS];
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
        return  Statistics.formatPercent(moneyIn, played, true);
    }
    
    public String getPercentage(int position) {
        return Statistics.formatPercent(posMoneyIn[position], posPlayed[position], true);
    }
    
    public static int getPositionIndex(int numPlayers, int playerPosition)
    {

        final int sbPos = numPlayers - 2;
        
        int posIndex = playerPosition >= sbPos ? playerPosition - sbPos : 
            (numPlayers - 1 - playerPosition); 
        
        return posIndex;
    }
    

    @Override
    public void calculate(HandInfo handInfo) {
        
        log.debug("VPIP player {} hand {}", preFlopPlayer, handInfo.handIndex);
        FlopTurnRiverState[] ftrStates = handInfo.roundStates;
        
        int playerPosition = ftrStates[0].players.indexOf(preFlopPlayer);
        int playerBet = ftrStates[0].playerBets[playerPosition];
        
        //final int sbPos = ftrStates[0].players.size() - 2;
        final int bbPos = ftrStates[0].players.size() - 1;
        
        if (playerPosition == bbPos
                && ftrStates[0].tableStakes*2 == playerBet
                && ftrStates[0].roundInitialBetter == null
                )
        {
           // log.debug("Player {} is an unraised big  blind", preFlopPlayer);
          //  return;
        }
        
        ++played;
        
        //Size 3 ; Btn 0 ==> 2
        //Size 4 ; Btn 1 ==> 2 ; MP 0 ==> 3
        //Size 5; Btn 2 ==> 2 ; MP 1 ==> 3 ; EP 0 ==> 4
        int posIndex = getPositionIndex(ftrStates[0].players.size(), playerPosition); 
        
        log.debug("Player pos {} pos index {}", playerPosition, posIndex);
            
        posPlayed[posIndex]++;
        
        
        final boolean playerAllin = ftrStates[0].allInMinimum[playerPosition] >= 0;
        
        if ( (playerPosition != bbPos && playerBet >= 2 * ftrStates[0].tableStakes)
                ||
                (playerPosition == bbPos && playerBet > 2 * ftrStates[0].tableStakes)
                ||
                //Supposons qu'un tapis est une relance
                playerAllin
                )
        {
            log.debug("Player {} entered pot for VPIP.  table stakes {}  player bet {} bb {}", preFlopPlayer,
                    ftrStates[0].tableStakes,
                    ftrStates[0].playerBets[playerPosition],
                    ftrStates[0].players.get(bbPos)
                    );
            moneyIn++;
            posMoneyIn[posIndex]++;
        }
    }
    
}

