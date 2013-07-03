package pkr;

public class Evaluation implements Comparable<Evaluation>{
    private HoleCards holeCards;
    private Score score;
    
    
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
    
    
}
