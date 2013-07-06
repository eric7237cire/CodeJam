package pkr.possTree;

import pkr.possTree.FlopTextureNode.TextureCategory;

public class EvaluationNode
{

    /*
     * Winning yes / no ?
     * 
     * top pair, top kicker ?
     * 2nd pair
     * low pair ?
     * 
     * overpair ?
     * 
     * set or better ?
     * 
     * losing outkicked ?
     * winning by kicker, which one?
     */
    public EvaluationNode() {
        // TODO Auto-generated constructor stub
    }
    
    public enum EvaluationCategory {
        TOP_PAIR("Top pair"),
        OVER_PAIR("Over pair"),
        SECOND_BEST_PAIR("2nd best pair"), //on board
        WINNING("Winning / tied"),
        SECOND_BEST_HAND("Second best hand"),
        LOSING("Losing"),
        BY_HAND("Det by hand"),
        BY_KICKER_1("Determined by first kicker"),
        BY_KICKER_2("Determined by second kicker"),
        BY_KICKER_3_PLUS("Determined by 3rd or later kicker");
        
        ;
        
        
        EvaluationCategory(String desc) {
            this.desc = desc;
        }

        private String desc;

        public String getDesc()
        {
            return desc;
        }
    }

    long flags;
    
    public void setFlag(EvaluationCategory flag) {
        flags |= 1L << flag.ordinal();
    }
    
    public boolean hasFlag(EvaluationCategory flag) {
        return (flags & 1L << flag.ordinal()) != 0;
    }
}
