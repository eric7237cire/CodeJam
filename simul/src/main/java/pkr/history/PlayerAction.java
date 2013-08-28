package pkr.history;

import com.google.common.base.Preconditions;

public class PlayerAction {
    public  int playerPosition;
    public String playerName;
    
   
    public enum Action
    {
        FOLD, 
        RAISE, //bet / reraise
        CALL, CHECK, 
        ALL_IN, //unknown if it is a raise or call
        RAISE_ALL_IN, CALL_ALL_IN,
        WON;
    }
    
    public Action action;
    
    //bet or raised this amount 
    public int amountRaised;
    
    public int incomingBetOrRaise;
    
    public int playerAmtPutInPotThisRound;
    
    //Le pot avant l'action
    public int pot;
    
    //Raises before this action (allins count)
    public int globalRaiseCount;
    
    //Stats before player acts
    public int playersLeft;
    public int playersAllIn;
    public int playersFolded;
    
    public String wonDesc;
    
    private PlayerAction()
    {
        
    }
    
    static PlayerAction createWon(int playerPosition, String playerName, 
            int pot, String winDesc)
    {
        PlayerAction action = new PlayerAction();
        action.playerPosition = playerPosition;
        action.playerName = playerName;
        action.action = Action.WON;
        action.pot = pot;
        
        action.wonDesc = winDesc;
        return action;
    }

    static PlayerAction createCall(int playerPosition, String playerName,
            int playerPrevPutInPot,
            int amountCalled, int potBeforeCall)
    {
        Preconditions.checkState(playerPrevPutInPot >= 0);
        PlayerAction action = new PlayerAction();
        action.playerPosition = playerPosition;
        action.playerName = playerName;
        action.action = Action.CALL;
        action.pot = potBeforeCall;
        action.playerAmtPutInPotThisRound = playerPrevPutInPot;
        action.incomingBetOrRaise = amountCalled;
        
        return action;
    }
    
    
    static PlayerAction createReraise(int playerPosition, String playerName, 
            int amountToCall, int raiseAmount, int playerAmtPutInPotThisRound, int potBeforeRaise)
    {
        PlayerAction action = new PlayerAction();
        action.playerPosition = playerPosition;
        action.playerName = playerName;
        action.action = Action.RAISE;
        action.pot = potBeforeRaise;
        
        action.playerAmtPutInPotThisRound = playerAmtPutInPotThisRound < 0 ? 0 : playerAmtPutInPotThisRound;
        action.incomingBetOrRaise = amountToCall;
        action.amountRaised = raiseAmount;
        
        return action;
    }
    
    static PlayerAction createFold(int playerPosition, String playerName, 
            int amountToCall, int pot, int playerAmtPutInPotThisRound)
    {
        PlayerAction action = new PlayerAction();
        action.playerPosition = playerPosition;
        action.playerName = playerName;
        action.action = Action.FOLD;
        action.pot = pot;
        
        action.incomingBetOrRaise = amountToCall;
        action.amountRaised = 0;
        
        action.playerAmtPutInPotThisRound = (playerAmtPutInPotThisRound < 0) ? 0 : playerAmtPutInPotThisRound;
        
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
        action.amountRaised = 0;
        
        return action;
    }
    
    static PlayerAction createBetAllin(int playerPosition,
            String playerName, 
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
            int amountToCall, int playerPrevPutInPot, int pot)
    {
        PlayerAction action = new PlayerAction();
        action.playerPosition = playerPosition;
        action.playerName = playerName;
        action.action = Action.ALL_IN;
        action.pot = pot;
        action.playerAmtPutInPotThisRound = playerPrevPutInPot;
        
        action.incomingBetOrRaise = amountToCall;
        action.amountRaised = -1;
        
        return action;
    }
    
    String getDesc() 
    {
        StringBuffer sb = new StringBuffer();
        
        if (action == Action.WON)
        {

            sb.append(playerName)
            .append(" wins showdown with pot $")
            .append(Statistics.moneyFormat.format(pot))
            .append("\n")
            .append(wonDesc)
            .append("\n");
            return sb.toString();
        }
        
        sb.append("\n** Player ")
        .append(playerName)
        .append(" position ")
        .append(playerPosition)
        .append("  Action [<b> ");
        if (action == Action.CALL)
        {
            sb.append("Call ")
            .append(Statistics.formatMoney(incomingBetOrRaise));
        } else if (action == Action.RAISE) {
            sb.append("Raise ")
            .append(Statistics.formatMoney(amountRaised));
        }  else if (action == Action.RAISE_ALL_IN) {
            sb.append("All in (Raise) ")
            .append(Statistics.formatMoney(amountRaised));
        } else {
            sb.append(action);
       
        
        }
        sb.append(" </b>]\n");
        
        int playerBet = playerAmtPutInPotThisRound;
        int amtToCall = incomingBetOrRaise;
        if (playerBet < 0)
            playerBet = 0;
        
        if (amtToCall > playerBet && !( action == Action.RAISE || action == Action.RAISE_ALL_IN)
        )
        {
            int diff = amtToCall - playerBet;
            double perc = 100.0 * diff / (pot + diff);
            double ratio = pot * 1.0 / diff; 
            
            double outsOne = perc / 2;
            
           // double betSizeToPot = 1.0 * diff / pot;
            //% must be ahead
           // double callBluff = 100*betSizeToPot / (1+betSizeToPot);
            
            
            sb.append("Amount to call $")
            .append(Statistics.moneyFormat.format(diff))
            .append(" for pot $")
            .append(Statistics.moneyFormat.format(pot))
            .append(".\n  Pot ratio (bluff catching) : ")
            .append(Statistics.df2.format(perc))
            .append("%  | 1 to ")
            .append(Statistics.df2.format(ratio))
            .append(" | ")
            .append(Statistics.df2.format(outsOne))
            .append("\n");
            
            sb.append("Implied odds : ")
            .append(" | 4 outs ")
            .append(Statistics.formatMoney((int) (diff * getRatio(getPerc(true, 4)))))
            .append(" | 5 outs ")
            .append(Statistics.formatMoney((int) (diff * getRatio(getPerc(true, 5)))))
            .append(" | 8 outs ")
            .append(Statistics.formatMoney((int) (diff * getRatio(getPerc(true, 8)))))
            .append(" | 9 outs ")
            .append(Statistics.formatMoney((int) (diff * getRatio(getPerc(true, 9)))))
            .append("\n");
          //  logOutput.debug("Must be ahead {}% of the time to call a bluff", 
              //      Statistics.df2.format(callBluff));
        }
        
        if ( action == Action.RAISE || action == Action.RAISE_ALL_IN)
        {
            int diff = amountRaised - playerAmtPutInPotThisRound;
            double betSizeToPot = 1.0 * diff / pot;
            //double bluff = 100.0*(betSizeToPot) / (1+betSizeToPot);
            
            sb.append("Raise amt $")
            .append(Statistics.moneyFormat.format(diff))
            .append(" | %")
            .append(Statistics.formatPercent(betSizeToPot, 1))
            .append(" of pot ")
            .append(Statistics.formatMoney(pot))
            .append("\nbluff % chance everyone must fold ")
            .append(Statistics.formatPercent(betSizeToPot, 1+betSizeToPot))
            .append("\n");
        }
        
        sb.append("\n");
        
        return sb.toString();
    }
    
    private static double getRatio(double perc)
    {
        return (1-perc) / perc;
    }
    
    private static double getPerc(boolean turn, int outs)
    {
        if (turn) {
            return (double) outs / 47.0;
        } else {
            return (double) outs / 46.0;
        }
    }
}
