import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

/**
 * 
 */

/**
 * @author berina
 *
 */
public class Solver {
	
	private boolean solveable;
	private int moves;
	private SearchNode lastNode;
	
	private class SearchNode implements Comparable<SearchNode> {
		
		Board board;
		SearchNode prev;
		int moves;
		
		public SearchNode(Board board, SearchNode prev, int moves) {
			this.board = board;
			this.prev = prev;
			this.moves = moves;
		}
		
		public Board getCurrent() {
			return board;
		}
		
		public SearchNode getPrev() {
			return prev;
		}
		
		public int getMoves() {
			return moves;
		}

		@Override
		public int compareTo(SearchNode node) {
			return (getCurrent().manhattan() + getMoves() - 
					(node.getCurrent().manhattan() + node.getMoves()));
		}
	}
	
	/** find a solution to the initial board (using the A* algorithm) */
	public Solver(Board initial) {
		if (initial == null)
			throw new IllegalArgumentException();
		MinPQ<SearchNode> pq = new MinPQ<>();
		MinPQ<SearchNode> twinPQ = new MinPQ<>();
		SearchNode node = new SearchNode(initial, null, 0);
		SearchNode twinNode = new SearchNode(initial.twin(), null, 0);
		pq.insert(node);
		twinPQ.insert(twinNode);
		while (!node.getCurrent().isGoal() || !twinNode.getCurrent().isGoal()) {
			node = pq.delMin();
			twinNode = twinPQ.delMin();
			if (node.getCurrent().isGoal()) {
				solveable = true;
				if (node.getMoves() < moves || moves == 0) {
					moves = node.getMoves();
					lastNode = node;
				}
				break;
			}
			if (twinNode.getCurrent().isGoal()) {
				solveable = false;
				moves = -1;
				break;
			}
			Iterable<Board> neighbors = node.getCurrent().neighbors();
			for (Board b : neighbors) {
				if (node.getPrev() != null) 
					if (b.equals(node.getPrev().getCurrent()))
						continue;
				SearchNode sn = new SearchNode(b, node, node.moves+1);
				pq.insert(sn);

			}
			Iterable<Board> twinNeighbors = twinNode.getCurrent().neighbors();
			for (Board b : twinNeighbors) {
				if (twinNode.getPrev() != null) 
					if (b.equals(twinNode.getPrev().getCurrent()))
						continue;
				SearchNode sn = new SearchNode(b, twinNode, twinNode.moves+1);
				twinPQ.insert(sn);

			}
//			System.out.println(pq.size());
		}
		
	}
	
	/** is the initial board solvable? */
    public boolean isSolvable() {
    	return solveable;
    }
    
    /** min number of moves to solve initial board; -1 if unsolvable */
    public int moves() {
    	return moves;
    }
    
    /** sequence of boards in a shortest solution; null if unsolvable */
    public Iterable<Board> solution() {
    	if (!isSolvable()) 
    		return null;
    	ArrayList<Board> sol = new ArrayList<>();
    	SearchNode n = lastNode;
    	while (n != null) {
    		sol.add(0, n.getCurrent());
    		n = n.getPrev();
    	}
    	return sol;
    }
    
    /** solve a slider puzzle (given below) */
    public static void main(String[] args) {
    	 In in = new In(args[0]);
    	    int n = in.readInt();
    	    int[][] blocks = new int[n][n];
    	    for (int i = 0; i < n; i++)
    	        for (int j = 0; j < n; j++)
    	            blocks[i][j] = in.readInt();
    	    Board initial = new Board(blocks);

    	    // solve the puzzle
    	    Solver solver = new Solver(initial);

    	    // print solution to standard output
    	    if (!solver.isSolvable())
    	        StdOut.println("No solution possible");
    	    else {
    	        StdOut.println("Minimum number of moves = " + solver.moves());
    	        for (Board board : solver.solution())
    	            StdOut.println(board);
    	    }
    }
}
