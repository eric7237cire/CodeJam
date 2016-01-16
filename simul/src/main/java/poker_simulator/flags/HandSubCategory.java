package poker_simulator.flags;

public enum HandSubCategory implements iFlag {
    
    SECOND_BEST_PAIR("2nd best pair"), //on board        
    BY_HAND_CATEGORY("Det by hand category"),
    BY_KICKER_HAND("Determined by hand"),
    BY_KICKER_1("Determined by first kicker"),
    BY_KICKER_2_PLUS("Determined by 2nd or later kicker"),
    VILLAIN_HIGH_CARD("Vs high card"),
    VILLAIN_PAIR("Vs pair"),
    VILLAIN_TWO_PAIR("Vs 2 pair"),
    VILLAIN_TRIPS("Vs trips"),
    VILLAIN_STRAIGHT("Vs straight"),
    VILLAIN_FLUSH("Vs flush"),
    VILLAIN_OTHER("Vs other"),
    ;
    
    HandSubCategory(String desc) {
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