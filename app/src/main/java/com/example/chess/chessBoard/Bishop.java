package com.example.chess.chessBoard;


/**
 * Represents a bishop
 * 
 * @author James Hadley, Sai Kalagotla
 *
 */
public class Bishop extends Piece {
		
	/**
	 * 
	 * @param color The color to which the bishop belongs
	 */
	public Bishop(String color) {
		this.color = color;
		this.name = "b";
	}
	

	@Override
	public boolean validFormat(int row1, int col1, int row2, int col2, Board board) {
		// Must be diagonal
		
		if (Math.abs(col2 - col1) == Math.abs(row2 - row1)) {
			return true;
		}
		return false;
	}
	
	
	public Piece copy() {
		Bishop copy = new Bishop(color);
		copy.setMoved(moved);
		return copy;
	}

}
