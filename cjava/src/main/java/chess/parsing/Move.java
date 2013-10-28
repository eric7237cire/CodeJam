package chess.parsing;

import java.util.List;

import chess.Piece;

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
        return moveNumber + ". " + whiteMove.sanTxt + " " + (blackMove != null ? blackMove.sanTxt : '*');
    }
}
