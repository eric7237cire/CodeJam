package pkr.history;

public class PlayerAction {
    public  int playerPosition;
    public String playerName;
    
    public enum Action
    {
        FOLD, 
        RAISE, //bet / reraise
        CALL, CHECK, 
        ALL_IN, //unknown if it is a raise or call
        RAISE_ALL_IN, CALL_ALL_IN;
    }
    
    public Action action;
    
    //Called or raised this amount -or- folded to amount
    public int amountRaised;
    
    public int incomingBetOrRaise;
    //Le pot avant l'action
    public int pot;
    
    //Raises before this action (allins count)
    public int globalRaiseCount;
    
    private PlayerAction()
    {
        
    }

    static PlayerAction createCall(int playerPosition, String playerName, 
            int amount, int potBeforeCall)
    {
        PlayerAction action = new PlayerAction();
        action.playerPosition = playerPosition;
        action.playerName = playerName;
        action.action = Action.CALL;
        action.pot = potBeforeCall;
        
        action.incomingBetOrRaise = amount;
        
        return action;
    }
    
    
    static PlayerAction createReraise(int playerPosition, String playerName, 
            int amountToCall, int raiseAmount, int potBeforeRaise)
    {
        PlayerAction action = new PlayerAction();
        action.playerPosition = playerPosition;
        action.playerName = playerName;
        action.action = Action.RAISE;
        action.pot = potBeforeRaise;
        
        action.incomingBetOrRaise = amountToCall;
        action.amountRaised = raiseAmount;
        
        return action;
    }
    
    static PlayerAction createFold(int playerPosition, String playerName, 
            int amountToCall, int pot)
    {
        PlayerAction action = new PlayerAction();
        action.playerPosition = playerPosition;
        action.playerName = playerName;
        action.action = Action.FOLD;
        action.pot = pot;
        
        action.incomingBetOrRaise = amountToCall;
        action.amountRaised = -1;
        
        return action;
    }
    
    static PlayerAction createCheck(int playerPosition, String playerName, 
             int pot)
    {
        PlayerAction action = new PlayerAction();
        action.playerPosition = playerPosition;
        action.playerName = playerName;
        action.action = Action.CHECK;
        action.pot = pot;
        
        action.incomingBetOrRaise = 0;
        action.amountRaised = -1;
        
        return action;
    }
    
    static PlayerAction createBetAllin(int playerPosition, String playerName, 
            int amountToCall, int pot)
    {
        PlayerAction action = new PlayerAction();
        action.playerPosition = playerPosition;
        action.playerName = playerName;
        action.action = Action.RAISE_ALL_IN;
        action.pot = pot;
        
        action.incomingBetOrRaise = amountToCall;
        action.amountRaised = -1;
        
        return action;
    }
    
    static PlayerAction createAllin(int playerPosition, String playerName, 
            int amountToCall, int pot)
    {
        PlayerAction action = new PlayerAction();
        action.playerPosition = playerPosition;
        action.playerName = playerName;
        action.action = Action.ALL_IN;
        action.pot = pot;
        
        action.incomingBetOrRaise = amountToCall;
        action.amountRaised = -1;
        
        return action;
    }
}
