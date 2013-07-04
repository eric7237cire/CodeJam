package pkr;

public class Evaluation implements Comparable<Evaluation>{
    private HoleCards holeCards;
    private Score score;
    private int position;
    
    //1st 2nd 3rd etc
    boolean won = false;
    boolean tied = false;
    
    //1 if won, a fraction for a tie, 0 if lost
    double realEquity;
    
    public Evaluation() {
        super();
        
        score = new Score();
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
        return score;
    }
    /**
     * @param score the score to set
     */
    public void setScore(Score score) {
        this.score = score;
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
