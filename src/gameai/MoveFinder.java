package gameai;
import java.util.ArrayList;


public abstract class MoveFinder implements Piece {
	int n;
	int player;
	int MAX_DEPTH = 4;
	double[] weights;
        
        boolean useQF = false;
        
	public static final int TRAINING_SIZE = 2;
	public static final int INF = Integer.MAX_VALUE / 2;
	public static final int NUM_FEATURES = 2;
	public static final double DEFAULT_WEIGHT = 1.0 / NUM_FEATURES;
	public static final double ETA = 0.1;	// learning rate for gradient descent
	
	public MoveFinder(int n, int player) {
		this.n = n;
		this.player = player;
		initialiseWeights();
	}
	
	public void initialiseWeights() {
		weights = new double[NUM_FEATURES];
		
		for (int i = 0; i < NUM_FEATURES; i++) {
			weights[i] = DEFAULT_WEIGHT;
		}
			
		// gradient descent
		/*
		System.out.println("Initialising weights...");
		
		ArrayList<Node> trainingSet = Node.getTrainingSet(TRAINING_SIZE, n, player);
		for (Node node : trainingSet) {
			System.out.println("using random node:");
			System.out.println(node.gb);
			System.out.println("weights before: " + weights[0] + ", " + weights[1]);
			double eval = evaluate(node);
			double trueUtil = getTrueUtility(node);
			while (Math.abs(eval - trueUtil) > EPS) {
				System.out.println("eval = " + eval);
				System.out.println("trueUtil = " + trueUtil);
				weights[0] -= ETA * (eval - trueUtil) * f1(node);
				weights[1] -= ETA * (eval - trueUtil) * f2(node);
				eval = evaluate(node);
				trueUtil = getTrueUtility(node);


			}
			System.out.println("weights after: " + weights[0] + ", " + weights[1]);
		}
		
		System.out.println("\nFinal weights:");
		System.out.println("w1 = " + weights[0]);
		System.out.println("w2 = " + weights[1]);
		*/
	}
		
	public abstract Move getMove(GameBoardMitch gb);
	public abstract double getTrueUtility(Node node);
	
	public double evaluate(Node node) {
		int w = node.gb.getWinner();
		if (w != EMPTY) {
			if (w == player) {
				return 1.0;
			} else if (w == DEAD || w == INVALID) {
				return 0.0;
			} else {
				return -1.0;
			}
		}
		
		double[] features = new double[NUM_FEATURES];
		features[0] = f1(node);
		features[1] = f2(node);
		
		// eval = w1*f1(s) + w2*f2(s)...
		double eval = 0;
		for (int i = 0; i < NUM_FEATURES; i++) {
			eval += weights[i] * features[i];
		}
		
		//return eval;
		return Math.tanh(eval);
	}
	
	// num player - num enemy
	public double f1(Node node) {
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
		
		return (double) (numPlayer - numEnemy) / n*n;
	}
	
	// number of unjumpable pieces - minus enemy's
	// TODO: take into account HOW jumpable a piece is, rather than binary yes/no
	public double f2(Node node) {
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
					// TODO: remove player from isJumpable header
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
		
		return (double) (numPlayer - numEnemy) / n*n;
	}
	
	public void setWeights(double[] weights) {
		this.weights = weights;
	}
	
	public void setMaxDepth(int maxDepth) {
		this.MAX_DEPTH = maxDepth;
	}
}
