package pkr.history.stats;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.FlopTurnRiverState;
import pkr.history.HandInfo;
import pkr.history.Statistics;
import pkr.history.iPlayerStatistic;

public class Pfr implements iPlayerStatistic
{

    private static Logger log = LoggerFactory.getLogger(Statistics.class);
    
    int unraisedToPlayer;
    int raisedPreflop;
    
    int[] posUnraisedToPlayer;
    int[] posRaisedPf;
    StringBuffer[] actionsDesc;
    
    double avgAmt;
        
    int nonAllInRaisePreflop;
    
    public int nTapis;
    
    private String preFlopPlayer;
    
    
    public Pfr(String playerName) {
        super();
        this.preFlopPlayer = playerName;
        
        posUnraisedToPlayer = new int[FlopTurnRiverState.MAX_PLAYERS];
        posRaisedPf = new int[FlopTurnRiverState.MAX_PLAYERS];
        actionsDesc = new StringBuffer[FlopTurnRiverState.MAX_PLAYERS];
        
        for(int i = 0; i < actionsDesc.length; ++i) {
            actionsDesc[i] = new StringBuffer();
        }
        
    }

    /**
     * @return the actionsDesc
     */
    public String getActionsDesc(int posIndex) {
        return StringEscapeUtils.escapeXml(actionsDesc[posIndex].toString());
    }
    
    public String getPercentage(int posIndex) {
        return Statistics.formatPercent(posRaisedPf[posIndex], posUnraisedToPlayer[posIndex], true);
    }
    
    @Override
    public String getId() {
        return "pfr";
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Pfr : ");
        sb.append(Statistics.formatPercent(raisedPreflop, unraisedToPlayer));
        sb.append(" (");
        sb.append(raisedPreflop);
        sb.append("/");
        sb.append(unraisedToPlayer);
        sb.append(") Avg amt : ");
        sb.append(Statistics.formatMoney(avgAmt, nonAllInRaisePreflop));
        sb.append(" Tapis : ");
        sb.append(nTapis);
        return sb.toString();
    }

    @Override
    public void calculate(HandInfo handInfo) {
        
        FlopTurnRiverState[] ftrStates = handInfo.roundStates;
        
        final boolean isPreFlopRaiser = StringUtils.equals(ftrStates[0].roundInitialBetter, preFlopPlayer);
        
        final int playerPosition = ftrStates[0].players.indexOf(preFlopPlayer);
        
        final boolean playerAllin = ftrStates[0].allInMinimum[playerPosition] >= 0;
        
        String link = DonkContLimped.buildLink(handInfo);
        int posIndex = Vpip.getPositionIndex(ftrStates[0].players.size(), playerPosition);
        
        
        
        //TODO  utiliser la position
        int raiserPosition = ftrStates[0].roundInitialBetter != null ? ftrStates[0].players.indexOf( ftrStates[0].roundInitialBetter ) : -1; 
        
        if (isPreFlopRaiser || raiserPosition > playerPosition || raiserPosition == -1 )
        {
            log.debug("Player {} could have preflop raised");
            ++unraisedToPlayer;
            
            ++posUnraisedToPlayer[posIndex];
            
            
        }
                
        final int playerBet = ftrStates[0].playerBets[playerPosition];
        
        if (isPreFlopRaiser)
        {
            ++raisedPreflop;
            
            ++posRaisedPf[posIndex];
            
            actionsDesc[posIndex]
            	.append("Player ")
            .append(preFlopPlayer);
            
            if (!playerAllin)
            {
                actionsDesc[posIndex].append(" raised to ")
                .append(Statistics.formatMoney(playerBet));
                ++nonAllInRaisePreflop;
                avgAmt += playerBet;
            } else {
                actionsDesc[posIndex].append(" went all in ");
                nTapis++;
            }
            
            actionsDesc[posIndex].append(DonkContLimped.lineEnd).append(link).append(DonkContLimped.lineEnd);
        }
        
    }
}