
public abstract class MoveFinder implements Piece {
	int n;
	int player;
	BoardEvaluator evaluator;
	int MAX_DEPTH = 6;
	public static final int INF = Integer.MAX_VALUE / 2;
	
	public MoveFinder(int n, int player) {
		this.n = n;
		this.player = player;
		this.evaluator = new AdvancedBoardEvaluator(n, player);
	}
		
	public abstract Move getMove(Gameboard gb);
	
	/* following methods for experimentation purposes */
	public void setEvaluator(BoardEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	public void setMaxDepth(int maxDepth) {
		this.MAX_DEPTH = maxDepth;
	}
}
