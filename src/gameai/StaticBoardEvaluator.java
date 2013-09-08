package gameai;

/* a ridiculously basic board evaluator
 */

public class StaticBoardEvaluator extends BoardEvaluator {

	public StaticBoardEvaluator(int n, int player) {
		super(n, player);
	}

	@Override
	public int evaluate(GameBoardMitch gb, int player) {
		int numPlayer = 0;
		int numEnemy = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				int piece = gb.board[i * n + j];
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
}
