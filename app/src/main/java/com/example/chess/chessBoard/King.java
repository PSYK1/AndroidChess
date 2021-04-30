package com.example.chess.chessBoard;

/**
 * Represents a king
 * 
 * @author James Hadley, Sai Kalagotla
 *
 */
public class King extends Piece {
	
	/**
	 * 
	 * @param color The color to which the king belongs
	 */
	public King(String color) {
		this.moved = false;
		this.color = color;
		this.name = "k";
	}
	

	@Override
	public boolean validFormat(int row1, int col1, int row2, int col2, Board board) {
		// Must be one tile away or castling
		
		// Check if Castling
		
		if (!moved && row1 == row2 && Math.abs(col2-col1) == 2) {
			// We must be castling, so check that rook hasn't moved either
			// Rest is checked elsewhere
			int rookCol = 0;
			if (col2 > 3) rookCol = 7; 
			if (board.getPiece(row1, rookCol) == null) {
				return false;
			} else if (board.getPiece(row1, rookCol).hasMoved()) {
				return false;
			}
			
			return true;
		}
		
		if (Math.abs(col2 - col1) < 2 && Math.abs(row2 - row1) < 2) {
	        return true;
	    }
	    return false;
	}
	
	
	public Piece copy() {
		King copy = new King(color);
		copy.setMoved(moved);
		return copy;
	}
}