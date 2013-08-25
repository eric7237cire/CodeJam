package pkr.history;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.common.collect.Lists;

public class HandInfoCollector
{

    public List<FlopTurnRiverState[]> masterList = Lists.newArrayList();
    
    
    
    List<Integer> startingLines;
    
    public List<HandInfo> listHandInfo;
    
    public HandInfoCollector() {
        masterList = Lists.newArrayList();
        
        startingLines = Lists.newArrayList();
        
        listHandInfo = Lists.newArrayList();
    }
    
    public void handFinished(HandInfo handInfo)
    {
        masterList.add(handInfo.roundStates);
        listHandInfo.add(handInfo);
        startingLines.add(handInfo.startingLine);
    }

}
