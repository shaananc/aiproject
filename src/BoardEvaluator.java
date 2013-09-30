
public abstract class BoardEvaluator implements Piece {
	// TODO: experiment with making "evaluate" a function of depth - smaller depth = higher utility

	int n;
	int player;
	
	public BoardEvaluator(int n, int player) {
		this.n = n;
		this.player = player;
	}
	
	public abstract int evaluate(MitchBoard gb, int player);
	
	public int evaluate(Node node) {
		return evaluate(node.gb, player);
	}
}
