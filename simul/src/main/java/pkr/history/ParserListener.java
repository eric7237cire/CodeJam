package pkr.history;



public interface ParserListener
{
    public ParserListener handleSuivi(String playerName, int bet);
    
    public ParserListener handleRelance(String playerName, int bet);
    
    public ParserListener handleCoucher(String playerName);
    
    public ParserListener handleParole(String playerName);
    
    public ParserListener handleTapis(String playerName, int lineNumber);
    
    public ParserListener handleShowdown(String playerName, int pot, String lineText);
    
    public ParserListener handleGagne(String playerName);
    
    public int replayLine();
}
