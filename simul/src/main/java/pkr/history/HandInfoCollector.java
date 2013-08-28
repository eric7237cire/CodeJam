package pkr.history;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.common.collect.Lists;

public class HandInfoCollector
{

    
    
    public List<HandInfo> listHandInfo;
    
    public HandInfoCollector() {
        
        listHandInfo = Lists.newArrayList();
    }
    
    public void handFinished(HandInfo handInfo)
    {
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
            
            handInfo.handLog.append("\n------------------------------");
            handInfo.handLog.append("\nStarting round <h2>")
            .append(Statistics.roundToStr(round))
            .append(" </h2> with ")
            .append(handInfo.roundStates[round].players.size())
            .append(" players.  Pot is $")
            .append(Statistics.moneyFormat.format(handInfo.roundStates[round].pot))
            .append("\n");
            
            List<PlayerAction> actions = handInfo.roundStates[round].actions;
            for(PlayerAction action : actions)
            {
                handInfo.handLog.append(action.getDesc());
            }
        
            
        }
    }

}
