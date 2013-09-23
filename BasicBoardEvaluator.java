
public class BasicBoardEvaluator extends BoardEvaluator implements Piece {

	// use these switches to change which evaluation/projectedUtility method we call
	int EVAL_SWITCH = 1;
	int PROJ_UTIL_SWITCH = 1;
	
	public BasicBoardEvaluator(int n, int player) {
		super(n, player);
	}
	
	// evaluate how "good" a node is
	@Override
	public int evaluate(Node node) {
		
		switch (EVAL_SWITCH) {
		case 1:
			return eval1(node);
		case 2:
			return eval2(node);
		case 3:
			return eval3(node);
		default:
			System.out.println("Error: EVAL_SWITCH not set correctly");
			System.exit(1);
		}
		
		// can't reach here
		return INVALID;
	}
	
	// num player pieces minus num enemy pieces
	public int eval1(Node node) {
	
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
		
		return numPlayer - numEnemy;		
	}
	
	// incorporate density of gameboard multiplicatively
	public int eval2(Node node) {
		int density = node.gb.getDensity();
		return eval1(node) * density;
	}
	
	// incorporate density of gameboard additively
	public int eval3(Node node) {
		int density = node.gb.getDensity();
		return eval1(node) + density;
	}
	
	// used to change which evaluate method is called
	public void setEvalSwitch(int evalSwitch) {
		this.EVAL_SWITCH = evalSwitch;
	}
	
	// used to change which setProjectedUtility method is called
	public void setProjUtilSwitch(int projUtilSwitch) {
		this.PROJ_UTIL_SWITCH = projUtilSwitch;
	}
}
