package pkr.possTree;

import pkr.Card;

public class FlopTextureNode
{
    /*
     * suits
     * 
     * 3 same suit
     * 2 same suit
     * 2 , 2 same suit
     * dif suit
     *
     * straight
     * 89T 
     *  89
     *  
     *  Ace
     *  No Ace
     *  2 or more J to A
     *  
     *  Category 
     *  
     *  Paired board
     *  Unpaired board  
     *  trips on board  
     *  2 pair on board
     */
    
    public enum TextureCategory {
        UNSUITED("Rainbow board"),
        SAME_SUIT_2("2 of the same suit"),
        SAME_SUIT_3("3 of the same suit"),
        SAME_SUIT_4("4 of the same suit"),
        SAME_SUIT_5("5 of the same suit"),
        HAS_AT_LEAST_ONE_ACE("At least 1 Ace");
        
        
        TextureCategory(String desc) {
            this.desc = desc;
        }

        private String desc;

        public String getDesc()
        {
            return desc;
        }
    }
    
    long flags;
    
    Card[] flop;
    
    //attributes are flags to create unique long id
    //means max 64 flags
    
    /**
     * 
     * @param flop
     */
    public FlopTextureNode(Card[] flop) {
        this.flop = flop;
        flags = 0;
    }
    
    public void setFlag(TextureCategory flag) {
        flags |= 1L << flag.ordinal();
    }
    
    public boolean hasFlag(TextureCategory flag) {
        return (flags & 1L << flag.ordinal()) != 0;
    }

}
