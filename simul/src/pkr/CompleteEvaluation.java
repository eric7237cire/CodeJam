package pkr;

import pkr.possTree.PossibilityNode;
import pkr.possTree.PossibilityNode.WinningLosingCategory;

import pkr.possTree.PossibilityNode.TextureCategory;
import pkr.possTree.PossibilityNode.WinningLosingCategory;
import pkr.possTree.PossibilityNode.HandSubCategory;
import pkr.possTree.PossibilityNode.HandCategory;

/*
 * winning at flop
 * [top pair, mid or lower pair, overpair, two pair, set, or flush/str8]
 * 
 * winning at turn
 *  
 * winning at river
 * 
 * 
 */
public class CompleteEvaluation implements Comparable<CompleteEvaluation>{
    private HoleCards holeCards;
    
    private Score[] roundScores;
    
    private int position;
    
    public static int ROUND_FLOP = 0;
    public static int ROUND_TURN = 1;
    public static int ROUND_RIVER = 2;
    
    //1st 2nd 3rd etc
    boolean won = false;
    boolean tied = false;
    
    //indexs -- round, level [ texture / win losisg / win losisg sub ]
    PossibilityNode[][] possibilityNodes;
        
    //1 if won, a fraction for a tie, 0 if lost
    double realEquity;
    
    boolean quads = false;
    
    public Score getRoundScore(int round) {
        return roundScores[round];
    }
    public void setRoundScore(int round, Score score) 
    {
        if (score.handLevel == HandLevel.QUADS) {
            quads = true;
        }
        roundScores[round] = score;
    }
    public PossibilityNode getPossibilityNode(int round, int level)
    {
        return possibilityNodes[round][level];
    }
    
    public boolean hasFlag(int round, WinningLosingCategory cat) 
    {
        return possibilityNodes[round][PossibilityNode.Levels.WIN_LOSE.ordinal()].hasFlag(cat);
    }
    
    public boolean hasFlag(int round, TextureCategory cat) 
    {
        return possibilityNodes[round][PossibilityNode.Levels.TEXTURE.ordinal()].hasFlag(cat);
    }
    
    public boolean hasFlag(int round, HandCategory cat) 
    {
        return possibilityNodes[round][PossibilityNode.Levels.HAND_CATEGORY.ordinal()].hasFlag(cat);
    }
    
    public boolean hasFlag(int round, HandSubCategory cat) 
    {
        return possibilityNodes[round][PossibilityNode.Levels.HAND_SUB_CATEGORY.ordinal()].hasFlag(cat);
    }
    
    public void setFlag(int round, WinningLosingCategory cat) 
    {
        possibilityNodes[round][PossibilityNode.Levels.WIN_LOSE.ordinal()].setFlag(cat);
    }
    
    public void setFlag(int round, TextureCategory cat) 
    {
        possibilityNodes[round][PossibilityNode.Levels.TEXTURE.ordinal()].setFlag(cat);
    }
    
    public void setFlag(int round, HandCategory cat) 
    {
        possibilityNodes[round][PossibilityNode.Levels.HAND_CATEGORY.ordinal()].setFlag(cat);
    }
    
    public void setFlag(int round, HandSubCategory cat) 
    {
        possibilityNodes[round][PossibilityNode.Levels.HAND_SUB_CATEGORY.ordinal()].setFlag(cat);
    }
     
    public void setPossibilityNode(int round, int level, PossibilityNode node)
    {
        possibilityNodes[round][level] = node;
    }
    
    
    public CompleteEvaluation() {
        super();
        
        
        roundScores = new Score[3];
        possibilityNodes = new PossibilityNode[3][4];
        
        for(int i = 0; i < 3; ++i) 
        {
            roundScores[i] = new Score();
            possibilityNodes[i][0] = new PossibilityNode(PossibilityNode.TextureCategory.values());
            possibilityNodes[i][1] = new PossibilityNode(PossibilityNode.WinningLosingCategory.values());
            possibilityNodes[i][2] = new PossibilityNode(PossibilityNode.HandCategory.values());
            possibilityNodes[i][3] = new PossibilityNode(PossibilityNode.HandSubCategory.values());
        }
    }
    /**
     * @return the holeCards
     */
    public HoleCards getHoleCards() {
        return holeCards;
    }
    /**
     * @param holeCards the holeCards to set
     */
    public void setHoleCards(HoleCards holeCards) {
        this.holeCards = holeCards;
    }
    /**
     * @return the score
     */
    public Score getScore() {
        return getRoundScore(ROUND_RIVER);
    }
    @Override
    public String toString()
    {
        return "Score " + getScore().toString() + " pos " 
    + getPosition() + " eq " + getRealEquity() + " won ? " + won + " tied ? "  + tied;
        
    }
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(CompleteEvaluation o) {
        return getScore().compareTo(o.getScore());
    }
    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }
    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }
    /**
     * @return the won
     */
    public boolean isWon() {
        return won;
    }
    /**
     * @param won the won to set
     */
    public void setWon(boolean won) {
        this.won = won;
    }
    /**
     * @return the tied
     */
    public boolean isTied() {
        return tied;
    }
    /**
     * @param tied the tied to set
     */
    public void setTied(boolean tied) {
        this.tied = tied;
    }
    /**
     * @return the realEquity
     */
    public double getRealEquity() {
        return realEquity;
    }
    /**
     * @param realEquity the realEquity to set
     */
    public void setRealEquity(double realEquity) {
        this.realEquity = realEquity;
    }
    
    
}
