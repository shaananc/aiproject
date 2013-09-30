

public abstract class MoveFinder implements Piece {
	int n;
	int player;
	BoardEvaluator evaluator;
	
	public MoveFinder(int n, int player) {
		this.n = n;
		this.player = player;
		evaluator = new StaticBoardEvaluator(n, player);
	}
	
	public abstract Move getMove(MitchBoard gb);
}
