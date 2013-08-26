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
    }

}
