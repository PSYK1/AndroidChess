package com.example.chess.chessBoard;

/**
 * A class to represent a single move on the chess board
 * 
 * @author Sai Kalagotla
 *
 */
public class Move {
	/**
	 * The row of the starting point
	 */
	int row1;
	/**
	 * The column of the starting point
	 */
	int col1;
	/**
	 * The row of the end point
	 */
	int row2;
	/**
	 * The column of the end point
	 */
	int col2;
	
	/**
	 * Constructor sets the position for the starting point and the end point of the move
	 * 
	 * @param row1 The row of the starting point for the move
	 * @param col1 The column of the starting point for the move
	 * @param row2 The row of the end point for the move
	 * @param col2 The column of the end point for the move
	 */
	public Move(int row1, int col1, int row2, int col2) {
		this.row1 = row1;
		this.col1 = col1;
		this.row2 = row2;
		this.col2 = col2;
	}

	/**
	 * Returns the row of the starting point
	 * 
	 * @return Integer, the row of the starting point
	 */
	public int getRow1() {
		return row1;
	}

	/**
	 * Sets the row of the starting point
	 * 
	 * @param row1 Integer, the row of the starting point
	 */
	public void setRow1(int row1) {
		this.row1 = row1;
	}

	/**
	 * Returns the column of the starting point
	 * 
	 * @return Integer, the column of the starting point
	 */
	public int getCol1() {
		return col1;
	}

	/**
	 * Sets the column of the starting point
	 * 
	 * @param col1 Integer, the column of the starting point
	 */
	public void setCol1(int col1) {
		this.col1 = col1;
	}

	/**
	 * Returns the row of the end point
	 * 
	 * @return Integer, row of the end point
	 */
	public int getRow2() {
		return row2;
	}

	/**
	 * Sets the row of the end point
	 * 
	 * @param row2 Integer, the row of the end point
	 */
	public void setRow2(int row2) {
		this.row2 = row2;
	}

	/**
	 * Returns the column of the end point
	 * 
	 * @return Integer, the column of the end point
	 */
	public int getCol2() {
		return col2;
	}

	/**
	 * Sets the column of the end point
	 * 
	 * @param col2 Integer, the column of the end point
	 */
	public void setCol2(int col2) {
		this.col2 = col2;
	}
	
	/**
	 * Prints the move
	 */
	public String toString() {
		return "(" + row1 + ", " + col1 + ") to (" + row2 + ", " + col2 + ")";
	}
}
