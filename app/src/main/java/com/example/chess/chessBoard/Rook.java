package com.example.chess.chessBoard;

/**
 * Represents a rook
 * 
 * @author James Hadley, Sai Kalagotla
 *
 */
public class Rook extends Piece {
		
	/**
	 * 
	 * @param color The color to which the rook belongs
	 */
	public Rook(String color) {
		this.color = color;
		this.name = "r";
	}

	
	@Override
	public boolean validFormat(int row1, int col1, int row2, int col2, Board board) {
		// Must be a straight line
		if (Math.abs(col2 - col1) == 0 || Math.abs(row2 - row1) == 0) {
			return true;
		}
		return false;
	}
	
	
	public Piece copy() {
		Rook copy = new Rook(color);
		copy.setMoved(moved);
		return copy;
	}
}