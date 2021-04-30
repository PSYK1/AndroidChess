package com.example.chess.chessBoard;

/**
 * Represents a queen
 * 
 * @author James Hadley, Sai Kalagotla
 *
 */
public class Queen extends Piece {
		
	/**
	 * 
	 * @param color The color to which the queen belongs
	 */
	public Queen(String color) {
		this.color = color;
		this.name = "q";
	}

	
	@Override
	public boolean validFormat(int row1, int col1, int row2, int col2, Board board) {
		// Must be diagonal or a straight line
		
		if (Math.abs(col2 - col1) == Math.abs(row2 - row1)) { // Diagonal
			return true;
		}
		
		if (Math.abs(col2 - col1) == 0 || Math.abs(row2 - row1) == 0) { // Straight line
			return true;
		}
		return false;
	}
	
	
	public Piece copy() {
		Queen copy = new Queen(color);
		copy.setMoved(moved);
		return copy;
	}
}