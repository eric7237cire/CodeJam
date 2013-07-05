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
public class Evaluation implements Comparable<Evaluation>{
    private HoleCards holeCards;
    
    private Score flopScore;
    private Score turnScore;
    private Score riverScore;
    private int position;
    
    //1st 2nd 3rd etc
    boolean won = false;
    boolean tied = false;
    
    FlopTextureNode flopTexture;
    public Score getFlopScore()
    {
        return flopScore;
    }
    public void setFlopScore(Score flopScore)
    {
        this.flopScore = flopScore;
    }
    public Score getTurnScore()
    {
        return turnScore;
    }
    public void setTurnScore(Score turnScore)
    {
        this.turnScore = turnScore;
    }
    public Score getRiverScore()
    {
        return riverScore;
    }
    public void setRiverScore(Score riverScore)
    {
        this.riverScore = riverScore;
    }
    public FlopTextureNode getFlopTexture()
    {
        return flopTexture;
    }
    public void setFlopTexture(FlopTextureNode flopTexture)
    {
        this.flopTexture = flopTexture;
    }
    public FlopTextureNode getTurnTexture()
    {
        return turnTexture;
    }
    public void setTurnTexture(FlopTextureNode turnTexture)
    {
        this.turnTexture = turnTexture;
    }
    public FlopTextureNode getRiverTexture()
    {
        return riverTexture;
    }
    public void setRiverTexture(FlopTextureNode riverTexture)
    {
        this.riverTexture = riverTexture;
    }
    public EvaluationNode getFlopEval()
    {
        return flopEval;
    }
    public void setFlopEval(EvaluationNode flopEval)
    {
        this.flopEval = flopEval;
    }
    public EvaluationNode getTurnEval()
    {
        return turnEval;
    }
    public void setTurnEval(EvaluationNode turnEval)
    {
        this.turnEval = turnEval;
    }
    public EvaluationNode getRiverEval()
    {
        return riverEval;
    }
    public void setRiverEval(EvaluationNode riverEval)
    {
        this.riverEval = riverEval;
    }
    FlopTextureNode turnTexture;
    FlopTextureNode riverTexture;
    EvaluationNode flopEval;
    EvaluationNode turnEval;
    EvaluationNode riverEval;
    
    //1 if won, a fraction for a tie, 0 if lost
    double realEquity;
    
    public Evaluation() {
        super();
        
        riverScore = new Score();
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
        return riverScore;
    }
    @Override
    public String toString()
    {
        return "Score " + riverScore.toString() + " pos " 
    + getPosition() + " eq " + getRealEquity() + " won ? " + won + " tied ? "  + tied;
        
    }
    /**
     * @param score the score to set
     */
    public void setScore(Score score) {
        this.riverScore = score;
    }
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Evaluation o) {
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
