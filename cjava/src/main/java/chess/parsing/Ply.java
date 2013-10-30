package chess.parsing;

import java.util.List;

import chess.Piece;

public class Ply {
    boolean isWhiteMove;

    Piece movedPiece;

    Piece capturedPiece;

    int moveNumber;

    List<List<Move>> variations;
    
    String sanTxt;
    
    String comment;
    
    String nagComment;
    Integer nagValue;
    

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(moveNumber);
        if (isWhiteMove) {
            ret.append(". ");
        } else {
            ret.append("... ");
        }
        ret.append(sanTxt);
        if (comment != null) {
            ret.append(" ");
            ret.append("Comment : ");
            ret.append(comment);
        }
        return ret.toString();
    }


    /**
     * @return the isWhiteMove
     */
    public boolean isWhiteMove() {
        return isWhiteMove;
    }


    /**
     * @param isWhiteMove the isWhiteMove to set
     */
    public void setWhiteMove(boolean isWhiteMove) {
        this.isWhiteMove = isWhiteMove;
    }


    /**
     * @return the movedPiece
     */
    public Piece getMovedPiece() {
        return movedPiece;
    }


    /**
     * @param movedPiece the movedPiece to set
     */
    public void setMovedPiece(Piece movedPiece) {
        this.movedPiece = movedPiece;
    }


    /**
     * @return the capturedPiece
     */
    public Piece getCapturedPiece() {
        return capturedPiece;
    }


    /**
     * @param capturedPiece the capturedPiece to set
     */
    public void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
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
     * @return the variations
     */
    public List<List<Move>> getVariations() {
        return variations;
    }


    /**
     * @param variations the variations to set
     */
    public void setVariations(List<List<Move>> variations) {
        this.variations = variations;
    }


    /**
     * @return the sanTxt
     */
    public String getSanTxt() {
        return sanTxt;
    }


    /**
     * @param sanTxt the sanTxt to set
     */
    public void setSanTxt(String sanTxt) {
        this.sanTxt = sanTxt;
    }


    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }


    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }


    /**
     * @return the nagComment
     */
    public String getNagComment() {
        return nagComment;
    }


    /**
     * @param nagComment the nagComment to set
     */
    public void setNagComment(String nagComment) {
        this.nagComment = nagComment;
    }


    /**
     * @return the nagValue
     */
    public Integer getNagValue() {
        return nagValue;
    }


    /**
     * @param nagValue the nagValue to set
     */
    public void setNagValue(Integer nagValue) {
        this.nagValue = nagValue;
    }
}
