package chess;

public enum Piece {
    Pawn('p'),
    Knight('n'),
    Bishop('b'),    
    Rook('r'),
    Queen('q'),
    King('k');
    
    private char ch;
    
    Piece(char pieceCh) {
        this.ch = pieceCh;
    }
}
