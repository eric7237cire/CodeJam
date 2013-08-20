package pkr.history.stats;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.FlopTurnRiverState;
import pkr.history.Statistics;
import pkr.history.iPlayerStatistic;

public class ThreeBet implements iPlayerStatistic
{

    private static Logger log = LoggerFactory.getLogger(Statistics.class);
    
    //Opted to 3 bet
    int thBetNum;
    //Incoming pre flop raise
    int thBetDenom;
    
    int thBetCallNum;
    int thBetFoldNum;
    int wasThBetCount;
    
    private String playerName;
    
    
    public ThreeBet(String playerName) {
        super();
        this.playerName = playerName;
    }

    static Vpip create(String playerName, int round)
    {
        return new Vpip(playerName);
    }
    
    @Override
    public String getId() {
        return "3bet";
    }

    @Override
    public String toString() {
        return "3bet : " ;
    }

    

    @Override
    public void calculate(FlopTurnRiverState[] ftrStates) {
        
        final boolean isPreFlopRaiser = StringUtils.equals(ftrStates[0].roundInitialBetter, playerName);
        
        if (!isPreFlopRaiser)
        {
            log.debug("Player {} could have 3 bet", playerName);
            ++thBetDenom;
        }
        final boolean playerHasReraised = 
                BooleanUtils.isTrue(ftrStates[0].hasReraised.get(playerName));
        
        if (playerHasReraised)
        {
            log.debug("Player {} has 3 bet", playerName);
            ++thBetNum;
        }
        
        final boolean playerHasFoldedReraise = 
                BooleanUtils.isTrue(ftrStates[0].foldedToBetOrRaise.get(playerName));
        
        final boolean playerHasCalledReraise = 
                BooleanUtils.isTrue(ftrStates[0].calledABetOrRaise.get(playerName));
        
        if (isPreFlopRaiser && (playerHasReraised || playerHasFoldedReraise || playerHasCalledReraise))
        {
            log.debug("Player {} was 3 bet", playerName);
            ++wasThBetCount;
            
            if (playerHasCalledReraise || playerHasReraised)
            {
                log.debug("Player {} did not fold", playerName);
                ++thBetCallNum;
            } else  if (playerHasFoldedReraise)
            {
                log.debug("Player {} folded to a  3 bet", playerName);
                ++thBetFoldNum;
            }
        }
        
        
        //int playerBet = ftrStates[0].getCurrentBet(playerName);
        //final boolean playerAllin = ftrStates[0].allInBet.containsKey(playerName);
        
        
    }

}
