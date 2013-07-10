package pkr.possTree;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.xml.stream.XMLStreamWriter;

import pkr.Card;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class FlopTextureNode implements iDisplayNode
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

    /* (non-Javadoc)
     * @see pkr.possTree.iDisplayNode#serialize(javax.xml.stream.XMLStreamWriter)
     */
    @Override
    public void serialize(XMLStreamWriter writer) {
        // TODO Auto-generated method stub
        
    }
    
    

    /* (non-Javadoc)
     * @see pkr.possTree.iDisplayNode#toLong()
     */
    @Override
    public long toLong() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (flags ^ (flags >>> 32));
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FlopTextureNode other = (FlopTextureNode) obj;
        if (flags != other.flags)
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        List<String> ret = Lists.newArrayList();
        
        for(TextureCategory cat : TextureCategory.values()) {
            if ( (flags & 1 << cat.ordinal()) != 0 ) {
                ret.add(cat.desc.replace(' ', '_'));
            }
        }
        return Joiner.on("--").join(ret) ;
    }
    
   
    

}
