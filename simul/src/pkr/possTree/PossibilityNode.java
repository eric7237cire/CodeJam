package pkr.possTree;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import pkr.HoleCards;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

/**
 * Stockage des drapaux pour un jouer, un Stores flags for 1 player in 1 round
 * in one "Level" ie flop texture, winning/losing etc
 *
 */
public class PossibilityNode implements iDisplayNode {

    public static enum Levels {
        TEXTURE,
        HAND_CATEGORY,
        WIN_LOSE,        
        HAND_SUB_CATEGORY
    }
    
    

    public enum TextureCategory  implements iFlag {
        UNSUITED("Rainbow board"),
        SAME_SUIT_2("2 of the same suit"),
        SAME_SUIT_3("3 of the same suit"),
        SAME_SUIT_4("4 of the same suit"),
        SAME_SUIT_5("5 of the same suit"),
        PAIRED_BOARD("Paired board"),
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
    
    public static enum WinningLosingCategory implements iFlag {
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
    
    public static enum HandCategory implements iFlag {
        
        HIDDEN_PAIR("Pocket pair"),
        PAIR_OVERCARDS_0("Pair 0 overcards"), //top pair or overpair
        PAIR_OVERCARDS_1("Pair 1 overcards"), //mid pair or hidden between 1st and 2nd
        PAIR_OVERCARDS_2("Pair 2 overcards"), //low pair or hidden between 2nd and 3rd
        PAIR_OVERCARDS_3("Pair 3 or more overcards"), // very low pair or hidden below 3rd best visible rank
        PAIR_ON_PAIRED_BOARD("Pair on board"),
        TWO_PAIR("Two pair (paired board)"),
        HIDDEN_TWO_PAIR("Two pair (unpaired board)"),
        VISIBLE_SET("Visible set"),
        HIDDEN_SET("Hidden set"),
        FLUSH("Flush"),
        FLUSH_DRAW("Flush draw"),
        STRAIGHT("Straight"),
        STRAIGHT_DRAW_2("Straight draw"),
        STRAIGHT_DRAW_1("Gut shot"),
        FULL_HOUSE("Full house"),
        HIGH_CARD("High card"),
        QUADS("4 of a kind")
        ;
        
        HandCategory(String desc) {
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
    
    public static enum HandSubCategory implements iFlag {
        
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
