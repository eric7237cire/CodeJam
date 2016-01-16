package poker_simulator.flags;
  
public enum Round implements iFlag {
	FLOP("Flop"), //on board        
    TURN("Turn"),
    RIVER("River")
    ;
    
    Round (String desc) {
        this.desc = desc;
    }

    private String desc;

    public String getDesc()
    {
        return desc;
    }

    /* (non-Javadoc)
     * @see pkr.possTree.iFlag#getIndex()
     */
    @Override
    public int getIndex() {
        return ordinal();
    }

    /* (non-Javadoc)
     * @see pkr.possTree.iFlag#getDescription()
     */
    @Override
    public String getDescription() {
        return desc;
    }
    
    public static Round fromInt(int round)
    {
    	if (round == 0)
    		return Round.FLOP;
    	if (round == 1)
    		return Round.TURN;
    	if (round == 2)
    		return Round.RIVER;
    	
    	throw new RuntimeException( "Invalid round: " + round );
    }
}
