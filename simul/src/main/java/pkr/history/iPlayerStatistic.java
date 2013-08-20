package pkr.history;

public interface iPlayerStatistic {
    String getId();
    
    //String getName();
    
    String getValue();
    
   // iPlayerStatistic create(String playerName, int round);
    
    void calculate(FlopTurnRiverState[] state);
}
