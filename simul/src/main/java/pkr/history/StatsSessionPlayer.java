package pkr.history;

public class StatsSessionPlayer {
    public int totalHands;
    
    //not counting big blind, non raised
    public int vpipNumerator;
    public int vpipDenom;
    
    //Calling a pre flop raise
    public int callOpenNumerator;
    public int callOpenDenom;
    
    public int preFlopRaises;
    public int preFlopTapis;
    
}
