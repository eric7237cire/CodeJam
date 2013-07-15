package pkr.possTree;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class PossibilityNode implements iDisplayNode {

    public static enum Levels {
        TEXTURE,
        WIN_LOSE,
        HAND_CATEGORY,
        HAND_SUB_CATEGORY
    }
    
    

    public enum TextureCategory  implements iFlag {
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
        
        OVER_PAIR("Over pair"),
        TOP_PAIR("Pair"),
        SECOND_VISIBLE_PAIR("2nd Pair"),
        PAIR("3rd Pair"),
        TWO_PAIR("Two pair"),
        HIDDEN_TWO_PAIR("Two pair (unpaired board)"),
        VISIBLE_SET("Visible set"),
        HIDDEN_SET("Hidden set"),
        FLUSH("Flush"),
        STRAIGHT("Straight")
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
        BY_KICKER_2_PLUS("Determined by 2nd or later kicker");
        
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

    public PossibilityNode(iFlag[] allFlags) {
        this.allFlags = allFlags;
    }

    // Map<iDisplayNode>
    public List<iDisplayNode> getChildren() {
        return null;
    }

    long flags;

    public void setFlag(iFlag flag) {
        flags |= 1L << flag.getIndex();
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
        return Joiner.on("--").join(ret) + "-" + flags;
    }
}
