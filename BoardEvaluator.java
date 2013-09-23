import java.util.ArrayList;



abstract public class BoardEvaluator implements Piece {

	int n;
	int player;
	
	public BoardEvaluator(int n, int player) {
		this.n = n;
		this.player = player;
	}
		
	// evaluate how "good" a node is
	abstract public int evaluate(Node node);
	
	public void setWeights(ArrayList<Integer> weights) {}
	
	// just use number of player pieces minus enemy
	public void setProjectedUtility(Node node) {
		int numPlayer = 0;
		int numEnemy = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				int piece = node.gb.board[i * n + j];
				if (piece == BLACK) {
					if (player == BLACK) {
						numPlayer++;
					} else {
						numEnemy++;
					}
				} else if (piece == WHITE) {
					if (player == WHITE) {
						numPlayer++;
					} else {
						numEnemy++;
					}
				}
			}
		}
		
		node.projectedUtility = numPlayer - numEnemy;
	}
}
