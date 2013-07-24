package pkr;

import pkr.possTree.PossibilityNode;
import pkr.possTree.PossibilityNode.HandCategory;
import pkr.possTree.PossibilityNode.HandSubCategory;
import pkr.possTree.PossibilityNode.TextureCategory;
import pkr.possTree.PossibilityNode.WinningLosingCategory;

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
    

    HoleCards[] bestHand;
    
    HoleCards[] secondBestHand;

    
    //indexs -- round, level [ texture / win losisg / win losisg sub ]
    PossibilityNode[][] possibilityNodes;
        
    //1 if won, a fraction for a tie, 0 if lost
    double realEquity;
    
    boolean quads = false;
    
    public CompleteEvaluation() {
        super();
        
        
        roundScores = new Score[3];
        possibilityNodes = new PossibilityNode[3][4];
        
        bestHand = new HoleCards[3];
        secondBestHand = new HoleCards[3];
        
        for(int i = 0; i < 3; ++i) 
        {
            roundScores[i] = new Score();
            possibilityNodes[i][PossibilityNode.Levels.TEXTURE.ordinal()] = new PossibilityNode(PossibilityNode.TextureCategory.values());
            possibilityNodes[i][PossibilityNode.Levels.WIN_LOSE.ordinal()] = new PossibilityNode(PossibilityNode.WinningLosingCategory.values());
            possibilityNodes[i][PossibilityNode.Levels.HAND_CATEGORY.ordinal()] = new PossibilityNode(PossibilityNode.HandCategory.values());
            possibilityNodes[i][PossibilityNode.Levels.HAND_SUB_CATEGORY.ordinal()] = new PossibilityNode(PossibilityNode.HandSubCategory.values());
        }
    }
    
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
    
    public void addSecondBestHand(int round, HoleCards hc) {
        
        
       secondBestHand[round] = hc;
        
        
       // possibilityNodes[round][PossibilityNode.Levels.WIN_LOSE.ordinal()].setHoleCards(hc);
       // possibilityNodes[round][PossibilityNode.Levels.HAND_SUB_CATEGORY.ordinal()].setHoleCards(hc);
    }
    
    public void addBestHand(int round, HoleCards hc) {
        bestHand[round] = hc;
       /*for(int i = 0; i < PossibilityNode.Levels.values().length; ++i) {
            possibilityNodes[round][PossibilityNode.Levels.WIN_LOSE.ordinal()].setHoleCards(hc);
            possibilityNodes[round][PossibilityNode.Levels.HAND_SUB_CATEGORY.ordinal()].setHoleCards(hc);
        }*/
    }
    
    public HoleCards getSecondBestHand(int round) 
    {
        return secondBestHand[round];
    }
    
    public HoleCards getBestHand(int round) 
    {
        return bestHand[round];
    }
     
    public void setPossibilityNode(int round, int level, PossibilityNode node)
    {
        possibilityNodes[round][level] = node;
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
    + getPosition() + " eq " + getRealEquity() ;
        
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
