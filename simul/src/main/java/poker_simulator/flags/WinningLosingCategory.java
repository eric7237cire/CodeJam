package poker_simulator.flags;

public enum WinningLosingCategory implements iFlag {
    WINNING("WinningOrTied"),
    SECOND_BEST_HAND("Second best hand"),
    LOSING("Losing 3rd or worst");
    
    
    
    WinningLosingCategory(String desc) {
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
    
    
}