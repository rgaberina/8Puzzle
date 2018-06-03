import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

/**
 * 
 */

/**
 * @author berina
 *
 */
public class Board {

	private final int[][] board;
	private final int n;
	private int hamming;
	private int manhattan;
	private int spaceX;
	private int spaceY;

	/** construct a board from an n-by-n array of blocks 
	 * (where blocks[i][j] = block in row i, column j) 
	 * assume that the constructor receives an n-by-n array 
	 * containing the n2 integers between 0 and n2 − 1, 
	 * where 0 represents the blank square.
	 * */
	public Board(int[][] blocks) {
		n = blocks.length;
		hamming = 0;
		manhattan = 0;
		board = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				board[i][j] = blocks[i][j];
				if (!isBlockCorrect(i, j)) {
					hamming++;
					manhattan += calculateManhattan(i, j);
				}
				if (board[i][j] == 0) {
					spaceX = i;
					spaceY = j;
				}
			}
		}
	}

	/** board dimension n */
	public int dimension() {
		return n;
	}

	private int calculateManhattan(int i, int j) {
		int a = board[i][j];
		if (a == 0) 
			return 0;
		int propI = (a - 1) / n;
		int propJ = (a - 1) % n;
		return Math.abs(propI - i) + Math.abs(propJ - j);
	}

	private int getCorrectBlock(int i, int j) {
		return (i * n) + j + 1;
	}

	private boolean isBlockCorrect(int i, int j) {
		if (board[i][j] == 0)
			return true;
		int correct = getCorrectBlock(i, j);
		return (board[i][j] == correct);
	}

	/** number of blocks out of place */
	public int hamming() {
		return hamming;
	}

	/** sum of Manhattan distances between blocks and goal */
	public int manhattan() {
		return manhattan;
	}

	/** is this board the goal board? */
	public boolean isGoal() {
		return hamming == 0;
	}

	/** a board that is obtained by exchanging any pair of blocks */
	public Board twin() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (board[i][j] != 0) {
					if (i < (n-1) && board[i+1][j] != 0)
						return move(i, j, i+1, j);
					if (j < (n-1) && board[i][j+1] != 0)
						return move(i, j, i, j+1);
				}
			}
		}
		return null;
	}

	/** does this board equal y? */
	public boolean equals(Object y) {
		if ((y == null) || (y.getClass() != Board.class))
			return false;
		Board b = (Board) y;
		if ((b.dimension() != dimension()) || (b.manhattan() != manhattan()) 
				|| (hamming() != b.hamming))
			return false;
		for (int i = 0; i < dimension(); i++) 
			for (int j = 0; j < dimension(); j++)
				if (b.board[i][j] != board[i][j])
					return false;
		return true;

	}

	/** all neighboring boards */
	public Iterable<Board> neighbors() {
		ArrayList<Board> neighbors = new ArrayList<>();
		if (spaceX > 0) 
			neighbors.add(move(spaceX, spaceY, spaceX-1, spaceY));
		if (spaceX < n-1)
			neighbors.add(move(spaceX, spaceY, spaceX+1, spaceY));
		if (spaceY > 0) 
			neighbors.add(move(spaceX, spaceY, spaceX, spaceY-1));
		if (spaceY < n-1)
			neighbors.add(move(spaceX, spaceY, spaceX, spaceY+1));
		return neighbors;
	}
	
	private int[][] copyBoard() {
		int[][] boardCopy = new int[n][n];
		for (int i = 0; i < n; i++) 
			for (int j = 0; j < n; j++) 
				boardCopy[i][j] = board[i][j];
		return boardCopy;
	}

	/** move the tile in (a,b) to (c,d) */
	private Board move(int a, int b, int c, int d) {
		int[][] newBoard = copyBoard();
		int temp = newBoard[a][b];
		newBoard[a][b] = newBoard[c][d];
		newBoard[c][d] = temp;
		return new Board(newBoard);
	}

	/** string representation of this board (in the output format specified below) */
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append(n + "\n");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) 
				string.append(String.format("%2d ", board[i][j]));
			string.append("\n");
		}
		return string.toString();
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
	    int n = in.readInt();
	    int[][] blocks = new int[n][n];
	    for (int i = 0; i < n; i++)
	        for (int j = 0; j < n; j++)
	            blocks[i][j] = in.readInt();
	    Board initial = new Board(blocks);
	    System.out.println(initial.twin());
	}

}
