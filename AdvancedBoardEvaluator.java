import java.util.ArrayList;


public class AdvancedBoardEvaluator extends BoardEvaluator implements Piece {
	// TODO: experiment with making "evaluate" a function of depth - smaller depth = higher utility

	ArrayList<Integer> weights;
	ArrayList<Integer> features;
	public static final int TRAINING_SIZE = 10;
	
	public AdvancedBoardEvaluator(int n, int player) {
		super(n, player);
		weights = new ArrayList<Integer>();
		weights.add(10);
		weights.add(10);
	}
	
	@Override
	public int evaluate(Node node) {
		features = new ArrayList<Integer>();
		fillFeatureValues(node, features);
		
		if (weights.size() != features.size()) {
			System.out.println("Error: size of weights ArrayList doesn't match");
			System.out.println("size of features ArrayList");
			System.exit(1);
		}
		
		int evaluation = 0;
		for (int i = 0; i < weights.size(); i++) {
			evaluation += weights.get(i) * features.get(i);
		}
		
		return evaluation;
	}
	
	@Override
	public void setWeights(ArrayList<Integer> weights) {
		this.weights = weights;
	}
	
	public void fillFeatureValues(Node node, ArrayList<Integer> features) {
		// TODO: combine some feature functions into one board iteration
		features.add(f1(node));
		features.add(f2(node));
	}
	
	// num player - num enemy
	public int f1(Node node) {
		int numPlayer = 0;
		int numEnemy = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				int piece = node.gb.board[i * n + j];
				if (player == WHITE) {
					if (piece == WHITE) {
						numPlayer++;
					} else if (piece == BLACK) {
						numEnemy++;
					}
				} else {
					// player == BLACK
					if (piece == BLACK) {
						numPlayer++;
					} else if (piece == WHITE) {
						numEnemy++;
					}
				}
			}
		}
		
		return numPlayer - numEnemy;
	}
	
	// number of unjumpable pieces - minus enemy's
	public int f2(Node node) {
		int numPlayer = 0;
		int numEnemy = 0;
		int enemy;
		if (player == WHITE) {
			enemy = BLACK;
		} else {
			enemy = WHITE;
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (node.gb.board[i*n + j] == player) {
					boolean jumpable = node.gb.isJumpable(i, j, player);
					if (!jumpable) {
						numPlayer++;
					}
				} else if (node.gb.board[i*n + j] == enemy) {
					boolean jumpable = node.gb.isJumpable(i, j, enemy);
					if (!jumpable) {
						numEnemy++;
					}
				}
			}
		}
		
		return numPlayer - numEnemy;
	}
}
