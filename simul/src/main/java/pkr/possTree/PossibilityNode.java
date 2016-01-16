package pkr.possTree;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import poker_simulator.flags.*;

/**
 * Stockage des drapaux pour un jouer, un Stores flags for 1 player in 1 round
 * in one "Level" ie flop texture, winning/losing etc
 *
 */
public class PossibilityNode implements iDisplayNode {

    public static enum Levels {
        TEXTURE(TextureCategory.values()),
        HAND_CATEGORY(HandCategory.values()),
        WIN_LOSE(WinningLosingCategory.values()),
        
        HAND_SUB_CATEGORY(HandSubCategory.values())
        ;
        
        iFlag[] flags;

        private Levels( iFlag[] flags) {
            this.flags = flags;
        }

        public iFlag[] getFlags()
        {
            return flags;
        }
        
        
    }
    
    

    public enum TextureCategory  implements iFlag {
        UNSUITED("Rainbow board"),
        SAME_SUIT_2("2 of the same suit"),
        SAME_SUIT_3("3 of the same suit"),
        SAME_SUIT_4("4 of the same suit"),
        SAME_SUIT_5("5 of the same suit"),
        STRAIGHT("Straight on board"),
        STRAIGHT_POSSIBLE("Straight possible on board"),
        PAIRED_BOARD("Paired board"),
        TWO_PAIRED_BOARD("2 Pairs on board"),
        TRIPS_BOARD("Trips board"),
        FULL_BOARD("full house on board"),
        UNPAIRED_BOARD("Un paired board"),
        HAS_AT_LEAST_ONE_ACE("At least 1 Ace");
        
        
        TextureCategory(String desc) {
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
    
    iFlag[] allFlags;
    
    //int[] cardsFreq;
    
    public PossibilityNode(iFlag[] allFlags) {
        this.allFlags = allFlags;
        
       
        
        
        //cardsFreq = new int[52];
    }
    
  
    // Map<iDisplayNode>
    public List<iDisplayNode> getChildren() {
        return null;
    }
    

    long flags;

    public void setFlag(iFlag flag) {
        flags |= 1L << flag.getIndex();
    }

    public void clearFlag(iFlag flag) {
        flags &= ~(1L << flag.getIndex());
    }
    public boolean hasFlag(iFlag flag) {
        return (flags & 1L << flag.getIndex()) != 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see pkr.possTree.iDisplayNode#toLong()
     */
    @Override
    public long toLong() {

        return flags;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (flags ^ (flags >>> 32));
        return result;
    }

    /*
     * (non-Javadoc)
     * 
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
        PossibilityNode other = (PossibilityNode) obj;
        if (flags != other.flags)
            return false;
        return true;
    }

    @Override
    public String toString() {
        List<String> ret = Lists.newArrayList();

        for (iFlag cat : allFlags) {
            if ((flags & 1 << cat.getIndex()) != 0) {
                ret.add(cat.getDescription().replace(' ', '_'));
            }
        }
        return Joiner.on("--").join(ret) ;
    }


}
