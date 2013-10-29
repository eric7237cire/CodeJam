package chess.parsing;


public class Move {
    public Move() {
        super();
        whiteMove = new Ply();
        blackMove = new Ply();
    }
    int moveNumber;
    Ply whiteMove;
    Ply blackMove;
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return moveNumber + ". " + 
     (whiteMove != null ? whiteMove.sanTxt : ".." ) + " " + (blackMove != null ? blackMove.sanTxt : '*');
    }
    /**
     * @return the moveNumber
     */
    public int getMoveNumber() {
        return moveNumber;
    }
    /**
     * @param moveNumber the moveNumber to set
     */
    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }
    /**
     * @return the whiteMove
     */
    public Ply getWhiteMove() {
        return whiteMove;
    }
    /**
     * @param whiteMove the whiteMove to set
     */
    public void setWhiteMove(Ply whiteMove) {
        this.whiteMove = whiteMove;
    }
    /**
     * @return the blackMove
     */
    public Ply getBlackMove() {
        return blackMove;
    }
    /**
     * @param blackMove the blackMove to set
     */
    public void setBlackMove(Ply blackMove) {
        this.blackMove = blackMove;
    }
}
