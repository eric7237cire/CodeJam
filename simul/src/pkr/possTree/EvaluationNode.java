package pkr.possTree;

import javax.xml.stream.XMLStreamWriter;

import pkr.possTree.FlopTextureNode.TextureCategory;

public class EvaluationNode implements iDisplayNode
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
        
        return flags;
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
        EvaluationNode other = (EvaluationNode) obj;
        if (flags != other.flags)
            return false;
        return true;
    }
    
    
}
