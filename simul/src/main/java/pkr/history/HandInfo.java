package pkr.history;

import org.apache.commons.lang3.StringEscapeUtils;

public class HandInfo
{

    StringBuffer handLog;
    public FlopTurnRiverState[] roundStates;
    public int startingLine;
    public int handIndex;
    HandInfo( int startingLine, int handIndex ) {
        super();
        this.startingLine = startingLine;
        this.handIndex = handIndex;
        this.handLog = new StringBuffer();
        this.roundStates = new FlopTurnRiverState[4];
    }
    public String getHandLog()
    {
        return (handLog.toString());
        //return StringEscapeUtils.escapeXml(handLog.toString());
    }
    public int getStartingLine()
    {
        return startingLine;
    }
    
    

}
