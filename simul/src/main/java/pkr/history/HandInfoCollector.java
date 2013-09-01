package pkr.history;

import java.util.List;

import com.google.common.collect.Lists;

public class HandInfoCollector
{

    
    
    public List<HandInfo> listHandInfo;
    
    public HandInfoCollector() {
        
        listHandInfo = Lists.newArrayList();
    }
    
    public void handFinished(HandInfo handInfo)
    {
    	if (handInfo.roundStates[0].actions.size() == 0)
    		return;
    	
        listHandInfo.add(handInfo);
        
        
            handInfo.handLog.append("\n***********************************************");
            handInfo.handLog.append("Starting Hand <h1>")
            .append(handInfo.handIndex)
            .append("</h1> line ")
            .append(handInfo.startingLine);
            
        
        
        for(int round = 0; round <= 3; ++round)
        {
            
            if (handInfo.roundStates[round] == null)
                break;
            
            if (handInfo.roundStates[round].actions.size() == 0)
            	break;
            
            handInfo.handLog.append("\n------------------------------");
            handInfo.handLog.append("\nStarting round <h2>")
            .append(Statistics.roundToStr(round))
            .append(" </h2> with ")
            .append(handInfo.roundStates[round].players.size())
            .append(" players.  Pot is $")
            .append(Statistics.moneyFormat.format(
            		round == 0 ? handInfo.roundStates[round].tableStakes * 3 :
            			handInfo.roundStates[round-1].pot))
            .append("\n");
            
            List<PlayerAction> actions = handInfo.roundStates[round].actions;
            for(PlayerAction action : actions)
            {
                handInfo.handLog.append(action.getDesc());
            }
        
            
           
            
        }
        
        handInfo.handLog
        .append(handInfo.winnerPlayerName)
        .append(" wins showdown with pot $")
        .append(Statistics.moneyFormat.format(handInfo.wonPot))
        .append("\n")
        .append(handInfo.winDesc)
        .append("\n");
    }

}
