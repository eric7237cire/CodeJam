package pkr;

import pkr.possTree.EvaluationNode;
import pkr.possTree.FlopTextureNode;

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
    
    FlopTextureNode[] roundTexture;
    EvaluationNode[] roundEval;
    
    //1 if won, a fraction for a tie, 0 if lost
    double realEquity;
    
    public Score getRoundScore(int round) {
        return roundScores[round];
    }
    public void setRoundScore(int round, Score score) 
    {
        roundScores[round] = score;
    }
    public FlopTextureNode getRoundTexture(int round)
    {
        return roundTexture[round];
    }
    public void setRoundTexture(int round, FlopTextureNode roundTexture)
    {
        this.roundTexture[round] = roundTexture;
    }
   
    public EvaluationNode getRoundEval(int round)
    {
        return roundEval[round];
    }
    public void setRoundEval(int round, EvaluationNode flopEval)
    {
        this.roundEval[round] = flopEval;
    }
    
    
    public CompleteEvaluation() {
        super();
        
        
        roundScores = new Score[3];
        roundEval = new EvaluationNode[3];
        roundTexture = new FlopTextureNode[3];
        
        for(int i = 0; i < 3; ++i) 
        {
            roundScores[i] = new Score();
            roundEval[i] = new EvaluationNode();
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
